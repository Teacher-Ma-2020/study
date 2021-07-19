

[TOC]



# 常用的基本命令



**cd:切换目录**

```properties
cd :#切换目录命令!   
cd ../usr:#跳转上一级的目录文件


cd /home/jie:#绝对路径跳转   （无论在哪） 
cd ..:#返回上一级目录
cd ~:#回到当前的用户目录
pwd :#显示当前用户所在的目录
```



**ls: 列出目录**

```bash
ls :默认列出目录下的所有文件名
ls -a:参数: all ,查看全部的文件,包括隐藏文件
ls -l:列出目录下子目录和文件的详细信息
ls -t:按最后修改时间排序
ls -S:按文件大小排序。（大写的S）
ls -r:排序时按倒序。
ls -al:查看全部的文件包括隐藏文件的属性和权限
```



**mkdir 创建一个目录**

```bash
mkdir jie1  #创建文件夹
mkdir -p jie1/jie2/jie3 # 用-p递归创建层级目录
```

**rmdir 删除目录**  （只能删除空目录）

```bash
rmdir jie1 #删除目录
rmdir -p jie1/jie2/jie3 #递归删除目录
```

**touch**    创建文件

**echo**  写文件

```bash
echo "zcj" >>a    
```

**cp 复制文件**

```bash
cp panelExec.log jie1   #复制  中间为文件  后面为拷贝到的地址
cp panelExec.log ../home
```



**rm 移除文件或者目录**

```bash
-f #忽略不存在的文件,不会出现警告,强制删除!
-r #递归删除目录!
-i #互动,删除询问是否删除
rm -rf /*    #删库到跑路（十分危险 不要尝试）
```



**mv 移动文件或者目录|重命名文件**

```bash
-f #强制
-u #只替换已经更新过的文件
                                                                         mv panelExec.log jie1 #移动文件
mv panelExec.log p.log #重命名文件夹名
```

**tar  解压文件**

```bahs
tar -zxvf apache-tomcat-9.0.37.tar.gz
```

**firewall 防火墙、端口**

```bash
#查看防火墙信息
systemctl status firewalld
# 开启防火墙端口 8080
firewall-cmd --zone=public --add-port=8080/tcp --permanent
# 重启防火墙
systemctl restart firewalld.service
# 查看所有开启的端口
firewall-cmd --list-ports
```







# 走进Redis



Redis（Remote Dictionary Server )，即远程字典服务。

是一个开源的使用ANSI C语言编写、支持网络、可基于内存亦可持久化的日志型、**Key-Value数据库**，并提供多种语言的API。

与memcached一样，为了保证效率，**数据都是缓存在内存中**。区别的是redis会周期性的把更新的数据写入磁盘或者把修改操作写入追加的记录文件，并且在此基础上实现了master-slave(主从)同步。



1. 多样的数据类型
2. 持久化
3. 集群
4. 事务



## 环境搭建



基本环境安装

```bash
yum install gcc-c++
# 然后进入redis目录下执行
make
# 然后执行
make install
```



redis开启与关闭

```bash
redis-server redis.config[文件位置]   #开启redis
shutdown    #进入redis 输入即可关闭
```



redis 使用

```bash
redis-cli -p 6379  #使用redis
ps -ef | grep redis   #查看redis线程
```



## 基础知识



**Redis是单线程的，Redis是基于内存操作的。**

所以Redis的性能瓶颈不是CPU,而是机器内存和网络带宽。

但是redis6以上就是**多线程**了



redis中默认有16个数据库  （0-15）



redis基本操作

```bash
set [key]  [value]  #设置kv

get [key]     #获取

select 8 # 切换数据库 DB 8

dbsize #获得该数据库的大小

keys *  #查看当前数据库中所有的key

del [key]  #移除这个kv

type [key]  #查看这个value的属性

exists [key]  #查看这个key是否存在kv对

flushdb  #清空当前数据库中的键值对。

flushall  #清空所有数据库的键值对
```

```bash
move [key]  [id]    #将kv对移动到哪个数据库   （(move password 1)）

ttl [key]  #查看kv对的有效时间    -1为永久  -2为不存在

expire [key] [time]  #设置该kv对的有效时间  （expire name 10）
```

# 五大数据类型



