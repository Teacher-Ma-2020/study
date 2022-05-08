# RabbitMQ

## 介绍

- MQ(message queue)，从字面意思上看，本质是个队列
- 在互联网架构中，MQ 是一种非常常 见的上下游“逻辑解耦+物理解耦”的消息通信服务。使用了 MQ 之后，消息发送上游只需要依赖 MQ，不 用依赖其他服务



> 四大核心

**生产者** 

产生数据发送消息的程序是生产者 

**交换机** 

交换机是 RabbitMQ 非常重要的一个部件，一方面它接收来自生产者的消息，另一方面它将消息 推送到队列中。

**队列**

队列是 RabbitMQ 内部使用的一种数据结构，尽管消息流经 RabbitMQ 和应用程序，但它们只能存储在队列中。队列仅受主机的内存和磁盘限制的约束，本质上是一个大的消息缓冲区。

**消费者** 

消费与接收具有相似的含义。消费者大多时候是一个等待接收消息的程序。请注意生产者，消费者和消息中间件很多时候并不在同一机器上。同一个应用程序既可以是生产者又是可以是消费者。

![image-20210713143433106](img\2.png)



## 常用命令

**添加开机**启动 RabbitMQ 服务 

- chkconfig rabbitmq-server on 

**启动服务**

-  /sbin/service rabbitmq-server start 

**查看服务状态**

-  /sbin/service rabbitmq-server status 

**停止服务(选择执行)**

-  /sbin/service rabbitmq-server stop 



**角色**

创建账号  ：rabbitmqctl add_user admin 123 

设置用户角色    ：rabbitmqctl set_user_tags admin administrator 

设置用户权限   ：rabbitmqctl set_permissions -p "/" admin ".*" ".*" ".*"

 当前用户和角色 ：rabbitmqctl list_users



通过http://121.43.55.23:15672  登陆RabbitMQ web界面



## 消息发送与接收



### 生产者与消费者

在下图中，“ P”是我们的生产者，“ C”是我们的消费者。中间的框是一个队列-RabbitMQ 代 表使用者保留的消息缓冲区

**生产者----队列-----消费者**

![image-20210713100407942](img\1.png)

**消息生产者**

```java
//消息生产者
public class Producer {
    private final static String QUEUE_NAME="zcj";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("121.43.55.23");
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("123456");

        //创建连接
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化 默认消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
         * 4.是否自动删除 最后一个消费者断开连接以后 该队列是否自动删除 true 自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String message="hello world";
        /**
         * 发送一个消息
         * 1.发送到哪个交换机
         * 2.路由的 key 是哪个
         * 3.其他的参数信息
         * 4.发送消息的消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕");
    }
}
```

**消息消费者**

```java
public class Consumer {
    private final static String QUEUE_NAME="zcj";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("consumer 等待消息接收");

        //声明 接收消息
        DeliverCallback deliverCallback=(consumerTag,delivery)->{
            String message=new String(delivery.getBody());
            System.out.println("consumer "+message);
        };

        //取消消息的回调
        CancelCallback cancelCallback=(consumerTag)->{
            System.out.println("消息消费被中断");
        };

        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者成功消费的回调
         * 4.消费者未成功消费的回调
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
```

**抽取工具类**

```java
public class RabbitMqUtils {
    public static Channel getChannel() throws IOException, TimeoutException {
        //连接工厂
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("121.43.55.23");
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("123456");

        return connectionFactory.newConnection().createChannel();

    }
}
```







### 工作队列

工作队列的主要思想是避免立即执行资源密集型任务

![image-20210713163618294](img\3.png)

**当一个生产者发送，两个消费者接收时**

- 生产者总共发送 4 个消息
- 消费者 1 和消费者 2 分别分得两个消息
- 并且是按照有序的依次接收信息

![image-20210713211335957](img\4.png)



### 不公平分发

- 当有多个消费者时，RabbitMQ**默认会采用公平分发**的模式，即使每个消费者处理效率不一样
- 这样会导致**处理速度快**的这个消费者很大一部分时间 **处于空闲状态**，



此时我们可以**设置预取值**

- 设置参数 **channel.basicQos(1);**   表示该消费端目前存放一条消息，只有**处理完**再从队列中拉取另一条消息，以此来保证消息队列的效率
- 也可以设置多条 **channel.basicQos(n);**  保证消费端可以存放n条  
- 如果不设置，则是默认的公平分发

```java
public class Consumer1 {
    public static final String TASK_NAME="ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //生成队列
        Channel channel=RabbitMqUtils.getChannel();
        System.out.println("Consumer1 响应时间短");

        DeliverCallback deliverCallback=(consumerTag,delivery)->{
            try {
                Thread.sleep(1000);       //沉睡一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String message=new String(delivery.getBody());
            System.out.println("Consumer1接收消息："+message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        };

        channel.basicQos(1);   //配置不公平分发
        channel.basicConsume(TASK_NAME,false,deliverCallback,(consumerTag)->{});
    }
}
```

```java
public class Consumer2 {
    public static final String TASK_NAME="ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //生成队列
        Channel channel= RabbitMqUtils.getChannel();
        System.out.println("Consumer2 响应时间长");

        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            try {
                Thread.sleep(30000);       //沉睡三十秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String message=new String(delivery.getBody());
            System.out.println("Consumer2接收消息："+message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        };
        channel.basicQos(1);   //配置不公平分发
        channel.basicConsume(TASK_NAME,false,deliverCallback,(consumerTag)->{});
    }
}
```











### 消息应答

- 当消费者处理任务过程中，如果挂掉的话，我们将丢失该正在处理的信息
- 为了保证消息在发送过程中不丢失，**rabbitmq 引入消息应答机制**，消费者在接 **收到消息并且处理该消息**之后，告诉 rabbitmq 它已经处理了，rabbitmq 可以把该消息删除了。



```java
channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);   //消费端接收   true为自动应答，false为手动应答
```

**自动应答**

