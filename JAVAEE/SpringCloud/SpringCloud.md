



# SpringCloud

参考视频：https://www.bilibili.com/video/BV18E411x7eT

参考博客：https://blog.csdn.net/u011863024/article/details/114298270 （强烈推荐）



## 简介

### 微服务介绍

- 微服务是一种架构风格
- 一个应用拆分为一组小型服务
- 每个服务运行在自己的进程内，也就是可独立部署和升级
- 服务围绕业务功能拆分
- 可以由全自动部署机制独立部署
- 去中心化，服务自治。服务可以使用不同的语言、不同的存储技术

![image-20210613102403469](img\1.png)



### 版本选择 

![image-20210613102712321](img\2.png)

- SpringBoot 和 SpringCloud需要一一对应
- 截至2021/6/13

> **学习环境**

| SpringCloud         | Hoxton.SR1    |
| ------------------- | ------------- |
| SpringBoot          | 2.2.2.RELEASE |
| SpringCloud alibaba | 2.1.0.RELEASE |
| JDK                 | 8             |







### 组件介绍

![image-20210613104829472](img\3.png)

![img](https://img-blog.csdnimg.cn/img_convert/b39a21012bed11a837c1edff840e5024.png)





## 服务注册中心

### Eureka

> 什么是服务治理

- 在传统的RPC远程调用框架中，管理每个服务与服务之间依赖关系比较复杂，管理比较复杂
- 所以需要使用服务治理，管理服务于服务之间依赖关系，可以实现服务调用、负载均衡、容错等，实现服务发现与注册。

![image-20210616173847533](img\4.png)



#### 服务注册

```xml
<!--eureka-server-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

**Eureka Server**提供服务注册

各个微服务节点通过配置启动后，会在EurekaServer中进行注册，这样EurekaServer中的服务注册表中将会存储所有可用服务节点的信息，服务节点的信息可以在界面中直观看到。



**yml文件**

```yaml
server:
  port: 7001

eureka:
  instance:
    hostname: locathost #eureka服务端的实例名称
  client:
    #false表示不向注册中心注册自己。
    register-with-eureka: false
    #false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
    fetch-registry: false
    service-url:
      #设置与Eureka server交互的地址查询服务和注册服务都需要依赖这个地址。
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```

**主启动**

```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaMain7001 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaMain7001.class,args);
    }
}
```

输入localhost:7001进入服务主页

![image-20210619110719611](img\5.png)



#### 服务入驻

```xml
<!--eureka-client-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

**yml配置**

```yaml
eureka:
  client:
    #表示是否将自己注册进Eurekaserver默认为true。
    register-with-eureka: true
    #是否从EurekaServer抓取已有的注册信息，默认为true。单节点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
    fetchRegistry: true
    service-url:
      defaultZone: http://localhost:7001/eureka
```

**主启动**

```java
@SpringBootApplication
@EnableEurekaClient
public class PaymentMain8081 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8081.class,args);
    }
}
```

![image-20210619115526709](img\6.png)





#### Eureka集群

