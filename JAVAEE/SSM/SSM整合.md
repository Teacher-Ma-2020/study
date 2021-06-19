#  SSM整合

## Mybatis层

导入依赖

```xml
    <dependencies>

        <!--        mybatis-spring-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.5</version>
        </dependency>
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
<!--        数据库连接池-->
        <dependency>
            <groupId>com.mchange</groupId>
            <artifactId>c3p0</artifactId>
            <version>0.9.5.2</version>
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

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.1.9.RELEASE</version>
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



### mybatis核心配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>
<!--    别名-->
    <settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>

    <typeAliases>
        <package name="com.jie.pojo"/>
    </typeAliases>

    <mappers>
        <package name="com.jie.dao.BookMapper"/>
    </mappers>
</configuration>
```



```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/smbms?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC
jdbc.username=root
jdbc.password=123456
```





### **dao层的接口**

```java
public interface BookMapper {
    int addBook(Books books);
    int deleteBook(@Param("bookID") int bookID);
    int updateBook(Books books);
    Books queryBook(@Param("bookID")int bookID);
    List<Books> queryAllBook();
    List<Books> queryNameBook(@Param("name") String name);
}

```

**xml实现**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.jie.dao.BookMapper">
    <insert id="addBook" parameterType="books">
        insert into ssmbuild.books(bookID, bookName, bookCounts, detail) VALUES (#{bookID},#{bookName},#{bookCounts},#{detail});
    </insert>
    <delete id="deleteBook">
        delete from ssmbuild.books where bookID=#{bookID};
    </delete>
    <update id="updateBook" parameterType="books">
        update ssmbuild.books set bookName=#{bookName},bookCounts=#{bookCounts},detail=#{detail} where bookID=#{bookID};
    </update>
    <select id="queryBook" resultType="books">
        select * from ssmbuild.books where bookID=#{bookID};
    </select>
    <select id="queryAllBook" resultType="books">
        select * from ssmbuild.books;
    </select>

    <select id="queryNameBook" resultType="books">
        select * from ssmbuild.books
        <where>
            <if test="name!=''">
                and bookName like  '%${name}%'
            </if>
        </where>
    </select>


</mapper>
```





### service层接口

```java
public interface BookService {
    int addBook(Books books);
    int deleteBook(int bookID);
    int updateBook(Books books);
    Books queryBook(int bookID);
    List<Books> queryAllBook();
    List<Books> queryNameBook(String name);

}

```

实现

```java
public class BookServiceImpl implements BookService {

//    service层调dao层
    private BookMapper bookMapper;

    public void setBookMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public int addBook(Books books) {
        return bookMapper.addBook(books);
    }

    public int deleteBook(int bookID) {
        return bookMapper.deleteBook(bookID);
    }

    public int updateBook(Books books) {
        return bookMapper.updateBook(books);
    }

    public List<Books> queryNameBook(String name) { return bookMapper.queryNameBook(name); }

    public Books queryBook(int bookID) {
        return bookMapper.queryBook(bookID);
    }

    public List<Books> queryAllBook() {
        return bookMapper.queryAllBook();
    }
}
```



## Spring整合

spring模板

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">


</beans>
```



### spring整合 dao层

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

<!--    1.关联数据库配置文件-->
    <context:property-placeholder location="classpath:database.properties"/>
<!--    2.连接池-->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${jdbc.driver}"/>
        <property name="jdbcUrl" value="${jdbc.url}"/>
        <property name="user" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>

<!--    3.sqlsessionFactory-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
    </bean>

<!--    扫描dao接口动态实现了注入-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
<!--        注入sqlSessionFactory-->
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
<!--        扫描的包-->
        <property name="basePackage" value="com.jie.dao"/>
    </bean>


</beans>
```



### spring整合 service层

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
<!--    扫描service的包-->
    <context:component-scan base-package="com.jie.service"/>

<!--    业务层实现注入spring-->
    <bean class="com.jie.service.BookServiceImpl" id="bookService">
        <property name="bookMapper" ref="bookMapper"/>
    </bean>

<!--    声明式事务-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

<!--    aop-->


</beans>
```



### spring整合 mvc层

web.xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:applicationContext.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

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

<!--    Session-->
    <session-config>
        <session-timeout>15</session-timeout>
    </session-config>
</web-app>
```



spring-mvc.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd
         http://www.springframework.org/schema/context
         https://www.springframework.org/schema/context/spring-context.xsd
         http://www.springframework.org/schema/mvc
         http://www.springframework.org/schema/mvc/spring-mvc.xsd">
<!--    注解驱动-->
    <mvc:annotation-driven/>
<!--    静态资源过滤-->
    <mvc:default-servlet-handler/>
<!--    扫描包-->
    <context:component-scan base-package="com.jie.controller"/>
<!--    视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

</beans>
```



### web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:applicationContext.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>

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

<!--    Session-->
    <session-config>
        <session-timeout>15</session-timeout>
    </session-config>
</web-app>
```



## 控制层

```java
@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    @Qualifier("bookServiceImpl")
    private BookService bookService;

    @RequestMapping("/allBook")
    private String list(Model model){
        List<Books> books = bookService.queryAllBook();
        model.addAttribute("list",books);
        return "allbook";
    }

    @RequestMapping("/addBook")
    public String toAddpage(){
        return "addBook";
    }

    @RequestMapping("/insertBook")
    public String Addpage(Books books){
        System.out.println("addbook"+books);
        bookService.addBook(books);
        return "redirect:/book/allBook";
    }

    @RequestMapping("/toUpdate/{i}")
    public String toUpdate(@PathVariable("i") int i, Model model){
        Books book = bookService.queryBook(i);
        System.out.println(book+"111");
        model.addAttribute("book",book);
        return "updateBook";
    }

    @RequestMapping("/Update/{id}")
    public String Update(@PathVariable("id") int id,Books books){
        books.setBookID(id);
        bookService.updateBook(books);
        return "redirect:/book/allBook";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id){
        bookService.deleteBook(id);
        return "redirect:/book/allBook";
    }

    @RequestMapping("/queryBook")
    public String queryBook(Model model,String queryName){
        List<Books> list = bookService.queryNameBook(queryName);
        model.addAttribute("list",list);
        return "allbook";
    }
}
```



## JSP前端



### **index.jsp**

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
    <style>
      a{
        text-decoration: none;
        font-size: 18px;
        color: black;
      }
      h3{
        width: 180px;
        height: 50px;
        margin: 100px auto;
        text-align: center;
        line-height: 38px;
        background: deepskyblue;
      }
    </style>
  </head>
  <body>
  <h3>
    <a href="${pageContext.request.contextPath}/book/allBook">进入书籍页面</a>
  </h3>
  </body>
</html>
```



