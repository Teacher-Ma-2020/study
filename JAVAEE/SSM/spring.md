# Spring



## 简介

![1](img\1.png)

- spring是轻量级、非入侵式的框架
- 控制反转（IOC） 面向切面编程（AOP）
- 支持事务的处理、对框架整合的支持



**Spring就是轻量级的控制反转（IOC） 面向切面编程（AOP）的框架**



## Maven包

```xml
    <dependencies>
        <dependency>
<!--            spring-->
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.2.9.RELEASE</version>
        </dependency>

        <!--        spring-jdbc-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.2.9.RELEASE</version>
        </dependency>

<!--        mybatis-spring-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.5</version>
        </dependency>
        <!--        mybatis-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.2</version>
        </dependency>
        <!--        mysql依赖-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.46</version>
        </dependency>


        <!--        lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.12</version>
        </dependency>

<!--        aop-->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.9.4</version>
        </dependency>
<!--        junit-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
        </dependency>

    </dependencies>


    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.12.4</version>
                <configuration>
                    <skipTests>true</skipTests>
                </configuration>
            </plugin>
        </plugins>

        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
        </resources>
    </build>
```



## IOC



### IOC理论

控制反转

**控制反转**（Inversion of Control，缩写为**IoC**），是[面向对象编程](https://baike.baidu.com/item/面向对象编程)中的一种设计原则，可以用来减低计算机[代码](https://baike.baidu.com/item/代码/86048)之间的[耦合度](https://baike.baidu.com/item/耦合度)。其中最常见的方式叫做**依赖注入**（Dependency Injection，简称**DI**）

![image-20201019214848335](\img\2.png)

**控制**：传统应用的对象又程序本身控制，使用spring后 对象由Spring控制

**反转**：程序本身不创建对象，而变成被动的接收对象

**依赖注入**：利用set方法来为对象注入

**ioc是一种编程思想，由主动编程变成被动的接收**

**IOC是spring的核心内容**



```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">



</beans>
```



### Ioc创建流程

配置 beans.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="mysql" class="com.jie.dao.UserMysqlImpl"/>

    <bean id="userDao" class="com.jie.dao.UserDaoImpl"/>

    <bean id="userService" class="com.jie.service.UserServiceImpl">
<!--        ref 引用Spring中创建好的对象-->
        <property name="userDao" ref="mysql"/>
    </bean>

</beans>
```

获取spring的容器  **由spring来实例对象**

```java
ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
UserServiceImpl service = (UserServiceImpl)context.getBean("userService");
service.getUser();
```





### IOC设置



#### 创建对象

**ioc默认调用无参构造** **并使用set方法传值**



若无无参构造 则有以下方法

1. 下标传值

   ```xml
   <bean id="zcj" class="com.jie.pojo.User">
       <constructor-arg index="0" value="zcj"/>
   </bean>
   ```

2. 根据数据类型**（不建议使用）**

   ```xml
   <bean id="zcj" class="com.jie.pojo.User">
       <constructor-arg index="0" value="zcj"/>
   </bean>
   ```

3. 直接通过参数名 **（推荐)**

```xml
<bean id="zcj" class="com.jie.pojo.User">
    <constructor-arg name="name" value="zcj2"/>
</bean>
```



#### 配置

##### **别名**  

别名与本身的名字都可以使用

1.

```xml
<alias name="zcj" alias="user"/>
```

2.  在name中设置别名（可以使用， 空格   ； 分割）

```xml
<bean id="zcj" class="com.jie.pojo.User" name="zcj1,zcj2,zcj3">
    <constructor-arg name="name" value="zcj2"/>
</bean>
```



##### import导入bean.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
    <import resource="beans.xml"/>
    <import resource="beans2.xml"/>

</beans>
```





## 依赖注入

### 构造器注入



### set注入

- 依赖注入：set注入

  - 依赖：bean对象的创建全部依赖容器
  - 注入：bean对象的所有属性，由容器来创建

- 方式

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://www.springframework.org/schema/beans
          https://www.springframework.org/schema/beans/spring-beans.xsd">
      <bean id="add" class="com.jie.pojo.Address">
          <property name="address" value="芜湖"/>
      </bean>
  
      <bean id="student" class="com.jie.pojo.Student">
  <!--        第一种 value注入 -->
          <property name="name" value="周成杰"/>
  <!--        第二种 bean注入-->
          <property name="address" ref="add"/>
  <!--        数组注入-->
          <property name="books">
              <array>
                  <value>java入门</value>
                  <value>c++入门</value>
                  <value>c#入门</value>
                  <value>python入门</value>
              </array>
          </property>
  <!--        list注入-->
          <property name="hobbies">
              <list>
                  <value>打游戏</value>
                  <value>看电影</value>
              </list>
          </property>
  <!--        map注入-->
          <property name="card">
              <map>
                  <entry key="身份证" value="123456"/>
                  <entry key="银行卡" value="123123213123"/>
              </map>
          </property>
  <!--        set注入-->
          <property name="games">
              <set>
                  <value>lol</value>
                  <value>cf</value>
                  <value>dnf</value>
              </set>
          </property>
          <property name="WIFI">
              <null/>
          </property>
          <property name="info">
              <props>
                  <prop key="username">admin</prop>
                  <prop key="password">123456</prop>
              </props>
          </property>
      </bean>
  </beans>
  ```

  

### 扩展方式注入

**p命名空间注入  （利用无参构造）**

引入约束标签

```xml
xmlns:p="http://www.springframework.org/schema/p"
```

配置

```xml
<!--    p命名空间注入-->
    <bean id="user" class="com.jie.pojo.User" p:name="zcj" p:age="18"/>
```



**c命名空间注入  （利用有参构造）**

```xml
xmlns:c="http://www.springframework.org/schema/c"
```

```xml
<!--    c命名空间注入  通过构造器注入-->
    <bean id="user2" class="com.jie.pojo.User" c:name="zcj2" c:age="21"/>
```





### bean作用域

![image-20201021205814476](D:\study\img\3.png)

1、单例模式（spring默认就是单例模式）

```xml
<bean id="user2" class="com.jie.pojo.User" c:name="zcj2" c:age="21" scope="singleton"/>
```

2.原型模式   

**从容器中获取的为创建的新对象**

```xml
<bean id="user2" class="com.jie.pojo.User" c:name="zcj2" c:age="21" scope="prototype"/>
```

3.其余的只能在wep中使用





## Bean自动装配

- 自动装配是Spring满足bean依赖的一种方式
- Spring会在上下文中自动寻找，并给bean装配属性

spring中的三种装配方式

1. 在xml中显示的配置
2. 在java中显示配置
3. 隐式的自动装配bean**【重点】**



### 1.byname自动装配

**根据set方法后面的名字自动装配**,若名字不相同则装配不成功

```xml
<bean name="people" class="com.jie.pojo.People" autowire="byName">
    <property name="name" value="zcj"/>
</bean>
```

### 2.byType自动装配

**根据数据类型装配**   但容器里的匹配class为唯一  否则报错

```xml
<bean name="people" class="com.jie.pojo.People" autowire="byType">
    <property name="name" value="zcj"/>
</bean>
```



### 注解自动装配

需要：

导入约束

```xml
xmlns:context="http://www.springframework.org/schema/context"
~~~~
http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context.xsd">
```

导入注解的支持

```xml
<context:annotation-config/>
```

如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       https://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">
    <context:annotation-config/>


</beans>
```



#### @Autowired

直接在**属性**上使用 或者在**set方法**使用

在ioc容器中寻找**类的 类型** 进行装配    （**byType**的方式）



```javascript
@Autowired
@Qualifier(value = "dog123")
private Dog dog;
```

添加注解   @Qualifier(value = "dog123")  可以指定名字对应的类进行装配



#### @Resource

默认与类型匹配  括号可以设置名字（“java原生态注解”）

```java
@Resource
private Cat cat;

@Resource(name = "dog123")
private Dog dog;
```

总结：

**@Autowired** 默认按type注入
**@Qualifier("cusInfoService") **  一般作为@Autowired()的修饰用



**@Resource(name="cusInfoService")** 默认按name注入，可以通过name和type属性进行选择性注入





## 注解开发



### IOC容器

**applicationContext.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       https://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>
    <context:component-scan base-package="com.jie"/>

</beans>
```



### 属性注入



导入范围包

```xml
<context:component-scan base-package="com.jie"/>
```





#### **@Component**

放在类上  默认为放在IOC容器<bean>中  名字为首字母小写

```java
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public String name;
}
```



#### **@Value**

默认set值

```java
public class User {
    @Value("zcj")
    public String name;
}
```



####  **@Component**衍生注解



与Component相同   代表MVC三层架构不同的位置注解

dao：@Repository

service：@Service

controller: @Controller

功能相同





### 自动装配

#### **@Autowired** 

默认按type注入

#### @Qualifier

在Autowired上进行按名字精准匹配

- **@Qualifier("cusInfoService") **  一般作为@Autowired()的修饰用



#### @Resource

- **@Resource(name="cusInfoService")** 默认按name注入，可以通过name和type属性进行选择性注入

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class People {
    @Resource
    private Cat cat;

    @Resource()
    private Dog dog;

    private String name;

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```





