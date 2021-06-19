# 简介

**Spring MVC是Spring Framework的一部分，是基于Java实现MVC的轻量级Web框架。**

特点：

1.轻量级，简单易学

2.高效，基于请求响应的MVC框架

3.与Spring兼容性好，无缝结合

4.约定优于配置

5.功能强大:RESTful、数据验证、格式化、本地化、主题等

6.简洁灵活

![image-20201029211045833](img_mvc\1.png)



# MAVEN

```xml
<dependencies>
        <!--        spring-jdbc-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.1.9.RELEASE</version>
        </dependency>

        <!--        mybatis-spring-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.5</version>
        </dependency>


        <!--        mysql依赖-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.46</version>
        </dependency>


        <!--        JSTL表达式的依赖-->
        <dependency>
            <groupId>javax.servlet.jsp.jstl</groupId>
            <artifactId>jstl-api</artifactId>
            <version>1.2</version>
        </dependency>

        <!--        lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.12</version>
        </dependency>


        <!--        junit-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
        </dependency>

<!--        spring-webmvc-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.1.9.RELEASE</version>
        </dependency>

<!--        jackson-->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.11.3</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.68</version>
        </dependency>



    </dependencies>

    <build>
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

# 第一个SpringMVC程序

web.xml配置

**注册DispatcherServlet**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <!--    注册DispatcherServlet-->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
<!--        绑定spring-->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc-servlet.xml</param-value>
        </init-param>
<!--        启动级别-->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```



**springmvc-servlet.xml  配置**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

<!--    处理器映射器-->
    <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>

<!--    处理器适配器-->
    <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>

<!--    视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

    <bean id="/hei" class="com.jie.controller.HelloController"/>
</beans>
```



## 流程

1.通过处理映射器

```xml
<!--    处理器映射器-->
    <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
```

- 来访问注册的控制请求

- ```xml
  <bean id="/he" class="com.jie.controller.HelloController"/>
  ```

2.调用处理适配器

```
<!--    处理器适配器-->
    <bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>
```

- 去调用控制层的程序

- ```java
  public class HelloController implements Controller {
      public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
          ModelAndView mv=new ModelAndView();
  
          //业务代码
          String name="user";
          mv.addObject("name",name);
  
          mv.setViewName("test");
  
          return mv;
      }
  }
  ```

  

3.返回给视图解析器

- ```xml
  <!--    视图解析器-->
      <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="InternalResourceViewResolver">
          <property name="prefix" value="/WEB-INF/jsp/"/>
          <property name="suffix" value=".jsp"/>
      </bean>
  ```

- 拼接后显示在页面上



# SpringMVC执行原理

![image-20201030152710262](img_mvc\2.png)



简要分析执行流程

1.DispatcherServlet表示前置控制器，是整个SpringMVC的控制中心。用户发出请求，DispatcherServlet接收请求并拦截请求。

- 我们假设请求的url为 : http://localhost:8080/SpringMVC/hello。
- 如上url拆分成三部分:
   http://localhost:8080服务器域名心. 
- SpringMVC部署在服务器上的web站点. 
- hello表示控制器
- 通过分析，如上url表示为:请求位于服务器localhost:8080上的SpringMVC站点的hello控制器。



2.HandlerMapping为处理器映射。DispatcherServlet调用HandlerMapping,HandlerMapping根据请求url查找Handler。

3.HandlerExecution表示具体的Handler,其主要作用是根据url查找控制器，如上url被查找控制器为: hello。

4.HandlerExecution将解析后的信息传递给DispatcherServlet,如解析控制器映射等。

5.HandlerAdapter表示处理器适配器，其按照特定的规则去执行Handler。

6.Handler让具体的Controller执行。





# 注解配置SpringMVC

实现步骤其实非常的简单:

1. 新建一个web项目
2. 导入相关jar包
3. 编写web.xml，注册DispatcherServlet
4. 编写springmvc配置文件
5. 接下来就是去创建对应的控制类，controier
6. 最后完善前端视图和controller之间的对应
7. 测试运行调试.



使用springMVC必须配置的三大件:

**处理器映射器、处理器适配器、视图解析器**

通常，我们只需要手动配置视图解析器，而处理器映射器和处理器适配器只需要开启注解驱动即可，而省去了大段的xml配置

## **注册DispatcherServlet**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <!--    注册DispatcherServlet-->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
<!--        绑定spring-->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc-servlet.xml</param-value>
        </init-param>
<!--        启动级别-->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```



