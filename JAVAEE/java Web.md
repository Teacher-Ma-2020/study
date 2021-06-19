





# jsp web



问题思考：

- 当你的浏览器中的地址栏输入地址并回车的一瞬间 页面能够展现出来，经历了什么

- 

- 重定向和转发的区别
  - 相同点：页面都会跳转
  
  - 不同点：转发url不会变化   重定向url会变化
  
  - ```java
    //重定向 状态码302
    resp.sendRedirect("/response_test_war/image");
    //转发
    req.getRequestDispatcher("/success.jsp").forward(req,resp);   (后端)
    pageContext.forward("/success.jsp")   （前端）
    ```
  
  

```java
        resp.setContentType("html/text;charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        req.setCharacterEncoding("utf-8");
```



## xml

- 用来保存数据 具有自我描述性
- 项目或者模块的配置文件
- 网络数据的传输格式

**在xml中设置错误跳转**

```xml
<error-page>
    <error-code>404</error-code>
    <location>/error/404.jsp</location>
</error-page>

<error-page>
    <error-code>500</error-code>
    <location>/error/500.jsp</location>
</error-page>
```







发布一个web网站 （结构）

```java
--webapps:tomcat服务器的web目录
    -ROOT
    -jieStudy:网站的目录名
        -WEB-INF
        	--classes：java程序
        	-lib：依赖的jar包
        	-web.xml 配置文件
        -index.html 默认的首页
        -static
        	-css
        	-js
```



## html

- HTTP（超文本传输协议）是一个简单的请求-响应协议，它通常运行在TCP之上

- http：80  https:443(安全的)

- http请求    ：客户端--发起请求（request）--服务器

  - 淘宝

  - ```java
    Request URL: https://www.taobao.com/  请求地址
    Request Method: GET   get方法/post方法
        
        get：能够携带的参数较少，大小有限，会在浏览器的url行显		示，不安全，但是高效
        host：能够携带的参数无限制，大小无限，不会在浏览器的url		行显示，安全，但是不高效
        
    Status Code: 200     状态码
    Remote Address: 218.98.8.102:443   淘宝的地址加端口
    Referrer Policy: unsafe-url
    ```

  - ```java
    Accept: text/html         告诉浏览器，它支持的数据类型
    Accept-Encoding: gzip, deflate, br   支持哪种编码格式
    Accept-Language: zh-CN,zh;q=0.9      告诉语言环境
    Cache-Control: no-cache              缓存控制
    Connection: keep-alive              告诉浏览器 断开还									是保持连接
    HOST：主机
    ```

  - 

- http响应：服务器--响应--客户端

  - 百度

  - ```java
    Accept: text/html
    Accept-Encoding: gzip, deflate, br
    Accept-Language: zh-CN,zh;q=0.9
    Cache-Control: no-cache
    Connection: keep-alive
    ```

  - 响应状态码

    - 200：请求响应成功
    - 3**：请求重定向
      - 重定向：重新给予新位置
    - 4**：找不到资源 404
    - 5**：服务器代码错误  5



## Maven

##### 概念

- 能够自动导入和配置jar包
- 核心思想：**约定大于配置**
- maven仓库：https://mvnrepository.com/
- 

动态web工程

- main：存放自己编写的java源码
- web存放web、工程的组件
  - WEB-INF 受保护的目录 浏览器无法直接访问
    - lib存放第三方的jar包
    - web.xml是整个动态web工程的配置部署扫描文件 比如servlet程序等等



## web需要的jar包

```xml
   <!--        jsp的依赖-->
    <dependency>
      <groupId>javax.servlet.jsp</groupId>
      <artifactId>javax.servlet.jsp-api</artifactId>
      <version>2.3.3</version>

    </dependency>
    <!--        servlet的依赖-->
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>3.1.0</version>
    </dependency>
    <!--        JSTL表达式的依赖-->
    <dependency>
      <groupId>javax.servlet.jsp.jstl</groupId>
      <artifactId>jstl-api</artifactId>
      <version>1.2</version>
    </dependency>
    <!--        standard的依赖-->
    <dependency>
      <groupId>taglibs</groupId>
      <artifactId>standard</artifactId>
      <version>1.1.2</version>
    </dependency>
    <!--        mysql依赖-->
    <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.46</version>
    </dependency>
	


```



## servlet

- javaEE规范之一 ，规范就是接口  动态web的技术
- servlet是javaWeb三大组件之一，分别是servlet程序  Filter过滤器 Listener监听器
- servelt是运行在服务器上的java小程序  可以就收客户端发送过来的请求 并响应给客户端

![image-20200916203854664](img\image-20200916203854664.png)



### servlet原理

![image-20200917210028069](img\image-20200917210028069.png)







### servlet映射配置

```java
package com.jie.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class helloSevlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        PrintWriter writer = resp.getWriter();//响应流
        writer.print("helloword");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
```

编写servlet的映射:在web中注册编写的servlet 并且给予访问路径

```xml
<!--    hello映射-->
    <servlet>
        <servlet-name>hello</servlet-name>
        <servlet-class>com.jie.servlet.helloSevlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>hello</servlet-name>
        <url-pattern>/hello</url-pattern>   <!--请求的路径-->
    </servlet-mapping>

<!--    error映射-->
    <servlet>
        <servlet-name>error</servlet-name>
        <servlet-class>com.jie.servlet.errorServelt</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>error</servlet-name>
        <url-pattern>/hello/*</url-pattern>   <!--请求的路径-->
    </servlet-mapping>
```

- mapping为路径映射  

  - 单个servlet可以指定多个映射

    - ```xml
      指定error映射
       <servlet>
          <servlet-name>error</servlet-name>
          <servlet-class>com.jie.servlet.errorServelt</servlet-class>
        </servlet>
        <servlet-mapping>
          <servlet-name>error</servlet-name>
          <url-pattern>/hello/*</url-pattern>   <!--请求的路径-->
        </servlet-mapping>
      ```

  - 可以使用通配符*

    ```xml
    <servlet-mapping>
      <servlet-name>hello</servlet-name>
      <url-pattern>/hello</url-pattern>   <!--请求的路径-->
    </servlet-mapping>
    <servlet-mapping>
      <servlet-name>hello</servlet-name>
      <url-pattern>/hello1</url-pattern>   <!--请求的路径-->
    </servlet-mapping>
    <servlet-mapping>
      <servlet-name>hello</servlet-name>
      <url-pattern>/hello/*</url-pattern>   <!--请求的路径-->
    </servlet-mapping>
    ```