- 消息发送后立即被认为已经传送成功，这种模式需要在高吞吐量和数据传输安全性方面做权衡
- 这种模式仅适用在消费者可以高效并 以某种速率能够处理这些消息的情况下使用。

```java
DeliverCallback deliverCallback=(consumerTag,delivery)->{
    /* -------   */   //如果此处发生错误  在采取自动应答的情况下 便会发生数据丢失 
    String message=new String(delivery.getBody());
    System.out.println("Consumer1接收消息："+message);
}
```



**手动应答**

- Channel.**basicAck**(用于肯定确认)    RabbitMQ 已知道该消息并且成功的处理消息，可以将其丢弃了 
- Channel.**basicNack**(用于否定确认)
- Channel.**basicReject**(用于否定确认)       相比于Channel.basicNack 少一个参数（是否**批量处理**）

```java
public class Consumer {
    private final static String QUEUE_NAME="zcj";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("consumer 等待消息接收");

        //声明 接收消息
        DeliverCallback deliverCallback=(consumerTag,delivery)->{
            String message=new String(delivery.getBody());
            System.out.println("consumer "+message);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);   //手动应答  表示该消息处理完毕
            															 //第二个参数表示是否批量处理
        };

        channel.basicConsume(QUEUE_NAME,false,deliverCallback,(consumerTag)->{});   //此处需要声明为false
        System.out.println("数据接收完毕");
    }
}
```



关于批量处理：

![image-20210713223452677](img\5.png)



### 消息自动重新入队

如果消费者由于某些原因失去连接(其通道已关闭，连接已关闭或 TCP 连接丢失)，导致消息未应答

RabbitMQ 将了解到消息未完全处理，并将对其重新排队。如果此时其他消费者 可以处理，它将很快将其重新分发给另一个消费者。这样，即使某个消费者偶尔死亡，也可以确 保不会丢失任何消息。

![image-20210714091419106](img\6.png)



- 例如当C1未能处理消息，只要C1**未应答**
- 那么消息1**不会丢失**，消息会重新入队，交由C2进行处理，抱着消息不缺失









### 持久化

#### 队列持久化

- 如果RabbitMQ处于某种原因宕机，那么队列和消息将会丢失
- 确保消息不会丢失：我们**需要将队列和消息**都**标记为持久化**。

需要在创建队列时声明队列为持久化队列

```java
//声明队列
channel.queueDeclare(TASK_NAME,true,false,false,null);    //第二个参数 表明为队列为持久化队列
```

![image-20210714110651334](img\7.png)



**需要注意**

- 如果之前声明的队列不是持久化的，需要把原先队列先删除，或者重新 创建一个持久化的队列，不然就会出现错误



#### 消息持久化

- 当队列持久化，但消息未持久化时，如果发送RabbitMQ宕机，仍会发送消息丢失
- 所以我们需要将消息持久化（存入磁盘）

```java
channel.basicPublish("",TASK_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());   
//发送消息时 声明MessageProperties.PERSISTENT_TEXT_PLAIN
```

![image-20210714112358441](img\8.png)



将消息标记为持久化并不能完全保证不会丢失消息，**持久性保证并不强**，但是对于我们的简单任务队列而言，这已经绰绰有余了





#### 发布确认

![image-20210714165356985](img\9.png)

- 当队列持久化，并且消息持久化时，如果在**消息发送到队列的中途发生故障**，就到会导致**消息仍未真正持久化**到队列中
- 只有**发布确认**后，才能真正完成消息的持久化



**开启发布确认**

```java
public class Producer {
    public static final String TASK_NAME="ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel= RabbitMqUtils.getChannel();
        channel.confirmSelect();  //开启发布确认

        channel.queueDeclare(TASK_NAME,true,false,false,null);

        Scanner scanner=new Scanner(System.in);
        while(true){
            String message=scanner.next();
            channel.basicPublish("",TASK_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            System.out.println("消息发送完毕:"+message);
        }
    }
}
```

在创建信道后，**channel.confirmSelect();**   开启发布确认



##### 单个确认

- 这是一种简单的确认方式，它是一种**同步确认发布**的方式，也就是发布一个消息之后只有它 被确认发布，后续的消息才能继续发布
- 这种确认方式有一个最大的缺点就是:发布速度**特别的慢**，但保证了每条消息的准确性



```java
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        String queueName = UUID.randomUUID().toString();
        Channel channel= RabbitMqUtils.getChannel();
        channel.confirmSelect();  //开启发布确认

        channel.queueDeclare(queueName,true,false,false,null);

        long start=System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            channel.basicPublish("",queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,String.valueOf(i).getBytes());
            boolean flag = channel.waitForConfirms();   //单个消息发布确认
            if (flag){
                System.out.println("确认发布:"+i);
            }
        }

        long end=System.currentTimeMillis();
        System.out.println("耗时"+(end-start)+"ms");
    }
}
```

当每发送一次进行确认时，耗时43s  （？？？？）



##### 批量确认

- **批量确实**先发布一批消息然后一起确认，可以极大地提高吞吐量
- 这种方式的缺点就是:当发生故障导致发布出现问题时，不知道是哪个消息出现问题

```java
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        String queueName = UUID.randomUUID().toString();
        Channel channel= RabbitMqUtils.getChannel();
        channel.confirmSelect();  //开启发布确认

        channel.queueDeclare(queueName,true,false,false,null);

        long start=System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            channel.basicPublish("",queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,String.valueOf(i).getBytes());
            if (i%100==0){
                boolean flag = channel.waitForConfirms();   //每100条确认一次
                if (flag){
                    System.out.println("确认发布:"+i);
                }
            }
        }

        long end=System.currentTimeMillis();
        System.out.println("耗时"+(end-start)+"ms");
    }
}
```

每一百条批量处理，耗时7s



##### 异步确认

![image-20210714225232962](img\10.png)



**启动另外一个线程进行异步确认**，保证发送消息的线程的效率

