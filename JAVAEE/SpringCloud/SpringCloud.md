



# SpringCloud

参考视频：https://www.bilibili.com/video/BV18E411x7eT

参考博客：https://blog.csdn.net/u011863024/article/details/114298270



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