Redis是一个开源（BSD许可），内存存储的数据结构服务器，可用作**数据库**，**高速缓存**和**消息队列代理**。它支持[字符串](https://www.redis.net.cn/tutorial/3508.html)、[哈希表](https://www.redis.net.cn/tutorial/3509.html)、[列表](https://www.redis.net.cn/tutorial/3510.html)、[集合](https://www.redis.net.cn/tutorial/3511.html)、[有序集合](https://www.redis.net.cn/tutorial/3512.html)，[位图](https://www.redis.net.cn/tutorial/3508.html)，[hyperloglogs](https://www.redis.net.cn/tutorial/3513.html)等数据类型。内置复制、[Lua脚本](https://www.redis.net.cn/tutorial/3516.html)、LRU收回、[事务](https://www.redis.net.cn/tutorial/3515.html)以及不同级别磁盘持久化功能，同时通过Redis Sentinel提供高可用，通过Redis Cluster提供自动[分区](https://www.redis.net.cn/tutorial/3524.html)。



## String(字符串)

String类似的使用场景：value除了是字符串还可以是数字，用途举例：

- 计数器
- 统计多单位的数量：uid:123666：follow 0
- 粉丝数
- 对象存储缓存

```bash
append [name] 123456   #在该k对应的value末尾添加数据   如果存在 则相当set添加

strlen [name]   #获取value的长度

incr [key]   #对value加1  (i++)   只对数值类型有效

decr [key]   #对value减一  (i--)

incrby [key] 10  #相当于 value+10

decrby [key] 5   #相当于 value-5

getrange name 1 3  #截取下标为1-3的字符串

getrange name 2 -1  #截取下标为2 到末尾的字符串
```

**setrange** 替换string中的数据

```bash
setrange [key] [index] [value]
setrange a 0 xxxx     #从 0 开始后面的4个字符替换成xxxx
					#如果不存在  则会set添加
```

**setex** 设置临时数据

```bash
setex [key] [value] [time]
setex a3 "asad" 60    #设置临时变量a3  保存时间为60s
```

**setnx** 判断之后再set

```bash
setnx [key] [value]
setnx name zcj      #如果没有name 则设置value为zcj的kv对， 如果有 则不设置
```

**mset**  同时设置多个kv 

**mget** 同时获取多个kv 

```bash
mset  name zcj name2 zcj2  #同时多个kv
mget  a name c   #获取 k为  a、name、c 的值
```

**msetnx** 判断后在设置多个kv  (必须所有k都没有)

```bash
msetnx q q w w  #设置 q：q   w：w的kv对
```

**getset**  先get再set

```bash
getset a abc  #先获取a的值 再将a设置为abc
```





 

## List(列表)



Redis列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）

首先我们列表，可以经过规则定义将其变为队列、栈、双端队列等

![[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-VPvbIltc-1597890996518)(狂神说 Redis.assets/image-20200813114255459.png)]](https://img-blog.csdnimg.cn/20200820104440398.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0RERERlbmdf,size_16,color_FFFFFF,t_70#pic_center)



正如图Redis中List是可以进行双端操作的，所以命令也就分为了LXXX和RLLL两类，有时候L也表示List例如LLEN



**应用：**

**消息排队！消息队列（Lpush Rpop）,栈（Lpush Lpop）**



> 方法

**lpush**  **rpush**    插入

```bash
lpush list 2    #向list 头节点中添加 a
rpush list 2    #向list 尾节点中添加 a
```

**lrange**  获取list值

```bash
lrange list 0 -1    #获取list的全部值   0：开始  -1:结束
```

**lpop  rpop**   移除

```bash
lpop list     #移除头节点的值
rpop list     #移除尾节点的值
```

**lrem**  移除指定数据

```bash
lrem list 1 2   # 移除list中的  1 个 2
```

**ltrim**  将范围外的其他数据移除

```bash
ltrim list 1 4   # 将list中 下标1-4的其他数据移除
```

**lset**  将指定下标的值替换

```bash
lset list 0 aaa   #将list下标为0的值设置为 aaa
```

**linsert**    指定数据前添加

```bash
linsert list before aaa 00   #list中 在 aaa 之前添加 00
linsert list after aaa 111   #list中 在 aaa 之后添加 111
```







> 小结

- list实际上是一个链表，before Node after , left, right 都可以插入值
- **如果key不存在，则创建新的链表**
- 如果key存在，新增内容
- 如果移除了所有值，空链表，也代表不存在
- 在两边插入或者改动值，效率最高！修改中间元素，效率相对较低





## Set(集合)

Redis的Set是string类型的无序集合。集合成员是唯一的，这就意味着集合中不能出现重复的数据。

Redis 中 集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是O(1)。

集合中最大的成员数为 232 - 1 (4294967295, 每个集合可存储40多亿个成员)。





> 方法

**sadd**  向set添加值

```bash
sadd set1 zcj   #set1中添加 zcj
```

**scrad**   获取个数

```bash
scard set   #获取集合的成员数
```

**smembers**  返回set中所有的值

```bash
smembers set  #返回集合中所有的成员
```

**sismember**   判断该set中有无此数据

```bash
sismember set zcj
```

**srem** 移除set中值

```bash
srem set zcj
```

**srandmember**  随机取n个值

```bash
srandmember set 2   #从set中随机取2个值
```

**spop**  随机移除元素

```bash
spop set
```

 **smove**  移动到另一个set

```bash
 smove set set2 zcj   #将set中的zcj 移到set2中
```

**sdiff** 判断差集

```bash
sdiff set set2    #以set为参照 比较set2的差集
```

 **sinter**  判断交集

```bash
 sinter set set2   #获得两个set中的交集
```

**sunion**   获得并集

```bash
sunion set set2
```





## Zset(有序集合)

**优先级**

不同的是每个元素都会关联一个double类型的分数（score）。redis正是通过分数来为集合中的成员进行从小到大的排序。

score相同：按字典顺序排序

有序集合的成员是唯一的,但分数(score)却可以重复。 



> 方法

**zadd** [zset]    [score]  [value]      向zset中添加value的同时 设置优先级

```bash
zadd zset 1 one 2 two 3 three  #向zset中添加  one 、two 、three  对应的socre为 1 2 3
```

**zrange**  获取范围中包含的所有value

```bash
zrange zset 0 -1   获取zset包含的有序value
```

 **zrangebyscore**   获取score范围的值

```bash
 zrangebyscore zset -inf +inf        #获取 zset中  score从负无穷到正无穷的值
 zrangebyscore zset -inf 2              #从负无穷到2
 zrangebyscore zset -inf +inf withscores  #在设置范围的同时  携带score输出
```

![image-20210118231238917](img\1.png)

**zrevrange**  范围内降序输出

```bash
zrevrange zset 0 -1 withscores   #携带score 降序输出
```

**zrem**   移除指定元素  （可移除多个）

```bash
zrem zset a     #移除zset中的 a 
```

**zcount**  获取score范围内的个数

```bash
zcount zset 0 +inf   #获取 score 0 到 正无穷的个数
```





## Hash(哈希)

Redis hash 是一个string类型的field和value的映射表，hash特别适合用于存储对象。（就是key-value 都是字段 一行数据）



一个hash里面 可以存储多个**Map**



**应用**

Hash变更的数据user name age，尤其是**用户信息**之类的，经常变动的信息！Hash更适合于对象的存储，Sring更加适合字符串存储！



> 方法

**hset**   添加hash

```bash
hset myhash k1 v1 k2 v2 k3 v3    #想myhash里面添加  [k1:v1]  [k2:v2]  [k3:v3]   三个map
```

 **hget**  获取

 ```bash
 hget myhash k3    #获取myhash中 k3对应的元素
 ```

**hmget**   取多个值

```bash
hmget myhash k1 k2   #获取myhash中 k1、k2对应的元素
```

**hgetall**   获取全部

```bash
hgetall myhash  #获取myhash中全部的 kv
```

**hdel**   删除对应的  kv

```bash
hdel myhash k2  #删除myhash 中的 k2 对应的kv
```

**hlen**   获取hash的长度   多少个（kv）

```bash
hlen myhash   #获取myhash中的map数量
```

**hexists**  判断是否存在该 kv

```bash
hexists myhash k  #查看myhash中是否含有k  对应的kv
```

**hkeys、hvals**   只获取 k  或者 v

```bash
hkeys myhash   #获取myhash中的k
hvals myhash   #获取myhash中的v
```

**hincrby**  将该v添加数值   （只对数字有效）

```bash
hincrby myhash number 2   #将myhash中的 number  +2 
```

**hsetnx**   判断是否存在再添加

```bash
hsetnx myhash number 10     #先判断myhash中是否存在该k  如果不存在则再添加 存在则不添加
```



# 三种特殊数据类型

## Geospatial(地理位置)

使用经纬度定位地理坐标



Geospatial 的底层是 **zset**

>方法

**geoadd**   添加位置

```bash
geoadd china:city 116 39 beijing
geoadd china:city 121 31 shanghai
eoadd china:city 106 29 chongqin      #添加名称并且设置 经度 纬度 值
```

**有效经纬度**

- 有效的经度从-180度到180度。
- 有效的纬度从-85.05112878度到85.05112878度。

**zrem **  由于geo的底层是zset  所以可以用 zrem移除坐标

```bash
zrem china:city chongqin
```

**geopos ** 获取该点的位置

```bash
geopos china:city beijing   #获取 city中 beijing的经纬度
```

**geodist**    获取两个位置的距离

```bash
geodist china:city beijing chongqin   #获取city中两个城市的距离
```

![image-20210120105432648](D:\study\Redis\img\2.png)

**georadius **   查取该点为圆心的具体距离内 有多少位置

```bash
georadius china:city 106 29 10000 km   

georadius china:city 106 29 10000 km withscoord   #携带坐标输出

georadius china:city 106 29 10000 km count 2   #限制查出来的数量
```

![image-20210120105719604](img\3.png)

**georadiusbymember**   以插入的坐标为中心寻找

```bash
georadiusbymember china:city beijing 1000 km   #以北京为中心 寻找半径1000km以内插入的位置
```

指定单位的参数 **unit** 必须是以下单位的其中一个：

- **m** 表示单位为米。
- **km** 表示单位为千米。
- **mi** 表示单位为英里。
- **ft** 表示单位为英尺。



 **geohash**   返回该位置的 hash值

```bash
 geohash china:city beijing chongqin   #返回重庆的hash值
```

![image-20210120111017893](img\4.png)





## Hyperloglog(基数统计)

Redis HyperLogLog 是用来做基数统计的算法，HyperLogLog 的优点是，在输入元素的数量或者体积非常非常大时，计算基数所需的空间总是固定的、并且是很小的。

花费 12 KB 内存，就可以计算接近 2^64 个不同元素的基数。

因为 HyperLogLog 只会根据输入元素来计算基数，而不会储存输入元素本身，所以 HyperLogLog 不能像集合那样，返回输入的各个元素。

其底层使用string数据类型



> 什么是基数？

数据集中**不重复**的元素的个数。



> 应用场景：

网页的访问量（UV）：一个用户多次访问，也只能算作一个人。

传统实现，存储用户的id,然后每次进行比较。当用户变多之后这种方式及其浪费空间，而我们的目的只是**计数**，Hyperloglog就能帮助我们利用最小的空间完成。



> 方法

**pfadd**   添加基数 

```bash
pfadd mykey a b c d e f f   #添加到mykey中的 实际为 abcdef
```

**pfcount**   计算基数个数

```bash
mykey：abcdef
mykey2：apq

pfcount mykey           #6 
pfcount mykey mykey2    #两个一起计算（不允许重复）    8
```

**pfmerge**  合并

```bash
pfmerge mykey3  mykey mykey2    #将1 和 2 合并为3
```





## BitMaps(位图)

使用位存储，信息状态只有 0 和 1

Bitmap是一串连续的2进制数字（0或1），每一位所在的位置为偏移(offset)，在bitmap上可执行AND,OR,XOR,NOT以及其它位操作。



**应用场景**

签到统计、状态统计





**setbit**   添加

![image-20210120122100090](img\5.png)

 相当于存储坐标 效果如下（灵魂画家）

![image-20210120122418691](D:\study\Redis\img\6.png)



**getbit ** 查看

```bash
getbit sign 5   #查看位置为5的编码
```

 **bitcount** 统计编码为1的个数

```bash
bitcount sign
```



# 事务



Redis的单条命令是保证原子性的，但是redis事务不能保证原子性



Redis事务本质：**一组命令的集合。**



事务中每条命令都会被序列化，执行过程中按顺序执行，不允许其他命令进行干扰。

- 一次性
- 顺序性
- 排他性

------

1. Redis事务没有隔离级别的概念
2. Redis单条命令是保证原子性的，但是事务不保证原子性！





## Redis事务操作过程

- 开启事务（`multi`）
- 命令入队
- 执行事务（`exec`）

> **执行事务**

所以事务中的命令在加入时都没有被执行，直到提交时才会开始执行(Exec)一次性完成。

![image-20210120124327628](img\7.png)

当添加的时候并没有执行

**exec**时才会一起执行



> 放弃事务

**discard**  放弃事务

![image-20210120124913170](D:\study\Redis\img\8.png)



**事务错误**

编译型异常   代码语法错误   >>>所有的命令都不执行

![image-20210120125618372](img\9.png)

代码逻辑错误 **其他命令可以正常执行 ** >>> 所以不保证事务原子性

![image-20210120125703487](img\10.png)

```bash
# 虽然中间有一条命令报错了，但是后面的指令依旧正常执行成功了。
# 所以说Redis单条指令保证原子性，但是Redis事务不能保证原子性。
```





## 监控



**悲观锁：**

- 很悲观，认为什么时候都会出现问题，无论做什么都会加锁

**乐观锁：**

- 很乐观，认为什么时候都不会出现问题，所以不会上锁！更新数据的时候去判断一下，在此期间是否有人修改过这个数据
- 获取version
- 更新的时候比较version



需要使用到**watch**

当watch 观察一个数据后  如果再另个客户端对这个数据进行操作  如果**启动事务**便会触发乐观锁

![image-20210120141636529](img\11.png)

另一个客户端的操作

![image-20210120142003604](img\12.png)

便导致了

- 事务触发了乐观锁
- 整个事务失效
- exec提交的同时  自动解除了money的乐观锁



# Jedis

使用Java来操作Redis，Jedis是Redis官方推荐使用的Java连接redis的客户端

```java
public class t {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        System.out.println(jedis.ping());
    }
}
```







# 整合springBoot

整合需要用到

**redisTemplate**



自定义   redisTemplate

```java
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        // 将template 泛型设置为 <String, Object>
        RedisTemplate<String, Object> template = new RedisTemplate();
        // 连接工厂，不必修改
        template.setConnectionFactory(redisConnectionFactory);
        /*
         * 序列化设置
         */
        // key、hash的key 采用 String序列化方式
        template.setKeySerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        // value、hash的value 采用 Jackson 序列化方式
        template.setValueSerializer(RedisSerializer.json());
        template.setHashValueSerializer(RedisSerializer.json());
        template.afterPropertiesSet();

        return template;
    }
}
```



写一个工具类





# Redis.conf配置文件

Redis在启动的时候是通过配置文件进行启动的

**1、Units**

单位，Redis配置文件中的单位对大小写不敏感

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123309871.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)