## **配置springmvc-servlet.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd">
<!--    扫描包-->
    <context:component-scan base-package="com.jie.controller"/>
<!--    过滤-->
    <mvc:default-servlet-handler/>

<!--    mvc注解驱动-->
<!--    自动装配  处理器映射器<bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>-->
<!--             处理器适配器<bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>-->
    <mvc:annotation-driven/>


<!--    视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver" id="InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

</beans>
```

**扫描包**
    <context:component-scan base-package="com.jie.controller"/>
**过滤**
    <mvc:default-servlet-handler/>

**mvc注解驱动**
    <mvc:annotation-driven/>





## **通过注解实现controller**

```java
@Controller
public class HelloController {
    @RequestMapping("/hei")
    public String hello(Model model) {
        model.addAttribute("msg","asdasd");
        return "test";
    }
}
```

**@Controller**    controller注解

**@RequestMapping("/hei")**   请求地址



# Controller控制器

**控制器Controller**

- 控制器复杂提供访问应用程序的行为，通常通过接口定义或注解定义两种方法实现。控制器负责解析用户的请求并将其转换为一个模型。
- 在Spring MVC中一个控制器类可以包含多个方法
- 在Spring MVC中，对于Controller的配置方式有很多种



**实现方式**

1.实现Controller接口

```java
public class controller01 implements Controller {
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        ModelAndView mv=new ModelAndView();

        mv.addObject("user","controller");

        return mv;
    }
}
```

注册

```xml
<bean id="/hei" class="com.jie.controller.controller01"/>
```



2.注解使用

```java
@Controller
public class controller02 {
    @RequestMapping("/asd")
    public String test(Model model){
        model.addAttribute("Zxc","user");
        return "test";
    }
}
```





# RestFul风格

**简洁  高效 安全**



概念
Restful就是一个资源定位及资源操作的风格。不是标准也不是协议，只是一种风格。基于这个风格设计的软件可以更简洁，更有层次，更易于实现缓存等机制。

功能

- ·资源     互联网所有的事物都可以被抽象为资源
- ·资源操作        使用POST、DELETE、PUT、GET，使用不同方法对资源进行操作。
- ·分别对应添加、删除、修改、查询。



传统方式操作资源:通过不同的参数来实现不同的效果!方法单一，post和 get

- . http://127.0.0.1/item/queryltem.action?id=1查询,GET
- . http://127.0.0.1/item/saveltem.action新增,POST
-  http://127.0.0.1/item/updateltem.action更新,POST
- . http://127.0.0.1 /item/deleteltem.action?id=1删除,GET或POST



使用**RESTful**操作资源:可以通过不同的请求方式来实现不同的效果!如下:请求地址一样，但是功能可以不同!

- http:/ /127.0.0.1/item/1查询,GET
- http://127.0.0.1/item新增,POS
- http://127.0.0.1/item更新,PUT
-  http://127.0.0.1/item/1删除,DELETE





## 1.初始配置

配置 @Controller

```java
@Controller
public class RestFulController {
    @RequestMapping("/add")
    public String add(int a, int b, Model model){
        int res=a+b;
        model.addAttribute("msg",res);
        return "test";
    }

}
```

url栏输入

```java
http://localhost:8080/springmvc03_annotation_war_exploded/add?a=1&b=2
```

重点末尾 加上  **?a=1&b=2**

响应画面

![image-20201031120900297](img_mvc\4.png)



## 2.注解访问

```java
@RequestMapping("/add/{a}/{b}")
public String add2(@PathVariable("a") int a,@PathVariable("b") int b, Model model){
    int res=a+b;
    model.addAttribute("msg",res);
    return "test";
}
```

url显示

```java
http://localhost:8080/springmvc03_annotation_war_exploded/add/5/9
```



指定类型访问

1.

```java
@RequestMapping(value =  "/add/{a}/{b}",method = RequestMethod.POST)
public String add2(@PathVariable("a") int a,@PathVariable("b") int b, Model model){
    int res=a+b;
    System.out.println(2);
    model.addAttribute("msg",res);
    return "test";
}