- 名字相同 class指向java文件   pattern申明目录



### ServletContext

1.数据共享

servletcontext可以实现数据共享  并且全局只有一个ServletContext对象

![image-20200919161200967](img\image-20200919161200967.png)

```java
        this.getInitParameter(); 初始化参数
        this.getServletConfig();  Servlet配置
        this.getServletContext(); Servlet上下午
      
```

在一个servlet中添加数据

```java
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("hello");

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
//        this.getInitParameter(); 初始化参数
//        this.getServletConfig();  Servlet配置
//        this.getServletContext(); Servlet上下午
        ServletContext servletContext = this.getServletContext();   //并且全局为同一个
        String username="杰哥哥"; //数据
        servletContext.setAttribute("username",username);  //将数据保存在了ServletContest中可以允许其他Servlet调用
        PrintWriter writer = resp.getWriter();
        writer.print("去取值吧");
```

另一个读取

```java
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    ServletContext context = this.getServletContext();
    String username = (String) context.getAttribute("username"); //取出

    resp.setCharacterEncoding("utf-8");
    resp.setContentType("text/html");

    PrintWriter writer = resp.getWriter();
    if (username==null){
        writer.print("还没取值呢哥哥");
    }
    else
    {
        writer.print("获取名字:"+username);
    }
```



2.获取初始化参数

- 设置

  ```xml
  <!--    配置web应用初始化数据-->
      <context-param>
          <param-name>url</param-name>
          <param-value>jdbc:mysql://localhost:3306/5555</param-value>
      </context-param>
  ```

- 获取

  ```java
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      ServletConfig config = this.getServletConfig();
      String url = config.getInitParameter("url");
      PrintWriter writer = resp.getWriter();
      writer.print(url);
  }
  ```

3.请求转发

- ```java
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          ServletContext context = this.getServletContext();
          System.out.println("进入了Demo02");
  //        RequestDispatcher dispatcher = context.getRequestDispatcher("/gp");  //转发的请求路径
  //        dispatcher.forward(req,resp);  //调用forward实现转发
          context.getRequestDispatcher("/gp").forward(req,resp);
      }
  ```

4.读取文件

- 打包给classes目录下

- 用文件流读取

- ```java
  public class propertiesServlet extends HttpServlet {
      @Override
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          InputStream inputStream = this.getServletContext().getResourceAsStream("/WEB-INF/classes/db.properties");
          Properties prop = new Properties();
          prop.load(inputStream);
          String username = prop.getProperty("username");
          String password = prop.getProperty("password");
  
          resp.getWriter().print(username+" "+password);
      }
  ```





### HttpServletResponse

##### 设置页面格式

```java
    resp.setCharacterEncoding("utf-8");
    resp.setContentType("text/html");
```





##### Redirect重定向

```java
public void sendRedirect(String location) throws IOException;
```

```java
//重定向 状态码302
resp.sendRedirect("/response_test_war/image");
//转发
req.getRequestDispatcher("/success.jsp").forward(req,resp);
```

${pageContext.request.contextPath}表示项目路径

```jsp
<form action="${pageContext.request.contextPath}/request" METHOD="get">
    登录名:<input type="text" name="user_name"><br>
    密码:<input type="password" name="password"><br>
    <input type="submit" value="提交">
</form>
```



### HttpServletRequest

获取参数

```java
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");

        //处理请求
//        req.getParameter()
        String user_name = req.getParameter("user_name");
        String password = req.getParameter("password");
        if (user_name.equals("319991012") && password.equals("123456")){
            resp.sendRedirect("/response_test_war/success.jsp");
        }else{
            resp.getWriter().print(user_name);
            resp.getWriter().print(password);
            resp.getWriter().print("账号密码错误");
        }
    }
```





## Cookie

### **会话**

用户打开浏览器  点击多个链接 访问资源，关闭浏览器 被称之为会话

**保存会话的两种技术**

cookie：客户端技术（响应，请求)

session：服务端技术，可以保存用户的对话，可以把信息或者数据放在Session中！



Session和cookie的区别

- cookie是把用户的数据写道用户的浏览器中
- session把用户的数据写到用户独占的Session中  服务器保存重要的信息 
- session由网页独占

**cookie**![image-20200924173742976](img\image-20200924173742976.png)

**session**

![image-20200924174020307](img\image-20200924174020307.png)



### 设置 取值

```java
//        获得cookie信息
        Cookie[] cookies = req.getCookies();
        //判断是否为空
        if (cookies!=null){
            for (Cookie cookie:cookies){
                if(cookie.getName().equals("name")){

                    out.write("name为"+cookie.getValue()+"<br>");
                }else if (cookie.getName().equals("lastTime")){
                    long lastTime = Long.parseLong(cookie.getValue());
                    Date date = new Date(lastTime);
                    out.write("上次访问的时间是"+date.toLocaleString()+"<br>");
                }else if(cookie.getName().equals("last_time")){
                    out.write("上次访问的时间 "+cookie.getValue());
                }
            }
        }else{
            out.write("第一次访问");
        }
        //创建cookie
        Cookie cookie = new Cookie("name", "jie");
        Cookie cookie1 = new Cookie("lastTime", System.currentTimeMillis() + "");  //时间戳
        Cookie cookie2 = new Cookie("last_time", new Date().toLocaleString());        //字符串

        resp.addCookie(cookie);
        resp.addCookie(cookie1);
        resp.addCookie(cookie2);
    }

```

1. 从请求中拿到Cookie信息

2. 服务器响应给客户端