![img](https://img-blog.csdnimg.cn/img_convert/94c4c3eca8c2f9eb7497fe643b9b0622.png)

##### 注册中心部署集群

将Eureka注册中心相互注册，相互守望

- 修改cloud-eureka-server7001配置文件

```yaml
server:
  port: 7001

eureka:
  instance:
    hostname: eureka7001.com #eureka服务端的实例名称
  client:
    register-with-eureka: false     #false表示不向注册中心注册自己。
    fetch-registry: false     #false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
    service-url:
    #集群指向其它eureka
      defaultZone: http://eureka7002.com:7002/eureka/
    #单机就是7001自己
      #defaultZone: http://eureka7001.com:7001/eureka/

```

- 修改cloud-eureka-server7002配置文件

```yaml
server:
  port: 7002

eureka:
  instance:
    hostname: eureka7002.com #eureka服务端的实例名称
  client:
    register-with-eureka: false     #false表示不向注册中心注册自己。
    fetch-registry: false     #false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
    service-url:
    #集群指向其它eureka
      defaultZone: http://eureka7001.com:7001/eureka/
    #单机就是7002自己
      #defaultZone: http://eureka7002.com:7002/eureka/

```



##### 服务部署集群

- 将支付服务8001微服务，订单服务80微服务发布到上面2台Eureka集群配置中

```yaml
eureka:
  client:
    #表示是否将自己注册进Eurekaserver默认为true。
    register-with-eureka: true
    #是否从EurekaServer抓取已有的注册信息，默认为true。单节点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
    fetchRegistry: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka, http://eureka7002.com:7002/eureka
```

![image-20210619170520552](img\7.png)



##### 负载均衡

部署多个服务中心后可实现负载均衡，减少服务器压力

```java
@Value("${server.port}")
private String serverPort;//可以根据配置文件 输出该服务的端口号
```

- 将请求端口改为 **服务名**  (spring.application.name)

![image-20210619171033515](img\8.png)



```java
@Slf4j
@RestController
public class OrderController {
    public static final String PAYMENT_URL="http://CLOUD-PAYMENT-SERVICE";

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/consumer/get")
    public CommonResult<Payment> get(int id){
        log.info(String.valueOf(id));
        CommonResult commonResult = restTemplate.getForObject(PAYMENT_URL + "/get?id="+id, CommonResult.class);
        return commonResult;
    }
}
```
- 将**restTemplate**加上负载均衡注解**@LoadBalanced**

```java
@Configuration
public class ApplicationContextConfig {
    @Bean
    @LoadBalanced//使用@LoadBalanced注解赋予RestTemplate负载均衡的能力
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```

- 就可以通过请求80端口，动态调用不同端口的服务，实现负载均衡



##### 服务信息完善

![image-20210619171836034](img\9.png)

```yaml
eureka:
  ...
  instance:
    instance-id: payment8001  #在eureka注册中展示可读性高的名称
    prefer-ip-address: true #地址栏显示ip
```



##### 服务发现

对于注册进eureka里面的微服务，可以通过服务发现来获得该服务的信息

```java
@Autowired
private DiscoveryClient discoveryClient;

@GetMapping("/discovery")
public CommonResult discovery(){
    List<String> services = discoveryClient.getServices();

    //打印注册中心下的所有服务
    log.info(services.toString());

    List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
    for (ServiceInstance instance:instances){
        //打印该服务的信息
        log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
    }

    return new CommonResult(200,"请求成功",services);
}
```



##### 自我保护

在自我保护模式中，**Eureka Server**会保护服务注册表中的信息，不再注销任何服务实例

> 什么是自我保护模式?

- 默认情况下，如果EurekaServer在一定时间内没有接收到某个微服务实例的心跳，EurekaServer将会注销该实例(默认90秒)
- 但是当网络分区故障发生(延时、卡顿、拥挤)时，微服务与EurekaServer之间无法正常通信，以上行为可能变得非常危险了
- 因为微服务本身其实是健康的，此时本不应该注销这个微服务

**在自我保护模式中，Eureka Server会保护服务注册表中的信息，不再注销任何服务实例**。



> 禁止自我保护

使用**eureka.server.enable-self-preservation = false**可以禁用自我保护模式

```yaml
eureka:
  ---

  server:
    enable-self-preservation: false
```

当该服务**超过等待上限未发送心跳**时， eureka注册中心将会**删除**该服务







### Zookeeper

- zookeeper是一个分布式协调工具，可以实现注册中心功能
- 服务注册使用的是**临时节点**

#### 服务注册

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
    <!--先排除自带的zookeeper3.5.3 防止与3.4.9起冲突-->
    <exclusions>
        <exclusion>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!--添加zookeeper3.4.9版本-->
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.4.9</version>
</dependency>
```

```yaml
server:
  port: 8004


spring:
  application:
    name: cloud-provider-payment
  cloud:
    zookeeper:
      connect-string: 121.43.55.*:2181
```

使用**@EnableDiscoveryClient**即可完成zookeeper注册

```java
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentMain8004 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8004.class,args);
    }
}
```

![image-20210625153009134](img\10.png)



#### 消费注册

```xml
<!-- SpringBoot整合zookeeper客户端 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
    <!--先排除自带的zookeeper-->
    <exclusions>
        <exclusion>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!--添加zookeeper3.4.9版本-->
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.4.12</version>
</dependency>
```

```yaml
server:
  port: 80

#服务别名----注册zookeeper到注册中心名称
spring:
  application:
    name: cloud-consumer-order
  cloud:
    zookeeper:
      connect-string: 121.43.55.*:2181 
```

```java
@SpringBootApplication
@EnableDiscoveryClient
public class OrderMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain80.class,args);
    }
}
```



**测试调用**

```java
@RestController
public class OrderController {
    public static final String INVOKE_URL="http://cloud-provider-payment/payment/zk";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/order/payment")
    public String payInfo(){
        String result=restTemplate.getForObject(INVOKE_URL,String.class);
        return result;
    }
}
```



### Consul

- Consul是一套开源的分布式服务发现和配置管理系统
- 提供了微服务系统中的服务治理、配置中心、控制总线等功能。这些功能中的每一个都可以根据需要单独使用，也可以一起使用以构建全方位的服务网格，总之Consul提供了一种完整的服务网格解决方案。

```yml
#启动开发模式（快速启动一个单节点Consul,但是不能数据持久化，不能用于生产环境）
consul agent -dev
```



#### 服务注册

```xml
<!--SpringCloud consul-server -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
```

```yml
spring:
  application:
    name: consul-provider-payment
  ####consul注册中心地址
  cloud:
    consul:
      host: localhost
      port: 8500