### allbook.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>书籍展示页面</title>
</head>
<body>

<form action="${pageContext.request.contextPath}/book/queryBook">
    查询名称：<input type="text" name="queryName">
    <input type="submit" value="查询">
</form>
<table border="1">
    <tr>
        <td><c:out value="书籍id"/></td>
        <td><c:out value="书籍名称"/></td>
        <td><c:out value="剩余数量"/></td>
        <td><c:out value="详情"/></td>
        <td><a href="${pageContext.request.contextPath}/book/addBook">添加</a></td>
    </tr>
    <c:forEach items="${list}" var="i">
        <tr>
            <td><c:out value="${i.bookID}"/></td>
            <td><c:out value="${i.bookName}"/></td>
            <td><c:out value="${i.bookCounts}"/></td>
            <td><c:out value="${i.detail}"/></td>
            <td>
                <a href="${pageContext.request.contextPath}/book/toUpdate/${i.bookID}">修改</a>
                |
                <a href="${pageContext.request.contextPath}/book/delete/${i.bookID}">删除</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
```

### addBook.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<form action="${pageContext.request.contextPath}/book/insertBook" >
    书籍名称：<input type="text" name="bookName" required/><br/>
    书籍数量：<input type="text" name="bookCounts" required/><br/>
    描述：<input type="text" name="detail" required/><br/>
    <input type="submit" value="添加"/>
</form>

</body>
</html>
```

updateBook.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="${pageContext.request.contextPath}/book/Update/${book.bookID}" >
        书籍名称：<input type="text" name="bookName" value="${book.bookName}" required/><br/>
        书籍数量：<input type="text" name="bookCounts" value="${book.bookCounts}" required/><br/>
        描述：<input type="text" name="detail" value="${book.detail}" required/><br/>
        <input type="submit" value="添加"/>
    </form>
</body>
</html>
```