3. ```java
   Cookie[] cookies = req.getCookies();
   获取值
   cookie.getName()
   cookie.getValue()
   添加
   Cookie cookie = new Cookie("name", "jie");
   resp.addCookie(cookie);
   ```

   ```java
   //传入中文时需要加码
   Cookie cookie = new Cookie("name", URLEncoder.encode("周成杰","utf-8"));
   //输出解码
   out.write("name为"+ URLDecoder.decode(cookie.getValue(),"UTF-8") +"<br>");
   ```

- 一个cooke只能保存一个信息
- 一个web可以给浏览器发送多个cookie'  最多只能发20个
- 一个浏览器只能存放300个cookie  cooke大小限制4kb

### 删除cookie

```java
//保证名字相同
Cookie cookie = new Cookie("name", "cheng");
cookie.setMaxAge(0);
resp.addCookie(cookie);
```



## Session

### 什么是**Session**

- 服务器会给每个用户（浏览器）创建session对象
- 一个session独占一个浏览器 只要浏览器没有关闭 这个Session就一直存在
- 用户登录之后 整个浏览器session都能访问



**添加**

```java
HttpSession session = req.getSession();

session.setAttribute("name","周成杰");
session.setAttribute("person",new person("周成杰",20));

String id = session.getId();
if (session.isNew()){
    writer.print("Session创建成功"+id);
}else{
    writer.print("Session已经在服务器中存在了"+id);
}
```

**获取**

```java
//获取
HttpSession session = req.getSession();
String name = (String) session.getAttribute("name");
writer.print(name+"<br>");

person person = (person) session.getAttribute("person");
writer.print(person);
```

**删除**

```java
HttpSession session = req.getSession();
session.removeAttribute("name");
session.invalidate();
```

或者在xml中配置时间

```xml
<!--    session保存时间30分钟-->
    <session-config>
        <session-timeout>30</session-timeout>
    </session-config>
```



### 会话自动过期

xml配置

```xml
15分钟后失效
<!--    设置Session的默认失效时间-->
    <session-config>
        <session-timeout>15</session-timeout>
    </session-config>
```



## JSP

### 概念

Java Server Pages：Java服务端页面 

jsp页面中可以嵌入Java代码,为用户提供动态数据





**原理**

服务器内部工作

- tomcat中有work目录
- IDEA使用tomcat会在tomcat中产生work目录

### jsp源码位置

```java
C:\Users\lenovo\bin\IdeaConfig\system\tomcat
```

jsp本质上就是**servlet**

```java
初始化
public void _jspInit() {
}

销毁
public void _jspDestroy() {
}

public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {
```

**内置对象**

```java
final javax.servlet.jsp.PageContext pageContext;   页面上下文
javax.servlet.http.HttpSession session = null;     session
final javax.servlet.ServletContext application;      
final javax.servlet.ServletConfig config;
javax.servlet.jsp.JspWriter out = null;
final java.lang.Object page = this;
```

输出前添加的代码

```java
response.setContentType("text/html");         //用于转换 被识别
pageContext = _jspxFactory.getPageContext(this, request, response,   //生成内置 对象
         null, true, 8192, true);
_jspx_page_context = pageContext;
application = pageContext.getServletContext();
config = pageContext.getServletConfig();
session = pageContext.getSession();
out = pageContext.getOut();
_jspx_out = out;
```



### **流程**

![image-20200925144856915](img\image-20200925144856915.png)



### **JSP基本语法**



1. 普通申明 会定义在java的 public void _jspService 的方法中

```jsp
<%= new java.util.Date()+"<br>"%>
    <%
      int num=0;
      for (int i=1;i<=100;i++){
        num+=i;
    %>
    <%
      }
      out.print(num);

    %>
```



2.JSP声明 会被放在生成的类中

```jsp
<%!
  static {
    System.out.println("静态代码块");
  }
  private int password=123456;
  public void study(){
    System.out.println("定义了方法");
  }
%>
```



设置头和尾

1. ​    将三个页面拼接到一起   本质上还是一个页面

```jsp
<%@include file="common/Header.jsp"%>
<%@include file="common/footer.jsp"%> 
```

2.    拼接在一起 实际为三个界面

```
<jsp:include page="common/Header.jsp"/>
<jsp:include page="common/footer.jsp"/>
```



##### **1.语法错误跳转**

```jsp
<%@ page errorPage="error/500.jsp" %>
```





### 内置对象

- **PageContext**      
- **Request**       
- **Response**
- **Seesion**
- **Application     （SerlvetContext）**
- config          （SerlvetConfig）
- out                                     
- page
- excepetion



**存入数据**

```java
//从底层到高层  page->request->session->application
```



```java
pageContext.setAttribute("name1","周");   //当前页面有效
pageContext.setAttribute("name11","周",PageContext.SESSION_SCOPE); //手动设置 有效范围

request.setAttribute("name2","成");       // 	只在一次页面中有效  页面跳转会携带数据
session.setAttribute("name3","杰");     //    个人单独的数据 一次会话有效
application.setAttribute("name4","啊");   //在保存在服务器 从打开服务器到关闭服务器

```

**取值**

```java
<%
    String name1 = (String)pageContext.findAttribute("name1");
    String name11 = (String)pageContext.findAttribute("name11");
    String name2 = (String)pageContext.findAttribute("name2");
    String name3 = (String)pageContext.findAttribute("name3");
    String name4 = (String)pageContext.findAttribute("name4");
    String name5 = (String)pageContext.findAttribute("name5");
    
%>

<h1>
    取值
</h1>
<h3>${name1}</h3>
<h3>${name11}</h3>
<h3>${name2}</h3>
<h3>${name3}</h3>
<h3>${name4}</h3>
<h3>${name5}</h3>
```





### 标签、表达式



##### **JSP标签**

转发 携带数据

```
<jsp:forward page="get.jsp">
    <jsp:param name="id" value="101"/>
</jsp:forward>
```