```java
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        String queueName = UUID.randomUUID().toString();
        Channel channel= RabbitMqUtils.getChannel();
        channel.confirmSelect();                                               //开启发布确认

        channel.queueDeclare(queueName,true,false,false,null);

        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            System.out.println("确认的消息："+deliveryTag);
        };
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("未确认的消息："+deliveryTag);
        };
        channel.addConfirmListener(ackCallback,nackCallback);                  //异步确实

        long start=System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            channel.basicPublish("",queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,String.valueOf(i).getBytes());
        }
        long end=System.currentTimeMillis();
        System.out.println("耗时"+(end-start)+"ms");
    }
}
```



**如何处理异步未确认消息** 

- 最好的解决的解决方案就是把未确认的消息放到一个基于内存的能被发布线程访问的队列
- 使用**ConcurrentLinkedQueue** 这个队列在 confirm callbacks 与发布线程之间进行消息的传递。



```java
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel=RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目 只要给到序列号
         * 3.支持并发访问
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new
                ConcurrentSkipListMap<>();
        /**
         * 确认收到消息的一个回调
         * 1.消息序列号
         * 2.true 可以确认小于等于当前序列号的消息
         * false 确认当前序列号消息
         */
        ConfirmCallback ackCallback = (sequenceNumber, multiple) -> {
            if (multiple) {
                //返回的是小于等于当前序列号的未确认消息 是一个 map
                ConcurrentNavigableMap<Long, String> confirmed =
                        outstandingConfirms.headMap(sequenceNumber, true);
                //清除该部分未确认消息
                confirmed.clear();
            }else{
                //只清除当前序列号的消息
                outstandingConfirms.remove(sequenceNumber);
            }
        };
        ConfirmCallback nackCallback = (sequenceNumber, multiple) -> {
            String message = outstandingConfirms.get(sequenceNumber);
            System.out.println("发布的消息"+message+"未被确认，序列号"+sequenceNumber);
        };
        /**
         * 添加一个异步确认的监听器
         * 1.确认收到消息的回调
         * 2.未收到消息的回调
         */
        channel.addConfirmListener(ackCallback, null);
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String message = "消息" + i;
            /**
             * channel.getNextPublishSeqNo()获取下一个消息的序列号
             * 通过序列号与消息体进行一个关联
             * 全部都是未确认的消息体
             */
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
            channel.basicPublish("", queueName, null, message.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + 1000 + "个异步确认消息,耗时" + (end - begin) +
                "ms");
    }
}
```

- 优点： 耗时短（53ms），可以存储未发送成功消息
- 缺点：代码长



## 交换机

RabbitMQ 消息传递模型的核心思想是: 

- **生产者生产的消息从不会直接发送到队列**
- **生产者只能将消息发送到交换机(exchange)**

交换机工作的内容非常简单，一方面它接收来 自生产者的消息，另一方面将它们推入队列。

交换机**必须确切知道如何处理收到的消息**：是应该把这些消息放到特定队列还是说把他们到许多队列中还是说应该丢弃它们

![image-20210715154930359](img\11.png)



**交换机类型**

- 直接(direct)
- 主题(topic) 
- 标题(headers) 
- 扇出(fanout)



**默认交换机**

```java
channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
```

第一个参数是交换机的名称。**空字符串**表示**默认或无名称交换机**：消息能路由发送到队列中其实 是由 routingKey(bindingkey)绑定 key 指定的



**绑定(bindings)**

绑定其实是 **交换机** 和 **队列** 之间的桥梁，它告诉我们 交换机 和哪个队列进行了绑定关系。比如说下面这张图告诉我们的就是 X 与 Q1 和 Q2 进行了绑定

![image-20210715160302694](img\12.png)



### 广播模式 （fanout）

- 它是将接收到的所有消息广播到**它知道的所有队列**中。
- 无论routingKey是否相同，**只要队列连接了该交换机**，都会收到消息

![image-20210715203232763](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20210715203232763.png)

**发送端 （EmitLog）**

```java
public class EmitLog {
    public static final String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();          //获取信道

        Scanner scanner=new Scanner(System.in);
        while (true){
            String message=scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());  //向该交换机发送消息
            System.out.println("EmitLog "+message);
        }
    }
}
```

**消费者（ReceiveLog）**

- 声明连接类型为**"fanout"**的**logs交换机**
- 即可接收该交换机的消息

```java
public class ReceiveLog1 {
    public static final String EXCHANGE_NAME="logs";

    public static void main(String[] args) throws Exception {
        //声明信道
        Channel channel = RabbitMqUtils.getChannel();
        //指明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //获取队列名称（临时队列）
        String queueName = channel.queueDeclare().getQueue();
        //建立连接
        channel.queueBind(queueName,EXCHANGE_NAME,"");

        System.out.println("等待接收消息");
        //声明 接收消息
        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            String message=new String(delivery.getBody());
            System.out.println("ReceiveLog1 "+message);
        };

        channel.basicConsume(queueName,true,deliverCallback,(consumerTag)->{});
    }
}
```



### 直接模式（direct）

direct 的工作方式是，**消息只去到它绑定的 routingKey 队列中去**。

![image-20210716095624430](img\13.png)



**发送端**

- 只需要向指定交换机发送消息即可
- 并携带routingKey

```java
public class DirectLogs {
    public static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();          //获取信道

        Scanner scanner=new Scanner(System.in);
        while (true){
            String message=scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"warning",null,message.getBytes());  //像交换机发送消息  并携带routingKey
            System.out.println("DirectLogs "+message);
        }
    }
}
```



**接收端**

- 配置交换机类型
- 创建队列
- 根据routingKey获取消息

```java
public class ReceiveLogsDirect01 {
    private static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel= RabbitMqUtils.getChannel();
        //声明交换机 和类型
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.DIRECT);
        //创建队列
        channel.queueDeclare("console",false,false,false,null);
        //建立连接,并设置 routingKey
        channel.queueBind("console",EXCHANGE_NAME,"info");
        channel.queueBind("console",EXCHANGE_NAME,"warning");

        System.out.println("等待接收消息");
        //声明 接收消息
        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            String message=new String(delivery.getBody());
            System.out.println("ReceiveLogsDirect01:info、warning "+message);
        };
        channel.basicConsume("console",true,deliverCallback,(consumerTag)->{});
    }
}
```