**2、includes**

包含，可以在Redis启动的时候再加载一些除了Redis.conf之外的其他的配置文件，和Spring的import，jsp的include类似

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123326776.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)





**3、NETWORK**

网络，表示Redis启动时开放的端口默认与本机绑定

```shell
bind 127.0.0.1
```

Redis指定监听端口，默认为6379

```shell
port 6379
```

表示服务器闲置多长时间（秒）后被关闭，如果这个这个数值为0，表示这个功能不起作用

```shell
timeout 300
```

是否开启保护模式，Redis默认开启，如果没有设置bind的IP地址和Redis密码，那么服务就会默认只能在本机运行

```shell
protected-mode yes
```





**4、GENERAL**

是否以守护进程的方式运行，即后台运行，一般默认为no，需要手动改为yes

```shell
daemonize yes
```

如果以守护进程的方式运行，就需要指定一个pid文件，在Redis启动时创建，退出时删除

```shell
pidfile /var/run/redis_6379.pid
```

配置日志等级，日志等级的可选项如下（翻译自配置文件，有改动）：

- debug：打印的信息较多，在工作中主要用于开发和测试
- verbose：打印的信息仅次于debug，但是格式较为工整
- notice：Redis默认配置，在生产环境中使用
- warning：只打印一些重要信息，比如警告和错误