```

```java
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentMain8006 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8006.class,args);
    }
}
```





#### 消费注册

- 注册同上
- 调用服务

```java
@RestController
public class OrderController {
    public static final String INVOKE_URL="http://consul-provider-payment/payment/zk";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/order/payment")
    public String payInfo(){
        String result=restTemplate.getForObject(INVOKE_URL,String.class);
        return result;
    }
}
```



通过**http://localhost:8500/**可进入可视化页面

![image-20210625212139548](img\11.png)



### CAP特点

CAP：

- C：Consistency (强一致性)

- A：Availability (可用性)

- P：Partition tolerance （分区容错性)


![img](https://img-blog.csdnimg.cn/img_convert/b41e0791c9652955dd3a2bc9d2d60983.png)

CAP理论的核心是：一个分布式系统**不可能**同时很好的满足**一致性，可用性和分区容错性**这三个需求。

因此，根据CAP原理将NoSQL数据库分成了满足CA原则、满足CP原则和满足AP原则三大类:

- CA - 单点集群，满足—致性，可用性的系统，通常在可扩展性上不太强大。
- CP - 满足一致性，分区容忍必的系统，通常性能不是特别高。                   （Zookeeper，Consul）
- AP - 满足可用性，分区容忍性的系统，通常可能对一致性要求低一些。    （Eureka）



## 服务调用

### Ribbon

Spring Cloud Ribbon是基于Netflix Ribbon实现的一套**客户端负载均衡的工具**。



先前工程项目没有引入spring-cloud-starter-ribbon也可以使用ribbon。

```xml
<dependency>
    <groupld>org.springframework.cloud</groupld>
    <artifactld>spring-cloud-starter-netflix-ribbon</artifactid>
</dependency>
```

这是因为spring-cloud-starter-netflix-eureka-client自带了spring-cloud-starter-ribbon引用。





```java
@Configuration
public class ApplicationConfig {
    @Bean
    @LoadBalanced  //加上此注解即可实现负载均衡
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```



**RestTemplate**

getForObject() / getForEntity() - **GET请求方法**

- getForObject()：返回对象为响应体中数据Json数据。

- getForEntity()：返回对象为ResponseEntity对象，包含了响应中的一些重要信息，比如响应头、响应状态码、响应体等。

```java
@GetMapping("/consumer/entityGet")
public CommonResult<Payment> EntityGet(int id){
    ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/get?id="+id, CommonResult.class);
    if (entity.getStatusCode().is2xxSuccessful()){
        System.out.println(entity.getStatusCode());  //获取状态码
        System.out.println(entity.getHeaders());  //获取头信息
        return entity.getBody();
    }else{
        return new CommonResult<>(entity.getStatusCodeValue(),"操作失败");
    }
}
```





#### 负载均衡规则

lRule：根据特定算法中从服务列表中选取一个要访问的服务

![img](https://img-blog.csdnimg.cn/img_convert/87243c00c0aaea211819c0d8fc97e445.png)



| RoundRobinRule            | 轮询 （默认）                                                |
| ------------------------- | ------------------------------------------------------------ |
| RandomRule                | 随机                                                         |
| RetryRule                 | 先按照RoundRobinRule的策略获取服务，如果获取服务失败则在指定时间内会进行重试 |
| WeightedResponseTimeRule  | 对RoundRobinRule的扩展，响应速度越快的实例选择权重越大，越容易被选择 |
| BestAvailableRule         | 会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务 |
| AvailabilityFilteringRule | 先过滤掉故障实例，再选择并发较小的实例                       |
| ZoneAvoidanceRule         | 默认规则,复合判断server所在区域的性能和server的可用性选择服务器 |





#### 规则替换

这个自定义配置类不能放在**@ComponentScan**所扫描的当前包下以及子包下，

![image-20210626171716333](img\12.png)



```java
@Configuration
public class MySelfRule {
    @Bean
    public IRule myRule(){
        return new RandomRule();  //定义为随机
    }
}
```

**主启动类**

```java
@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name = "CLOUD-PAYMENT-SERVICE",configuration = MySelfRule.class)  //加上此注解实现规则替换
public class OrderMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain80.class,args);
    }
}
```



### OpenFeign

**Feign能干什么**

- Feign旨在使编写Java Http客户端变得更容易。
- **通过feign只需要定义服务绑定接口且以声明式的方法**，优雅而简单的实现了服务调用。

**OpenFeign**

- OpenFeign是Spring Cloud在Feign的基础上支持了SpringMVC的注解,如@RequesMapping等等
- OpenFeign的@Feignclient可以解析SpringMVC的@RequestMapping注解下的接口，并通过动态代理的方式产生实现类，实现类中做负载均衡并调用其他服务。

```xml
<!--openfeign-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```



#### 服务调用

```yaml
#yml配置文件
eureka:
  client:
    register-with-eureka: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