### 作用域

#### @Scope

原型模式

```java
@Scope("prototype")
public class User {
    @Value("zcj")
    public String name;
}
```

单例模式

```java
@Scope("singleton")
public class User {
    @Value("zcj")
    public String name;
}
```



总结：

XML万能 适合各种场景

注解轻巧  维护相对复杂



**最佳：**

XML负责管理bean

注解只负责属性的注入



## Java配置Spring（Configuration）

### @Configuration

加上**@Configuration**注解后   这个类就会变为原来的IOC容器池  添加bean

能使用xml配置的任何功能



**实体类**

### @ComponentScan

扫描文件夹

**配置文件**

```java
@Configuration
@ComponentScan("com.jie.pojo")
@Import(jieConfig2.class)
public class jieConfig {
    
    @Bean
    @Scope("prototype")
    public User getUser(){
        System.out.println("123321");
        return  new User();
    }
}
```





### **@Bean**

在方法上加入**@Bean**  即为xml中的<bean> 注入



**测试类**

```java
public class test1 {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(jieConfig.class);
        User user = context.getBean("getUser", User.class);
        User user2 = context.getBean("user", User.class);
        System.out.println(user+" "+user2);
    }
}
```

若用appconfig配置  需要使用类

**new AnnotationConfigApplicationContext(jieConfig.class)**  读取