### 主题模式（topic）

发送到类型是 topic 交换机的消息的 **routing_key 不能随意写**，

- 它必须是一个单词列表，**以点号分隔开**,比如说："stock.usd.nyse", "nyse.vmw", "quick.orange.rabbit"

**routing_key规则如下**：

- ***(星号)**可以代替一个单词 
- **#(井号)**可以替代零个或多个单词

![image-20210716142601686](img\14.png)

![image-20210716142623408](D:\study\JAVAEE\RabbitMQ\img\15.png)



**生产者**

```java
public class EmitLogTopic {
    public static final String EXCHANGE_NAME="topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();          //获取信道


        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        bindingKeyMap.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        bindingKeyMap.put("quick.orange.fox","被队列 Q1 接收到");
        bindingKeyMap.put("lazy.brown.fox","被队列 Q2 接收到");
        bindingKeyMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        bindingKeyMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");
        Iterator<String> iterator = bindingKeyMap.keySet().iterator();
        
        while (iterator.hasNext()){
            String routing_key = iterator.next();
            channel.basicPublish(EXCHANGE_NAME,routing_key,null,(routing_key+" "+bindingKeyMap.get(routing_key)).getBytes("UTF-8"));  //像交换机发送消息  并携带routingKey
        }
    }
}
```





**消费者**

```java
public class ReceiveTopic02 {
    private static final String EXCHANGE_NAME="topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel= RabbitMqUtils.getChannel();      //获取信道
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);     //声明队列，
        channel.queueDeclare("Q2",false,false,false,null);
        channel.queueBind("Q2",EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind("Q2",EXCHANGE_NAME,"lazy.#");   //设置队列的routing key

        System.out.println("等待接收消息");
        //声明 接收消息
        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            String message=new String(delivery.getBody());
            System.out.println("ReceiveTopic01:"+message);
        };
        channel.basicConsume("Q2",true,deliverCallback,(consumerTag)->{});
    }
}
```





## 死信队列

某些时候由于特定的原因导致 queue 中的某些消息无法被消费，这样的消息如果没有后续的处理，就变成了死信，有死信自然就有了死信队列。

**应用场景:**

- 为了保证订单业务的消息数据不丢失，需要使用到 RabbitMQ 的死信队列机制
- 当消息消费发生异常时，将消息投入死信队列中
- 用户在商城下单成功并点击去支付后在指定时间未支付时自动失效



**死信的来源** 

- 消息TTL**过期** 
- 队列达到最大长度(队列满了，无法再添加数据到 mq 中) 
- 消息被拒绝 (basic.reject 或 basic.nack)并且 requeue=false.

![image-20210716151412134](img\16.png)



### 消息过期

**生产者**

- 设置消息过期时间 （也可以在 消费端设置）

```java
public class Producer {
    private static final String NORMAL_EXCHANGE="normal_exchange";

    public static void main(String[] args) throws Exception  {
        Channel channel= RabbitMqUtils.getChannel();
        //发送消息设置过期时间  10s
        AMQP.BasicProperties properties =new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 0; i < 10; i++) {
            channel.basicPublish(NORMAL_EXCHANGE,"live",properties,String.valueOf(i).getBytes());
        }
    }
}
```



**消费者1**

- **配置两台交换机，两个队列**
- 通过信道，串联交换机与队列
- **设置普通队列转入死信队列的条件**
- 接收普通队列的里消息并输出



```java
public class Consumer1 {
    private static final String NORMAL_EXCHANGE="normal_exchange";
    private static final String DEAD_EXCHANGE="dead_exchange";
    private static final String NORMAL_QUEUE="normal_queue";
    private static final String DEAD_QUEUE="dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel= RabbitMqUtils.getChannel();
        
        //声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);

        //声明队列的转换条件
        Map<String,Object> changeMap=new Hashtable<>();

//        //设置过期时间   10s   可以由生产者设置
//        changeMap.put("x-message-ttl",10000);

        //正常队列设置死信交换机 参数 key 是固定值
        changeMap.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //正常队列设置死信 routing-key 参数 key 是固定值
        changeMap.put("x-dead-letter-routing-key", "dead");

        channel.queueDeclare(NORMAL_QUEUE,false,false,false,changeMap);   //此处设置入死信队列的条件
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //捆绑交换机与队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"live");
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"dead");

        //声明 接收消息
        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            String message=new String(delivery.getBody());
            System.out.println("Consumer1接收消息:"+message);
        };
        channel.basicConsume(NORMAL_QUEUE,true,deliverCallback,(consumerTag)->{});
    }
}
```



**消费者2**

- 接收死信队列中的消息并输出

```java
public class Consumer2 {
    private static final String DEAD_EXCHANGE="dead_exchange";
    private static final String DEAD_QUEUE="dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel= RabbitMqUtils.getChannel();
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"dead");

        System.out.println("等待接收消息");
        //声明 接收消息
        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            String message=new String(delivery.getBody());
            System.out.println("Consumer2 "+message);
        };
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,(consumerTag)->{});
    }
}
```







### 超出队列长度

- **配置该队列的长度限制**
- 超过限度的队列，会转入死信队列
- 如果该队列之前创建过，此时改变跳转队列的条件的话，运行会报错，需要先删除之前的队列

```java
package com.jie.dead;

import com.jie.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.Hashtable;
import java.util.Map;

public class Consumer1 {
    private static final String NORMAL_EXCHANGE="normal_exchange";
    private static final String DEAD_EXCHANGE="dead_exchange";
    private static final String NORMAL_QUEUE="normal_queue";
    private static final String DEAD_QUEUE="dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel= RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);

        //声明队列的转换条件
        Map<String,Object> changeMap=new Hashtable<>();

        //正常队列设置死信交换机 参数 key 是固定值
        changeMap.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //正常队列设置死信 routing-key 参数 key 是固定值
        changeMap.put("x-dead-letter-routing-key", "dead");
        //设置长度的限制
        changeMap.put("x-max-length",6);

        channel.queueDeclare(NORMAL_QUEUE,false,false,false,changeMap);
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //捆绑交换机与队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"live");
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"dead");

        //声明 接收消息
        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            String message=new String(delivery.getBody());
            System.out.println("Consumer1接收消息:"+message);
        };
        channel.basicConsume(NORMAL_QUEUE,true,deliverCallback,(consumerTag)->{});
    }
}
```