```
<jsp:useBean id="zcj" class="com.jie.pojo.person" scope="page"/>
<jsp:setProperty name="zcj" property="sid" value="1"/>
<jsp:setProperty name="zcj" property="gender" value="男"/>
<jsp:setProperty name="zcj" property="class_id" value="1"/>
<jsp:setProperty name="zcj" property="sname" value="zcj"/>

id:<jsp:getProperty name="zcj" property="sid"/>
gender:<jsp:getProperty name="zcj" property="gender"/>
class:<jsp:getProperty name="zcj" property="class_id"/>
sname:<jsp:getProperty name="zcj" property="sname"/>
```





##### **EL表达式： ${}**

- 获取数据
- 执行运算
- 获取web开发的常用对象



## **JSTL表达式**  



JSTL标签库是为了弥补HTML标签的不足，他自定义了许多标签供使用，功能与java代码相同

**核心标签**

核心标签是最常用的 JSTL标签。引用核心标签库的语法如下：

```
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
```

![image-20200926171304599](img\image-20200926171304599.png)



使用JSTL需要在Tomcat的lib中加入 JTSL和standard的包 否则会报错



if

```jsp
<c:if test="${param.id=='admin'}" var="isAdmin">
    <c:out value="你是管理员"/>
</c:if>

<c:out value="${isAdmin}"/>
```

```jsp
<c:set var="score" value="99"/>
<c:choose>
    <c:when test="${score==100}">
        满分
    </c:when>
    <c:when test="${score>=60}">
        及格
    </c:when>
    <c:when test="${score<60}">
        不及格
    </c:when>
</c:choose>
```



for each

```jsp
<%
    List<String> p= new ArrayList<String>();
    p.add(0,"周");
    p.add(1,"成");
    p.add(2,"杰");
    request.setAttribute("list",p);
%>
<c:forEach var="p" items="${list}">
    <c:out value="${p}"/>
</c:forEach>
```

**begin表示开始  end结束  step为步速**

```jsp
<c:forEach var="p" items="${list}" begin="0" end="8" step="2">
    <c:out value="${p}"/>
</c:forEach>
```



## Filter过滤器 

过滤器 过滤网站的数据

- 处理中文乱码
- 登录验证

###### 

配置Filter代码

```java
public class EncodingFilter implements Filter {
    @Override

    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("启动");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");

        System.out.println("过滤前");
        chain.doFilter(request,response);//让过滤器进行通行 固定代码
        System.out.println("过滤后");
    }

    @Override
    public void destroy() {
        System.out.println("销毁");
    }
}
```

在xml中配置

```xml
    <filter>
        <filter-name>Encoding</filter-name>
        <filter-class>com.jie.Filter.EncodingFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>Encoding</filter-name>

<!--        只要是servlet包下的任何请求-->
        <url-pattern>/servlet/*</url-pattern>
    </filter-mapping>
```



## Listener监听器

**注册监听器   只要session创建便会执行**

```java
public class onlineCountListener implements HttpSessionListener {
    @Override
    //创建session监听
    //一旦创建seesion便会执行事件
    public void sessionCreated(HttpSessionEvent se) {

        System.out.println(se.getSession().getId());

        ServletContext ctx = se.getSession().getServletContext();
        Integer online = (Integer)ctx.getAttribute("online");
        if (online==null){
            online=new Integer(1);
        }else{
            int count=online.intValue();
            online=new Integer(count+1);

        }
        ctx.setAttribute("online",online);
    }

    @Override
    //销毁
    public void sessionDestroyed(HttpSessionEvent se) {
        ServletContext ctx = se.getSession().getServletContext();
        Integer online = (Integer)ctx.getAttribute("online");
        if (online==null){
            online=new Integer(0);
        }else{
            online=new Integer(online-1);

        }
        ctx.setAttribute("online",online);
    }
```

在xml中配置

```xml
<listener>
    <listener-class>com.jie.listener.onlineCountListener</listener-class>
</listener>
```





## JDBC

java连接数据库

![image-20200929102502488](img\image-20200929102502488.png)



**解决中文乱码问题** 同时设置参数

```java
String url="jdbc:mysql://localhost:3306/jdbc?useUnicode=true&characterEncoding=UTF-8";
String username="root";
String password="123456";
```



### **JDBC六部曲**

**1.加载驱动**

```java
Class.forName("com.mysql.jdbc.Driver");
```

**2.连接数据库**

```java
Connection connection = DriverManager.getConnection(url, username, password);
```

**3.向数据库发送sql的对象 statement：CRUD**

```java
Statement statement=connection.createStatement();
```

**4.编写sql**

```
String sql="select * from jdbc.users";
```

**5.执行 查询**

```java
ResultSet resultSet = statement.executeQuery(sql);


while(resultSet.next()){
    System.out.println("id="+resultSet.getObject("id"));
    System.out.println("na="+resultSet.getObject("sname"));
    System.out.println("passwords="+resultSet.getObject("password"));
    System.out.println("email="+resultSet.getObject("email"));
    System.out.println("birthday="+resultSet.getObject("birthday"));
}
```

**6.关闭连接、释放资源   先开后关 与io相同**

```java
resultSet.close();
statement.close();
connection.close();
```



**设置中vm选项导入 -Dfile.encoding=GB2312  否则会出乱码**  （个人问题）





### **增删改查预编译 ** Update

```sql
        //预编译
        String sql="insert into jdbc.users(id, sname, password, email, birthday) VALUES (?,?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,4);
        preparedStatement.setString(2,"周成杰");
        preparedStatement.setString(3,"123456");
        preparedStatement.setString(4,"31999@qq.com");
        preparedStatement.setDate(5,new Date(new java.util.Date().getTime()));

//        执行sql
        int i = preparedStatement.executeUpdate();
        if (i>0){
            System.out.println("插入成功@");
        }

        preparedStatement.close();
        connection.close();
```

### 查询预编译 Query

```java
Connection connection = DriverManager.getConnection(url,username,password);
String sql="select * from smbms.test";
PreparedStatement preparedStatement = connection.prepareStatement(sql);

ResultSet resultSet = preparedStatement.executeQuery();
while(resultSet.next()){
    System.out.println(resultSet.getObject("id"));
    System.out.println(resultSet.getObject("password"));
}


resultSet.close();
preparedStatement.close();
connection.close();
```





### **事务**

ACID原则:保证数据的安全