```

```java
//主启动
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class OrderFeignMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderFeignMain80.class,args);
    }
}
```



业务类

- 业务逻辑接口+@FeignClient配置调用provider服务
- 新建PaymentFeignService接口并新增注解@FeignClient

**Service层**

```java
@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE")   //此处是服务注册中心-消费端的名字
public interface PaymentFeignService
{
    @GetMapping(value = "/payment/get/{id}")  //此处的Mapping需要和 服务端的Mapping相同
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);

}
```

**controller层**

```java
@RestController
@Slf4j
public class OrderFeignController
{
    @Resource
    private PaymentFeignService paymentFeignService;

    @GetMapping(value = "/consumer/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id)
    {
        return paymentFeignService.getPaymentById(id);
    }
}
```

- Feign自带**负载均衡**配置项
- 接口的Mapping需要和 服务端的Mapping相同



#### 超时

- 当Feign访问服务端时，**默认时间为1秒**
- 当服务端的相应时间超过时，便会报超时异常



**YML文件里需要开启OpenFeign客户端超时控制**

```yaml
#设置feign客户端超时时间(OpenFeign默认支持ribbon)(单位：毫秒)
ribbon:
  #指的是建立连接后从服务器读取到可用资源所用的时间
  ReadTimeout: 5000
  #指的是建立连接所用的时间，适用于网络状况正常的情况下,两端连接所用的时间
  ConnectTimeout: 5000
```



#### 日志

Feign提供了日志打印功能，我们可以通过配置来调整日恙级别，从而了解Feign 中 Http请求的细节。



日志级别

- NONE：默认的，不显示任何日志;
- BASIC：仅记录请求方法、URL、响应状态码及执行时间;
- HEADERS：除了BASIC中定义的信息之外，还有请求和响应的头信息;
- FULL：除了HEADERS中定义的信息之外，还有请求和响应的正文及元数据。

配置日志bean

```java
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    Logger.Level feignLoggerLevel()
    {
        return Logger.Level.FULL;
    }
}
```


YML文件里需要开启日志的Feign客户端

```yaml
logging:
  level:
    # feign日志以什么级别监控哪个接口
    com.lun.springcloud.service.PaymentFeignService: debug
```







## 服务降级

### Hystrix

Hystrix是一个用于处理分布式系统的**延迟**和**容错**的开源库，在分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，Hystrix能够保证在一个依赖出问题的情况下，**不会导致整体服务失败，避免级联故障，以提高分布式系统的弹性**。



#### 服务降级

服务器忙，请稍后再试，不让客户端等待并立刻返回一个友好提示，fallback

> **哪些情况会出发降级**

- 程序运行导常
- 超时
- 服务熔断触发服务降级
- 线程池/信号量打满也会导致服务降级



```xml
<!--hystrix-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```



超时导致服务器变慢(转圈) - 超时不再等待

出错(宕机或程序运行出错) - 出错要有兜底

解决：

- 对方服务(8001)超时了，调用者(80)不能一直卡死等待，必须有服务降级。
- 对方服务(8001)down机了，调用者(80)不能一直卡死等待，必须有服务降级。
- 对方服务(8001)OK，调用者(80)自己出故障或有自我要求(自己的等待时间小于服务提供者)，自己处理降级。





需要在主启动类添加**@EnableHystrix** 注解

1. 然后对可能出现超时或错误的方法进行服务降级
2. **无论**该方法**超时**还是**发生异常 **都会调用指定的fallback方法

```java
    @Override
    @HystrixCommand(fallbackMethod = "payment_Timeout",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="3000")
    })
    public String paymentInfo_error(int id) {
//        if(id==10){
//            throw new MyException(444,"asd");
//        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程："+Thread.currentThread().getName()+" \t error"+id;
    }

    public String payment_Timeout(int id){
        return "线程："+Thread.currentThread().getName()+" \t error"+id+" time out";
    }
```



> 消费端降级

当使用feign进行服务调用时

```yml
#开启
feign:
  hystrix:
    enabled: true
```

```java
@SpringBootApplication
@EnableFeignClients
@EnableHystrix//添加到此处
public class OrderHystrixMain80{
    public static void main(String[] args){
        SpringApplication.run(OrderHystrixMain80.class,args);
    }
}
```
```java
@GetMapping("/feign/payment/hystrix/error/{id}")
@HystrixCommand(fallbackMethod = "paymentTimeOutFallbackMethod",commandProperties = {
        @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="15000")
})
public String paymentInfo_TimeOut(@PathVariable("id") Integer id)
{
    return paymentFeignService.paymentInfo_TimeOut(id);
}