![image-20210716173131445](img\17.png)



### 消息被拒接

- 设置手动应答，并拒绝指定消息

```java
public class Consumer1 {
    private static final String NORMAL_EXCHANGE="normal_exchange";
    private static final String DEAD_EXCHANGE="dead_exchange";
    private static final String NORMAL_QUEUE="normal_queue";
    private static final String DEAD_QUEUE="dead_queue";

    public static void main(String[] args) throws Exception {
        Channel channel= RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);

        //声明队列的转换条件
        Map<String,Object> changeMap=new Hashtable<>();

        //正常队列设置死信交换机 参数 key 是固定值
        changeMap.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //正常队列设置死信 routing-key 参数 key 是固定值
        changeMap.put("x-dead-letter-routing-key", "dead");


        channel.queueDeclare(NORMAL_QUEUE,false,false,false,changeMap);
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //捆绑交换机与队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"live");
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"dead");

        //声明 接收消息
        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            String message=new String(delivery.getBody());
            if (message.equals("5")){
                System.out.println("Consumer1拒绝消息:"+message);
                //手动应答  拒绝该消息
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(),false);
            }else{
                System.out.println("Consumer1接收消息:"+message);
                //手动应答  接收该消息
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,(consumerTag)->{});
    }
}
```



## SpringBoot整合

**依赖**

```xml
<dependencies>
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
   </dependency>

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
   </dependency>

   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-amqp</artifactId>
   </dependency>
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
   </dependency>

   <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>fastjson</artifactId>
      <version>1.2.47</version>
   </dependency>
   <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
   </dependency>
   <!--swagger-->
   <dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-swagger2</artifactId>
      <version>2.9.2</version>
   </dependency>
   <dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-swagger-ui</artifactId>
      <version>2.9.2</version>
   </dependency>
   <!--RabbitMQ 测试依赖-->
   <dependency>
      <groupId>org.springframework.amqp</groupId>
      <artifactId>spring-rabbit-test</artifactId>
      <scope>test</scope>
   </dependency>

</dependencies>
```



**配置文件**

```properties
spring.rabbitmq.host=121.43.55.*
spring.rabbitmq.port=5672
spring.rabbitmq.username=root
spring.rabbitmq.password=123456
```



### 生产者-队列-消费者

![image-20220425110543132](img\30.png)

**生产者**

```java
@Autowired
RabbitTemplate rabbitTemplate;

@GetMapping("/sendQueue/{msg}")
public void sendQueue(@PathVariable("msg") String msg){
    //想zcj队列发送消息
    rabbitTemplate.convertAndSend("zcj",msg);
}
```



**消费者**

监听名字为"zcj"的队列，可以指定是否消息持久化，是否自动删除

```java
@Component
public class QueueConsumer {

    @RabbitListener(queuesToDeclare = @Queue(value = "zcj",declare = "true",autoDelete = "true"))
    public void receivel(String message){
        System.out.println("接受消息"+ message);
    }
}
```

也可以有多个消费者分别处理队列消息

```java
@Component
public class QueueConsumer {

    @RabbitListener(queuesToDeclare = @Queue(value = "zcj",declare = "true",autoDelete = "true"))
    public void receivel(String message){
        System.out.println("消费者1接受消息"+ message);
    }

    @RabbitListener(queuesToDeclare = @Queue(value = "zcj",declare = "true",autoDelete = "true"))
    public void receivel2(String message){
        System.out.println("消费者2接受消息"+ message);
    }
}
```





### 广播模式(fanout)

**代码架构图******

![image-20220425111758513](img\31.png)

当消息发送到交换机时，所有和fanout交换机绑定的队列都会收到消息

```java
@Autowired
RabbitTemplate rabbitTemplate;

@GetMapping("/sendQueue/{msg}")
public void sendQueue(@PathVariable("msg") String msg){
    //向logs交换机发送消息
    rabbitTemplate.convertAndSend("logs","",msg);
}
```

```java
@Component
public class QueueConsumer {

    @RabbitListener(bindings = {
        @QueueBinding(
                value = @Queue, //创建队列
                exchange = @Exchange(value = "logs",type = "fanout")//绑定交换机并指定类型、名称
        )
    })
    public void exchangeMsg(String message){
        System.out.println("接收到交换机logs消息"+message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue, //创建队列
                    exchange = @Exchange(value = "logs",type = "fanout")//绑定交换机并指定类型、名称
            )
    })
    public void exchangeMsg2(String message){
        System.out.println("接收到交换机logs消息"+message);
    }
}
```



### 直接模式（direct ）

direct 的工作方式是，**消息只去到它绑定的 routingKey 队列中去**。

![image-20210716095624430](img\13.png)



```java
@Autowired
RabbitTemplate rabbitTemplate;

@GetMapping("/sendQueue/{msg}")
public void sendQueue(@PathVariable("msg") String msg){
    //向logs交换机发送消息
    rabbitTemplate.convertAndSend("logs.direct","info",msg);
}
```

```java
@Component
public class QueueConsumer {

    @RabbitListener(bindings = {
        @QueueBinding(
                value = @Queue, //创建临时队列
                exchange = @Exchange(value = "logs.direct",type = "direct"),//绑定交换机并指定类型、名称
                key = {"info","warn"} //指定key
        )
    })
    public void exchangeMsg(String message){
        System.out.println("info，warn消息"+message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue, //创建临时队列
                    exchange = @Exchange(value = "logs.direct",type = "direct"),//绑定交换机并指定类型、名称
                    key = {"error"}
            )
    })
    public void exchangeMsg2(String message){
        System.out.println("error消息"+message);
    }
}
```