- 开启事务
- 事务提交  commit
- 事务回滚   rollback
- 关闭事务



junit单元测试

```xml
//依赖
<dependency>
  <groupId>junit</groupId>
  <artifactId>junit</artifactId>
  <version>4.11</version>
</dependency>
```



```java
//通知数据库开启事务
connection.setAutoCommit(false);
```



无错误 提交 有错误回滚

```java
String sql="update jdbc.account set money=money-100 where name='z'";
connection.prepareStatement(sql).executeUpdate();

int i=1/0;

String sql2="update jdbc.account set money=money+100 where name='c'";
connection.prepareStatement(sql2).executeUpdate();

connection.commit();
System.out.println("提交成功");
```



##### 完整代码

```java
@Test
public void Test() {

    String url="jdbc:mysql://localhost:3306/jdbc?usrUnicode=true&characterEncoding=utf-8";
    String username= "root";
    String password="123456";

    Connection connection=null;

    try {
        Class.forName("com.mysql.jdbc.Driver");

        connection = DriverManager.getConnection(url, username, password);

        //通知数据库开启事务
        connection.setAutoCommit(false);

        String sql="update jdbc.account set money=money-100 where name='z'";
        connection.prepareStatement(sql).executeUpdate();

        int i=1/0;

        String sql2="update jdbc.account set money=money+100 where name='c'";
        connection.prepareStatement(sql2).executeUpdate();

        connection.commit();
        System.out.println("提交成功");

    } catch (Exception e) {
        try {
            System.out.println("事务回滚");
            connection.rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        e.printStackTrace();
    }finally {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
```



## MVC、三层架构



### MVC架构

**什么是MVC: Model  view  Controll    模型 视图  控制器**

![image-20200927144213632](img\image-20200927144213632.png)

Model  模型

- 业务处理：业务逻辑（Service）
- 数据持久层：CRUD（Dao）

Controller（Servlet）  控制层

- 接受用户的请求：（req：请求参数、session信息。。。。）
- 交给业务层处理对应的代码
- 控制视图的跳转

View    视图层

- 展示数据
- 提供链接发起的Servlet请求（a、from、img。。。。）

![image-20201002212727081](img\image-20201002212727081.png)





**视图View**

负责页面的显示；与用户的交互。包含各种表单。 实现视图用到的技术有html/css/jsp/js等前端技术。

用户交互：用户鼠标点击页面；填写页面中各种表单........等等

**模型Model**

模型负责各个功能的实现（如登录、增加、删除功能）。模型用JavaBean实现。

**JavaBeans :**①是Java中一种特殊的类（换言之：JavaBean就是一个Java类）.一个Java类 ，满足以下要求，则可称为一个JavaBean **a. public修饰的类，提供public 无参构造方法** **b. 所有属性 都是private** **c. 提供getter和setter方法** ②从使用层面来看，JavaBean分为2大类： ***\*a. 封装业务逻辑的JavaBean\** （eg:LoginDao.java 封装了登录逻辑）** ***\*b. 封装数据的JavaBean\** （实体类：eg：Student.java  Vedio.java 。往往对应于数据库中的一张表，即数据库中有个Student表，项目中就有个Student.java类）**  ③JavaBean 是一个可以重复使用的组件，通过编写一个组件来实现某种通用功能，“一次编写、任何地方执行、任何 地方重用”。

**控制器Controller**

控制器负责将视图与模型一一对应起来。相当于一个模型分发器。所谓分发就是：①接收请求，并将该请求跳转（转发，重定向）到模型进行处理。②模型处理完毕后，再通过控制器，返回给视图中的请求处。**建议使用Servlet实现控制器。**





### 三层架构

**三层架构分为：表现层（UI）(web层)***、**业务逻辑层（BLL）(service层)、数据访问层（DAL）*\*(dao层)\** \**，\**再加上实体类库（Model）**

1.实体类库（Model），在Java中，往往将其称为Entity实体类。数据库中用于存放数据，而我们通常选择会用一个专门的类来抽象出数据表的结构，类的属性就一对一的对应这表的属性。

·一般来说，Model实体类库层需要被DAL层，BIL层和UI层引用。

2.数据访问层（DAL），主要是存放对数据类的访问，即对数据库的添加、删除、修改、更新等基本操作

 ·DAL就是根据业务需求，构造SQL语句，构造参数，调用帮助类，获取结果，DAL层被BIL层调用

3.业务逻辑层（BLL）

·BLL层好比是桥梁，将UI表示层与DAL数据访问层之间联系起来。所要负责的，就是处理涉及业务逻辑相关的问题，比如在调用访问数据库之前，先处理数据、判断数据。

![watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmasdMzQxMA==,size_16,color_FFFFFF,t_70](img\watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmasdMzQxMA==,size_16,color_FFFFFF,t_70.png)





### 两者的关系

![watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZGbmV0L3dlaXhpbl80MjE1MzQxMA==,size_16,color_FFFFFF,t_70](img\watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZGbmV0L3dlaXhpbl80MjE1MzQxMA==,size_16,color_FFFFFF,t_70.png)





## 下载文件

- 获取下载文件的路径

- 获取下载文件名

- 设置想办法让浏览器支持下载我们需要的的东西

- 创建缓冲区

- 获取下载文件的输入流

- 获取output stream对象

- 将FileOutputStream流写入buffer缓冲区

- 使用output stream将缓存区中的数据输出到客户端

- ```java
      protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
  //         - 获取下载文件的路径
  //        String path=this.getServletContext().getRealPath("/kuang.png");
          String path="C:\\Users\\lenovo\\Desktop\\JAVAEE\\maven_servlet\\response_test\\src\\main\\resources\\kuang.PNG";
          System.out.println("下载路径为："+path);
  //         - 获取下载文件名
          String filename = path.substring(path.lastIndexOf("\\") + 1);
  //         - 设置想办法让浏览器支持下载我们需要的的东西
          resp.setHeader("Content-Disposition","attachment;filename="+filename+ URLEncoder.encode(filename,"UTF-8"));
  //         - 获取下载文件的输入流
          FileInputStream in = new FileInputStream(path);
  //           创建缓冲区
          int len=0;
          byte[] buffer = new byte[1024];
  //         - 获取output stream对象
          ServletOutputStream out = resp.getOutputStream();
  //         - 将FileOutputStream流写入buffer缓冲区 使用output stream将缓存区中的数据输出到客户端
          while((len=in.read(buffer))>0){
              out.write(buffer,0,len);
          }
          in.close();
          out.close();
      }
  ```

  