```shell
loglevel notice
```

打印的日志文件名称，如果为空，表示标准输出，在配置守护进程的模式下会将输出信息保存到/dev/null

```shell
logfile ""
```

数据库支持数量，16个

```shell
databases 16
```





**5、SNAPSHOTTING**

中文翻译为快照，如果在规定的时间内，数据发生了几次更新，那么就会将数据同步备份到一个文件中

Redis的持久化有两种方式，一种是RDB，一种是AOF。SNAPSHOTTING主要针对的是Redis持久化中的RDB

Redis是一个内存数据库，如果不采用持久化对数据进行保存，那么就会出现断电即失的尴尬场面

```shell
# 在900秒内，至少有一个key被修改（添加），就会进行持久化操作
save 900 1
# 在300秒内，至少有10个key被修改，就会进行持久化操作
save 300 10
# 在60秒内，至少有1万个key被修改，就会进行持久化操作
save 60 10000
```

如果Redis在进行持久化的时候出现错误，是否停止写入，默认为是

```shell
top-writes-on-bgsave-error yes
```

是否在进行数据备份时压缩持久化文件，默认为是，这个操作会耗费CPU资源，可以设置为no

```shell
rdbcompression yes
```

在保存持久化文件的同时，对文件内容进行数据校验

```shell
rdbchecksum yes
```

持久化文件保存的目录，默认保存在当前目录下

```shell
dir ./
```



**6、REPLICATION**

复制主机上的数据，当前配置所指定的IP和端口号即为主机

```shell
# Redis在配置文件中将此配置注释，默认不使用，下同
# replicaof <masterip> <masterport>
```

如果配置的主机有密码，需要配置此密码以通过master的验证

```shell
# masterauth <master-password>
```



**7、SECRULITY**

安全，可以在配置文件中设置Redis的登录密码

 **CONFIG GET requirepass**   获取密码

**CONFIG set requirepass**  设置密码

**auto  [密码]**    输入密码



**8、CLIENT**

Redis允许存在的客户端的最大数量，默认有一万个

```shell
maxclients 10000
```

Redis配置最大的内存容量

```shell
maxmemory <bytes>
```

内存达到上限之后默认的处理策略

```shell
maxmemory-policy noeviction
```

处理策略有以下几种

- noeviction:默认策略，不淘汰，如果内存已满，添加数据是报错。
- allkeys-lru:在所有键中，选取最近最少使用的数据抛弃。
- volatile-lru:在设置了过期时间的所有键中，选取最近最少使用的数据抛弃。
- allkeys-random: 在所有键中，随机抛弃。
- volatile-random: 在设置了过期时间的所有键，随机抛弃。
- volatile-ttl:在设置了过期时间的所有键，抛弃存活时间最短的数据





**9、APPEND ONLY MODE**

这是Redis持久化的另一种方式，AOF，AOF模式默认不开启，Redis默认开启的是持久化模式是RDB，在大部分情况下，RDB的模式完全够用

```shell
appendonly no
```

AOF持久化的文件名称

```shell
appendfilename "appendonly.aof"
```

每秒执行一次同步，但是可能会丢失这一秒的数据

```shell
# 对于 appendfsync 它有以下几个属性 
# appendfsync always 表示每次修改都会进行数据同步，速度较慢，消耗性能
# appendfsync no 不执行同步，不消耗性能
appendfsync everysec # 数据不同步，每秒记录一次
```



# Redis持久化

## RDB

RDB，全称Redis DataBase。

什么是RDB，在指定的时间间隔内将数据集快照写入到磁盘中，在恢复数据的时候将这些快照文件读取到内存中，对应的配置文件中的**SNAPSHOTTING**，可以查看在上面提到的Redis的配置文件

![image-20210123202938677](img\13.png)



Redis会单独创建出一个子进程（fork）来进行持久化，会将数据写入到一个临时文件中，待持久化操作结束之后，临时文件会将已经持久化完成的文件替换掉，在这个过程中，主进程不进行任何IO操作，这也就确保RDB极高的性能，相比于RDB和AOF，RDB的模式会比AOF更加的高效。如果在进行大数据恢复，并且对于数据的精度要求不高，那么就可以使用RDB，Redis的持久化默认的也是RDB，在一般情况下（生产环境）不需要修改这个配置

什么是fork？fork就是复制一个和当前一模一样的进程作为原进程的子进程

**RDB保存的文件就是dump.rdb文件**

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123349353.png#pic_center)

> RDB触发机制

1、满足配置文件的save规则的情况下，会自动触发RDB规则

```shell
# 在900秒内，至少有一个key被修改（添加），就会进行持久化操作
save 900 1
# 在300秒内，至少有10个key被修改，就会进行持久化操作
save 300 10
# 在60秒内，至少有1万个key被修改，就会进行持久化操作
save 60 10000
```

2、执行FLUSHALL命令，也会触发RDB规则，但是没有意义，因为文件内容为空

3、退出Redis，也会产生dump.rdb文件（退出Redis默认执行save命令）

4、在客户端中使用save或者bgsave命令，也可以触发RDB规则但是这两种规则有所不同