### 主题模式（topic）

根据routing_key来给队列发送消息

**routing_key规则如下**：

- ***(星号)**可以代替一个单词 
- **#(井号)**可以替代零个或多个单词

![image-20210716142601686](img\14.png)



```java
@GetMapping("/sendQueue/{msg}")
public void sendQueue(@PathVariable("msg") String msg){
    //向logs交换机发送消息
    rabbitTemplate.convertAndSend("logs.topic","error.1",msg);
}
```

```java
@Component
public class QueueConsumer {

    @RabbitListener(bindings = {
        @QueueBinding(
                value = @Queue, //创建临时队列
                exchange = @Exchange(value = "logs.topic",type = "topic"),//绑定交换机并指定类型、名称
                key = {"info.*","warn.*"}
        )
    })
    public void exchangeMsg(String message){
        System.out.println("info，warn消息"+message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue, //创建临时队列
                    exchange = @Exchange(value = "logs.topic",type = "topic"),//绑定交换机并指定类型、名称
                    key = {"error.*"}
            )
    })
    public void exchangeMsg2(String message){
        System.out.println("error消息"+message);
    }
}
```



### 另外实现方式



![image-20210718103203375](img\25.png)

**建立连接**

```java
@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    @Bean
    public DirectExchange  confirmExchange(){
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    @Bean("confirmQueue")
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }
    // 声明确认队列绑定关系
    @Bean
    public Binding queueBinding(@Qualifier("confirmQueue") Queue queue,
                                @Qualifier("confirmExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("key1");
    }
}
```



**生产者**

```java
@RestController
@RequestMapping("/confirm")
@Slf4j
public class ConfirmController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //向交换机发送消息
    @GetMapping("/sendMsg/{msg}")
    public void confirmSend(@PathVariable("msg") String msg){
        log.info("当前时间,{},发送信息给ttl队列,{}",new Date().toString(),msg);
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,"key1","发送消息 ："+msg);

    }
}
```

**消费者**

```java
@Component
@Slf4j
public class DeadConsumer {
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void confirmReceive(Message message, Channel channel) throws Exception{
        String msg=new String(message.getBody());
        log.info("当前时间：{},获得消息：{}",new Date().toString(),msg);
    }
}
```







## 延迟队列  

延时队列

- 延时队列就是用来存放需要在指定时间被处理的元素的队列。

**使用场景**

-  订单在十分钟之内未支付则自动取消 
- 新创建的店铺，如果在十天内都没有上传过商品，则自动发送消息提醒。
- 用户注册成功后，如果三天内没有登陆则进行短信提醒。 
- 用户发起退款，如果三天内没有得到处理则通知相关运营人员。 
- 预定会议后，需要在预定的时间点前十分钟通知各个与会人员参加会议



### 实现延时队列

![image-20210717141153086](img\18.png)



**配置Config  (创建队列和建立关系)**

```java
@Configuration
public class TtlQueueConfig {
    //普通交换机
    public static final String X_EXCHANGE = "X";
    //普通队列
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    //死信交换机
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //死信队列
    public static final String DEAD_LETTER_QUEUE = "QD";


    //创建两个交换机
    @Bean
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }
    @Bean
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //创建队列A、B  并设置传入死信交换机的条件与key
    @Bean
    public Queue queueA(){
        Map<String,Object> changeMap=new HashMap<>();
        //设置死信交换机
        changeMap.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //设置死信 routing-key 参数 key 是固定值
        changeMap.put("x-dead-letter-routing-key", "YD");
        //设置过期时间
        changeMap.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(changeMap).build();
    }

    @Bean
    public Queue queueB(){
        Map<String,Object> changeMap=new HashMap<>();
        //设置死信交换机
        changeMap.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //设置死信 routing-key 参数 key 是固定值
        changeMap.put("x-dead-letter-routing-key", "YD");
        //设置过期时间
        changeMap.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(changeMap).build();
    }

    //创建死信队列 用于接收消息
    @Bean
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    //交换机和队列之间进行绑定
    @Bean
    public Binding queueA_bind_exchangeX(@Qualifier("queueA") Queue queueA,
                                         @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }
    @Bean
    public Binding queueB_bind_exchangeX(@Qualifier("queueB") Queue queueB,
                                         @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }
    @Bean
    public Binding queueD_bind_exchangeY(@Qualifier("queueD") Queue queueD,
                                         @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

}
```



**生产者**

```java
@RestController
@RequestMapping("/ttl")
@Slf4j
public class MsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //向交换机发送消息
    @GetMapping("/sendMsg/{msg}")
    public void send(@PathVariable("msg") String msg){
        log.info("当前时间,{},发送信息给ttl队列,{}",new Date().toString(),msg);
        rabbitTemplate.convertAndSend("X","XA","消息发送给10s队列："+msg);
        rabbitTemplate.convertAndSend("X","XB","消息发送给40s队列："+msg);
    }

}
```

**消费者**

```java
@Component
@Slf4j
public class DeadConsumer {
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) throws Exception{
        String msg=new String(message.getBody());
        log.info("当前时间：{},死信队列消息：{}",new Date().toString(),msg);
    }
}
```

**结果图**

![image-20210717141434119](img\19.png)







### 自定义等待时间

在这里新增了一个队列 QC,绑定关系如下,该队列不设置 TTL 时间

![image-20210717160818087](img\20.png)

```java
@RestController
@RequestMapping("/ttl")
@Slf4j
public class MsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    //向交换机发送消息 和延迟时间
    @GetMapping("/sendMsg/{msg}/{ttlTime}")
    public void send(@PathVariable("msg") String msg,@PathVariable("ttlTime") String ttlTime){
        log.info("当前时间,{},发送信息给Qc队列,{}",new Date().toString(),msg);
        rabbitTemplate.convertAndSend("X","XC",msg+" 过期时间 "+ttlTime,(message)->{
            message.getMessageProperties().setExpiration(ttlTime);
            return message;
        });
    }
}

```