## 文件上传



#### jar包

```xml
commons-io
	<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.6</version>
	</dependency>

<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.4</version>
</dependency>
```



在表单中

```jsp
<form action="" enctype="multipart/form-data" method="post">
```



![image-20201005184220478](img\image-20201005184220478.png)

jsp表单

```jsp
<form action="${pageContext.request.contextPath}/upload.do" enctype="multipart/form-data" method="post">
    上传用户<input type="text" name="username"><br/>
    <p><input type="file" name="file1"></p>
    <p><input type="file" name="file2"></p>
    <p><input type="submit" value="提交">|<input type="reset"></p>
</form>
```

xml

```xml
<servlet>
    <servlet-name>FileServlet</servlet-name>
    <servlet-class>com.jie.servlet.FileServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>FileServlet</servlet-name>
    <url-pattern>/upload.do</url-pattern>
</servlet-mapping>
```

servlet

```java
package com.jie.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class FileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //判断上传的文件是普通的表单还是带文件的表单
            if (!ServletFileUpload.isMultipartContent(request)) {
                return;//如果是普通文件，我们可以直接返回
            } //如果通过了这个if，说明我们的表单是带文件上传的；

            //创建上传文件的保存路径，建议在WEB-INF路径下，安全，用户无法直接访问上传的文件；
            String uploadPath = this.getServletContext().getRealPath("/WEB-INF/upload");
            File uploadFile = new File(uploadPath);
            if (!uploadFile.exists()) {//如果目录不存在，创建这样一个目录；
                uploadFile.mkdir();
            }

            //临时路径，假如文件超过了预期的大小，我们就把他放到一个临时文件中，过几天自动删除，或者提醒用户转存为永久
            String tmpPath = this.getServletContext().getRealPath("/WEB-INF/tmp");
            File file = new File(tmpPath);
            if (!file.exists()) {//如果目录不存在，创建这样一个目录；
                file.mkdir();
            }

            //处理上传的文件，一般都需要通过流来获取，我们可以使用request.getInputStream(),原生态的文件上传流获取，十分麻烦
            //但是我们都建议使用 Apache的文件上传组件来实现，common-fileupload，它需要依赖于 commons-io组件；

            //1.创建DiskFileItemFactory对象，处理文件上传路径或者大小限制的；
            DiskFileItemFactory factory = getDiskFileItemFactory(file);
            //2.获取ServletFileUpload
            ServletFileUpload upload = getServletFileUpload(factory);
            //3.处理上传的文件
            String msg = uploadParseRequest(upload, request, uploadPath);

            //servlet请求转发消息
            request.setAttribute("msg",msg);
            request.getRequestDispatcher("info.jsp").forward(request,response);

        } catch (FileUploadException e) {
            e.printStackTrace();
        }

    }


    public static DiskFileItemFactory getDiskFileItemFactory(File file) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //通过这个工厂设置一个缓冲区，当上传的文件大于这个缓冲区的时候，将他放到临时文件中；
        factory.setSizeThreshold(1024 * 1024); //缓存区大小为1M
        factory.setRepository(file);//临时目录的保存目录，需要一个File
        return factory;
    }

    public static ServletFileUpload getServletFileUpload(DiskFileItemFactory factory) {
        ServletFileUpload upload = new ServletFileUpload(factory);
        //监听文件上传进度；
        upload.setProgressListener(new ProgressListener() {
            @Override
            //pBytesRead:已经读取到的文件大小
            //pContentLength ： 文件大小
            public void update(long pBytesRead, long pContentLength, int pItems) {
                Long p1=new Long(pBytesRead);
                Long p2=new Long(pContentLength);
                BigDecimal decimal = new BigDecimal(p1).divide(new BigDecimal(p2),2,BigDecimal.ROUND_HALF_UP);
                System.out.println("总大小："+pContentLength+"已上传:"+pBytesRead+"进度:"+decimal);
            }
        });

        //处理乱码问题
        upload.setHeaderEncoding("UTF-8");
        //设置单个文件的最大值
        upload.setFileSizeMax(1024 * 1024 * 10);
        //设置总共能够上传文件的大小
        //1024 = 1kb * 1024 = 1M * 10 = 10M
        upload.setSizeMax(1024 * 1024 * 10);

        return upload;
    }


    public static String uploadParseRequest(ServletFileUpload upload,HttpServletRequest request,String uploadPath)
            throws FileUploadException, IOException {

        String msg = "";

        //3.把前端请求解析，封装成一个FileItem对象
        List<FileItem> fileItems = upload.parseRequest(request);
        for (FileItem fileItem : fileItems) {
            if (fileItem.isFormField()){ //判断上传的文件是普通的表单还是带文件的表单
                //getFieldName指的是前端表单控件的name；
                String name = fileItem.getFieldName();
                String value = fileItem.getString("UTF-8"); //处理乱码
                System.out.println(name+":"+value);
            }else { //判断它是上传的文件

                //=======================处理文件===============================//

                //拿到文件名字
                String uploadFileName = fileItem.getName();
                System.out.println("上传的文件名："+uploadFileName);

                if (uploadFileName.trim().equals("")||uploadFileName==null){
                    continue;
                }

                //获得上传的文件名  /images/girl/paojie.png
                String fileName = uploadFileName.substring(uploadFileName.lastIndexOf("/") + 1);
                //获得文件的后缀名
                String fileExtName = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1);
                    /*
                        如果文件后缀名 fileExtName 不是我们所需要的
                        就直接return，不处理，告诉用户文件类型不对。
                    */

                System.out.println("文件信息 [件名："+fileName+"---文件类型"+fileExtName+"]");

                //可以使用UUID（唯一识别的通用码），保证文件名唯一；
                //UUID.randomUUID()，随机生一个唯一识别的通用码；
                String uuidPath = UUID.randomUUID().toString();

                //=======================处理文件完毕===============================//

                //存到哪？ uploadPath
                //文件真实存在的路径 realPath
                String realPath =   uploadPath+"/"+uuidPath;
                //给每个文件创建一个对应的文件夹
                File realPathFile = new File(realPath);
                if (!realPathFile.exists()){
                    realPathFile.mkdir();
                }

                //=======================存放地址完毕===============================//

                //获得文件上传的流
                InputStream inputStream = fileItem.getInputStream();

                //创建一个文件输出流
                //realPath = 真实的文件夹；
                //差了一个文件; 加上输出文件的名字+"/"+uuidFileName
                FileOutputStream fos = new FileOutputStream(realPath+"/"+fileName);

                //创建一个缓冲区
                byte[] buffer = new byte[1024*1024];

                //判断是否读取完毕
                int len = 0;
                //如果大于0说明还存在数据；
                while ((len=inputStream.read(buffer))>0){
                    fos.write(buffer,0,len);
                }

                //关闭流
                fos.close();
                inputStream.close();

                msg = "文件上传成功！";
                fileItem.delete(); //上传成功，清除临时文件
                //=======================文件传输完毕===============================//
            }

        }

        return msg;

    }

}
```