- save命令会完全占用当前进程去进行持久化操作，也就是说，save命令只管保存，不管其他，只要有进程过来，一律阻塞
- bgsave命令会在后台运行，手动fork子进程进行操作，并且还会相应客户端的请求





> 如何进行数据恢复

1、首先使用一个Redis命令查看持久化文件保存的位置

```shell
config get dir
127.0.0.1:6379> CONFIG GET dir
1) "dir"
2) "/usr/local/bin"
```

2、然后将dump.rdb文件放到Redis的启动目录下即可

优点：

1. 适合大规模数据修复！
2. 对数据精度要求不高

缺点：

1. 在持久化的时候需要一定的时间间隔，如果在一定的间隔时间内服务器意外宕机，那么就会丢失最后一次持久化的数据
2. 因为RDB持久化是需要fork出一份子进程进行IO操作的，也就是说，在原本的进程当中再复制出一个一模一样的进程作为子进程在内存中运行，内存的承载就会变为原来的两倍





## AOF

Redis的另一种持久化方式，AOF，全名为Append Only File，它用日志的形式来记录每一个写操作，将Redis执行过的命令进行记录（读操作不记录），只追加文件，不改写文件。Redis在启动时会自动加载AOF持久化的文件重新构建数据，也就是Redis重启会把AOF持久化文件中的信息从前到后执行一次以完成数据的恢复

AOF持久化对应的配置文件的位置是**APPEND ONLY MODE**

如何启动AOF，看下图

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123413354.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)

修改完配置之后，只需重新启动就可

这里有一个小细节需要注意：**如果AOF和RDB模式在配置文件中都有开启的话，为了保证数据的安全性，在Redis启动时会优先使用AOF**

Redis的AOF持久化保存的文件名称就叫做**appendonly.aof**







如果说appendonly.aof文件的内容发生了一些错误，那么在Redis进行启动时，会出现问题

如果appendonly.aof内部发生错误，咋办？Redis中提供了一个可以修复aof文件的修复工具叫做redis-check-aof

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123439448.png#pic_center)

怎么使用呢，下面有一条命令

```shell
redis-check-aof --fix appendonly.aof

[root@bogon bin]# redis-check-aof --fix appendonly.aof 
0x             167: Expected \r\n, got: 6769
AOF analyzed: size=383, ok_up_to=351, diff=32
This will shrink the AOF from 383 bytes, with 32 bytes, to 351 bytes
Continue? [y/N]: y
Successfully truncated AOF
```

> Redis优缺点

优点：

它支持 ***同步记录*** 和 ***异步记录*** ，对应的配置文件的属性分别是下面两个

```shell
appendfsync always       # 同步记录，客户端中一有写操作，即刻记录，数据的完整性好，但是性能较差
appendfsync everysec     # 异步记录，每秒记录一次，但是服务器如果在这一秒之内宕机，这一秒的数据就会丢失
appendfsync no           # 不记录
```

缺点：

从恢复数据的角度来说，AOF所恢复的数据量一定是比RDB来得大的，从恢复数据的时间的角度来说，AOF的时间也是大于RDB的





### AOF的重写机制

AOF持久化本质就是采用日志的形式对文件内容进行追加，为了防止追加之后这个文件变得越大，所以Redis推出了一种针对于AOF文件的**重写机制**，如果AOF文件的大小超过配置文件中所设定的阈值时，会自动触发重写机制对文件内容进行压缩，只对可以恢复数据的命令进行保留，针对于这种重写机制，也可以在客户端中对这种重写机制进行手动触发，只需要一个命令

```shell
bgrewriteaof
```

原理

当AOF文件持久追加并且越来越大时，Redis会fork出一条新进程来对文件进行重写，和RDB一样，AOF也是先写临时文件再将其替换掉。Redis会对新进程中的数据进行遍历，每次都会遍历set和set有关的命令。重写并没有读取原来的appendonly.aof文件，而是使用命令将内存中的数据库内容进行重写得到一个新的文件

**Redis会将上一次重写的AOF文件大小进行记录，如果当前文件的大小超过源文件的一倍并且大小大于64M时就会触发重写操作**

可以在配置文件中查看重写的信息

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123501266.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)

## 总结

- RDB可以在指定的时间间隔内对数据集进行持久化快照存储
- AOF持久化记录每次客户端发送给Redis服务器的写操作，服务器中重启时会重新执行命令恢复原始数据，AOF持久化的每一次记录都会追加在文件的末尾，并且Redis有重写机制的存在使得AOF的文件被控制在合理的大小
- 如果Redis只做缓存，如果说只希望数据在服务器启动的时候存在，可以不使用任何的持久化方式
- 刚刚上面讲到一个小细节，如果两种持久化同时开启，Redis服务器会默认先找AOF持久化，因为AOF的保存数据集要比RDB要完整，这也就是Redis考虑安全的原因







# Redis发布订阅

Redis的发布订阅（publish/subscribe）是一种消息通信模式，发送者（publish）发送消息，订阅者（subscribe）接收消息

Redis客户端可以订阅任意数量的频道

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123521181.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)

如图，有三个客户端订阅了一个Channel1

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123536594.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)

当Channel1的后台发送了一个数据到Channel1的频道中，这三个订阅了Channel1的客户端就会同时收到这个数据

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123557985.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)

命令：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123616735.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)

这些都是用来实现数据通信的命令，现实中的场景可以是网络聊天室，广播等

订阅端

```shell
127.0.0.1:6379> SUBSCRIBE zcj   # 订阅一个频道叫zcj
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "zcj"
3) (integer) 1
# 一旦开始订阅，会立即占用当前进程去监听自己所订阅的那个Channel
1) "message"
2) "smallyellow"
3) "Hello!I love Java!!"
1) "message"
2) "smallyellow"
3) "Hello!I love Redis!!"
```

发送端

```shell
127.0.0.1:6379> PUBLISH zcj "Hello!I love Java!!"  # 往频道zcj中发布一条消息
(integer) 1
127.0.0.1:6379> PUBLISH zcj "Hello!I love Redis!!"
(integer) 1
127.0.0.1:6379> 
```





> 原理

Redis是C语言编写，在实现消息的发布和订阅的时候，Redis将其封装在一个后缀名为点c的文件中，pubsub.c

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123637685.png#pic_center)

通过subscribe和publish命令实现消息的发布和订阅，当用户订阅了一个频道之后，redis-server里面维护了一个字典，字典里有很多个Channel（频道），字典的值就是一个链表，链表中保存的是订阅了这个频道的用户，使用publish命令往频道发送数据之后，redis-server使用此频道作为key，去遍历这个指定的value链表，将信息依次发送过去

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123704135.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)