**交换机和队列的连接**  （干脆全部copy了）

```java
package com.jie.mq.boot_mq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Configuration
public class TtlQueueConfig {
    //普通交换机
    public static final String X_EXCHANGE = "X";
    //普通队列
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    //死信交换机
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //死信队列
    public static final String DEAD_LETTER_QUEUE = "QD";


    //创建两个交换机
    @Bean
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }
    @Bean
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //创建队列A、B  并设置传入死信交换机的条件与key
    @Bean
    public Queue queueA(){
        Map<String,Object> changeMap=new HashMap<>();
        //设置死信交换机
        changeMap.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //设置死信 routing-key 参数 key 是固定值
        changeMap.put("x-dead-letter-routing-key", "YD");
        //设置过期时间
        changeMap.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(changeMap).build();
    }

    @Bean
    public Queue queueB(){
        Map<String,Object> changeMap=new HashMap<>();
        //设置死信交换机
        changeMap.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //设置死信 routing-key 参数 key 是固定值
        changeMap.put("x-dead-letter-routing-key", "YD");
        //设置过期时间
        changeMap.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(changeMap).build();
    }

    @Bean
    public Queue queueC(){
        Map<String,Object> changeMap=new HashMap<>();
        //设置死信交换机
        changeMap.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //设置死信 routing-key 参数 key 是固定值
        changeMap.put("x-dead-letter-routing-key", "YD");

        return QueueBuilder.durable(QUEUE_C).withArguments(changeMap).build();
    }

    //创建死信队列 用于接收消息
    @Bean
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    //交换机和队列之间进行绑定
    @Bean
    public Binding queueA_bind_exchangeX(@Qualifier("queueA") Queue queueA,
                                         @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }
    @Bean
    public Binding queueB_bind_exchangeX(@Qualifier("queueB") Queue queueB,
                                         @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }
    @Bean
    public Binding queueC_bind_exchangeX(@Qualifier("queueC") Queue queueC,
                                         @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }

    @Bean
    public Binding queueD_bind_exchangeY(@Qualifier("queueD") Queue queueD,
                                         @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

}
```

**消费者**

```java
@Component
@Slf4j
public class DeadConsumer {
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) throws Exception{
        String msg=new String(message.getBody());
        log.info("当前时间：{},死信队列消息：{}",new Date().toString(),msg);
    }
}
```



注意：

- **自定义过期会导致消息阻塞**
- 需要先发出的消息发布后，后面的消息才能发布
- 即使后面的消息等待时间远小于前面的等待时间

![image-20210717183816191](img\21.png)



​	

### 解决等待堵塞

此处需要使用RabbitMQ插件

![image-20210717183920575](img\22.png)

**架构图**

![image-20210717184000370](img\23.png)





**创建队列与交换机**

```java
@Configuration
public class DelayQueueConfig {
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    //自定义交换机
    @Bean
    public CustomExchange delayExchange(){
        Map<String, Object> args = new HashMap<>();
        //自定义交换机的类型
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message",true,false,args);
    }

    //队列
    @Bean
    public Queue delayQueue(){
        return new Queue(DELAYED_QUEUE_NAME);
    }

    //建立连接
    @Bean
    public Binding bind(@Qualifier("delayExchange") CustomExchange delayExchange,@Qualifier("delayQueue") Queue delayQueue){
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAYED_ROUTING_KEY).noargs();

    }
}
```



**生产者**

```java
//基于插件  发送消息 和 延迟时间
@GetMapping("/XsendMsg/{msg}/{ttlTime}")
public void Xsend(@PathVariable("msg") String msg,@PathVariable("ttlTime") Integer ttlTime){
    log.info("当前时间,{},基于插件发送信息队列,{}",new Date().toString(),msg);
    rabbitTemplate.convertAndSend(DelayQueueConfig.DELAYED_EXCHANGE_NAME,DelayQueueConfig.DELAYED_ROUTING_KEY,msg+" 过期时间 "+ttlTime,(message)->{
        message.getMessageProperties().setDelay(ttlTime);
        return message;
    });
}
```

**消费者**

```java
@Component
@Slf4j
public class DeadConsumer {
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) throws Exception{
        String msg=new String(message.getBody());
        log.info("当前时间：{},死信队列消息：{}",new Date().toString(),msg);
    }

    @RabbitListener(queues = DelayQueueConfig.DELAYED_QUEUE_NAME)
    public void XreceiveD(Message message, Channel channel) throws Exception{
        String msg=new String(message.getBody());
        log.info("当前时间：{},死信队列消息：{}",new Date().toString(),msg);
    }
}
```

**可实现不堵塞发布**

![image-20210717194056008](img\24.png)





## 发布确认

- **当RabbitMQ服务端宕机时，发送的消息会丢失**
- 此时需要引入发布确认机制



![image-20210718111137771](img\26.png)



### 交换机确认

**配置文件**

- 加入spring.rabbitmq.publisher-confirm-type=correlated  

```properties
spring.rabbitmq.host=121.43.55.*
spring.rabbitmq.port=5672
spring.rabbitmq.username=root
spring.rabbitmq.password=123456
spring.rabbitmq.publisher-confirm-type=correlated  
```



在生产者端加入**CorrelationData**

```java
@RestController
@RequestMapping("/confirm")
@Slf4j
public class ConfirmController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //向交换机发送消息
    @GetMapping("/sendMsg/{msg}")
    public void confirmSend(@PathVariable("msg") String msg){
        CorrelationData correlationData=new CorrelationData("1");  //设置回调参数
        
        log.info("当前时间,{},发送信息给ttl队列,{}",new Date().toString(),msg);
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,"key12","发送消息 ："+msg,correlationData);  //此处传入correlationData
    }
}
```



**实现回调接口**