```

2.

```java
@GetMapping("/add/{a}/{b}")
public String add3(@PathVariable("a") int a,@PathVariable("b") int b, Model model){
    int res=a+b;
    System.out.println(3);
    model.addAttribute("msg",res);
    return "test";
}
```





# 转发和重定向



springmvc原生的转发和重定向

**不配置 视图解析器**

转发

```java
@Controller
public class ModelTest {

    @RequestMapping("/m1/t1")
    public String test1(Model model){
        model.addAttribute("msg","sadasd");
        return "forward:/WEB-INF/jsp/test.jsp";
    }
    
}
```





配置视图解析器

```java
转发
@RequestMapping("/m1/t1")
public String test1(Model model){
    model.addAttribute("msg","sadasd");
    return "test";
}

```

```java
重定向
@RequestMapping("/m1/t2")
public String test2(Model model){
    model.addAttribute("msg","sadasd");
    return "redirect:/index.jsp";
}
```



# 接收参数



**接收普通参数**

```java
@Controller
public class UserController {
    @RequestMapping("/t1")
    public String test1(@RequestParam("username") String name, Model model){
        model.addAttribute("msg",name);
        return "test";
    }
}
```

**@RequestParam** 

作用在于改变 url变量的名字     并且使原来的变量名不可用

建议书写并且名字相同





**接收对象**

```java
@RequestMapping("/t2")
public String test2(User user,Model model){
    model.addAttribute("msg",user);
    return "test";
}
```

url地址

```java
http://localhost:8080/springmvc03_annotation_war_exploded/t2?id=1&name=zcj&age=10
```



**Model  只有塞寥几个方法只适合用于储存数据，简化了新手对于Model对象的操作和理解;**

**ModelMap  继承了LinkegMap ，除了实现了自身的一些方法，同样的继承 LinkedMap 的方法和特性;**

**NodelAndview 可以在储存数据的同时，可以进行设置返回的逻辑视图，进行控制展示层的跳转。**



# 过滤器

spirng自带的过滤器

```xml
<!--    过滤器-->
<filter>
    <filter-name>encoding</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>utf-8</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>encoding</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```



# JSON

```xml
<!--        jackson-->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.11.3</version>
        </dependency>
```



什么是JSON?

JSON(JavaScript Object Notation, JS对象标记)是一种轻量级的数据交换格式，目前使用特别广泛。

- ·采用完全独立于编程语言的文本格式来存储和表示数据。·简洁和清晰的层次结构使得JSON成为理想的数据交换语言。
- ·易于人阅读和编写，同时也易于机器解析和生成，并有效地提升网络传输效率。
  在JavaScript语言中，一切都是对象。因此，任何JavaScript支持的类型都可以通过JSON来表示，例如字符串、数字、对象、数组等。看看他的要求和语法格式:
- 对象表示为键值对，数据由逗号分隔
  - ·花括号保存对象
  - 方括号保存数组
- JSON键值对是用来保存JavaScript对象的一种方式，和JavaScript对象的写法也大同小异键/值对组合中的键名写在前面并用双引号""包裹，使用冒号:分隔，然后紧接着值:
  { "name" : "QinJiang" }
  { "age ": "3"}
  { "sex":“男"}

 

```html
    <script type="text/javascript">

        var user={
            name:"zcj",
            age:3,
            sex:"男"
        };

        var json=JSON.stringify(user);

        console.log(json)

        console.log("~~~~~~~")
        var obj=JSON.parse(json);
        console.log(obj);

    </script>