//降级方法
public String paymentTimeOutFallbackMethod(@PathVariable("id") Integer id){
    return "系统繁忙";
}
```



##### 全局配置降级

- 除了个别重要核心业务有专属，其它普通的可以通过@DefaultProperties(defaultFallback = “”)统一跳转到统一处理结果页面
- 通用的和独享的各自分开，避免了代码膨胀，合理减少了代码量
- 全局配置需要 方法与 指定Fallback 方法 **返回类型一致**

```java
@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")  //使用此注解指定Fallback方法
public class OrderFeignController {
 	---
        
    @GetMapping("/consumer/get")
    @HystrixCommand                          //需要调用全局配置时 使用此注解
    public CommonResult getPayment(){
        int a=1/0;
        return paymentFeignService.discovery();
    }

        // 下面是全局fallback方法
    public CommonResult payment_Global_FallbackMethod()
    {
        return new CommonResult(444,"系统错误");
    }
    
    
    @GetMapping("/feign/payment/hystrix/error/{id}")
    @HystrixCommand(fallbackMethod = "paymentTimeOutFallbackMethod",commandProperties = {              //指定降级方法时调用此注解
        @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="15000")
    })
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id)
    {
        return paymentFeignService.paymentInfo_TimeOut(id);
    }

    //降级方法
    public String paymentTimeOutFallbackMethod(@PathVariable("id") Integer id){
        return "系统繁忙";
    }
}
```





另外 当**服务端宕机**时，我们可以通过Feign的接口去实现对接口的兜底方法

```java
@Service
public class PaymentFeignServiceImpl implements PaymentFeignService {  //实现接口
    @Override
    public CommonResult discovery() {
        return new CommonResult(444,"无法调用该服务");
    }

    @Override
    public String paymentInfo_OK(Integer id) {
        return "-----PaymentFallbackService fall paymentInfo_OK";
    }

    @Override
    public String paymentInfo_TimeOut(Integer id) {
        return "-----PaymentFallbackService fall paymentInfo_TimeOut";
    }
}
```

实现类

```java
@Component
@FeignClient(value = "CLOUD-PAYMENT-SERVICE",fallback = PaymentFeignServiceImpl.class)  //首选会调取注册中心的服务方法，若宕机，便选择实现类方法
public interface PaymentFeignService {
    @GetMapping("/discovery")
    public CommonResult discovery();

    @GetMapping("/payment/hystrix/ok/{id}")
    String paymentInfo_OK(@PathVariable("id") Integer id);

    @GetMapping("/payment/hystrix/error/{id}")
    String paymentInfo_TimeOut(@PathVariable("id") Integer id);
}
```





#### 服务熔断

熔断机制是应对雪崩效应的一种微服务链路保护机制。当扇出链路的某个微服务出错不可用或者响应时间太长时，会进行服务的降级，进而熔断该节点微服务的调用，快速返回错误的响应信息。**当检测到该节点微服务调用响应正常后，恢复调用链路**。



发生熔断

- 达到请求次数 （默认10s内 20次请请求）
- 超过允许的错误的百分比  （默认超过50%失败）
- 达到以上阈值，熔断器便会打开  
- 当打开时，所有的请求都不会进行转发
- 一段时间之后（默认是5秒)，这个时候断路器是半开状态，会让其中一个请求进行转发。如果成功，断路器会关闭，若失败，继续开启。



```java
//=====服务熔断
@HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
        @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),// 是否开启断路器
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),// 请求次数
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"), // 时间窗口期
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),// 失败率达到多少后跳闸
})
public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
    if(id < 0) {
        throw new RuntimeException("******id 不能负数");
    }
    String serialNumber = IdUtil.simpleUUID();

    return Thread.currentThread().getName()+"\t"+"调用成功，流水号: " + serialNumber;
}

