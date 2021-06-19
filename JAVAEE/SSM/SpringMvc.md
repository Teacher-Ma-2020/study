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



# maven

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

        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
            <version>1.3.3</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
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
public String add2(@PathVariable int a,@PathVariable int b, Model model){
    int res=a+b;
    System.out.println(2);
    model.addAttribute("msg",res);
    return "test";
}

```

2.

```java
@GetMapping("/add/{a}/{b}")
public String add3(@PathVariable int a,@PathVariable int b, Model model){
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





# 拦截器

```java
package com.jie.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("处理前");
        return true;
        //true 放行
        //false 拦截
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("处理后");

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("清理");
    }
}
```

xml中配置

```xml
<!--    mvc拦截器配置-->
    <mvc:interceptors>
        <mvc:interceptor>
<!--            /**  : 该请求下的所有请求-->
            <mvc:mapping path="/**"/>
            <bean class="com.jie.config.MyInterceptor"/>
        </mvc:interceptor>
    </mvc:interceptors>
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





# JQuery

**Jqury就是js的整合方法**

- jQuery提供多个与AJAX有关的方法。
- 通过jQuery AJAX方法，您能够使用HTTP Get和 HTTP Post从远程服务器上请求文本、HTML、XML或JSON –同时您能够把这些外部数据直接载入网页的被选元素中。
- jQuery不是生产者，而是大自然搬运工。
- jQuery Ajax本质就是XMLHttpRequest，对他进行了封装，方便调用!



失去焦点

```jsp
<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2020/11/8
  Time: 11:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
    <script src="${pageContext.request.contextPath}/statics/js/jquery-3.5.1.js"></script>

    <script>
      function a(){
        $.post({
          url:"${pageContext.request.contextPath}/a1",
          data:{"username":$("#username").val()},
          success:function (data){
            alert(data);
          }
        })
      }
    </script>
  </head>
  <body>

<%--  失去焦点--%>
  用户名：<input type="text" id="username" onblur="a()">

  </body>
</html>
```



# 文件上传下载



文件上传是项目开发中最常见的功能之一 ,springMVC 可以很好的支持文件上传，但是SpringMVC上下文中默认没有装配MultipartResolver，因此默认情况下其不能处理文件上传工作。如果想使用Spring的文件上传功能，则需要在上下文中配置MultipartResolver。

前端表单要求：为了能上传文件，必须将表单的method设置为POST，并将enctype设置为multipart/form-data。只有在这样的情况下，浏览器才会把用户选择的文件以二进制数据发送给服务器；

**对表单中的 enctype 属性做个详细的说明：**

- application/x-www=form-urlencoded：默认方式，只处理表单域中的 value 属性值，采用这种编码方式的表单会将表单域中的值处理成 URL 编码方式。
- multipart/form-data：这种编码方式会以二进制流的方式来处理表单数据，这种编码方式会把文件域指定文件的内容也封装到请求参数中，不会对字符编码。
- text/plain：除了把空格转换为 "+" 号外，其他字符都不做编码处理，这种方式适用直接通过表单发送邮件。

```xml
<form action="" enctype="multipart/form-data" method="post">
   <input type="file" name="file"/>
   <input type="submit">
</form>
```

一旦设置了enctype为multipart/form-data，浏览器即会采用二进制流的方式来处理表单数据，而对于文件上传的处理则涉及在服务器端解析原始的HTTP响应。在2003年，Apache Software Foundation发布了开源的Commons FileUpload组件，其很快成为Servlet/JSP程序员上传文件的最佳选择。

- Servlet3.0规范已经提供方法来处理文件上传，但这种上传需要在Servlet中完成。
- 而Spring MVC则提供了更简单的封装。
- Spring MVC为文件上传提供了直接的支持，这种支持是用即插即用的MultipartResolver实现的。
- Spring MVC使用Apache Commons FileUpload技术实现了一个MultipartResolver实现类：
- CommonsMultipartResolver。因此，SpringMVC的文件上传还需要依赖Apache Commons FileUpload的组件。



> 文件上传

1、导入文件上传的jar包，commons-fileupload ， Maven会自动帮我们导入他的依赖包 commons-io包；

```xml
<!--文件上传-->
<dependency>
   <groupId>commons-fileupload</groupId>
   <artifactId>commons-fileupload</artifactId>
   <version>1.3.3</version>
</dependency>
<!--servlet-api导入高版本的-->
<dependency>
   <groupId>javax.servlet</groupId>
   <artifactId>javax.servlet-api</artifactId>
   <version>4.0.1</version>
</dependency>
```

2、配置bean：multipartResolver

【**注意！！！这个bena的id必须为：multipartResolver ， 否则上传文件会报400的错误！在这里栽过坑,教训！**】

```xml
<!--文件上传配置-->
<bean id="multipartResolver"  class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
   <!-- 请求的编码格式，必须和jSP的pageEncoding属性一致，以便正确读取表单的内容，默认为ISO-8859-1 -->
   <property name="defaultEncoding" value="utf-8"/>
   <!-- 上传文件大小上限，单位为字节（10485760=10M） -->
   <property name="maxUploadSize" value="10485760"/>
   <property name="maxInMemorySize" value="40960"/>
</bean>
```

CommonsMultipartFile 的 常用方法：

- **String getOriginalFilename()：获取上传文件的原名**
- **InputStream getInputStream()：获取文件流**
- **void transferTo(File dest)：将上传文件保存到一个目录文件中**

 我们去实际测试一下

3、编写前端页面

```xml
<form action="/upload" enctype="multipart/form-data" method="post">
 <input type="file" name="file"/>
 <input type="submit" value="upload">
</form>
```

4、**Controller**

```java
package com.kuang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

@Controller
public class FileController {
   //@RequestParam("file") 将name=file控件得到的文件封装成CommonsMultipartFile 对象
   //批量上传CommonsMultipartFile则为数组即可
   @RequestMapping("/upload")
   public String fileUpload(@RequestParam("file") CommonsMultipartFile file , HttpServletRequest request) throws IOException {

       //获取文件名 : file.getOriginalFilename();
       String uploadFileName = file.getOriginalFilename();

       //如果文件名为空，直接回到首页！
       if ("".equals(uploadFileName)){
           return "redirect:/index.jsp";
      }
       System.out.println("上传文件名 : "+uploadFileName);

       //上传路径保存设置
       String path = request.getServletContext().getRealPath("/upload");
       //如果路径不存在，创建一个
       File realPath = new File(path);
       if (!realPath.exists()){
           realPath.mkdir();
      }
       System.out.println("上传文件保存地址："+realPath);

       InputStream is = file.getInputStream(); //文件输入流
       OutputStream os = new FileOutputStream(new File(realPath,uploadFileName)); //文件输出流

       //读取写出
       int len=0;
       byte[] buffer = new byte[1024];
       while ((len=is.read(buffer))!=-1){
           os.write(buffer,0,len);
           os.flush();
      }
       os.close();
       is.close();
       return "redirect:/index.jsp";
  }
}
```

5、测试上传文件，OK！



**采用file.Transto 来保存上传的文件**

1、编写Controller

```java
/*
* 采用file.Transto 来保存上传的文件
*/
@RequestMapping("/upload2")
public String  fileUpload2(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) throws IOException {

   //上传路径保存设置
   String path = request.getServletContext().getRealPath("/upload");
   File realPath = new File(path);
   if (!realPath.exists()){
       realPath.mkdir();
  }
   //上传文件地址
   System.out.println("上传文件保存地址："+realPath);

   //通过CommonsMultipartFile的方法直接写文件（注意这个时候）
   file.transferTo(new File(realPath +"/"+ file.getOriginalFilename()));

   return "redirect:/index.jsp";
}
```

2、前端表单提交地址修改

3、访问提交测试，OK！



> 文件下载

**文件下载步骤：**

1、设置 response 响应头

2、读取文件 -- InputStream

3、写出文件 -- OutputStream

4、执行操作

5、关闭流 （先开后关）

**代码实现：**

```java
@RequestMapping(value="/download")
public String downloads(HttpServletResponse response ,HttpServletRequest request) throws Exception{
   //要下载的图片地址
   String  path = request.getServletContext().getRealPath("/upload");
   String  fileName = "基础语法.jpg";

   //1、设置response 响应头
   response.reset(); //设置页面不缓存,清空buffer
   response.setCharacterEncoding("UTF-8"); //字符编码
   response.setContentType("multipart/form-data"); //二进制传输数据
   //设置响应头
   response.setHeader("Content-Disposition",
           "attachment;fileName="+URLEncoder.encode(fileName, "UTF-8"));

   File file = new File(path,fileName);
   //2、 读取文件--输入流
   InputStream input=new FileInputStream(file);
   //3、 写出文件--输出流
   OutputStream out = response.getOutputStream();

   byte[] buff =new byte[1024];
   int index=0;
   //4、执行 写出操作
   while((index= input.read(buff))!= -1){
       out.write(buff, 0, index);
       out.flush();
  }
   out.close();
   input.close();
   return null;
}
```

前端

```
<a href="/download">点击下载</a>
```

测试，文件下载OK，大家可以和我们之前学习的JavaWeb原生的方式对比一下，就可以知道这个便捷多了!