```



## jackson



**后端返回json**

```java
@Controller
public class UserController {
    @RequestMapping(value = "/j1",produces = "application/json;charset=utf-8")
    @ResponseBody
    public String jison1() throws JsonProcessingException {
//        jackson
        ObjectMapper mapper = new ObjectMapper();

        User user = new User("周成杰", 123, "asd");

        String s = mapper.writeValueAsString(user);

        return s;
    }
}
```



**@ResponseBody**

加上这个标签后   便不会走视图解析器

**@RequestMapping(value = "/j1",produces = "application/json;charset=utf-8")**

设置json 编码格式



或者编写全局json配置文件

```xml
<!--    json编码-->
    <mvc:annotation-driven>
        <mvc:message-converters>
            <bean class="org.springframework.http.converter.StringHttpMessageConverter">
                <constructor-arg value="UTF-8"/>
            </bean>
            <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
                <property name="objectMapper">
                    <bean class="org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean">
                        <property name="failOnEmptyBeans" value="false"/>
                    </bean>
                </property>
            </bean>
        </mvc:message-converters>
    </mvc:annotation-driven>
```



或者编写

### 前后端分离json标签

```java
@RestController
public class UserController {
    @RequestMapping(value = "/j1")
    public String jison1() throws JsonProcessingException {
//        jackson
        ObjectMapper mapper = new ObjectMapper();

        User user = new User("周成杰", 123, "asd");

        String s = mapper.writeValueAsString(user);

        return s;
    }

    @RequestMapping(value = "/j2")
    public String jison2() throws JsonProcessingException {
//        jackson
        ObjectMapper mapper = new ObjectMapper();

        User user1 = new User("周成杰", 123, "asd");
        User user2 = new User("周成杰", 123, "asd");
        User user3 = new User("周成杰", 123, "asd");
        User user4 = new User("周成杰", 123, "asd");

        List<User> list=new ArrayList<User>();
        list.add(user1);
        list.add(user2);
        list.add(user3);
        list.add(user4);

        String s = mapper.writeValueAsString(list);
        return s;
    }
    
        @RequestMapping("/j3")
    public String jison3() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        Date date=new Date();

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        String format = sdf.format(date);

        String s = mapper.writeValueAsString(format);
        return s;
    }
}
```



**@RestController** 

作用为将下面的所有controller方法设置为  不访问视图解析器   



## fastjson

fastjson主要的三个类

**【JSONObject代表json对象】**

- .JSONObject实现了Map接口，猜想JSONObject底层操作是由Map实现的。
- JSONObject对应json对象，通过各种形式的get()方法可以获取json对象中的数据，也可利用诸如size()，isEmpty()等方法获取"键:值"对的个数和判断是否为空。其本质是通过实现Map接口并调用接口中的方法完成的。



**[JSONArray 代表json对象数组】**

内部是有List接口中的方法来完成操作的。



**[JSON代表JSONObject和JSONArray的转化】**

- JSON类源码分析与使用
- 仔细观察这些方法，主要是实现json对象，json对象数组，javabean对象，json字符串之间的相互转化。



```java
@RequestMapping(value = "/j4")
public String jison4() throws JsonProcessingException {


    User user1 = new User("周成杰", 123, "asd");
    User user2 = new User("周成杰", 123, "asd");
    User user3 = new User("周成杰", 123, "asd");
    User user4 = new User("周成杰", 123, "asd");

    List<User> list=new ArrayList<User>();
    list.add(user1);
    list.add(user2);
    list.add(user3);
    list.add(user4);

    String s = JSON.toJSONString(list);
    return s;
}
```