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



```
jQuery.ajax(...)
部分参数：
url：请求地址
type：请求方式，GET、POST（1.9.0之后用method）
headers：请求头
data：要发送的数据
contentType：即将发送信息至服务器的内容编码类型(默认: "application/x-www-form-urlencoded; charset=UTF-8")
async：是否异步
timeout：设置请求超时时间（毫秒）
beforeSend：发送请求前执行的函数(全局)
complete：完成之后执行的回调函数(全局)
success：成功之后执行的回调函数(全局)
error：失败之后执行的回调函数(全局)
accepts：通过请求头发送给服务器，告诉服务器当前客户端可接受的数据类型
dataType：将服务器端返回的数据转换成指定类型
"xml": 将服务器端返回的内容转换成xml格式
"text": 将服务器端返回的内容转换成普通文本格式
"html": 将服务器端返回的内容转换成普通文本格式，在插入DOM中时，如果包含JavaScript标签，则会尝试去执行。
"script": 尝试将返回值当作JavaScript去执行，然后再将服务器端返回的内容转换成普通文本格式
"json": 将服务器端返回的内容转换成相应的JavaScript对象
"jsonp": JSONP 格式使用 JSONP 形式调用函数时，如 "myurl?callback=?" jQuery 将自动替换 ? 为正确的函数名，以执行回调函数
```





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