## 代理模式

代理模式

代理模式是AOP的底层

分为：静态代理和动态代理



### 静态代理

![image-20201023151113007](img\4.png)



抽象角色: 使用抽象类和接口来解决

```java
public interface Rent {
    void rent();
}
```

真实角色：被代理的角色

```java
public class Host implements Rent {
    public void rent() {
        System.out.println("租房");
    }
}
```

代理角色：代理真实角色，做真实角色的事后 可以做一下附属操作

```java
public class Proxy implements Rent {
    private Host host;

    public Proxy() {
    }

    public Proxy(Host host) {
        this.host = host;
    }

    public void rent() {
        say();
        host.rent();
        send();
    }

    public void send(){
        System.out.println("中介收费");
    }
    public void say(){
        System.out.println("中介找人");
    }
}
```

客户：访问代理客户的人

```java
public class Client {
    public static void main(String[] args) {
        Host host=new Host();
        host.rent();

        Proxy proxy=new Proxy(host);
        proxy.rent();
    }
}
```



优点：

- 可以使真实角色的操作更加纯粹，不用关注公共业务
- 公共交给了代理   实现了业务的分工
- 公共方法发展扩展的时候 方便管理

缺点：

- 一个真实角色代表一个代理角色   多个角色效率会便低  (使用动态代理解决)





### 动态代理

- 动态代理和静态代理相同
- 动态代理的代理类是动态生成的，不是直接写死的
- 动态代理分为两大类：基于接口的动态代理，基于类的动态代理
  - 基于接口：jdk动态代理
  - 基于类：cglib
  - java字节码实现：javasist 



jdk动态代理

实现**InvocationHandler**接口 

使用**Proxy**对象   **提供创建动态代理类和实例的静态方法**



模板

```java
package com.jie.demo04;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyInvocationHandler implements InvocationHandler {

    //被代理的接口
    private Object target;

    public void setTarget(Object target) {
        this.target = target;
    }


    //生成代理对象
    public Object getProxy(){
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }

    //处理代理实例
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log(method.toString());
        Object result=method.invoke(target,args);
        return result;
    }

    public void log(String msg){
        System.out.println("执行了"+msg);
    }

}

```