发布订阅的实现场景

1、实时沟通消息系统

2、微信公众号（点击关注，后台发送一篇博客，订阅的用户就可以监听到）

…

还有一些比较复杂的场景，可以使用消息中间件来做，RabbitMQ，RocketMQ，kafka…





# Redis主从复制

Redis的主从复制，实际上就是将一台Redis服务器的数据，复制到其他Redis的服务器。前者被称为服务器的主节点（master/leader），后者就是从节点（slave/follower），数据的复制只能由主节点到从节点，Master以写主，slave以读为主

实际上每台服务器都是一个主节点，一个主节点可以有零个或多个从节点，并且每一个从节点只能有一个主节点

**主从复制有哪些作用？**

1、主从复制可以实现数据备份，这是除了持久化的另一种实现数据保存的方式

2、实现故障快速修复，因为要实现主从复制必然需要多台服务器，一旦主节点挂掉，从节点可以代替主节点提供服务

3、主从复制配合读写分离，可以实现分担服务器负载，如果在生产环境中读的操作远大于写的操作时，可以通过多个从节点进行分担负载，以提高Redis的并发量

4、主从复制也是Redis集群搭建和哨兵机制的基础

将Redis运用于项目中，是不会只使用一台服务器进行搭建的，因为：

- 如果是单个Redis，那么单个服务器将会独自完成来自客户端的读写操作，负载较大，而且只有一台Redis服务器，太容易挂掉了，一不小心宕掉了就很麻烦
- 单个服务器，内存容量是有上限的，不管这台Redis服务器的内存再怎么大，也不可能完全用来存储内存，并且单台Redis服务器的内存占用一般不会超过20个G

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123737911.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)

主从复制，读写分离，在大部分情况下，很多人浏览网页更多的则是读操作，这是架构中常常会用到的一种模式，用来缓解读压力

## 配置

因为Redis默认启动时本身就是一个master，所以在进行主从复制的搭建时，只需要配置从库即可，查询当前库的状态信息，可以使用下面这个命令

```shell
127.0.0.1:6379> INFO replication           # 语法：INFO replication  查看当前库的信息

# Replication
role:master                                # 当前库的角色：master 
connected_slaves:0                         # 连接的节点数量：0
master_replid:dac6206fe3b477c70197db4e02696ca29b85e6be
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:0
second_repl_offset:-1
repl_backlog_active:0
repl_backlog_size:1048576
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0
```

复制出三个配置文件，修改4个信息

1、搭建主从复制，不同的服务器就需要不同的端口号

2、pidfile的文件名称

3、logfile 日志文件名称

4、Redis默认RDB持久化，所以dump.rdb的名称也是需要修改的

修改完毕之后启动，如果进程存在，就表示搭建完毕

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123805947.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)





## 一主二从

所有的Redis服务器在默认情况下都是一个主节点，在进行配置时，一般都是只需配置从机即可

说白了，就是“认老大”，那怎么“认老大”嘞，可以看下面的命令

**SLAVEOF**  

```shell
# 从机 6380 “认老大”
127.0.0.1:6380> SLAVEOF 127.0.0.1 6379
OK
127.0.0.1:6380> INFO replication
# Replication
role:slave                              # 当前角色 从机
master_host:127.0.0.1                   # 主机信息 127.0.0.1
master_port:6379                        # 主机端口号 6379
master_link_status:up                   # 主机状态 在线
master_last_io_seconds_ago:9
master_sync_in_progress:0
slave_repl_offset:14
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:f99776a9a6bc1e30551ac53e3ca674e6c9988120
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:14
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:14

# 可以在主机中查看当前有何改动

127.0.0.1:6379> INFO replication
# Replication
role:master
connected_slaves:1                                             # 连接的从机数量 1
slave0:ip=127.0.0.1,port=6380,state=online,offset=154,lag=1    # 从机信息，有IP地址，端口号，状态等
master_replid:f99776a9a6bc1e30551ac53e3ca674e6c9988120
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:154
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:154
```

如果从机都配置完毕，就可以在主机中使用INFO命令进行查看当前状态



> **恢复主节点**

在一主二从的基础上，如果主机挂了，不能让两个从机待在那傻傻的待命吧，总得写吧，那要写，咋写？有一个命令

```shell
SLAVEOF no one  #这个命令表示当前数据库停止与其他数据库的同步关系，使得当前数据库成为主库
```



实际上，这样在命令行中的这种主从配置，只是暂时的，真正要想实现永久的主从配置，就需要在配置文件中配置，可以查看上面的Redis配置文件中讲到的



**读写分离**

配置完了之后，**主机可以写，从机不能写**，这就是读写分离

```shell
# 主机中设置值
127.0.0.1:6379> SET k1 v1 
OK
127.0.0.1:6379> SET k2 v2 
OK
127.0.0.1:6379> KEYS *
1) "k2"
2) "k1"
```

主机的设置值，同样会被两个从机获取到

```shell
127.0.0.1:6380> KEYS *
1) "k2"
2) "k1"
#######################################################################################################################33
127.0.0.1:6381> KEYS *
1) "k2"
2) "k1"
```

但是作为从机在默认情况下是不能写的

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123830494.png#pic_center)



> 在实现一主二从时需要注意的问题

1、如果说在实现一主二从时，主机意外的挂掉了，那么**剩下的从机在主机还没到来之前原地待命**，这个时候主机掉了，就意味着失去了写操作。并且主机重新上线之后，还是保持着原来的身份，从机依旧可以获取到主机写入的信息

2、这个时候是如果是从机挂掉了，就代表这个从机已经断开连接，等待再次上线时，就不会连接原来的主机，它自己又变成了主机，但是原先的数据依旧存在，如果其再次重新连接，那么主机当中新添加的数据同样也会被共享到



> 什么是复制原理？

如果一个Slave指定了一个master做老大，那么Master节点就会发送一个叫做sync的同步命令，在使用了SLAVEOF命令之后，Master接收到了，就会启动后台的*存盘进程*，开始收集修改数据集的命令，在存盘进程执行完毕之后，会将数据文件发送给Slave，实现一次完全同步

说到同步，这个有两个概念需要多聊一嘴

1、全量复制，Slave文件在接到Master发来的数据文件之后，会将其存盘并加载到内存

2、增量复制，Master和Slave已经实现同步的情况下，如果Master继续修改数据集，会将命令再次收集起来，再发送给Slave

**只要是重新连接Master的服务器，在连接之后都会自动执行全量复制！！！**





## 一带一路