public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id) {
    return "id 不能负数   id: " +id;
}
```



> 全部配置

```java
@HystrixCommand(fallbackMethod = "fallbackMethod", 
                groupKey = "strGroupCommand", 
                commandKey = "strCommand", 
                threadPoolKey = "strThreadPool",
                
                commandProperties = {
                    // 设置隔离策略，THREAD 表示线程池 SEMAPHORE：信号池隔离
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
                    // 当隔离策略选择信号池隔离的时候，用来设置信号池的大小（最大并发数）
                    @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "10"),
                    // 配置命令执行的超时时间
                    @HystrixProperty(name = "execution.isolation.thread.timeoutinMilliseconds", value = "10"),
                    // 是否启用超时时间
                    @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
                    // 执行超时的时候是否中断
                    @HystrixProperty(name = "execution.isolation.thread.interruptOnTimeout", value = "true"),
                    
                    // 执行被取消的时候是否中断
                    @HystrixProperty(name = "execution.isolation.thread.interruptOnCancel", value = "true"),
                    // 允许回调方法执行的最大并发数
                    @HystrixProperty(name = "fallback.isolation.semaphore.maxConcurrentRequests", value = "10"),
                    // 服务降级是否启用，是否执行回调函数
                    @HystrixProperty(name = "fallback.enabled", value = "true"),
                    // 是否启用断路器
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
                    // 该属性用来设置在滚动时间窗中，断路器熔断的最小请求数。例如，默认该值为 20 的时候，如果滚动时间窗（默认10秒）内仅收到了19个请求， 即使这19个请求都失败了，断路器也不会打开。
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "20"),
                    
                    // 该属性用来设置在滚动时间窗中，表示在滚动时间窗中，在请求数量超过 circuitBreaker.requestVolumeThreshold 的情况下，如果错误请求数的百分比超过50, 就把断路器设置为 "打开" 状态，否则就设置为 "关闭" 状态。
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    // 该属性用来设置当断路器打开之后的休眠时间窗。 休眠时间窗结束之后，会将断路器置为 "半开" 状态，尝试熔断的请求命令，如果依然失败就将断路器继续设置为 "打开" 状态，如果成功就设置为 "关闭" 状态。
                    @HystrixProperty(name = "circuitBreaker.sleepWindowinMilliseconds", value = "5000"),
                    // 断路器强制打开
                    @HystrixProperty(name = "circuitBreaker.forceOpen", value = "false"),
                    // 断路器强制关闭
                    @HystrixProperty(name = "circuitBreaker.forceClosed", value = "false"),
                    // 滚动时间窗设置，该时间用于断路器判断健康度时需要收集信息的持续时间
                    @HystrixProperty(name = "metrics.rollingStats.timeinMilliseconds", value = "10000"),
                    
                    // 该属性用来设置滚动时间窗统计指标信息时划分"桶"的数量，断路器在收集指标信息的时候会根据设置的时间窗长度拆分成多个 "桶" 来累计各度量值，每个"桶"记录了一段时间内的采集指标。
                    // 比如 10 秒内拆分成 10 个"桶"收集这样，所以 timeinMilliseconds 必须能被 numBuckets 整除。否则会抛异常
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "10"),
                    // 该属性用来设置对命令执行的延迟是否使用百分位数来跟踪和计算。如果设置为 false, 那么所有的概要统计都将返回 -1。
                    @HystrixProperty(name = "metrics.rollingPercentile.enabled", value = "false"),
                    // 该属性用来设置百分位统计的滚动窗口的持续时间，单位为毫秒。
                    @HystrixProperty(name = "metrics.rollingPercentile.timeInMilliseconds", value = "60000"),
                    // 该属性用来设置百分位统计滚动窗口中使用 “ 桶 ”的数量。
                    @HystrixProperty(name = "metrics.rollingPercentile.numBuckets", value = "60000"),
                    // 该属性用来设置在执行过程中每个 “桶” 中保留的最大执行次数。如果在滚动时间窗内发生超过该设定值的执行次数，
                    // 就从最初的位置开始重写。例如，将该值设置为100, 滚动窗口为10秒，若在10秒内一个 “桶 ”中发生了500次执行，
                    // 那么该 “桶” 中只保留 最后的100次执行的统计。另外，增加该值的大小将会增加内存量的消耗，并增加排序百分位数所需的计算时间。
                    @HystrixProperty(name = "metrics.rollingPercentile.bucketSize", value = "100"),
                    
                    // 该属性用来设置采集影响断路器状态的健康快照（请求的成功、 错误百分比）的间隔等待时间。
                    @HystrixProperty(name = "metrics.healthSnapshot.intervalinMilliseconds", value = "500"),
                    // 是否开启请求缓存
                    @HystrixProperty(name = "requestCache.enabled", value = "true"),
                    // HystrixCommand的执行和事件是否打印日志到 HystrixRequestLog 中
                    @HystrixProperty(name = "requestLog.enabled", value = "true"),

                },
                threadPoolProperties = {
                    // 该参数用来设置执行命令线程池的核心线程数，该值也就是命令执行的最大并发量
                    @HystrixProperty(name = "coreSize", value = "10"),
                    // 该参数用来设置线程池的最大队列大小。当设置为 -1 时，线程池将使用 SynchronousQueue 实现的队列，否则将使用 LinkedBlockingQueue 实现的队列。
                    @HystrixProperty(name = "maxQueueSize", value = "-1"),
                    // 该参数用来为队列设置拒绝阈值。 通过该参数， 即使队列没有达到最大值也能拒绝请求。
                    // 该参数主要是对 LinkedBlockingQueue 队列的补充,因为 LinkedBlockingQueue 队列不能动态修改它的对象大小，而通过该属性就可以调整拒绝请求的队列大小了。
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "5"),
                }
               )
public String doSomething() {
	...
}
```





#### Hystrix图形化



注册端加入依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
```
```yml
server:
  port: 9001
```

```java
@SpringBootApplication
@EnableHystrixDashboard
public class HystrixDashboardMain9001
{
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardMain9001.class, args);
    }
}
```