实例使用

```java
public class Client {
    public static void main(String[] args) {
        //真实角色
        Host host=new Host();

        //设置代理
        ProxyInvocationHandler p= new ProxyInvocationHandler();
        p.setTarget(host);

        //获得代理角色
        Rent rent= (Rent) p.getProxy();

        //执行
        rent.rent();
    }
}
```





## AOP

AOP(Aspect Oriented Programming)意为∶面向切面编程，通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术。AOP是OOP的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容，是函数式编程的一种衍生范型。利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。

![image-20201023210407337](img\5.png)



### Aop在Spring中的作用

提供声明式事务;允许用户自定义切面

- 横切关注点:跨越应用程序多个模块的方法或功能。即是，与我们业务逻辑无关的，但是我们需要关注的部分，就是横切关注点。如日志，安全，缓存，事务等等...
- 切面(ASPECT)︰横切关注点被模块化的特殊对象。即，它是一个类。
- 通知(Advice) :切面必须要完成的工作。即，它是类中的一个方法。
- 目标(Target)︰被通知对象。
- 代理(Proxy)︰向目标对象应用通知之后创建的对象。
- 切入点(PointCut) ︰切面通知执行的“地点"的定义。
- 连接点(JointPoint) :与切入点匹配的执行点。

![image-20201024172304687](img\6.png)



![image-20201024172354260](img\7.png)



### 使用springAPI接口

设置前置日志

```java
//spring的代理方法
public class Log implements MethodBeforeAdvice {

    //method: 要执行的目标对象的方法
    //arg  参数
    //target:目标对象
    public void before(Method method, Object[] arg, Object target) throws Throwable {
        System.out.println(target.getClass().getName()+"的"+method.getName()+"被执行了");
    }
}
```

后置日志

```java
public class AfterLog implements AfterReturningAdvice {
    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {
        System.out.println("执行了"+method.getName()+" 返回结果为:"+o);
    }
}
```

配置xml文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">

<!--    注册bean-->
    <bean id="userService" class="com.jie.service.UserServiceImpl"/>
    <bean id="log" class="com.jie.log.Log"/>
    <bean id="afterLog" class="com.jie.log.AfterLog"/>

<!--    配置aop-->
<!--    //方式1-->
    <aop:config>
<!--        切除点-->
        <aop:pointcut id="pointcut" expression="execution(* com.jie.service.UserServiceImpl.*(..))"/>
<!--        执行环绕-->
        <aop:advisor advice-ref="log" pointcut-ref="pointcut"/>
        <aop:advisor advice-ref="afterLog" pointcut-ref="pointcut"/>
    </aop:config>

</beans>
```

测试

```java
public class Test1 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        //代理是个接口
        UserService userService = context.getBean("userService", UserService.class);
        userService.add();
    }
}
```



### 使用自定义类

设置自定义类

```java
public class DiyProxy {
    public void before(){
        System.out.println("before");

    }

    public void after(){
        System.out.println("after");

    }
}
```

在xml中设置切面

```xml
<!--    方式2-->
    <bean id="diy" class="com.jie.div.DiyProxy"/>
    <aop:config>
<!--        自定义切面-->
        <aop:aspect ref="diy">
<!--            切入点-->
            <aop:pointcut id="point" expression="execution(* com.jie.service.UserServiceImpl.*(..))"/>
<!--            通知-->
            <aop:before method="before" pointcut-ref="point"/>
            <aop:after method="after" pointcut-ref="point"/>
        </aop:aspect>
    </aop:config>
```

测试

```java
public class Test2 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        //代理是个接口
        UserService userService = context.getBean("userService", UserService.class);
        userService.add();
    }
}
```



### 使用注解配置

```java
//标注为切面
@Aspect
public class AnnotationPoint {
    @Before("execution(* com.jie.service.UserServiceImpl.*(..))")
    public void before(){
        System.out.println("before");

    }

    @After("execution(* com.jie.service.UserServiceImpl.*(..))")
    public void after(){
        System.out.println("after");

    }
}
```

xml中配

```xml
    <!--    方式3-->
    <bean id="annotationPoint" class="com.jie.div.AnnotationPoint"/>
<!--    开启注解支持-->
    <aop:aspectj-autoproxy/>
```