## 发送邮件



### jar包

```xml
<dependency>
    <groupId>javax.mail</groupId>
    <artifactId>mail</artifactId>
    <version>1.4.7</version>
</dependency>
<dependency>
    <groupId>javax.activation</groupId>
    <artifactId>activation</artifactId>
    <version>1.1.1</version>
</dependency>

```



![image-20201006152646775](img\image-20201006152646775.png)





![image-20201006152559396](img\image-20201006152559396.png)



```java
public static void main(String[] args) throws Exception {

    Properties prop = new Properties();
    prop.setProperty("mail.host", "smtp.qq.com");
    prop.setProperty("mail.transport.protocol", "smtp"); // 邮件发送协议
    prop.setProperty("mail.smtp.auth", "true"); // 需要验证用户名密码

    // 关于QQ邮箱，还要设置SSL加密，加上以下代码即可
    MailSSLSocketFactory sf = new MailSSLSocketFactory();
    sf.setTrustAllHosts(true);
    prop.put("mail.smtp.ssl.enable", "true");
    prop.put("mail.smtp.ssl.socketFactory", sf);

    //使用JavaMail发送邮件的5个步骤

    //创建定义整个应用程序所需的环境信息的 Session 对象

    Session session = Session.getDefaultInstance(prop, new Authenticator() {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            //发件人邮件用户名、授权码
            return new PasswordAuthentication("319991012@qq.com", "bksskeflcidmbgdd");
        }
    });


    //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
    session.setDebug(true);

    //2、通过session得到transport对象
    Transport ts = session.getTransport();

    //3、使用邮箱的用户名和授权码连上邮件服务器
    ts.connect("smtp.qq.com", "319991012@qq.com", "bksskeflcidmbgdd");

    //4、创建邮件

    //创建邮件对象
    MimeMessage message = new MimeMessage(session);

    //指明邮件的发件人
    message.setFrom(new InternetAddress("319991012@qq.com"));

    //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
    message.setRecipient(Message.RecipientType.TO, new InternetAddress("319991012@qq.com"));

    //邮件的标题
    message.setSubject("只包含文本的简单邮件");

    //邮件的文本内容
    message.setContent("你好啊！", "text/html;charset=UTF-8");

    //5、发送邮件
    ts.sendMessage(message, message.getAllRecipients());

    ts.close();
}
```



## SMBMS项目



### JavaBean

实体类

javaBean特定的写法：

- 必须要有一个无参构造
- 属性必须私有化
- 必须有对应的get set方法

一般用来和数据库的字段做映射 ORM （对象映射关系）

- 表-->类
- 字段-->属性
- 行记录-->对象



### SMBMS搭建



![image-20201001150922355](\img\image-20201001150922355.png)



导入数据库

编写数据库公共类

```java
package com.jie.dao;


//操作数据库的公共类


import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class BaseDao {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    static {
        //加载资源
        Properties properties = new Properties();
        InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");


        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver=properties.getProperty("driver");
        url=properties.getProperty("url");
        username=properties.getProperty("username");
        password=properties.getProperty("password");

    }

    //获取数据的连接
    public static Connection getConnection(){
        Connection connection=null;
        try {
            Class.forName(driver);
            connection= DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(driver);
        return connection;
    }

    //编写查询公共类
    public static ResultSet execute(Connection connection,String sql,Object []params,ResultSet resultSet,PreparedStatement preparedStatement ) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);

        for (int i = 0; i <params.length ; i++) {
            preparedStatement.setObject(i+1,params[i]);
        }
        resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    //编写增删改查公共方法
    public static int execute(Connection connection,String sql,Object []params,PreparedStatement preparedStatement ) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);

        for (int i = 0; i <params.length ; i++) {
            preparedStatement.setObject(i+1,params[i]);
        }
        int update = preparedStatement.executeUpdate();
        return update;
    }

    //释放资源
    public static boolean closeResourse(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet){
        boolean flag=true;
        if (resultSet!=null){
            try {
                resultSet.close();
                resultSet=null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag=false;
            }
        }

        if (preparedStatement!=null){
            try {
                preparedStatement.close();
                preparedStatement=null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag=false;
            }
        }

        if (connection!=null){
            try {
                connection.close();
                connection=null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                flag=false;
            }
        }
        return flag;
    }

}
```

过滤器

```java
request.setCharacterEncoding("utf-8");
response.setContentType("utf-8");
chain.doFilter(request,response);
```

配置资源文件

```java
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306?useUnicode=true&characterEncoding=utf-8
username=root
password=123456
```

导入静态资源



### 登录功能实现

流程

![image-20201001160418971](img\image-20201001160418971.png)



##### MVC



###### Model

数据访问层 Dao

编写DAO登录用户的接口