启动后访问**http://localhost:9001/hystrix**

![image-20210706223940464](img\13.png)





**服务端**

加入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```



```java
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
public class PaymentHystrixMain8001
{
    public static void main(String[] args) {
            SpringApplication.run(PaymentHystrixMain8001.class, args);
    }


    /**
     *此配置是为了服务监控而配置，与服务容错本身无关，springcloud升级后的坑
     *ServletRegistrationBean因为springboot的默认路径不是"/hystrix.stream"，
     *只要在自己的项目里配置上下面的servlet就可以了
     *否则，Unable to connect to Command Metric Stream 404
     */
    @Bean
    public ServletRegistrationBean getServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
}
```

![image-20210706224944437](img\14.png)

点击按钮， 即可查看降级信息



## 服务网关

### GetWay

- Gateway是在Spring生态系统之上构建的API网关服务，基于Spring 5，Spring Boot 2和Project Reactor等技术。
- Gateway旨在提供一种简单而有效的方式来对API进行路由，以及提供一些强大的过滤器功能，例如:熔断、限流、重试等

**作用**

- 反向代理
- 鉴权
- 流量控制
- 熔断
- 日志监控
- …

**微服务架构中网关的位置**

![img](https://img-blog.csdnimg.cn/img_convert/5877d4b9035ead9cd2d037609dceb442.png)



#### 项目搭建

```xml
    <!--gateway-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
```

```yaml
server:
  port: 9527

spring:
  application:
    name: cloud-gateway

eureka:
  instance:
    hostname: cloud-gateway-service
  client: #服务提供者provider注册进eureka服务列表内
    service-url:
      register-with-eureka: true
      fetch-registry: true
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
```

主启动

```java
@SpringBootApplication
@EnableEurekaClient
public class GetMain9527 {
    public static void main(String[] args) {
        SpringApplication.run(GetMain9527.class,args);
    }
}
```



#### 配置网关

> 方法1.配置yml

```yaml
server:
  port: 9527

spring:
  application:
    name: cloud-gateway