啥叫一带一路？*这一个节点它可以是上一个几点的Slave，可以是下一个节点的Master，这种既是Master又是Slave的状态就叫一带一路（随便乱取的，别搞我）*

这种主从复制的特性，在实际应用场景中可以有多个节点，这样的节点具有Master和Slave的双重身份（但是实际上它还是一个Slave节点，只不过它是一个可以进行写操作的Slave节点），可以分担Redis的写压力，并且在真正的Master节点挂掉之后，这些具有双重身份的就可以顶上去工作，有效解决了一主二从的不可写问题

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123849226.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)

可以使用SLAVEOF命令重新定义，被指定的IP和端口号的节点，就是具有Master和Slave双重身份的节点

```shell
SLAVEOF 127.0.0.1 6380  # 在端口号为6381的服务器输入命令
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123905996.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)





## 哨兵模式

从原本的主从复制来说，如果主机宕掉了，需要手动设置一台从机为一个新的主机，但是这种手动的配置需要人工干预，耗费时间和人力，并且在人工干预的时间内还会造成服务不可用的情况，对于开发者来说这不是一种好的解决方案。Redis从2.8开始出现了哨兵模式

![image-20210124234511927](img\14.png)

哨兵模式可以理解为谋权篡位的自动版，可以在后台监视主机是否故障，如果发生了故障就会以投票的方式在从机中选出一个作为主机

Redis提供了哨兵的命令，并且它是一个特殊的模式，它会创建出一个完全独立于Redis服务器的进程，**一个哨兵可以对多台服务器进行监控，并且可以有多个哨兵**，每个哨兵会定时发送PING命令给服务器，并且还要在一定时间内得到服务器的响应，得到响应之后哨兵模式才会判定你现在状态正常。如果在规定的时间内它发送的请求主机没有得到响应，那么哨兵便会初步判断，当前主机是**主观下线**，其余的哨兵发现这台主机没有在规定时间内响应数据，那么便会以每秒一次的频率对主机进行判断，它确实是主观下线了，那么主机就会被标记为**客观下线**，主机挂掉之后，哨兵便会通过投票的方式在挂掉的主机下的从机中选出一个作为新主机

**多哨兵模式**

![image-20210125192225957](D:\study\Redis\img\15.png)

> 测试哨兵模式

在Linux中使用编辑器编写一个文件，文件名称叫**sentinel.conf**，这里需要注意，配置文件名称定死，不能乱写

```shell
# 哨兵服务默认端口
port 26379

# 哨兵模式默认工作目录
dir /tmp

# 属性格式：sentinel monitor 主机名称 IP port 1 后面的数字1代表主机挂了，Slave投票让谁成为新主机
sentinel monitor myredis 127.0.0.1 6379 1
```

**redis-sentinel sentinel.conf**  启动哨兵服务

```shell
redis-sentinel sentinel.conf
[root@bogon bin]# redis-sentinel sentinel.conf 
1616:X 17 Nov 2020 11:11:25.752 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
1616:X 17 Nov 2020 11:11:25.752 # Redis version=5.0.10, bits=64, commit=00000000, modified=0, pid=1616, just started
1616:X 17 Nov 2020 11:11:25.752 # Configuration loaded
1616:X 17 Nov 2020 11:11:25.753 * Increased maximum number of open files to 10032 (it was originally set to 1024).
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 5.0.10 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in sentinel mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 26379
 |    `-._   `._    /     _.-'    |     PID: 1616
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

1616:X 17 Nov 2020 11:11:25.753 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
1616:X 17 Nov 2020 11:11:25.753 # Sentinel ID is 8cfc91b9eb7651b632c2dc77dd1af983c6ccdb78
1616:X 17 Nov 2020 11:11:25.753 # +monitor master myredis 127.0.0.1 6379 quorum 1
1616:X 17 Nov 2020 11:11:25.755 * +slave slave 127.0.0.1:6381 127.0.0.1 6381 @ myredis 127.0.0.1 6379
1616:X 17 Nov 2020 11:11:25.759 * +slave slave 127.0.0.1:6380 127.0.0.1 6380 @ myredis 127.0.0.1 6379
```

如果Master主节点挂掉了，从机中就会随机选择一个出来成为一个新的Master，这是C语言的内部算法，可以在哨兵服务的进程日志中查看新的Master是谁

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123925153.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119123941825.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)

就算此时原来的Master回来了，在6380面前也只能是个弟弟

> 哨兵模式的优劣

优点：

1、哨兵集群是基于主从复制来实现的，主从复制的优点全部具备

2、主从可以切换，故障可以转移，提升系统可用性

3、哨兵模式就是主从模式的升级，谋权篡位的手动到自动，更加健壮

缺点：

1、在线扩容比较麻烦，集群的数量达到上限，就会变得十分繁琐

2、实现哨兵模式的配置较为麻烦，如果出现故障，还会涉及到一些shell脚本的运行，这些都是非常麻烦的操作

关于哨兵模式的全部配置，有兴趣的建议你们看看这篇博客：https://blog.csdn.net/u012441222/article/details/80751390

```shell
# Example sentinel.conf
 
# 哨兵sentinel实例运行的端口 默认26379
port 26379
 
# 哨兵sentinel的工作目录
dir /tmp
 
# 哨兵sentinel监控的redis主节点的 ip port 
# master-name  可以自己命名的主节点名字 只能由字母A-z、数字0-9 、这三个字符".-_"组成。
# quorum 当这些quorum个数sentinel哨兵认为master主节点失联 那么这时 客观上认为主节点失联了
# sentinel monitor <master-name> <ip> <redis-port> <quorum>
  sentinel monitor mymaster 127.0.0.1 6379 2
 
# 当在Redis实例中开启了requirepass foobared 授权密码 这样所有连接Redis实例的客户端都要提供密码
# 设置哨兵sentinel 连接主从的密码 注意必须为主从设置一样的验证密码
# sentinel auth-pass <master-name> <password>
sentinel auth-pass mymaster MySUPER--secret-0123passw0rd
 
 
# 指定多少毫秒之后 主节点没有应答哨兵sentinel 此时 哨兵主观上认为主节点下线 默认30秒
# sentinel down-after-milliseconds <master-name> <milliseconds>
sentinel down-after-milliseconds mymaster 30000
 
# 这个配置项指定了在发生failover主备切换时最多可以有多少个slave同时对新的master进行 同步，
这个数字越小，完成failover所需的时间就越长，
但是如果这个数字越大，就意味着越 多的slave因为replication而不可用。
可以通过将这个值设为 1 来保证每次只有一个slave 处于不能处理命令请求的状态。
# sentinel parallel-syncs <master-name> <numslaves>
sentinel parallel-syncs mymaster 1
 
 
 