```java
public interface UserDao  {

    //得到要登录的用户
    public User getLoginUser(Connection connection,String userCode) throws SQLException;

}
```

实现类

```java
public class UserDaoImpl implements UserDao {
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {

        PreparedStatement pstm=null;
        ResultSet rs=null;
        User user=null;

         if(connection!=null){
             String sql="select * from smbms.smbms_user where userCode=?";
             Object[] params={userCode};

             rs = BaseDao.execute(connection, pstm, rs, sql, params);
             if (rs.next()){
                 user = new User();
                 user.setId(rs.getInt("id"));
                 user.setUserCode(rs.getString("userCode"));
                 user.setUserName(rs.getString("userName"));
                 user.setUserPassword(rs.getString("userPassword"));
                 user.setGender(rs.getInt("gender"));
                 user.setBirthday(rs.getDate("birthday"));
                 user.setPhone(rs.getString("phone"));
                 user.setAddress(rs.getString("address"));
                 user.setUserRole(rs.getInt("userRole"));
                 user.setCreatedBy(rs.getInt("createdBy"));
                 user.setCreationDate(rs.getTimestamp("creationDate"));
                 user.setModifyBy(rs.getInt("modifyBy"));
                 user.setModifyDate(rs.getTimestamp("modifyDate"));
             }
             BaseDao.closeResourse(null,pstm,rs);
         }

        return user;

    }
}
```



业务逻辑层 Service

业务层接口

```java
public interface UserService {
    //用户登录
    public User login(String useCode, String password);
}
```

实现

```java
public class UserServicelmpl implements UserService {


    //引入dao层
    private UserDao userDao;

    private UserServicelmpl(){
        userDao=new UserDaoImpl();
    }


    //通过业务层调用dao层
    @Override
    public User login(String useCode, String password) {
        Connection connection=null;
        User user=null;


        try {
            connection= BaseDao.getConnection();
            user=userDao.getLoginUser(connection,useCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResourse(connection,null,null);
        }
        return user;
    }

    public static void main(String[] args) {
        UserServicelmpl userServicelmpl = new UserServicelmpl();
        User admin = userServicelmpl.login("admin","123456");
        System.out.println(admin.getBirthday());
    }
}
```



###### Control

查询数据是否配对

```java
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode = req.getParameter("userCode");
        String password = req.getParameter("userPassword");
        UserService userService=new UserServicelmpl();
        User user = userService.login(userCode, password);

        if(user!=null){  //查有此人
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            //转到
            resp.sendRedirect("jsp/frame.jsp");
        }else{  //无法登录
            req.setAttribute("error","用户名或者密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }



    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
```

退出登录

```java
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.removeAttribute(Constants.USER_SESSION);
        resp.sendRedirect(req.getContextPath()+"/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```

退出session优化

```java
public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request=(HttpServletRequest) req;
    HttpServletResponse response=(HttpServletResponse) resp;


    if (request.getSession().getAttribute(Constants.USER_SESSION)!=null){
        chain.doFilter(req,resp);
    }else{
        response.sendRedirect(request.getContextPath()+"/error.jsp");
    }


}
```



###### View

```java
jsp静态资源导入
```



### 密码修改

#### **Model**

##### dao层 数据查询

接口

```java
public int updatePwd(Connection connection,int id,int password) throws SQLException;
```

实现类

```java
//修改用户密码
@Override
public int updatePwd(Connection connection, int id, int password) throws SQLException {
    PreparedStatement pstm=null;
    int execute=0;

    if (connection!=null){
        String sql="update smbms.smbms_user set userPassword=? where ?";
        Object []params={password,id};
        execute = BaseDao.execute(connection, pstm, sql, params);
        BaseDao.closeResourse(null,pstm,null);
    }
    return execute;

}
```



##### service 业务层

```java
//修改用户密码
@Override
public boolean updatePwd(int id, String pwd) {
    Connection connection = null;
    boolean flag=false;


    try {
        connection=BaseDao.getConnection();
        if (userDao.updatePwd(connection, id, pwd)>0){
            flag=true;
        }
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }finally {
        BaseDao.closeResourse(connection,null,null);
    }
    return flag;
}
```

#### **Control**

```java
public void update(HttpServletRequest req, HttpServletResponse resp){
    Object o =  req.getSession().getAttribute(Constants.USER_SESSION);

    String newpassword=req.getParameter("newpassword");
    String olodpassword=req.getParameter("oldpassword");


    boolean flag=false;

    if (o!=null && !StringUtils.isNullOrEmpty(newpassword) && ((User)o).getUserPassword().equals(olodpassword)){
        UserService userService=new UserServicelmpl();
        flag = userService.updatePwd(((User) o).getId(), newpassword);
        if (flag){
            req.setAttribute(Constants.USER_MESSAGE,"修改密码成功，请退出重新登录");
            req.getSession().removeAttribute(Constants.USER_SESSION);
        }else{
            req.setAttribute(Constants.USER_MESSAGE,"修改密码失败");
        }
    }else{
        req.setAttribute(Constants.USER_MESSAGE,"修改密码失败");
    }
    try {
        resp.sendRedirect(req.getContextPath()+"/jsp/pwdmodify.jsp");
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```









# Test

```xml
<context-param>
    <param-name>user</param-name>
    <param-value>admin</param-value>
</context-param>
<context-param>
    <param-name>password</param-name>
    <param-value>123</param-value>
</context-param>
<servlet>
    <servlet-name>config</servlet-name>
    <servlet-class>com.jie.servlet.servlet1</servlet-class>
    <init-param>
        <param-name>namespace</param-name>
        <param-value>123</param-value>
    </init-param>
</servlet>
<servlet-mapping>
    <servlet-name>config</servlet-name>
    <url-pattern>/1</url-pattern>
</servlet-mapping>
```

```java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    ServletConfig servletConfig = getServletConfig();
    resp.getWriter().println(getServletContext().getInitParameter("user"));
    resp.getWriter().println(getServletContext().getInitParameter("password"));
    resp.getWriter().println(getServletConfig().getInitParameter("namespace"));
}
```