###########################################3
  cloud:
    gateway:
      routes:
        - id: payment_routh #payment_route    #路由的ID，没有固定规则但要求唯一，建议配合服务名
          uri: http://localhost:8001          #匹配后提供服务的路由地址
          #uri: lb://cloud-payment-service #匹配后提供服务的路由地址
          predicates:
            - Path=/payment/circuit/**         # 断言，路径相匹配的进行路由

        - id: payment_routh2 #payment_route    #路由的ID，没有固定规则但要求唯一，建议配合服务名
          uri: http://localhost:8001          #匹配后提供服务的路由地址
          #uri: lb://cloud-payment-service #匹配后提供服务的路由地址
          predicates:
            - Path=/discovery         # 断言，路径相匹配的进行路由

############################################
eureka:
  instance:
    hostname: cloud-gateway-service
  client: #服务提供者provider注册进eureka服务列表内
    service-url:
      register-with-eureka: true
      fetch-registry: true
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
```



分别调用

- http://localhost:8001/payment/circuit/1
- http://localhost:9527/payment/circuit/1

![image-20210708093243474](img\15.png)

可实现9527网关访问8001的 路由匹配



> 方法2.配置config

```java
@Configuration
public class GetWayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder)
    {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("path_route_csdn",
                r -> r.path("/csdn")
                        .uri("https://blog.csdn.net/nate_18163126")).build();
        return routes.build();
    }

    @Bean
    public RouteLocator customRouteLocator2(RouteLocatorBuilder routeLocatorBuilder)
    {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("path_route_bli",
                r -> r.path("/get")
                        .uri("http://localhost:8001/get")).build();
        return routes.build();
    }
}
```



#### 动态路由

默认情况下Gateway会根据注册中心注册的服务列表，以注册中心上微服务名为路径创建**动态路由进行转发，从而实现动态路由的功能**

服务端需要加入

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```



```yml
spring:
  application:
    name: cloud-gateway

  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
      routes:
        - id: payment_routh #payment_route    #路由的ID，没有固定规则但要求唯一，建议配合服务名
          #uri: http://localhost:8001
          uri: lb://cloud-payment-service #匹配后提供服务的路由地址
          predicates:
            - Path=/payment/circuit/**         # 断言，路径相匹配的进行路由

        - id: payment_routh2 #payment_route    #路由的ID，没有固定规则但要求唯一，建议配合服务名
#          uri: http://localhost:8001
          uri: lb://cloud-payment-service #匹配后提供服务的路由地址
          predicates:
            - Path=/discovery         # 断言，路径相匹配的进行路由
```

- 将url改为注册中心的服务名，即可实现负载均衡





#### 断言

- 断言：predicates
- Predicate就是为了实现一组匹配规则，让请求过来找到对应的Route进行处理。

常用的Route种类

- The **After** Route Predicate Factory
- The **Before** Route Predicate Factory
- The **Between** Route Predicate Factory
- The **Cookie** Route Predicate Factory
- The Header Route Predicate Factory
- The Host Route Predicate Factory
- The Method Route Predicate Factory
- The Path Route Predicate Factory
- The Query Route Predicate Factory
- The RemoteAddr Route Predicate Factory
- The weight Route Predicate Factory



**After路由断言**

```yml
spring:
  application:
    name: cloud-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
      routes:
        - id: payment_routh2 #payment_route    #路由的ID，没有固定规则但要求唯一，建议配合服务名
          uri: lb://cloud-payment-service #匹配后提供服务的路由地址
          predicates:
            - Path=/discovery         # 断言，路径相匹配的进行路由
            - After=2021-07-11T16:04:57.939+08:00[Asia/Shanghai]   # 这个时间后才能起效
```

时间戳获得方式

```java
public static void main(String[] args) {
    ZonedDateTime time=ZonedDateTime.now();
    System.out.println(time);
}
```

**Before同理**

**Between**配置

```yml
spring:
  cloud:
    gateway:
      routes:
      - id: between_route
        uri: https://example.org
        # 两个时间点之间
        predicates:
        - Between=2017-01-20T17:42:47.789-07:00[America/Denver], 2017-01-21T17:42:47.789-07:00[America/Denver]
```

**Cookie配置**

- 必须要带上   username为zcj的cookie才能访问

```yml
spring:
  cloud:
    gateway:
      routes:
        - id: payment_routh2 #payment_route    #路由的ID，没有固定规则但要求唯一，建议配合服务名
#          uri: http://localhost:8001          #匹配后提供服务的路由地址
          uri: lb://cloud-payment-service #匹配后提供服务的路由地址
          predicates:
            - Path=/discovery         # 断言，路径相匹配的进行路由
            - Cookie=username, zcj     #必须要带上   username为zcj的cookie才能访问
```

**访问**

```cmd
curl http://localhost:9527/discovery --cookie "username=zcj"   #cmd
	{"code":200,"message":"请求成功8001","data":["cloud-gateway","cloud-payment-service"]}
```





**Header配置**

```yml
          predicates:
            - Path=/discovery         # 断言，路径相匹配的进行路由
            - Header=count, \d+       # 需要 header携带的count为数字
```

![image-20210711180830742](img\16.png)



**Method配置**

```yml
          predicates:
            - Path=/discovery         # 断言，路径相匹配的进行路由
#            - Cookie=username, zcj
#            - Header=count, \d+
            - Method=GET
```





#### Filter

- 路由过滤器可用于修改进入的HTTP请求和返回的HTTP响应，路由过滤器只能指定路由进行使用。Spring Cloud Gateway内置了多种路由过滤器，他们都由GatewayFilter的工厂类来产生。

```java
@Component
@Slf4j
public class GateWayFilter implements GlobalFilter, Ordered {
    //过滤器配置
    //必须携带name的参数的请求才能通过过滤器
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {  
        log.info("---------"+new Date());
        String name = exchange.getRequest().getQueryParams().getFirst("name");
        if (name==null){
            log.info("----------非法用户");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
```



## 服务配置



### Config

![img](https://img-blog.csdnimg.cn/img_convert/d5462e3b8c3a063561f5f8fc7fde327e.png)

SpringCloud Config为微服务架构中的微服务提供集中化的外部配置支持，配置服务器为各个不同微服务应用的所有环境提供了一个中心化的外部配置。



```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>
```

**主启动**

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigMain3344 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigMain3344.class,args);
    }
}
```

**yml配置文件**

```yml
server:
  port: 3344

spring:
  application:
    name:  cloud-config-center #注册进Eureka服务器的微服务名
  cloud:
    config:
      server:
        git:
          uri: https://github.com/Teacher-Ma-2020/springcloud-config.git #GitHub上面的git仓库名字
          ####搜索目录
          search-paths:
            - springcloud-config

      ####读取分支
      label: master

#服务注册到eureka地址
eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka
```

**访问网址**：http://localhost:3344/master/application.yml  即可获得配置文件

- 格式：/ {label} /  {application}-{profile}.yml

![image-20210712092855659](img\17.png)





## 服务总线

### BUS

Spring Cloud Bus是用来将分布式系统的节点与轻量级消息系统链接起来的框架，它整合了Java的事件处理机制和消息中间件的功能。Spring Clud Bus目前支持RabbitMQ和Kafka。

**能干嘛**

Spring Cloud Bus能管理和传播分布式系统间的消息，就像一个分布式执行器，可用于广播状态更改、事件推送等，也可以当作微服务间的通信通道。

![img](https://img-blog.csdnimg.cn/img_convert/26c6ced30935219d4717814a446eb67a.png)