# 故障转移的超时时间 failover-timeout 可以用在以下这些方面： 
#1. 同一个sentinel对同一个master两次failover之间的间隔时间。
#2. 当一个slave从一个错误的master那里同步数据开始计算时间。直到slave被纠正为向正确的master那里同步数据时。
#3.当想要取消一个正在进行的failover所需要的时间。  
#4.当进行failover时，配置所有slaves指向新的master所需的最大时间。不过，即使过了这个超时，slaves依然会被正确配置为指向master，但是就不按parallel-syncs所配置的规则来了
# 默认三分钟
# sentinel failover-timeout <master-name> <milliseconds>
sentinel failover-timeout mymaster 180000
 
# SCRIPTS EXECUTION
 
#配置当某一事件发生时所需要执行的脚本，可以通过脚本来通知管理员，例如当系统运行不正常时发邮件通知相关人员。
#对于脚本的运行结果有以下规则：
#若脚本执行后返回1，那么该脚本稍后将会被再次执行，重复次数目前默认为10
#若脚本执行后返回2，或者比2更高的一个返回值，脚本将不会重复执行。
#如果脚本在执行过程中由于收到系统中断信号被终止了，则同返回值为1时的行为相同。
#一个脚本的最大执行时间为60s，如果超过这个时间，脚本将会被一个SIGKILL信号终止，之后重新执行。
 
#通知型脚本:当sentinel有任何警告级别的事件发生时（比如说redis实例的主观失效和客观失效等等），将会去调用这个脚本，
这时这个脚本应该通过邮件，SMS等方式去通知系统管理员关于系统不正常运行的信息。调用该脚本时，将传给脚本两个参数，
一个是事件的类型，
一个是事件的描述。
如果sentinel.conf配置文件中配置了这个脚本路径，那么必须保证这个脚本存在于这个路径，并且是可执行的，否则sentinel无法正常启动成功。
#通知脚本
# sentinel notification-script <master-name> <script-path>
  sentinel notification-script mymaster /var/redis/notify.sh
 
# 客户端重新配置主节点参数脚本
# 当一个master由于failover而发生改变时，这个脚本将会被调用，通知相关的客户端关于master地址已经发生改变的信息。
# 以下参数将会在调用脚本时传给脚本:
# <master-name> <role> <state> <from-ip> <from-port> <to-ip> <to-port>
# 目前<state>总是“failover”,
# <role>是“leader”或者“observer”中的一个。 
# 参数 from-ip, from-port, to-ip, to-port是用来和旧的master和新的master(即旧的slave)通信的
# 这个脚本应该是通用的，能被多次调用，不是针对性的。
# sentinel client-reconfig-script <master-name> <script-path>
 sentinel client-reconfig-script mymaster /var/redis/reconfig.sh
 
 
```





# 缓存穿透和雪崩

对于Redis缓存来说，使用Redis的缓存，它提升了应用程序的性能和效率，并且缓存在高并发场景中起到了非常重要的作用，如果针对数据的一致性来说，Redis的缓存就是一个非常致命的问题，这种问题有三个。

## 缓存穿透

在大多数场景中，数据库里的id字段一般来说都是自增。如果说，用户发送了一个请求，会首先进入缓存，查不到，就进入数据库，进了数据库，也查不到，查不到的话，如果说访问量较少，那还好，直接返回不存在嘛，因为这种少量的缓存穿透实际上是不可避免的，但是，一旦有一些不怀好意的坏蛋，在发送请求查询数据库的时候，主键的id字段故意给你来一个负数或者是一些远大于数据库最大id的数，而且进行巨大量的并发访问，这时候缓存中肯定是没有的，那这些请求就直接压给数据库了，数据库扛不住这么大的东西呀，那咋办，不解决数据库就只能挂掉呀。对此，有三种解决方案

1、在进行项目的整合时需要使用到API接口层，在接口层中定义自己的规则，对于不合法的参数可以直接返回，对于调用此接口的API的对象进行严查，任何可能发生的情况都要考虑到

2、在缓存中设置一个空对象，使用空对象完成后续请求

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119124016698.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)

3、使用一个东西，叫做布隆过滤器（Bloom Filter），布隆过滤器使用的是一个bit数组和一个hash算法实现的数据结构，并且内存的使用率极低。使用布隆过滤器可以快速判断当前key是否存在，和Java的Optional类有点相似，**布隆过滤器告诉你这个key不存在，那么它就一定不存在**

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119124045179.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)





## 缓存击穿

缓存击穿，它是缓存穿透的一种特殊情况，一般情况下没有公司会去实现这样的业务，因为没有这样一条非常非常高频的热点数据能够搞垮一台服务器，可能性是非常小的

举个栗子，如果有一个非常高频的热点key，在某一个时刻过期，与此同时又有非常非常多的请求并发访问这个key，因为缓存时间已过，现在全部的请求又开始全部压在数据库上面了，很容易导致服务器挂掉

**解决方案**

可以设置为当前key永不过期，但是不推荐这种做法，因为这样会长期占用Redis的内存空间

还有一种就是Redis的分布式锁，如果说当前有非常非常多的请求传进来，这个时候有分布式锁的保护，可以允许这些请求中的其中一个放进来，缓存找不到就直接查数据库嘛，查完了再把数据放到缓存中让其他的请求直接查缓存即可

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119124106736.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NjQ2ODQ3NA==,size_16,color_FFFFFF,t_70#pic_center)





## 缓存雪崩

缓存雪崩指的是，在同一个时间内，一大批的缓存，集体失效，就好像没有被缓存过一样，这个时候假设说有双十一双十二的这种秒杀活动，恰好在这个时刻缓存是成批成批的失效，那么缓存失效，这些请求也还是直接压上数据库的，如果服务器在没有加锁的情况下，这种流量几乎是瞬间达到一个峰值的，对于一个服务器来说，也是会出现宕机的情况

**解决方案**

1、搭建Redis集群，在高可用的情况下，配置多台服务器，在服务器中保存同样的数据，这样即使是集群中的一台甚至是多台挂掉的情况下，也依旧能够正常工作

2、如果不搭建集群，也可以这么做：项目启动时，将数据库和Redis进行数据同步，将数据库中部分的数据信息首先加载进入Redis缓存，并且在加载进入缓存时，可以将这些缓存数据的存活时间设置为随机，并且在数据库和Redis缓存需要在一定的时间之内进行同步更新