```java
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback {

    @Autowired
    RabbitTemplate template;

    /**
     * 由于是RabbitTemplate内部的接口 需要 set传入
     */
    @PostConstruct
    public void init(){
        template.setConfirmCallback(this);
    }

    /**
     * 回调方法
     * @param correlationData   接收的参数
     * @param b                 交换机是否接收成功
     * @param s                 交换机接收失败的原因， 如果接收成功为null
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id = correlationData != null ? correlationData.getId() : null;
        if (b){
            log.info("交换机接收成功！ ID为{}的消息",id);
        }else{
            log.info("交换机接收失败！ ID为{}的消息,原因为{}",id,s);
        }
    }
}
```





### 回退消息

- 当消息发送到交换机，但由于其他原因（比如routing key错误） 导致未能发送到队列中
- 此时仍会导致消息丢失，需要开启回退消息

配置文件加入

```properties
spring.rabbitmq.publisher-returns=true
```



设置回调方法

```java
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    @Autowired
    RabbitTemplate template;

    /**
     * 由于是RabbitTemplate内部的接口 需要 set传入
     */
    @PostConstruct
    public void init(){
        template.setConfirmCallback(this);
        template.setReturnCallback(this);
    }

    /**
     * 回调方法
     * @param correlationData   接收的参数
     * @param b                 交换机是否接收成功
     * @param s                 交换机接收失败的原因， 如果接收成功为null
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id = correlationData != null ? correlationData.getId() : null;
        if (b){
            log.info("交换机接收成功！ ID为{}的消息",id);
        }else{
            log.info("交换机接收失败！ ID为{}的消息,原因为{}",id,s);
        }
    }

    /**
     *消息回调方法，消息未能到队列时，调用此方法
     */
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        log.error("消息{},被交换机{}退回，退回原因：{}，路由key：{}",new String(message.getBody()),s1,s,s2);
    }
}
```





## 备份交换机

- 通过备份交换机也可以实现消息不丢失
- 当发送到接收交换机，但是key错误、无法找到指定队列时，会发送到备份交换机

![image-20210718173303032](img\27.png)



**创建交换机与队列，并连接**

```java
@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    public static final String WARNING_QUEUE_NAME = "warning.queue";

    //备份交换机
    @Bean
    public FanoutExchange  backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }


    @Bean
    public DirectExchange  confirmExchange(){
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME).build();   //连接备用交换机
    }

    @Bean
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }
    // 声明确认队列绑定关系
    @Bean
    public Binding queueBinding(@Qualifier("confirmQueue") Queue queue,
                                @Qualifier("confirmExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("key1");
    }


    @Bean
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }
    @Bean
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    @Bean
    public Binding backUpQueueBinding(@Qualifier("backupQueue") Queue queue,
                                @Qualifier("backupExchange") FanoutExchange backupExchange){
        return BindingBuilder.bind(queue).to(backupExchange);
    }

    @Bean
    public Binding WaringQueueBinding(@Qualifier("warningQueue") Queue queue,
                                @Qualifier("backupExchange") FanoutExchange backupExchange){
        return BindingBuilder.bind(queue).to(backupExchange);
    }

}
```

**生产者**

- 一个发送成功、一个发送失败

```java
@RestController
@RequestMapping("/confirm")
@Slf4j
public class ConfirmController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //向交换机发送消息
    @GetMapping("/sendMsg/{msg}")
    public void confirmSend(@PathVariable("msg") String msg){
        log.info("当前时间,{},发送信息给ttl队列,{}",new Date().toString(),msg);
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,"key1","发送消息 ："+msg);
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,"key2","发送消息 ："+msg);
    }
}
```



**消费者**

```java
@Component
@Slf4j
public class DeadConsumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void confirmReceive(Message message, Channel channel) throws Exception{
        String msg=new String(message.getBody());
        log.info("当前时间：{},获得消息：{}",new Date().toString(),msg);
    }

    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void WarnReceive(Message message, Channel channel) throws Exception{
        String msg=new String(message.getBody());
        log.warn("当前时间：{},报警：{}",new Date().toString(),msg);
    }

}
```

![image-20210718220703373](img\28.png)





## 优先队列

只有当消费者不足,即消息无法被即使消费而阻塞的时候,优先队列才会根据优先级来分配任务的执行顺序.

**配置类**

```java
@Configuration
public class priQueue {
    private static final String EXCHANGE = "priority-exchange";

    public static final String QUEUE = "priority-queue";

    private static final String ROUTING_KEY = "priority.queue";
    @Bean
    DirectExchange exchange1(){
        return new DirectExchange(EXCHANGE);
    }
    @Bean
    Queue queue1(){
        Map<String,Object>map = new HashMap<>();
        map.put("x-max-priority",10);//设置最大的优先级数量
        return new Queue(QUEUE,true,false,false,map);
    }
    @Bean
    public Binding binding1(){
        return BindingBuilder.bind(queue1()).to(exchange1()).with(ROUTING_KEY);
    }
}


```

**生产者**

```java

@Component
public class priConsumer {
    private static final String EXCHANGE = "priority-exchange";

    public static final String QUEUE = "priority-queue";

    private static final String ROUTING_KEY = "priority.queue";
    @Autowired
    RabbitTemplate template;
    public void test(){
        for(int i=10;i>1;i--){
            int finalI = i;
            template.convertAndSend(EXCHANGE,ROUTING_KEY,"queue:"+i, message -> {
                message.getMessageProperties().setPriority(finalI);
                return message;
            });
        }

    }
}
```

**消费者**

```java
@Component
@RabbitListener(queues ="priority-queue")
public class pricon {
    @RabbitHandler
    public void hand(String str){
        System.out.println("接受到了一个消息:"+str);
    }
}
```

**结果**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200416204305744.png)

## 惰性队列

- 当消息过多时，可以实现惰性队列
- 消息存在内存中，减少rabbitmq的内存消耗

```java
Map<String, Object> args = new HashMap<String, Object>();
args.put("x-queue-mode", "lazy");
channel.queueDeclare("myqueue", false, false, false, args);
```

![image-20210719195427236](img\29.png)

