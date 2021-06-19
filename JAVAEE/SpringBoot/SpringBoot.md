 

[TOC]



# 注解

## **spring**

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/aop
        http://www.springframework.org/schema/aop/spring-aop.xsd">

</beans>
```

**@SpringBootApplication**：标注即为为SpringBoot的主核心类 ，运行该类的main方法即为启动SpringBoot的核心应用

**@ConfigurationProperties**  告诉springBoot是本类的相关属性和配置文件的相关属性进行绑定

- **prefix**  :  与配置文件中的名称进行对应

**@Component** ：提交给SpringBoot容器

**@Configuration**：配置文件类

- proxyBeanMethods = false：单实例

- @Bean  ：加在方法名上 表示添加bean

**@Import(User2.class)** ：在配置类上可以导入Bean对象

**@ConditionalOnBean(name = "user2")** ：条件装配：满足Conditional指定的条件，则进行组件注入

**@RequestPart** ：获得上传文件



----------------------------------------

## **mybatis**

**@TableName("user")**：表示mybatis-plus 与数据库对应的数据名 (若不添加此注解 则会采用Bean类的类名)

**@TableField(exist = false)**: mybatis-plus中默认 类中的属性名和数据库的列名必须是一一对应的

如果有其他的类属性，则需要加上这个注解

**@Mapper**表示该结构为Mapper，不写这个可以使用

**@MapperScan**  如果不写**@Mapper** 扫描到的接口也为Mapper



## JUnit注解



- **@Test :**表示方法是测试方法。但是与JUnit4的@Test不同，他的职责非常单一不能声明任何属性，拓展的测试将会由Jupiter提供额外测试
- **@ParameterizedTest :**表示方法是参数化测试，
- **@RepeatedTest :**表示方法可重复执行， 设置重复次数
- **@DisplayName :**为测试类或者方法设置名称
- **@BeforeEach :**表示在每个单元测试之前执行   
  - （如果调用多个测试测试方法  所有测试方法都会执行一次 each方法）
- **@AfterEach :**表示在每个单元测试之后执行
- **@BeforeAll :**表示在所有单元测试之前执行
  - 该注解 在测试方法中只会调用一次  （需要设置为静态方法）
- **@AfterAll :**表示在所有单元测试之后执行
- **@Tag :**表示单元测试类别，类似于JUnit4中的@Categories
- **@Disabled :**表示测试类或测试方法不执行，类似于JUnit4中的@Ignore
- **@Timeout :**表示测试方法运行如果超过了指定时间将会返回错误
- **@ExtendWith :**为测试类或测试方法提供扩展类引用
- **@Nested**:用于内部类的整体测试

# thymeleaf模板

```html
xmlns:th="http://www.thymeleaf.org"
xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
xmlns:shiro="http://www.pollix.at/thymeleaf/shiro"

<html lang="en" xmlns:th="http://www.thymeleaf.org" 
				xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
				xmlns:shiro="http://www.pollix.at/thymeleaf/shiro">
```





# 1.SpringBoot入门



SpringBoot用来简化Spring应用开发，整合Spring技术栈的大整合，JavaEE一站式解决方案



**优点**∶

- 快速创建独立运行的Spring项目以及与主流框架集成
- 使用嵌入式的Servlet容器，应用无需打成WAR包
- starters自动依赖与版本控制
- 大量的自动配置，简化开发，也可修改默认值
- 无需配置XML，无代码生成，开箱即用
- 准生产环境的运行时应用监控
- 与云计算的天然集成





## 1.1Maven设置

给maven 的settings.xml配置文件的profiles标签添加：（设置使用的jdk版本）

开发工具中的maven设置为自己配置的maven

```xml
<profile>
  <id>jdk-1.8</id>
  <activation>
    <activeByDefault>true</activeByDefault>
    <jdk>1.8</jdk>
  </activation>
  <properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
    <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
  </properties>
</profile>
```



## 1.2程序

**主程序**

```java
//主程序
@SpringBootApplication
public class HelloController {
    public static void main(String[] args) {
        //spring应用
        SpringApplication.run(HelloBoot.class,args);
    }
}
```

**servie**

```java
@Controller
public class HelloBoot {
    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        return "hello Word";
    }
}
```



**@SpringBootApplication**：标注即为为SpringBoot的主核心类 ，运行该类的main方法即为启动SpringBoot的核心应用







## 1.4生成jar包

```xml
<build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>1.5.9.RELEASE</version>
            </plugin>
        </plugins>
    </build>
```



将生成的jar包执行

![image-20201213200104019](img\1.png)





# 2.自动配置原理



## 2.1 SpringBoot自动配置

- 自动配好Tomcat

- - 引入Tomcat依赖。
  - 配置Tomcat

```xml
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-tomcat</artifactId>
      <version>2.4.1.RELEASE</version>
      <scope>compile</scope>
</dependency>
```

- 自动配好SpringMVC

- - 引入SpringMVC全套组件
  - 自动配好SpringMVC常用组件（功能）

- 自动配好Web常见功能，如：字符编码问题

- - SpringBoot帮我们配置好了所有web开发的常见场景

- 默认的包结构

- - 主程序所在包及其下面的所有子包里面的组件都会被默认扫描进来
  - 无需以前的包扫描配置
  - 想要改变扫描路径，@SpringBootApplication(scanBasePackages=**"com.jie"**)

- - - 或者@ComponentScan 指定扫描路径

```
@SpringBootApplication
等同于
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan("com.atguigu.boot")
```



- 各种配置拥有默认值

- - 默认配置最终都是映射到某个类上，如：MultipartProperties
  - 配置文件的值最终会绑定每个类上，这个类会在容器中创建对象

- 按需加载所有自动配置项

- - 非常多的starter
  - 引入了哪些场景这个场景的自动配置才会开启
  - SpringBoot所有的自动配置功能都在 spring-boot-autoconfigure 包里面
  - 

- ......



## 2.2 组件添加

1.**@Configuration**：表明为配置类

- (proxyBeanMethods = true)    保证每个@Bean方法被调用多少次返回的组件都是单实例的
- (proxyBeanMethods = false)   保证每个@Bean方法被调用多少次返回的组件都是新创建的



2.**@Bean**  ：加在方法名上 表示添加bean

```java
@Configuration(proxyBeanMethods = false) //工厂模式
public class MyConfig {
    @Bean //给容器中添加组件。以方法名作为组件的id。返回类型就是组件类型。返回的值，就是组件在容器中的实例
    public User user01(){
        User zhangsan = new User("zhangsan", 18);
        //user组件依赖了Pet组件
        zhangsan.setPet(tomcatPet());
        return zhangsan;
    }

    @Bean("tom")
    public Pet tomcatPet(){
        return new Pet("tomcat");
    }
}
```



3.**@Import(User2.class)** ：在配置类上可以导入Bean



4.**@ConditionalOnBean(name = "user2")** ：条件装配：满足Conditional指定的条件，则进行组件注入

![image-20201223212852309](img\4.png)



## 2.3 原生配置文件引入

**@ImportResource** 注解用于导入Spring的配置文件，让配置文件里面的内容生效；

- springboot默认加载的配置文件名是**`application`**，如果配置文件名不是这个是不会被容器加载的,需要使用注解导入


```java
@ImportResource(locations = {"classpath:beans.xml"})
@SpringBootApplication
public class SpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

}
```



## 2.4 配置绑定 

**@ConfigurationProperties**

1.@Component + @ConfigurationProperties

读取 Properties文件里的属性  再通过**@Component**注入到容器中

```java
@Component
@ConfigurationProperties(prefix = "tomcat")
public class Cat {
    private String name;
    private int id;
}
```



2.@EnableConfigurationProperties + @ConfigurationProperties

也可以通过开启类的绑定功能 由**@EnableConfigurationProperties**将其注入到容器中

```java
@EnableConfigurationProperties(Cat.class)
public class MyConfig {
    @ConditionalOnBean(name = "user2")
    @Bean
    public Dog dog() {
        return new Dog();
    }
}
```

```java
@ConfigurationProperties(prefix = "tomcat")
public class Cat {
    private String name;
    private int id;
}
```

# 3.配置文件

SpringBoot使用一个全局的配置文件，配置文件名`application`是固定的；

- application.properties
- application.yml

配置文件的作用：修改SpringBoot自动配置的默认值；SpringBoot在底层都给我们自动配置好；



## 2.1 YAML

以前的配置文件；大多都使用的是 **xxxx.xml**文件；

 YAML：**以数据为中心**，比json、xml等更适合做配置文件；



K:V对      书写

**普通字符**

默认不需要书写引号

若加入引号：

双引号；不会转义字符串里面的特殊字符；特殊字符会作为本身想表示的意思

```yml
name: "zhangsan \n lisi"：输出；zhangsan 换行 lisi
```



单引号；会转义特殊字符，特殊字符最终只是一个普通的字符串数据

```yml
name: 'zhangsan \n lisi'：输出；zhangsan \n lisi
```



### 对象、Map（属性和值）

`k: v`在下一行来写对象的属性和值的关系；注意缩进

1. ```yaml
   person:
     name: 张三
     gender: 男
     age: 22Copy to clipboardErrorCopied
   ```

2. 行内写法

   ```yaml
   person: {name: 张三,gender: 男,age: 22}Copy to clipboardErrorCopied
   ```

### 数组（List、Set）

1. ```yaml
   fruits: 
     - 苹果
     - 桃子
     - 香蕉Copy to clipboardErrorCopied
   ```

2. 行内写法

   ```yaml
   fruits: [苹果,桃子,香蕉]
   ```



## 2.2 YML传入数据

需要配置文件处理器（yml**自动提示**）

```xml
<!--导入配置文件处理器，配置文件进行绑定就会有提示-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

配置插件保证打成jar包的过程中 **不需要导入**文件处理器

```xml
 <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-configuration-processor</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
```



实体类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "user")
public class User {
    public int id;
    private String name;
    private Boolean isBoy;
    private Date birthday;
    private Dog dog;
    private int []nums;
    private Map<String,Object> map;
}
```



**@ConfigurationProperties**  告诉springBoot是本类的相关属性和配置文件的相关属性进行绑定

**prefix**  :  与配置文件中的名称进行对应

**@Component** ：提交给SpringBoot容器



application.yml

```yaml
server:
  port: 80

user:
  id: 1
  name: zcj
  isBoy: false
  birthday: 2020/12/16
  nums: [1,2]
  map: {k1: 1,k2: 2}
  dog:
    name: 小狗
```



测试类

```java
@SpringBootTest
class SpringbootApplicationTests {

    @Autowired
    User user;

    @Test
    void contextLoads() {
        System.out.println(user);
    }

}
```



结果如图

![image-20201216181034877](img\2.png)



## 2.3 Properties传值

```java
user.id=1
user.name=zcj
user.nums=1,2,3
user.birthday=2020/12/16
user.is-boy=false
user.map.k1=1
user.map.k2=2
user.dog.name=小狗狗
```



## 2.4 配置文件占位符

```java
${random.value}
${random.int}
${random.long}
${random.int(10)}
${random.int[1024,65536]}
```



可以引用在配置文件中配置的其他属性的值，如果使用一个没有在配置文件中的属性，则会原样输出



可以使用`:`指定默认值

```yaml
server:
  port: 8080

user:
  id: ${random.int(1,10)}
  u_name: zcj${random.uuid}
  isBoy: false
  birthday: 2020/12/16
  nums: [1,2]
  map: {k1: 1,k2: 2}
  dog:
    name: ${user.u_name：ddd}
```



## 2.5 Profile功能

Profile是Spring对不同环境提供不同配置功能的支持，可以通过激活、指定参数等方式快速切换环境



方法1：

**多profile文件形式**

文件名格式：application-{profile}.properties/yml，例如：

- application-dev.properties
- application-prod.properties

可以在主配置文件中指定激活哪个配置文件  

```yaml
spring.profiles.active=dev
```

以激活的配置文件作为启动配置内容 优先进行配置



方法2：

yml支持多文档块方式

```yaml
spring:
  profiles:
    active: prod
---
server:
  port: 8081
spring:
  profiles: dev2
---
server:
  port: 80
  servlet:
    context-path: /hello2
spring:
  profiles: prod

```



**激活指定profile的三种方式**

1. 在配置文件中指定 spring.profiles.active=dev（如上）

   

2. 项目打包后在命令行启动（cmd）

   ```shell
   java -jar xxx.jar --spring.profiles.active=dev
   ```

3. 虚拟机参数

   ```
   -Dspring.profiles.active=dev
   ```



## 2.6 配置文件加载位置

springboot 启动会扫描以下位置的application.properties或者application.yml文件作为Spring boot的默认配置文件

```
file: ./config/

    file: ./

        classpath: /config/

            classpath: /        
```

优先级由高到底，高优先级的配置会覆盖低优先级的配置（优先级低的先加载）；

SpringBoot会从这四个位置全部加载主配置文件；**互补配置**；



## 2.7 外部配置加载顺序  (了解)



**SpringBoot也可以从以下位置加载配置； 优先级从高到低；高优先级的配置覆盖低优先级的配置，所有的配置会形成互补配置**

1. **命令行参数** 

   所有的配置都可以在命令行上进行指定

   ```
   java -jar xxx.jar --server.port=8087  --server.context-path=/abc
   ```

   多个配置用空格分开； --配置项=值

2. 来自java:comp/env的JNDI属性 

3. Java系统属性（System.getProperties()） 

4. 操作系统环境变量 

5. RandomValuePropertySource配置的random.*属性值 

**由jar包外向jar包内进行寻找；**

**优先加载带profile**

1. jar包外部的`application-{profile}.properties`或`application.yml`(带spring.profile)配置文件 
2. jar包内部的`application-{profile}.properties`或`application.yml`(带spring.profile)配置文件 



**再来加载不带profile**

1. jar包**外部**的`application.properties`或`application.yml`(不带spring.profile)配置文件*
2. jar包**内部**的`application.properties`或`application.yml`(不带spring.profile)配置文件 



1. @Configuration注解类上的@PropertySource 
2. 通过SpringApplication.setDefaultProperties指定的默认属性 

# 4.WEB开发

## 4.1 静态资源

静态资源放在类路径下： 

- `/static` 
- `/public`
-  `/resources` 
-  `/META-INF/resources`

访问 ： 当前项目根路径/ + 静态资源名

![image-20201224164522812](img\5.png)



**静态资源访问前缀**

默认无前缀

设置

```yaml
spring:
  mvc:
    static-path-pattern: /res/**
```

访问路径： 当前项目 + static-path-pattern + 静态资源名       



## 4.2 普通参数与基本注解

注解：

**@RequestParam**： 接收前端传递

**@RequestAttribute**：获取request请求中的数据

```java
@GetMapping("/hello/{id}")
@ResponseBody
public String hello6(@PathVariable("id") String id,@RequestAttribute("name") String name){
    System.out.println(name);
    return id;
}
```

**@PathVariable("a")** : 接收Restful格式传递的参数

```java
@GetMapping("/hello/{id}")
@ResponseBody
public String hello5(@PathVariable("id") String id){
    return id;
}
```

**@RequestHeader** ：获取请求头中的数据

**@MatrixVariable**:  获取矩阵变量的值(SpringBoot默认是禁用了矩阵变量的功能)

```properties
url: http://localhost/hello/1;name=a;password=b
```

```java
@GetMapping("/hello/{id}")
@ResponseBody
public String hello6(@PathVariable("id") String id,@MatrixVariable("name") String name, @MatrixVariable("password") String password){
    System.out.println(name);
    System.out.println(password);
    return id;
}
```



## 4.3 REST映射

开启REST请求：

```yaml
spring:
  mvc:
    hiddenmethod:
      filter:
        enabled: true
```

请求

```html
<!--get-->
<form action="/index" method="get">
    <input type="submit" value="get">
</form>
<!--post-->
<form action="/index" method="post">
    <input type="submit" value="post">
</form>
<!--delete-->
<form action="/index" method="post">
    <input type="hidden" name="_method" value="DELETE">
    <input type="submit" value="DELETE">
</form>
<!--put-->
<form action="/index" method="post">
    <input type="hidden" name="_method" value="PUT">
    <input type="submit" value="put">
</form>
```

接收

```java
@GetMapping("/index") //=@RequestMapping(value = "/index",method = RequestMethod.GET)
public String hello(){
    return "GET";
}

@PostMapping("/index") //=@RequestMapping(value = "/index",method = RequestMethod.POST)
public String hello2(){
    return "POST";
}

@DeleteMapping("/index") //=@RequestMapping(value = "/index",method = RequestMethod.DELETE)
public String hello3(){
    return "DELETE";
}

@PutMapping("/index") //=@RequestMapping(value = "/index",method = RequestMethod.PUT)
public String hello4(){
    return "PUT";
}
```





## 4.4 Thymeleaf

thymeleaf使用

1、引入Starter

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
```



Thymeleaf模板

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

</body>
</html>
```

### 遍历

```html
<tr class="gradeX" th:each="user:${users}">
    <td>Trident</td>
    <td th:text="${user.username}">Internet</td>
    <td th:text="${user.password}">Win 95+</td>
</tr>
```



### 判断                                                                                                                                                                                                

​                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         

### 抽取模板

**方式1**

 加入**th:fragment**=“命名”

```html
<head th:fragment="commontheader">
    <link href="css/style.css" th:href="@{/css/style.css}" rel="stylesheet">
    <link href="css/style-responsive.css" th:href="@{/css/style-responsive.css}" rel="stylesheet">

    <script src="js/html5shiv.js"  th:src="@{/js/html5shiv.js}"></script>
    <script src="js/respond.min.js" th:src="@{/js/respond.min.js}"></script>
</head>
```

声明

```html
<link th:include="common  :: commontheader">
```



**方式2**

声明id

```html
<div id="leftmenu" class="left-side sticky-left-side">
	····
</div>
```

调用

```java
<div th:replace="common:: #leftmenu"></div>
```



## 4.5 拦截器

1.生成拦截类 实现HandlerInterceptor

```java
//登陆检查
public class LoginInterceptor implements HandlerInterceptor {

    //目标方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("loginUser")!=null){
            return true;
        }
        session.setAttribute("msg","请重新登陆");
        response.sendRedirect("/");
        return false;
    }

    //目标方法执行之后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //页面渲染以后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
```



2.配置拦截类

```java
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {   //实现WebMvcConfigurer接口

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")     //添加拦截器并保存路径
                .excludePathPatterns("/","/login","/css/**","/fonts/**","/images/**","/js/**");    //设置放行路径
    }
}
```



其中需要放行静态资源

- 方法1

  - ```java
    excludePathPatterns("/","/login","/css/**","/fonts/**","/images/**","/js/**")
    ```

- 方法2

  - 配置文件设置静态路径

  - ```properties
    spring.mvc.static-path-pattern=/static/**
    ```

  - 然后只需要放行

  - ```java
    excludePathPatterns("/","/login","/static/**")
    ```

  



## 4.6 文件上传



### 4.6.1前端

```html
<form role="form" th:action="@{/upload}" method="post" enctype="multipart/form-data">
    <div class="form-group">
        <label for="exampleInputEmail1">邮件</label>
        <input type="email" class="form-control"  id="exampleInputEmail1" name="email" placeholder="Enter email">
    </div>
    <div class="form-group">
        <label for="exampleInputPassword1">名字</label>
        <input type="password" class="form-control" id="exampleInputPassword1" name="username" placeholder="Password">
    </div>
    <div class="form-group">
        <label for="exampleInputFile">头像</label>
        <input type="file" id="exampleInputFile" name="headerImg">
    </div>
    <div class="form-group">
        <label for="exampleInputFile" >生活照</label>
        <input type="file" name="photos" multiple >
        <!--多文件提交-->
    </div>
    <div class="checkbox">
        <label>
            <input type="checkbox"> Check me out
        </label>
    </div>
    <button type="submit" class="btn btn-primary">提交</button>
</form>
```

**单文件上传**

```html
<div class="form-group">
    <label for="exampleInputFile">头像</label>
    <input type="file" id="exampleInputFile" name="headerImg">
</div>
```
多文件上传需要加上**multiple**

```html
<div class="form-group">
    <label for="exampleInputFile" >生活照</label>
    <input type="file" name="photos" multiple >
    <!--多文件提交-->
</div>
```



### 4.6.2后端

propertis控制文件配置上传**大小限制**

```properties
#设置上传单个文件大小
spring.servlet.multipart.max-file-size=10MB
#设置上传总文件大小
spring.servlet.multipart.max-request-size=100MB
```

控制层

```java
//    MultipartFile  自动封装上传过来的文件
    @PostMapping("/upload")
    public String upload(@RequestParam("email") String email,
                         @RequestParam("username") String username,
                         @RequestPart("headerImg") MultipartFile headerImg,
                         @RequestPart("photos") MultipartFile[] photos) throws IOException {
        if (!headerImg.isEmpty()){
            String originalFilename = headerImg.getOriginalFilename();                //获得文件名
            headerImg.transferTo(new File("D:\\txt\\"+originalFilename));    //上传到本地   也可以使用getInputStream获得流
        }

        if(photos.length>0){
            for (MultipartFile photo:photos){
                if (!photo.isEmpty()){
                    String originalFilename = photo.getOriginalFilename();//获得文件名
                    photo.transferTo(new File("D:\\txt\\"+originalFilename));
                }
            }
        }

        return "main";
    }
```



**@RequestPart** ：获得上传文件   

- **MultipartFile**：自动封装上传过来的文件
  - **headerImg.isEmpty()**：判断是否为空
  - **headerImg.getOriginalFilename()** 获得文件名称
  - **headerImg.transferTo(new File("D:\\txt\\"+originalFilename))** 上传文件路径
  - **headerImg.getInputStream()** ：获取文件流





## 4.7 异常处理

- 默认情况下，Spring Boot提供`/error`处理所有错误的映射

- 对于机器客户端，它将生成JSON响应，其中包含错误，HTTP状态和异常消息的详细信息。对于浏览器客户端，响应一个“ whitelabel”错误视图，以HTML格式呈现相同的数据

- ![image.png](img\7.png)![image.png](img\8.png)

  

### 4.7.1自定义异常页面

![image-20210105172816369](img\6.png)

在templates文件下放入 404、500 html  错误时会自动显示项目中的错误页面

也可以取名字为 4xx.html    5xx.html ,当错误为4或者5**开头**时 便会显示对应的页面



### 4.7.2 页面显示错误信息





## 4.8 原生注解注入



### 4.8.1导入servletAPI

无论是导入Servlet、Filter、Listener 都需要在主配置类上加入**@ServletComponentScan**    

并且指明扫描的包

（如果不加,即为扫描主配置类所在目录下的所有包）

```java
@ServletComponentScan(basePackages = "com.jie.boot.servlet")
@SpringBootApplication
public class BootAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootAdminApplication.class, args);
    }

}
```





1.导入 **@WebServlet**(urlPatterns = "/my")  注解 并且**标明路径**

```java
@WebServlet(urlPatterns = "/my")
public class MyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("asd");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```



2.导入Filter  **@WebFilter**(urlPatterns = {"/css/*","/images/*","/my"})

```java
@Slf4j
@WebFilter(urlPatterns = {"/css/*","/images/*","/my"})
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("过滤器初始化成功");
    }

    @Override
    public void destroy() {
        log.info("过滤器销毁");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("Filet工作");
        filterChain.doFilter(servletRequest,servletResponse); //放行
    }
}
```



3.导入Listener

```java
@Slf4j
@WebListener
public class MyListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("监听器创建");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("监听器销毁");
    }
}
```



### 4.8.2 使用RegistrationBean

将ServletRegistrationBean`, `FilterRegistrationBean`,  `ServletListenerRegistrationBean 注入到容器中

```java
@Configuration(proxyBeanMethods = false) //保证serlvet、监听器、过滤器都是单实例的
public class MyRegistConfig {
//    servlet
    @Bean
    public ServletRegistrationBean myServlet(){
        MyServlet myServlet=new MyServlet();
        return new ServletRegistrationBean(myServlet,"/my");
    }

//    过滤器
    @Bean
    public FilterRegistrationBean myFiler(){
        MyFilter myFilter=new MyFilter();
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean(myFilter);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return  filterRegistrationBean;
    }

    //监听器
    @Bean
    public ServletListenerRegistrationBean myListener(){
        MyListener myListener=new MyListener();
        return new ServletListenerRegistrationBean(myListener);
    }
}
```



**@Configuration**(proxyBeanMethods = false) //保证serlvet、监听器、过滤器都是单实例的

## 4.9 日志



### 4.9.1 日志介绍

市面上的日志框架

**`JUL`、`JCL`、`Jboss-logging`、`logback`、`log4j`、`log4j2`、`slf4j`....**

| 日志门面 （日志的抽象层）                                    | 日志门面 （日志的抽象层）                         |
| ------------------------------------------------------------ | ------------------------------------------------- |
| JCL（Jakarta Commons Logging **SLF4j（Simple Logging Facade for Java）** jboss-loggi | JUL（java.util.logging） Log4j Log4j2 **Logback** |

左边选一个门面（抽象层）、右边来选一个实现；

例：SLF4j-->Logback

SpringBoot选用 **`SLF4j`**和**`logback`**



### 4..9.2 SLF4j使用



**如何让系统中所有的日志都统一到slf4j**

1. 将系统中其他日志框架先排除出去；
2. 用中间包来替换原有的日志框架（适配器的类名和包名与替换的被日志框架一致）；
3. 我们导入slf4j其他的实现



**日志的级别**

由低到高   trace<debug<info<warn<error

可以调整输出的日志级别；日志就只会在这个级别以后的高级别生效



日志会默认到info级别

![image-20201221220522205](img\3.png)



**日志配置**

```properties
#输出日志的路径
logging.file.path=/springboot/log

#输出日志的文件名
logging.file.name= spring.log

# 也可以指定一个包路径 logging.level.com.xxx=error
logging.level.root=error
        
#  在控制台输出的日志的格式
logging.pattern.console=%d{yyyy-MM-dd} [%thread] %-5level %logger{50} - %msg%n

# 指定文件中日志输出的格式
logging.pattern.file=%d{yyyy-MM-dd} === [%thread] === %-5level === %logger{50} ==== %msg%n
```



logging.file.path=/springboot/log     对应的为磁盘位置（D:/springboot/log/）



### 4.9.3 指定配置

配置文件详细

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">

    <!--定义日志文件的存储地址 勿在 LogBack 的配置中使用相对路径-->
    <property name="LOG_HOME" value="/home" />

    <!--控制台日志， 控制台输出 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度,%msg：日志消息，%n是换行符-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS}--- [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>

    <!--文件日志， 按照每天生成日志文件 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <FileNamePattern>${LOG_HOME}/TestWeb.log.%d{yyyy-MM-dd}.log</FileNamePattern>
            <!--日志文件保留天数-->
            <MaxHistory>30</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
        <!--日志文件最大的大小-->
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <MaxFileSize>10MB</MaxFileSize>
        </triggeringPolicy>
    </appender>

    <!-- show parameters for hibernate sql 专为 Hibernate 定制 -->
    <logger name="org.hibernate.type.descriptor.sql.BasicBinder" level="TRACE" />
    <logger name="org.hibernate.type.descriptor.sql.BasicExtractor" level="DEBUG" />
    <logger name="org.hibernate.SQL" level="DEBUG" />
    <logger name="org.hibernate.engine.QueryParameters" level="DEBUG" />
    <logger name="org.hibernate.engine.query.HQLQueryPlan" level="DEBUG" />

    <!--myibatis log configure-->
    <logger name="com.apache.ibatis" level="TRACE"/>
    <logger name="java.sql.Connection" level="DEBUG"/>
    <logger name="java.sql.Statement" level="DEBUG"/>
    <logger name="java.sql.PreparedStatement" level="DEBUG"/>

    <!-- 日志输出级别 -->
    <root level="DEBUG">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```





指定配置

给类路径下放上每个日志框架自己的配置文件即可；SpringBoot就不使用他默认配置的了

| Logging System | Customization                                                |
| -------------- | ------------------------------------------------------------ |
| Logback        | `logback-spring.xml`, `logback-spring.groovy`, `logback.xml` or `logback.groovy` |
| Log4j2         | `log4j2-spring.xml` or `log4j2.xml`                          |
| JDK            | `logging.properties`                                         |



logback.xml：直接就被日志框架识别了；

**logback-spring.xml**：日志框架就不直接加载日志的配置项，由SpringBoot解析日志配置，可以使用SpringBoot的高级Profile功能

```xml
<springProfile name="staging">
    <!-- configuration to be enabled when the "staging" profile is active -->
</springProfile>
```



# 5.数据访问



## 5.1 JDBC 测试

配置数据库驱动(可以指定版本)

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```



配置数据库参数  （yml）

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/web?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC
    username: root
    password: 123456
```



测试代码

- 通过bean里的 **JdbcTemplate**   自动装配   然后调用方法进行操作

```java
@SpringBootTest
class BootAdminApplicationTests {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
        Long aLong = jdbcTemplate.queryForObject("select count(*) from web.students", Long.class);
        System.out.println(aLong);
    }

}
```



## 5.2 自定义 Druid数据源

1.自定义配置

```java
@Configuration
public class MyDataSourceConfig {

    @ConfigurationProperties("spring.datasource")  //通过与配置文件中的属性绑定  并且注入到类中
    @Bean
    public DataSource dataSource() throws SQLException {
        DruidDataSource druidDataSource=new DruidDataSource();
        druidDataSource.setFilters("stat");                      //开启Druid 检测功能

        return druidDataSource;
    }
}
```



使用 **@ConfigurationProperties**  与配置文件中的参数进行绑定  再装配到SpringBoot的容器中



当访问数据库时

![image-20210109152605181](img\9.png)

![image-20210109152644783](img\10.png)





## 5.3 引入官方Druid-starter

引入Druid

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.17</version>
</dependency>
```



配置文件中设置

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/web?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC
    username: root
    password: 123456

    druid:
      aop-patterns: com.jie.boot.*  #监控SpringBean
      filters: stat,wall     # 底层开启功能，stat（sql监控），wall（防火墙）

      stat-view-servlet:   # 配置监控页功能
        enabled: true
        login-username: admin
        login-password: admin
        resetEnable: false

      web-stat-filter:  # 监控web
        enabled: true
        urlPattern: /*
        exclusions: '*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*'


      filter:
        stat:    # 对上面filters里面的stat的详细配置
          slow-sql-millis: 1000
          logSlowSql: true
          enabled: true
        wall:
          enabled: true
          config:
            drop-table-allow: false
```





## 5.4 整合Mybatis

导入依赖

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.4</version>
</dependency>
```



### 5.4.1 导入配置文件



方法1： 导入**mybatis-config.xml**   配置文件

```yaml
#配置mybatis
mybatis:
  config-location: classpath:mybatis/mybatis-config.xml
  mapper-locations: classpath:mybatis/mapper/*.xml
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>

</configuration>
```





方法2：不需要配置文件，直接在**configuration**中设置即可

```yaml
#配置mybatis
mybatis:
  #config-location: classpath:mybatis/mybatis-config.xml
  mapper-locations: classpath:mybatis/mapper/*.xml
  configuration:
  	***
```



若**configuration、mybatis-config.xml** 同时设置 则会报错



**配置Mapper-service -controller**

```java
@Mapper   /*接口需要使用Mapper注解*/
public interface StudentMapper {
    Student getStudentByAge(int age);
}
```

```java
@Service
public class StudentService {
    @Autowired
    private StudentMapper studentMapper;

    public Student getStudentByAge(int age){
        return studentMapper.getStudentByAge(age);
    }

}
```

```java
@Controller
public class TableController {
    @Autowired
    private StudentService studentService;
    
    @ResponseBody
    @GetMapping("/age/{age}")
    public String age(@PathVariable("age") int age){
        System.out.println(age);
        if (studentService.getStudentByAge(age)!=null){
            return studentService.getStudentByAge(age).toString();
        }
        return null;
    }
}
```



### 5.4.2 配置Mapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.jie.boot.mapper.StudentMapper">

    <select id="getStudentByAge" parameterType="int" resultType="com.jie.boot.pojo.Student">
        select * from web.students where age=#{age}  order by age desc limit 0,1
    </select>

</mapper>
```



也可以使用**注解**的方式实现接口

```java
@Mapper
public interface StudentMapper {
    Student getStudentByAge(int age);

    @Select(" select * from web.students")
    List<Student> getStudentList();
}
```



## 5.5 整合mybatis-plus

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.1</version>
</dependency>
```

**准备Bean**

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User {
    private int id;
    private int password;
    private String name;
    private String sex;

    @TableField(exist = false)
    private boolean IsBoy;
}
```



**@TableName("user")**：表示mybatis-plus 与数据库对应的数据名 (若不添加此注解 则会采用Bean类的类名)

**@TableField(exist = false)**: mybatis-plus中默认 类中的属性名和数据库的列名必须是一一对应的

如果有其他的类属性，则需要加上这个注解





**Mapper**

```java
public interface UserMapper extends BaseMapper<User> {
}
```

**Service**

```java
/*需要继承IService*/
public interface UserService extends IService<User> {
}
```

```java
/*实现类需要继承ServiceImpl<UserMapper, User>*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
```

**Controller**

```java
@GetMapping("/dynamic_table")
public String dynamic_table(Model model){
    //获得全部参数
    List<User> list = userService.list();
    model.addAttribute("users",list);
    return "table/dynamic_table";
}

@GetMapping("/delete/{id}")
public String delete(@PathVariable("id") int id){
    userService.removeById(id);
    return "redirect:/dynamic_table";
}

```

前端

```html
<tr class="gradeX" th:each="user,stat:${users}">
    <td th:text="${stat.count}">#</td>
    <td th:text="${user.id}">Trident</td>
    <td th:text="${user.password}">Internet Explorer 4.0</td>
    <td th:text="${user.name}">Win 95+</td>
    <td th:text="${user.sex}">4</td>
    <td class="center hidden-phone"><a th:href="@{/delete/{id}(id=${user.id})}" type="button">X</a></td>
</tr>
```



Mybatis-plus 仍需学习





# 6 单元测试

## 6.1、JUnit5常用注解



- **@Test :**表示方法是测试方法。但是与JUnit4的@Test不同，他的职责非常单一不能声明任何属性，拓展的测试将会由Jupiter提供额外测试
- **@ParameterizedTest :**表示方法是参数化测试，下方会有详细介绍
- **@RepeatedTest :**表示方法可重复执行， 设置重复次数
- **@DisplayName :**为测试类或者方法设置名称
- **@BeforeEach :**表示在每个单元测试之前执行   
  - （如果调用多个测试测试方法  所有测试方法都会执行一次 each方法）
- **@AfterEach :**表示在每个单元测试之后执行
- **@BeforeAll :**表示在所有单元测试之前执行
  - 该注解 在测试方法中只会调用一次  （需要设置为静态方法）
- **@AfterAll :**表示在所有单元测试之后执行
- **@Tag :**表示单元测试类别，类似于JUnit4中的@Categories
- **@Disabled :**表示测试类或测试方法不执行，类似于JUnit4中的@Ignore
- **@Timeout :**表示测试方法运行如果超过了指定时间将会返回错误
- **@ExtendWith :**为测试类或测试方法提供扩展类引用

```java
import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class BootAdminApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {
        System.out.println(userMapper.selectById(1L));
    }

    @Test
    @DisplayName("ww")
    public void redis(){
        ValueOperations operations = redisTemplate.opsForValue();
        operations.set("name2","zc");
        System.out.println(operations.get("name2"));
    }

    @RepeatedTest(5)  //方法重复执行5次
    public void s(){
        System.out.println("!1");
    }

    @Test
    @DisplayName("ee")
//    @Timeout(value = 5,unit = TimeUnit.MILLISECONDS)   //设置整个测试方法如果超过多少时间 报异常
    public void ee(){
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ee");
    }

    @BeforeEach
    public void start(){
        System.out.println("start");
    }

    @AfterEach
    public void end(){
        System.out.println("end");
    }

    @Test
    @Disabled  //不执行
    public void dis(){
        System.out.println(1);
    }

}

```





## 6.2、断言（assertions）

断言（assertions）是测试方法中的核心部分，用来对测试需要满足的条件进行验证。**这些断言方法都是 org.junit.jupiter.api.Assertions 的静态方法**。JUnit 5 内置的断言可以分成如下几个类别：

**检查业务逻辑返回的数据是否合理。**

**所有的测试运行结束以后，会有一个详细的测试报告；**

1、简单断言

用来对单个值进行简单的验证。如：

| 方法            | 说明                                 |
| --------------- | ------------------------------------ |
| assertEquals    | 判断两个对象或两个原始类型是否相等   |
| assertNotEquals | 判断两个对象或两个原始类型是否不相等 |
| assertSame      | 判断两个对象引用是否指向同一个对象   |
| assertNotSame   | 判断两个对象引用是否指向不同的对象   |
| assertTrue      | 判断给定的布尔值是否为 true          |
| assertFalse     | 判断给定的布尔值是否为 false         |
| assertNull      | 判断给定的对象引用是否为 null        |
| assertNotNull   | 判断给定的对象引用是否不为 null      |

```java
@Test
@DisplayName("simple assertion")
public void simple() {
     assertEquals(3, 1 + 2, "simple math");   //测试是否相等
     assertNotEquals(3, 1 + 1);

     assertNotSame(new Object(), new Object());
     Object obj = new Object();
     assertSame(obj, obj);

     assertFalse(1 > 2);
     assertTrue(1 < 2);

     assertNull(null);
     assertNotNull(new Object());
}
```

![image-20210127201037712](img\11.png)



2、数组断言

通过 **assertArrayEquals** 方法来判断两个对象或原始类型的数组是否相等

```java
@Test
@DisplayName("array assertion")
public void array() {
 assertArrayEquals(new int[]{1, 2}, new int[] {1, 2});
}
```



3、组合断言

assertAll 方法接受多个 org.junit.jupiter.api.Executable 函数式接口的实例作为要验证的断言，可以通过 lambda 表达式很容易的提供这些断言



```java
@Test
@DisplayName("assert all")
public void all() {
 assertAll("Math",
    () -> assertEquals(2, 1 + 1),
    () -> assertTrue(1 > 0)
 );
}
```



4、异常断言

在JUnit4时期，想要测试方法的异常情况时，需要用**@Rule**注解的ExpectedException变量还是比较麻烦的。而JUnit5提供了一种新的断言方式**Assertions.assertThrows()** ,配合函数式编程就可以进行使用。

```java
@Test
@DisplayName("异常测试")
public void exceptionTest() {
    ArithmeticException exception = Assertions.assertThrows(
           //扔出断言异常
            ArithmeticException.class, () -> System.out.println(1 % 0));

}
```



5、超时断言

Junit5还提供了**Assertions.assertTimeout()** 为测试方法设置了超时时间

```java
@Test
@DisplayName("超时测试")
public void timeoutTest() {
    //如果测试方法时间超过1s将会异常
    Assertions.assertTimeout(Duration.ofMillis(1000), () -> Thread.sleep(500));
}
```



6、快速失败

通过 fail 方法直接使得测试失败

```java
@Test
@DisplayName("fail")
public void shouldFail() {
 fail("This should fail");
}
```



7、前置条件（assumptions）

JUnit 5 中的前置条件（**assumptions【假设】**）类似于断言，不同之处在于**不满足的断言会使得测试方法失败**，而不满足的**前置条件只会使得测试方法的执行终止**。前置条件可以看成是测试方法执行的前提，当该前提不满足时，就没有继续执行的必要。

**assumeTrue**
**assumeFalse**

```java
@DisplayName("前置条件")
public class AssumptionsTest {
 private final String environment = "DEV";
 
 @Test
 @DisplayName("simple")
 public void simpleAssume() {
    assumeTrue(Objects.equals(this.environment, "DEV"));
    assumeFalse(() -> Objects.equals(this.environment, "PROD"));
 }
 
 @Test
 @DisplayName("assume then do")
 public void assumeThenDo() {
    assumingThat(
       Objects.equals(this.environment, "DEV"),
       () -> System.out.println("In DEV")
    );
 }
}
```

assumeTrue 和 assumFalse 确保给定的条件为 true 或 false，不满足条件会使得测试执行终止。assumingThat 的参数是表示条件的布尔值和对应的 Executable 接口的实现对象。只有条件满足时，Executable 对象才会被执行；当条件不满足时，测试执行并不会终止。



## 6.3、嵌套测试

JUnit 5 可以通过 Java 中的内部类和**@Nested** 注解实现嵌套测试，从而可以更好的把相关的测试方法组织在一起。

在内部类中可以使用@BeforeEach 和@AfterEach 注解，而且嵌套的层次没有限制。

```java
@DisplayName("A stack")
class TestingAStackDemo {

    Stack<Object> stack;

    @Test
    @DisplayName("is instantiated with new Stack()")
    void isInstantiatedWithNew() {
        new Stack<>();
    }

    @Nested
    @DisplayName("when new")
    class WhenNew {

        @BeforeEach
        void createNewStack() {
            stack = new Stack<>();
        }

        @Test
        @DisplayName("is empty")
        void isEmpty() {
            assertTrue(stack.isEmpty());
        }

        @Test
        @DisplayName("throws EmptyStackException when popped")
        void throwsExceptionWhenPopped() {
            assertThrows(EmptyStackException.class, stack::pop);
        }

        @Test
        @DisplayName("throws EmptyStackException when peeked")
        void throwsExceptionWhenPeeked() {
            assertThrows(EmptyStackException.class, stack::peek);
        }

        @Nested
        @DisplayName("after pushing an element")
        class AfterPushing {

            String anElement = "an element";

            @BeforeEach
            void pushAnElement() {
                stack.push(anElement);
            }

            @Test
            @DisplayName("it is no longer empty")
            void isNotEmpty() {
                assertFalse(stack.isEmpty());
            }

            @Test
            @DisplayName("returns the element when popped and is empty")
            void returnElementWhenPopped() {
                assertEquals(anElement, stack.pop());
                assertTrue(stack.isEmpty());
            }

            @Test
            @DisplayName("returns the element when peeked but remains not empty")
            void returnElementWhenPeeked() {
                assertEquals(anElement, stack.peek());
                assertFalse(stack.isEmpty());
            }
        }
    }
}
```



## 6.3、参数化测试

参数化测试是JUnit5很重要的一个新特性，它使得用不同的参数多次运行测试成为了可能，也为我们的单元测试带来许多便利。



利用**@ValueSource**等注解，指定入参，我们将可以使用不同的参数进行多次单元测试，而不需要每新增一个参数就新增一个单元测试，省去了很多冗余代码。


**@ValueSource**: 为参数化测试指定入参来源，支持八大基础类以及String类型,Class类型

**@NullSource**: 表示为参数化测试提供一个null的入参

**@EnumSource**: 表示为参数化测试提供一个枚举入参

**@CsvFileSource**：表示读取指定CSV文件内容作为参数化测试入参

**@MethodSource**：表示读取指定方法的返回值作为参数化测试入参(注意方法返回需要是一个流)



> 当然如果参数化测试仅仅只能做到指定普通的入参还达不到让我觉得惊艳的地步。让我真正感到他的强大之处的地方在于他可以支持外部的各类入参。如:CSV,YML,JSON 文件甚至方法的返回值也可以作为入参。只需要去实现**ArgumentsProvider**接口，任何外部文件都可以作为它的入参。

```java
@ParameterizedTest
@ValueSource(strings = {"one", "two", "three"})
@DisplayName("参数化测试1")
public void parameterizedTest1(String string) {
    System.out.println(string);
    Assertions.assertTrue(StringUtils.isNotBlank(string));
}


@ParameterizedTest
@MethodSource("method")    //指定方法名
@DisplayName("方法来源参数")
public void testWithExplicitLocalMethodSource(String name) {
    System.out.println(name);
    Assertions.assertNotNull(name);
}

static Stream<String> method() {
    return Stream.of("apple", "banana");
}
```



## 6.4、迁移指南

在进行迁移的时候需要注意如下的变化：

- 注解在 org.junit.jupiter.api 包中，断言在 org.junit.jupiter.api.Assertions 类中，前置条件在 org.junit.jupiter.api.Assumptions 类中。
- 把@Before 和@After 替换成@BeforeEach 和@AfterEach。
- 把@BeforeClass 和@AfterClass 替换成@BeforeAll 和@AfterAll。
- 把@Ignore 替换成@Disabled。
- 把@Category 替换成@Tag。
- 把@RunWith、@Rule 和@ClassRule 替换成@ExtendWith。



# 7 指标监控



## 7.1、简介

未来每一个微服务在云上部署以后，我们都需要对其进行监控、追踪、审计、控制等。SpringBoot就抽取了Actuator场景，使得我们每个微服务快速引用即可获得生产级别的应用监控、审计等功能。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606886483335-697ee1c1-2f69-43ab-bddc-3a038382319c.png)

2、1.x与2.x的不同

![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606884394162-ac7f2d8e-7abb-44df-9998-fb0f2705f238.png?x-oss-process=image%2Fwatermark%2Ctype_d3F5LW1pY3JvaGVp%2Csize_14%2Ctext_YXRndWlndS5jb20g5bCa56GF6LC3%2Ccolor_FFFFFF%2Cshadow_50%2Ct_80%2Cg_se%2Cx_10%2Cy_10)





3、如何使用

- 引入场景
- 访问 http://localhost:8080/actuator/**
- 暴露所有监控信息为HTTP

```yaml
management:
  endpoints:
    enabled-by-default: true #暴露所有端点信息
    web:
      exposure:
        include: '*'  #以we.b方式暴露
```

- 测试

http://localhost:8080/actuator/beans    包含的bean

http://localhost:8080/actuator/configprops

http://localhost:8080/actuator/metrics    指标监控

http://localhost:8080/actuator/metrics/jvm.gc.pause

[http://localhost:8080/actuator/](http://localhost:8080/actuator/metrics)endpointName/detailPath
。。。。。。





4、可视化

https://github.com/codecentric/spring-boot-admin





## 7.2、常用端点

**1、最常使用的端点**



| ID                 | 描述                                                         |
| ------------------ | ------------------------------------------------------------ |
| `auditevents`      | 暴露当前应用程序的审核事件信息。需要一个`AuditEventRepository组件`。 |
| `beans`            | 显示应用程序中所有Spring Bean的完整列表。                    |
| `caches`           | 暴露可用的缓存。                                             |
| `conditions`       | 显示自动配置的所有条件信息，包括匹配或不匹配的原因。         |
| `configprops`      | 显示所有`@ConfigurationProperties`。                         |
| `env`              | 暴露Spring的属性`ConfigurableEnvironment`                    |
| `flyway`           | 显示已应用的所有Flyway数据库迁移。 需要一个或多个`Flyway`组件。 |
| `health`           | 显示应用程序运行状况信息。                                   |
| `httptrace`        | 显示HTTP跟踪信息（默认情况下，最近100个HTTP请求-响应）。需要一个`HttpTraceRepository`组件。 |
| `info`             | 显示应用程序信息。                                           |
| `integrationgraph` | 显示Spring `integrationgraph` 。需要依赖`spring-integration-core`。 |
| `loggers`          | 显示和修改应用程序中日志的配置。                             |
| `liquibase`        | 显示已应用的所有Liquibase数据库迁移。需要一个或多个`Liquibase`组件。 |
| `metrics`          | 显示当前应用程序的“指标”信息。                               |
| `mappings`         | 显示所有`@RequestMapping`路径列表。                          |
| `scheduledtasks`   | 显示应用程序中的计划任务。                                   |
| `sessions`         | 允许从Spring Session支持的会话存储中检索和删除用户会话。需要使用Spring Session的基于Servlet的Web应用程序。 |
| `shutdown`         | 使应用程序正常关闭。默认禁用。                               |
| `startup`          | 显示由`ApplicationStartup`收集的启动步骤数据。需要使用`SpringApplication`进行配置`BufferingApplicationStartup`。 |
| `threaddump`       | 执行线程转储。                                               |



如果您的应用程序是Web应用程序（Spring MVC，Spring WebFlux或Jersey），则可以使用以下附加端点：

| ID           | 描述                                                         |
| ------------ | ------------------------------------------------------------ |
| `heapdump`   | 返回`hprof`堆转储文件。                                      |
| `jolokia`    | 通过HTTP暴露JMX bean（需要引入Jolokia，不适用于WebFlux）。需要引入依赖`jolokia-core`。 |
| `logfile`    | 返回日志文件的内容（如果已设置`logging.file.name`或`logging.file.path`属性）。支持使用HTTP`Range`标头来检索部分日志文件的内容。 |
| `prometheus` | 以Prometheus服务器可以抓取的格式公开指标。需要依赖`micrometer-registry-prometheus`。 |





最常用的Endpoint

- **Health：监控状况**
- **Metrics：运行时指标**
- **Loggers：日志记录**





**2、Health Endpoint**

健康检查端点，我们一般用于在云平台，平台会定时的检查应用的健康状况，我们就需要Health Endpoint可以为平台返回当前应用的一系列组件健康状况的集合。

重要的几点：

- health endpoint返回的结果，应该是一系列健康检查后的一个汇总报告
- 很多的健康检查默认已经自动配置好了，比如：数据库、redis等
- 可以很容易的添加自定义的健康检查机制

![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606908975702-4f9a3208-15ca-4a78-9f76-939ef986db7e.png)

```yaml
management:
  endpoints:
    enabled-by-default: true
    web:
      exposure:
        include: '*'
  endpoint:
    health:
      show-details: always
```



**3、Metrics Endpoint**

提供详细的、层级的、空间指标信息，这些信息可以被pull（主动推送）或者push（被动获取）方式得到；

- 通过Metrics对接多种监控系统
- 简化核心Metrics开发
- 添加自定义Metrics或者扩展已有Metrics



![image.png](https://cdn.nlark.com/yuque/0/2020/png/1354552/1606909073222-c6e77ca3-4b1c-4f38-a1c6-8614dec4f7bc.png)







## 7.3、管理Endpoints

**1、开启与禁用Endpoints**

- 默认所有的Endpoint除过shutdown都是开启的。

- 或者禁用所有的Endpoint然后手动开启**指定的Endpoint**

```yaml
management:
  endpoints:
    enabled-by-default: false
    web:
      exposure:
        include: '*'
  endpoint:
    health:
      show-details: always
      enabled: true
    info:
      enabled: true
```

![image-20210128215320085](img\12.png)



2、暴露**Endpoints**

支持的暴露方式

- HTTP：默认只暴露**health**和**info** Endpoint
- **JMX**：默认暴露所有Endpoint
- 除过health和info，剩下的Endpoint都应该进行保护访问。如果引入SpringSecurity，则会默认配置安全访问规则

| ID                 | JMX  | Web  |
| ------------------ | ---- | ---- |
| `auditevents`      | Yes  | No   |
| `beans`            | Yes  | No   |
| `caches`           | Yes  | No   |
| `conditions`       | Yes  | No   |
| `configprops`      | Yes  | No   |
| `env`              | Yes  | No   |
| `flyway`           | Yes  | No   |
| `health`           | Yes  | Yes  |
| `heapdump`         | N/A  | No   |
| `httptrace`        | Yes  | No   |
| `info`             | Yes  | Yes  |
| `integrationgraph` | Yes  | No   |
| `jolokia`          | N/A  | No   |
| `logfile`          | N/A  | No   |
| `loggers`          | Yes  | No   |
| `liquibase`        | Yes  | No   |
| `metrics`          | Yes  | No   |
| `mappings`         | Yes  | No   |
| `prometheus`       | N/A  | No   |
| `scheduledtasks`   | Yes  | No   |
| `sessions`         | Yes  | No   |
| `shutdown`         | Yes  | No   |
| `startup`          | Yes  | No   |
| `threaddump`       | Yes  | No   |





## 7.4、定制 Endpoint

### **1、定制 Health 信息**

```java
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MyHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        int errorCode = check(); // perform some specific health check
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }

}

构建Health
Health build = Health.down()
                .withDetail("msg", "error service")
                .withDetail("code", "500")
                .withException(new RuntimeException())
                .build();
management:
    health:
      enabled: true
      show-details: always #总是显示详细信息。可显示每个模块的状态信息
```



```java
@Component
public class MyComHealthIndicator extends AbstractHealthIndicator {

    /**
     * 真实的检查方法
     * @param builder
     * @throws Exception
     */
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        //mongodb。  获取连接进行测试
        Map<String,Object> map = new HashMap<>();
        // 检查完成
        if(1 == 2){
//            builder.up(); //健康
            builder.status(Status.UP);
            map.put("count",1);
            map.put("ms",100);
        }else {
//            builder.down();
            builder.status(Status.OUT_OF_SERVICE);
            map.put("err","连接超时");
            map.put("ms",3000);
        }


        builder.withDetail("code",100)
                .withDetails(map);

    }
}
```



### **2、定制info信息**

常用两种方式

**1、编写配置文件**

```yaml
info:
  appName: boot-admin
  version: 2.0.1
  mavenProjectName: @project.artifactId@  #使用@@可以获取maven的pom文件值
  mavenProjectVersion: @project.version@
```

**2、编写InfoContributor**

```java
import java.util.Collections;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class ExampleInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("example",
                Collections.singletonMap("key", "value"));
    }

}
```

http://localhost:8080/actuator/info 会输出以上方式返回的所有info信息



## 7.5、定制Metrics信息

**1、SpringBoot支持自动适配的Metrics**

- JVM metrics, report utilization of:

  - Various memory and buffer pools

  - Statistics related to garbage collection
  - Threads utilization
  - Number of classes loaded/unloaded

- CPU metrics
- File descriptor metrics
- Kafka consumer and producer metrics
- Log4j2 metrics: record the number of events logged to Log4j2 at each level
- Logback metrics: record the number of events logged to Logback at each level
- Uptime metrics: report a gauge for uptime and a fixed gauge representing the application’s absolute start time
- Tomcat metrics (`server.tomcat.mbeanregistry.enabled` must be set to `true` for all Tomcat metrics to be registered)
- [Spring Integration](https://docs.spring.io/spring-integration/docs/5.4.1/reference/html/system-management.html#micrometer-integration) metrics



## 7.6、增加定制Metrics

```java
class MyService{
    Counter counter;
    public MyService(MeterRegistry meterRegistry){
         counter = meterRegistry.counter("myservice.method.running.counter");
    }

    public void hello() {
        counter.increment();
    }
}


//也可以使用下面的方式
@Bean
MeterBinder queueSize(Queue queue) {
    return (registry) -> Gauge.builder("queueSize", queue::size).register(registry);
}
```



## 7.7、定制Endpoint

```java
@Component
@Endpoint(id = "container")
public class DockerEndpoint {


    @ReadOperation
    public Map getDockerInfo(){
        return Collections.singletonMap("info","docker started...");
    }

    @WriteOperation
    private void restartDocker(){
        System.out.println("docker restarted....");
    }

}
```

场景：开发**ReadinessEndpoint**来管理程序是否就绪，或者**Liveness** **Endpoint**来管理程序是否存活；





## 7.8、可视化监控

> **服务端**

导入

```xml
<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-server</artifactId>
    <version>2.3.1</version>
</dependency>
```

加上**@EnableAdminServer**  就能启动可视化server

```java
@SpringBootApplication
@EnableAdminServer
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```



> **Client注册**

```properties
spring.boot.admin.client.url=http://localhost:8888
spring.boot.admin.client.instance.prefer-ip=true
spring.application.name=web
```

可以修改**server**端的端口 防止冲突

```properties
server.port=8888
```

即可实现可视化监控

![image-20210129171628781](img\13.png)



# 8 安全

## 8.1 SpringSecurity

### 简介

Spring Security 是针对Spring项目的安全框架，也是Spring Boot底层安全模块默认的技术选型，他可以实现强大的Web安全控制，对于安全控制，我们仅需要引入 spring-boot-starter-security 模块，进行少量的配置，即可实现强大的安全管理！

记住几个类：

- `WebSecurityConfigurerAdapter`：自定义Security策略
- `AuthenticationManagerBuilder`：自定义认证策略
- `@EnableWebSecurity`：开启WebSecurity模式

Spring Security的两个主要目标是 “认证” 和 “授权”（访问控制）。

**“认证”（Authentication）**

身份验证是关于验证您的凭据，如用户名/用户ID和密码，以验证您的身份。

身份验证通常通过用户名和密码完成，有时与身份验证因素结合使用。

**“授权” （Authorization）**

授权发生在系统成功验证您的身份后，最终会授予您访问资源（如信息，文件，数据库，资金，位置，几乎任何内容）的完全权限。

这个概念是通用的，而不是只在Spring Security 中存在。

### 认证和授权

目前，我们的测试环境，是谁都可以访问的，我们使用 Spring Security 增加上认证和授权的功能

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```



代码如下：

```java
//开去security模式
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {    //授权

        //设置放行路径  和 需要权限的路径
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");
        //返回登陆界面
        http.formLogin();
        http.logout().logoutSuccessUrl("/");   //注销后跳转到某个地址
         http.rememberMe();   //添加记录用户据 按钮
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {   //认证
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())   //密码需要加密
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1","vip2","vip3")
                .and()
                .withUser("zcj").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1")
                .and()
                .withUser("zcj2").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1","vip2");
    }
}
```

- 设置哪些地方需要登陆权限
- 同时设置账户密码所拥有的权限
  - root拥有vip1,vip2,vip3的全部权限
  - zc1仅拥有vip1
  - zcj2拥有vip2
- 同时密码需要加密

![image-20210131145334206](img\14.png)

**记住我**

```java
http.rememberMe();
```



### 注销

注销路径为**/logout**

![image-20210131135736200](img\15.png)

点击即可退出当前用户    

注销后跳转地址设置

```java
http.logout().logoutSuccessUrl("/");   //注销后跳转到某个地址
```

完整代码

```java
//开去security模式
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {    //授权

        //设置放行路径  和 需要权限的路径
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");
        //返回登陆界面
        http.formLogin();
        http.logout().logoutSuccessUrl("/");   //注销后跳转到某个地址
        http.rememberMe();   //添加记录用户据 按钮
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {   //认证
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())   //密码需要加密
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1","vip2","vip3")
                .and()
                .withUser("zcj").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1")
                .and()
                .withUser("zcj2").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1","vip2");
    }
}
```



### 自定义登陆界面

```java
http.formLogin().loginPage("/toLogin");   //自定义登陆界面
```

- 登陆界面会走  **/toLogin** 请求
- 同时前端提交的请求也必须写   **/toLogin** 
- 而默认的前端账户名为   **username**
- 默认的前端密码名为  **password**



**自定义属性**

```java
http.formLogin()
        .loginPage("/Login")             //自定义用户登陆请求
        .loginProcessingUrl("/toLogin")  //登陆成功后的跳转请求   （from）
        .usernameParameter("user")       //修改前端姓名
        .passwordParameter("pwd");       //修改密码
```

前端**添加记住我按钮**

```html
<input type="radio" name="remember">记住我
```

```java
http.rememberMe().rememberMeParameter("remember");      //写入前端的name
```

完整代码

```java
//开去security模式
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {    //授权

        //设置放行路径  和 需要权限的路径
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");
        //返回登陆界面
        http.formLogin();
        http.logout().logoutSuccessUrl("/");   //注销后跳转到某个地址
        http.rememberMe().rememberMeParameter("remember");
        http.csrf().disable(); //禁用跨站csrf攻击防御
        http.formLogin()
                .loginPage("/Login")             //自定义用户登陆请求
                .loginProcessingUrl("/toLogin")  //登陆成功后的跳转请求   （from）
                .usernameParameter("user")       //修改前端姓名
                .passwordParameter("pwd");       //修改密码
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {   //认证
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())   //密码需要加密
                .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1","vip2","vip3")
                .and()
                .withUser("zcj").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1")
                .and()
                .withUser("zcj2").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1","vip2");
    }
}
```



### 前端内容显示选择

导入themeleaf和security整合包

```xml
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity4</artifactId>
    <version>3.0.4.RELEASE</version>
</dependency>
```

导入头部标签

```html
<html lang="en" xmlns:th="http://www.thymeleaf.org"  xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
```



**判断是否有用户登陆**

> **sec:authorize="!isAuthenticated()**

用户未登陆显示以下内容

```html
<div sec:authorize="!isAuthenticated()">
    <a class="item" th:href="@{/toLogin}">
        <i class="address card icon"></i> 登录
    </a>
</div>
```

用户登陆显示以下内容

> **sec:authorize="isAuthenticated()**

```html
<div sec:authorize="isAuthenticated()">
    <a class="item">
        用户名：<span sec:authentication="name"></span>
    </a>
    <a class="item" th:href="@{/logout}">
        <i class="arrow circle right icon"></i> 注销
    </a>
</div>
```

**获取用户姓名**

```html
<a class="item">
    用户名：<span sec:authentication="name"></span>
</a>
```

**判断用户有无此权限**

sec:authorize="hasRole('vip1')" ：**该用户拥有vip1权限就显示一下内容**

```html
<div class="column" sec:authorize="hasRole('vip1')">
    <div class="ui raised segment">
        <div class="ui">
            <div class="content">
                <h5 class="content">Level 1</h5>
                <hr>
                <div><a th:href="@{/level1/1}"><i class="bullhorn icon"></i> Level-1-1</a></div>
                <div><a th:href="@{/level1/2}"><i class="bullhorn icon"></i> Level-1-2</a></div>
                <div><a th:href="@{/level1/3}"><i class="bullhorn icon"></i> Level-1-3</a></div>
            </div>
        </div>
    </div>
</div>
```





## 8.2 shiro

### 8.0 整合思路

![image-20200525185630463](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200525185630463.png)

### 8.1 创建springboot项目

![image-20200523100842032](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200523100842032.png)

### 8.2 引入shiro依赖

```xml
<dependency>
  <groupId>org.apache.shiro</groupId>
  <artifactId>shiro-spring-boot-starter</artifactId>
  <version>1.5.3</version>
</dependency>
```



### 8.3 配置shiro环境

**创建配置类**

![image-20210217112836322](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\1.png)

**1.配置shiroFilterFactoryBean**

```java
@Bean
public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager){
  //创建shiro的filter
  ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
  //注入安全管理器
  shiroFilterFactoryBean.setSecurityManager(securityManager);
 	
  return shiroFilterFactoryBean;
}
```

**2.配置DefaultWebSecurityManager**

```java
@Bean
public DefaultWebSecurityManager getSecurityManager(Realm realm){
  DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
  defaultWebSecurityManager.setRealm(realm);
  return defaultWebSecurityManager;
}
```

**3.创建自定义realm**

```java
public class CustomerRealm extends AuthorizingRealm {
    //处理授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //处理认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }
}
```

**4.配置自定义realm**

```java
//创建自定义realm
@Bean
public Realm getRealm(){
  return new CustomerRealm();
}
```

**5.编写控制器跳转至index.html**

```java
@Controller
public class IndexController {
    @RequestMapping("index")
    public String index(){
        System.out.println("跳转至主页");
        return "index";
    }
}
```

![image-20200523101733157](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200523101733157.png)

![image-20200523101857528](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200523101857528.png)

**6.启动springboot应用访问index**

![image-20200523101955121](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200523101955121.png)

- 注意:
  - **默认在配置好shiro环境后默认环境中没有对项目中任何资源进行权限控制,所有现在项目中所有资源都可以通过路径访问**

**7.加入权限控制**

- 修改ShiroFilterFactoryBean配置

  ```java
  //注入安全管理器
  shiroFilterFactoryBean.setSecurityManager(securityManager);
  Map<String,String> map =  new LinkedHashMap<>();
  map.put("/**","authc");
  //配置认证和授权规则
  shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
  ```

  ![image-20200523102303320](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200523102303320.png)

  - **/\**** 代表拦截项目中一切资源  **authc** 代表shiro中的一个filter的别名,详细内容看文档的shirofilter列表

**8.重启项目访问查看**

![image-20200523102831750](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200523102831750.png)





### 8.4 常见过滤器

- 注意: **shiro提供和多个默认的过滤器，我们可以用这些过滤器来配置控制指定url的权限：**

| 配置缩写          | 对应的过滤器                   | 功能                                                         |
| ----------------- | ------------------------------ | ------------------------------------------------------------ |
| anon              | AnonymousFilter                | 指定url可以匿名访问  （不需要认证）                          |
| authc             | FormAuthenticationFilter       | 指定url需要form表单登录，默认会从请求中获取`username`、`password`,`rememberMe`等参数并尝试登录，如果登录不了就会跳转到loginUrl配置的路径。我们也可以用这个过滤器做默认的登录逻辑，但是一般都是我们自己在控制器写登录逻辑的，自己写的话出错返回的信息都可以定制嘛。 |
| authcBasic        | BasicHttpAuthenticationFilter  | 指定url需要basic登录                                         |
| logout            | LogoutFilter                   | 登出过滤器，配置指定url就可以实现退出功能，非常方便          |
| noSessionCreation | NoSessionCreationFilter        | 禁止创建会话                                                 |
| perms             | PermissionsAuthorizationFilter | 需要指定权限才能访问                                         |
| port              | PortFilter                     | 需要指定端口才能访问                                         |
| rest              | HttpMethodPermissionFilter     | 将http请求方法转化成相应的动词来构造一个权限字符串，这个感觉意义不大，有兴趣自己看源码的注释 |
| roles             | RolesAuthorizationFilter       | 需要指定角色才能访问                                         |
| ssl               | SslFilter                      | 需要https请求才能访问                                        |
| user              | UserFilter                     | 需要已登录或“记住我”的用户才能访问                           |

### 8.5 认证实现

**开发认证界面**

![image-20200526082345776](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200526082345776.png)

```html
<form action="${pageContext.request.contextPath}/user/login" method="post">
  用户名:<input type="text" name="username" > <br/>
  密码  : <input type="text" name="password"> <br>
  <input type="submit" value="登录">
</form>
```

**controller**

```java
@Controller
public class UrlController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/loginUrl")
    public String loginUrl(String username,String password){

        //通过创建的subject用户与后端进行比对
        Subject subject= SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(username,password));   //登陆
            return "index";
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误");
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("密码错误");
        }
        return "redirect:/login";
    }
}
```

- **在认证过程中使用subject.login进行认证**

**3.开发realm中返回静态数据(未连接数据库)**

```java
@Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("==========================");
        String principal = (String) token.getPrincipal();
        if("xiaochen".equals(principal)){
            return new SimpleAuthenticationInfo(principal,"123",this.getName());
        }
        return null;
    }
}
```

**4.启动项目以realm中定义静态数据进行认证**

![image-20200526082550343](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200526082550343.png)

![image-20200526082639318](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200526082639318.png)

![image-20200526082620621](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200526082620621.png)

- **认证功能没有md5和随机盐的认证就实现啦**





### 8.6 退出认证

**1.开发页面退出连接**

![image-20200526082851800](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200526082851800.png)

**2.controller**

```java
    @GetMapping("/loginOut")
    public String loginOut(){
        Subject subject=SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/login";
    }
```

**3.修改退出连接访问退出路径**

![image-20200526083056062](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200526083056062.png)

**4.退出之后访问受限资源立即返回认证界面**

![image-20200526083148253](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200526083148253.png)





### 8.7 MD5、Salt的认证实现

开发数据库注册

**开发注册界面**

```html
<form th:action="@{/loginUrl}" class="axis" >
    用户名：<input type="text" name="username"><br>
    密码：<input type="password" name="password"> <br>
    <input type="submit" value="登陆">
    <a href="/register">注册</a>
</form>
```

![image-20200526200230982](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200526200230982.png)

**1.创建数据表结构**

![image-20200526200425569](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200526200425569.png)



**3.配置application.properties配置文件**

```properties
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/sql_test?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC
    username: root
    password: 123456

mybatis:
  mapper-locations: classpath:mybatis/mapper/*.xml
  type-aliases-package: com.jie.shiro.pojo

```



**4.创建pojo**

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String username;
    private String password;
    private String salt;
}

```

**5.创建Mapper接口**

```java
@Mapper
public interface UserMapper {
    int addUser(User user);
}
```

**6.开发mapper配置文件**

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.jie.shiro.mapper.UserMapper">
    <insert id="addUser" parameterType="com.jie.shiro.pojo.User">
        insert into sql_test.shirouser(username, password, salt) values(#{username},#{password},#{salt});
    </insert>
</mapper>
```

**7.开发service接口**

```java
public interface UserService {
    int addUser(User user);
}
```

**8.创建salt工具类**

```java
public class SaltUtils {
    /**
     * 生成salt的静态方法
     */
    public static String getSalt(int n){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890!@#$%^&*()".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char aChar = chars[new Random().nextInt(chars.length)];
            sb.append(aChar);
        }
        return sb.toString();
    }
}
```

**9.开发service实现类**

通过service层进行 **Md5+salt+hash** 加密

```java
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public int addUser(User user) {
        //使用加密技术
        String salt = SaltUtils.getSalt(8);
        user.setSalt(salt);
        Md5Hash md5Hash=new Md5Hash(user.getPassword(),user.getSalt(),1024);
        user.setPassword(md5Hash.toHex());
        return userMapper.addUser(user);
    }
}
```

**10.开发Controller**

```java
@GetMapping("/register")
public String register(){
    return "register";
}

@GetMapping("/registerUser")
public String registerUser(User user){
    try {
        userService.addUser(user);
        return "redirect:/login";
    } catch (Exception e) {
        e.printStackTrace();
        return "redirect:/register";
    }
}
```

**11.启动项目进行注册**

![image-20200526200946730](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200526200946730.png)

----





### 8.8  数据库 (注册、登陆)

> 注册

使用工具类为每个用户生成不同的salt

```java
public class SaltUtils {
    //生成salt的静态方法
    public static String getSalt(int n){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890!@#$%^&*()".toCharArray();
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < n; i++) {
            char a = chars[new Random().nextInt(chars.length)];
            sb.append(a);
        }
        return sb.toString();
    }
}
```

```java
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public int addUser(User user) {
        String salt = SaltUtils.getSalt(8);
        user.setSalt(salt);
        Md5Hash md5Hash=new Md5Hash(user.getPassword(),user.getSalt(),1024);
        user.setPassword(md5Hash.toHex());
        return userMapper.addUser(user);
    }
}
```



> 登陆

```java
@Override
protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
    String principal = (String)authenticationToken.getPrincipal();
    User user = userService.findByUserName(principal);
    if (principal.equals("admin")){   //加入随机salt
        return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(), ByteSource.Util.bytes(user.getSalt()),this.getName());
    }
    return null;
}
```

```java
//自定义Realm
@Bean
public Realm getRealm(){
    CustomerRealm customerRealm=new CustomerRealm();

    HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
    hashedCredentialsMatcher.setHashIterations(1024);   //加入编码次数和格式
    hashedCredentialsMatcher.setHashAlgorithmName("md5");

    customerRealm.setCredentialsMatcher(hashedCredentialsMatcher);
    return customerRealm;
}
```







### 8.9 授权实现

导入

```xml
<dependency>
    <groupId>com.github.theborakompanioni</groupId>
    <artifactId>thymeleaf-extras-shiro</artifactId>
    <version>2.0.0</version>
</dependency>
```

还需要加入config

```java
@Bean(name = "shiroDialect")
public ShiroDialect shiroDialect(){
    return new ShiroDialect();
}
```



**对用户进行授权**

```java
@Override
protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

    String principal = (String) principalCollection.getPrimaryPrincipal();

    if ("admin".equals(principal)){
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole("admin");             //赋予admin角色

        simpleAuthorizationInfo.addStringPermission("user:*:*");   //赋予user的所有权限
        return simpleAuthorizationInfo;
    }
    return null;
}
```



**前端授权显示**

```html
<h1>系统主页</h1>
<a href="/loginOut">退出</a>
<u1>
    <li><a th:href="@{/store}" href="">商品管理</a></li>
    <div shiro:hasRole="admin">
        <li><a href="">用户管理</a></li>
            <ul>
                <shiro:hasPermission name="user:add:*">
                    <li ><a href="">添加</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="user:delete:*">
                    <li><a href="">删除</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="user:update:*">
                    <li><a href="">修改</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="user:find:*">
                    <li><a href="">查询</a></li>
                </shiro:hasPermission>
            </ul>
        <li><a href="">订单管理</a></li>
    </div>
    <li><a th:href="@{/url}" href="">物流管路</a></li>
</u1>
```



**方法中判断权限**

```java
@GetMapping("/store")
@ResponseBody
public String store(){
    Subject subject=SecurityUtils.getSubject();  //获取当前角色
    if (subject.hasRole("admin")){
        return "可以访问";
    }else {
        return "无权限";
    }
}
```



**注解授权调用**

- @RequiresRoles               用来基于角色进行授权
- @RequiresPermissions    用来基于权限进行授权

```java
@RequiresRoles(value={"admin","user"})//用来判断角色  同时具有 admin user
@RequiresPermissions("user:update:01") //用来判断权限字符串
@RequestMapping("save")
public String save(){
  System.out.println("进入方法");
  return "redirect:/index.jsp";
}
```

![image-20200527203415114](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200527203415114.png)

----







### 8.10 授权数据持久化

![image-20200527204839080](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200527204839080.png)



----

**4.创建dao方法**

```java
 //根据用户名查询所有角色
User findRolesByUserName(String username);
//根据角色id查询权限集合
List<Perms> findPermsByRoleId(String id);
```

**5.mapper实现**

```xml
<resultMap id="userMap" type="User">
  <id column="uid" property="id"/>
  <result column="username" property="username"/>
  <!--角色信息-->
  <collection property="roles" javaType="list" ofType="Role">
    <id column="id" property="id"/>
    <result column="rname" property="name"/>
  </collection>
</resultMap>

<select id="findRolesByUserName" parameterType="String" resultMap="userMap">
  SELECT u.id uid,u.username,r.id,r.NAME rname
  FROM t_user u
  LEFT JOIN t_user_role ur
  ON u.id=ur.userid
  LEFT JOIN t_role r
  ON ur.roleid=r.id
  WHERE u.username=#{username}
</select>

<select id="findPermsByRoleId" parameterType="String" resultType="Perms">
  SELECT p.id,p.NAME,p.url,r.NAME
  FROM t_role r
  LEFT JOIN t_role_perms rp
  ON r.id=rp.roleid
  LEFT JOIN t_perms p ON rp.permsid=p.id
  WHERE r.id=#{id}
</select>
```

**6.Service接口**

```java
//根据用户名查询所有角色
User findRolesByUserName(String username);
//根据角色id查询权限集合
List<Perms> findPermsByRoleId(String id);
```

**7.Service实现**

```java
@Override
public List<Perms> findPermsByRoleId(String id) {
  return userDAO.findPermsByRoleId(id);
}

@Override
public User findRolesByUserName(String username) {
  return userDAO.findRolesByUserName(username);
}
```

**8.修改自定义realm**

```java
public class CustomerRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取身份信息
        String primaryPrincipal = (String) principals.getPrimaryPrincipal();
        System.out.println("调用授权验证: "+primaryPrincipal);
        //根据主身份信息获取角色 和 权限信息
        UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        User user = userService.findRolesByUserName(primaryPrincipal);
        //授权角色信息
        if(!CollectionUtils.isEmpty(user.getRoles())){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            user.getRoles().forEach(role->{
                simpleAuthorizationInfo.addRole(role.getName());
                //权限信息
                List<Perms> perms = userService.findPermsByRoleId(role.getId());
                if(!CollectionUtils.isEmpty(perms)){
                    perms.forEach(perm->{
                        simpleAuthorizationInfo.addStringPermission(perm.getName());
                    });
                }
            });
            return simpleAuthorizationInfo;
        }
        return null;
    }
}
```

![image-20200527213821611](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200527213821611.png)



---





### 8.11 使用Cache (缓存)

#### **1.Cache 介绍**

- Cache 缓存: **计算机内存中一段数据**  
- 作用: **用来减轻DB的访问压力,从而提高系统的查询效率**
- 流程: 

![image-20200530090656417](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200530090656417.png)

**2.使用shiro中默认EhCache实现缓存**

1.引入依赖

```xml
<!--引入shiro和ehcache-->
<dependency>
  <groupId>org.apache.shiro</groupId>
  <artifactId>shiro-ehcache</artifactId>
  <version>1.5.3</version>
</dependency>
```

2.开启缓存

```java
    //自定义Realm
    @Bean
    public Realm getRealm(){
        CustomerRealm customerRealm=new CustomerRealm();

        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashIterations(1024);
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        customerRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        customerRealm.setCacheManager(new EhCacheManager());
        //开启全局缓存
        customerRealm.setCachingEnabled(true);
        //开启认证缓存
        customerRealm.setAuthenticationCachingEnabled(true);
        //开启授权缓存
        customerRealm.setAuthorizationCachingEnabled(true);

        //设置name
        customerRealm.setAuthenticationCacheName("Authentication");
        customerRealm.setAuthorizationCacheName("Authorization");
        return customerRealm;
    }
```

![image-20200529173859939](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200529173859939.png)

3.启动刷新页面进行测试

- 注意:如果控制台没有任何sql展示说明缓存已经开启





#### 2.shiro中使用Redis

##### 1.引入redis依赖

```xml
<!--redis整合springboot-->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

##### 2.配置redis连接

```properties
spring.redis.port=6379
spring.redis.host=localhost
spring.redis.database=0
```

![image-20200530084616799](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200530084616799.png)

##### 3.启动redis服务

```powershell
➜  bin ls
dump.rdb        redis-check-aof redis-cli       redis-server    redis.conf
redis-benchmark redis-check-rdb redis-sentinel  redis-trib.rb
➜  bin ./redis-server redis.conf
```

![image-20200530081954871](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200530081954871.png)

4.开发**RedisCacheManager**

```java
public class RedisCacheManager implements CacheManager {
    @Override
    public <K, V> Cache<K, V> getCache(String cacheName) throws CacheException {
        System.out.println("缓存名称: "+cacheName);
        return new RedisCache<K,V>(cacheName);
    }
}
```

**5.开RedisCache实现**

```java
public class RedisCache<K,V> implements Cache<K,V> {

    private String cacheName;

    public RedisCache() {
    }

    public RedisCache(String cacheName) {
        this.cacheName = cacheName;
    }

    @Override
    public V get(K k) throws CacheException {
        System.out.println("获取缓存:"+ k);
        return (V) getRedisTemplate().opsForHash().get(this.cacheName,k.toString());
    }

    @Override
    public V put(K k, V v) throws CacheException {
        System.out.println("设置缓存key: "+k+" value:"+v);
        getRedisTemplate().opsForHash().put(this.cacheName,k.toString(),v);
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        return (V) getRedisTemplate().opsForHash().delete(this.cacheName,k.toString());
    }

    @Override
    public v remove(k k) throws CacheException {
        return (v) getRedisTemplate().opsForHash().delete(this.cacheName,k.toString());
    }

    @Override
    public void clear() throws CacheException {
        getRedisTemplate().delete(this.cacheName);
    }

    @Override
    public int size() {
        return getRedisTemplate().opsForHash().size(this.cacheName).intValue();
    }

    @Override
    public Set<k> keys() {
        return getRedisTemplate().opsForHash().keys(this.cacheName);
    }

    @Override
    public Collection<v> values() {
        return getRedisTemplate().opsForHash().values(this.cacheName);
    }

    private RedisTemplate getRedisTemplate(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }


    //封装获取redisTemplate
    private RedisTemplate getRedisTemplate(){
        RedisTemplate redisTemplate = (RedisTemplate) ApplicationContextUtils.getBean("redisTemplate");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
```



**6.启动项目测试发现报错**

![image-20200530100850618](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200530100850618.png)

![image-20200530100948598](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200530100948598.png)

- 错误解释: **由于shiro中提供的simpleByteSource实现没有实现序列化,所有在认证时出现错误信息**

- 解决方案: **需要自动salt实现序列化**

  - 自定义salt实现序列化

    ```java
    //自定义salt实现  实现序列化接口
    public class MyByteSource extends SimpleByteSource implements Serializable {
        public MyByteSource(String string) {
            super(string);
        }
    }
    ```

  - 在realm中使用自定义salt

    ```java
     @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
      System.out.println("==========================");
      //根据身份信息
      String principal = (String) token.getPrincipal();
      //在工厂中获取service对象
      UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
      User user = userService.findByUserName(principal);
      if(!ObjectUtils.isEmpty(user)){
        return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(), 
                                          new MyByteSource(user.getSalt()),this.getName());
      }
      return null;
    }
    ```

    ![image-20200530101301543](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200530101301543.png)

**7.再次启动测试,发现可以成功放入redis缓存**

![image-20200530101617692](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200530101617692.png)

----



### 8.12 加入验证码验证

**0.开发页面加入验证码**

```html
请输入验证码:<input type="text" name="codes"><img th:src="@{/getImage}" /><br>
```

- 开发控制器

  ```java
      @GetMapping("/getImage")
      public void getImage(HttpSession session, HttpServletResponse response) throws IOException {
          String code = VerifyCodeUtils.generateVerifyCode(4);
          session.setAttribute("code",code);
          ServletOutputStream os = response.getOutputStream();
          response.setContentType("image/png");
          VerifyCodeUtils.outputImage(220,60,os,code);
      }
  ```

- ```java
  package com.jie.shiro.utils;
  
  import javax.imageio.ImageIO;
  import java.awt.*;
  import java.awt.geom.AffineTransform;
  import java.awt.image.BufferedImage;
  import java.io.File;
  import java.io.FileOutputStream;
  import java.io.IOException;
  import java.io.OutputStream;
  import java.util.Arrays;
  import java.util.Random;
  
  /**
   *@创建人  cx
   *@创建时间  2018/11/27 17:36
   *@描述   验证码生成
   */
  public class VerifyCodeUtils{
  
      //使用到Algerian字体，系统里没有的话需要安装字体，字体只显示大写，去掉了1,0,i,o几个容易混淆的字符
      public static final String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
      private static Random random = new Random();
  
  
      /**
       * 使用系统默认字符源生成验证码
       * @param verifySize    验证码长度
       * @return
       */
      public static String generateVerifyCode(int verifySize){
          return generateVerifyCode(verifySize, VERIFY_CODES);
      }
      /**
       * 使用指定源生成验证码
       * @param verifySize    验证码长度
       * @param sources   验证码字符源
       * @return
       */
      public static String generateVerifyCode(int verifySize, String sources){
          if(sources == null || sources.length() == 0){
              sources = VERIFY_CODES;
          }
          int codesLen = sources.length();
          Random rand = new Random(System.currentTimeMillis());
          StringBuilder verifyCode = new StringBuilder(verifySize);
          for(int i = 0; i < verifySize; i++){
              verifyCode.append(sources.charAt(rand.nextInt(codesLen-1)));
          }
          return verifyCode.toString();
      }
  
      /**
       * 生成随机验证码文件,并返回验证码值
       * @param w
       * @param h
       * @param outputFile
       * @param verifySize
       * @return
       * @throws IOException
       */
      public static String outputVerifyImage(int w, int h, File outputFile, int verifySize) throws IOException{
          String verifyCode = generateVerifyCode(verifySize);
          outputImage(w, h, outputFile, verifyCode);
          return verifyCode;
      }
  
      /**
       * 输出随机验证码图片流,并返回验证码值
       * @param w
       * @param h
       * @param os
       * @param verifySize
       * @return
       * @throws IOException
       */
      public static String outputVerifyImage(int w, int h, OutputStream os, int verifySize) throws IOException{
          String verifyCode = generateVerifyCode(verifySize);
          outputImage(w, h, os, verifyCode);
          return verifyCode;
      }
  
      /**
       * 生成指定验证码图像文件
       * @param w
       * @param h
       * @param outputFile
       * @param code
       * @throws IOException
       */
      public static void outputImage(int w, int h, File outputFile, String code) throws IOException{
          if(outputFile == null){
              return;
          }
          File dir = outputFile.getParentFile();
          if(!dir.exists()){
              dir.mkdirs();
          }
          try{
              outputFile.createNewFile();
              FileOutputStream fos = new FileOutputStream(outputFile);
              outputImage(w, h, fos, code);
              fos.close();
          } catch(IOException e){
              throw e;
          }
      }
  
      /**
       * 输出指定验证码图片流
       * @param w
       * @param h
       * @param os
       * @param code
       * @throws IOException
       */
      public static void outputImage(int w, int h, OutputStream os, String code) throws IOException{
          int verifySize = code.length();
          BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
          Random rand = new Random();
          Graphics2D g2 = image.createGraphics();
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
          Color[] colors = new Color[5];
          Color[] colorSpaces = new Color[] { Color.WHITE, Color.CYAN,
                  Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
                  Color.PINK, Color.YELLOW };
          float[] fractions = new float[colors.length];
          for(int i = 0; i < colors.length; i++){
              colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
              fractions[i] = rand.nextFloat();
          }
          Arrays.sort(fractions);
  
          g2.setColor(Color.GRAY);// 设置边框色
          g2.fillRect(0, 0, w, h);
  
          Color c = getRandColor(200, 250);
          g2.setColor(c);// 设置背景色
          g2.fillRect(0, 2, w, h-4);
  
          //绘制干扰线
          Random random = new Random();
          g2.setColor(getRandColor(160, 200));// 设置线条的颜色
          for (int i = 0; i < 20; i++) {
              int x = random.nextInt(w - 1);
              int y = random.nextInt(h - 1);
              int xl = random.nextInt(6) + 1;
              int yl = random.nextInt(12) + 1;
              g2.drawLine(x, y, x + xl + 40, y + yl + 20);
          }
  
          // 添加噪点
          float yawpRate = 0.05f;// 噪声率
          int area = (int) (yawpRate * w * h);
          for (int i = 0; i < area; i++) {
              int x = random.nextInt(w);
              int y = random.nextInt(h);
              int rgb = getRandomIntColor();
              image.setRGB(x, y, rgb);
          }
  
          shear(g2, w, h, c);// 使图片扭曲
  
          g2.setColor(getRandColor(100, 160));
          int fontSize = h-4;
          Font font = new Font("Algerian", Font.ITALIC, fontSize);
          g2.setFont(font);
          char[] chars = code.toCharArray();
          for(int i = 0; i < verifySize; i++){
              AffineTransform affine = new AffineTransform();
              affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), (w / verifySize) * i + fontSize/2, h/2);
              g2.setTransform(affine);
              g2.drawChars(chars, i, 1, ((w-10) / verifySize) * i + 5, h/2 + fontSize/2 - 10);
          }
  
          g2.dispose();
          ImageIO.write(image, "jpg", os);
      }
  
      private static Color getRandColor(int fc, int bc) {
          if (fc > 255)
              fc = 255;
          if (bc > 255)
              bc = 255;
          int r = fc + random.nextInt(bc - fc);
          int g = fc + random.nextInt(bc - fc);
          int b = fc + random.nextInt(bc - fc);
          return new Color(r, g, b);
      }
  
      private static int getRandomIntColor() {
          int[] rgb = getRandomRgb();
          int color = 0;
          for (int c : rgb) {
              color = color << 8;
              color = color | c;
          }
          return color;
      }
  
      private static int[] getRandomRgb() {
          int[] rgb = new int[3];
          for (int i = 0; i < 3; i++) {
              rgb[i] = random.nextInt(255);
          }
          return rgb;
      }
  
      private static void shear(Graphics g, int w1, int h1, Color color) {
          shearX(g, w1, h1, color);
          shearY(g, w1, h1, color);
      }
  
      private static void shearX(Graphics g, int w1, int h1, Color color) {
  
          int period = random.nextInt(2);
  
          boolean borderGap = true;
          int frames = 1;
          int phase = random.nextInt(2);
  
          for (int i = 0; i < h1; i++) {
              double d = (double) (period >> 1)
                      * Math.sin((double) i / (double) period
                      + (6.2831853071795862D * (double) phase)
                      / (double) frames);
              g.copyArea(0, i, w1, 1, (int) d, 0);
              if (borderGap) {
                  g.setColor(color);
                  g.drawLine((int) d, i, 0, i);
                  g.drawLine((int) d + w1, i, w1, i);
              }
          }
  
      }
  
      private static void shearY(Graphics g, int w1, int h1, Color color) {
  
          int period = random.nextInt(40) + 10; // 50;
  
          boolean borderGap = true;
          int frames = 20;
          int phase = 7;
          for (int i = 0; i < w1; i++) {
              double d = (double) (period >> 1)
                      * Math.sin((double) i / (double) period
                      + (6.2831853071795862D * (double) phase)
                      / (double) frames);
              g.copyArea(i, 0, 1, h1, 0, (int) d);
              if (borderGap) {
                  g.setColor(color);
                  g.drawLine(i, (int) d, i, 0);
                  g.drawLine(i, (int) d + h1, i, h1);
              }
  
          }
  
      }
      public static void main(String[] args) throws IOException {
          //获取验证码
          String s = generateVerifyCode(4);
          //将验证码放入图片中
          outputImage(260,60,new File("/Users/chenyannan/Desktop/安工资料/aa.jpg"),s);
          System.out.println(s);
      }
  }
  ```

- 放行验证码

  ![image-20200530141757606](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200530141757606.png)

- 开发页面

  ![image-20200530141828004](D:\study\JAVAEE\shiro\Shiro 实战教程.assets\image-20200530141828004.png)

- 修改认证流程

  ```java
      @GetMapping("/loginUrl")
      public String loginUrl(String username,String password,HttpSession session,String codes){
          String code = (String) session.getAttribute("code");
          if (codes.equalsIgnoreCase(code)){
              //通过创建的subject用户与后端进行比对
              Subject subject= SecurityUtils.getSubject();
              try {
                  subject.login(new UsernamePasswordToken(username,password));   //登陆
                  return "index";
              } catch (UnknownAccountException e) {
                  e.printStackTrace();
                  System.out.println("用户名错误");
              }catch (IncorrectCredentialsException e){
                  e.printStackTrace();
                  System.out.println("密码错误");
              }
          }else {
              System.out.println("验证码错误");
          }
          return "redirect:/login";
      }
  ```
  

  

  

  
----

### 8.13.hymeleaf权限控制

1.引入扩展依赖

```xml
<dependency>
    <groupId>com.github.theborakompanioni</groupId>
    <artifactId>thymeleaf-extras-shiro</artifactId>
    <version>2.0.0</version>
</dependency>
```

2.页面中引入命名空间

-  xmlns:shiro="http://www.pollix.at/thymeleaf/shiro"

```html
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
      xmlns:shiro="http://www.pollix.at/thymeleaf/shiro">
......
```

**3.加入shiro的方言配置**

- 页面标签不起作用一定要记住加入方言处理

```java
@Bean(name = "shiroDialect")
public ShiroDialect shiroDialect(){
  return new ShiroDialect();
}
```

![image-20200601210335151](D:/study/JAVAEE/shiro/Shiro 实战教程.assets/image-20200601210335151.png)



4**.常见权限控制标签使用**

```html
<!-- 验证当前用户是否为“访客”，即未认证（包含未记住）的用户。 -->
<p shiro:guest="">Please <a href="login.html">login</a></p>


<!-- 认证通过或已记住的用户。 -->
<p shiro:user="">
    Welcome back John! Not John? Click <a href="login.html">here</a> to login.
</p>

<!-- 已认证通过的用户。不包含已记住的用户，这是与user标签的区别所在。 -->
<p shiro:authenticated="">
    Hello, <span shiro:principal=""></span>, how are you today?
</p>
<a shiro:authenticated="" href="updateAccount.html">Update your contact information</a>

<!-- 输出当前用户信息，通常为登录帐号信息。 -->
<p>Hello, <shiro:principal/>, how are you today?</p>


<!-- 未认证通过用户，与authenticated标签相对应。与guest标签的区别是，该标签包含已记住用户。 -->
<p shiro:notAuthenticated="">
    Please <a href="login.html">login</a> in order to update your credit card information.
</p>

<!-- 验证当前用户是否属于该角色。 -->
<a shiro:hasRole="admin" href="admin.html">Administer the system</a><!-- 拥有该角色 -->

<!-- 与hasRole标签逻辑相反，当用户不属于该角色时验证通过。 -->
<p shiro:lacksRole="developer"><!-- 没有该角色 -->
    Sorry, you are not allowed to developer the system.
</p>

<!-- 验证当前用户是否属于以下所有角色。 -->
<p shiro:hasAllRoles="developer, 2"><!-- 角色与判断 -->
    You are a developer and a admin.
</p>

<!-- 验证当前用户是否属于以下任意一个角色。  -->
<p shiro:hasAnyRoles="admin, vip, developer,1"><!-- 角色或判断 -->
    You are a admin, vip, or developer.
</p>

<!--验证当前用户是否拥有指定权限。  -->
<a shiro:hasPermission="userInfo:add" href="createUser.html">添加用户</a><!-- 拥有权限 -->

<!-- 与hasPermission标签逻辑相反，当前用户没有制定权限时，验证通过。 -->
<p shiro:lacksPermission="userInfo:del"><!-- 没有权限 -->
    Sorry, you are not allowed to delete user accounts.
</p>

<!-- 验证当前用户是否拥有以下所有角色。 -->
<p shiro:hasAllPermissions="userInfo:view, userInfo:add"><!-- 权限与判断 -->
    You can see or add users.
</p>

<!-- 验证当前用户是否拥有以下任意一个权限。  -->
<p shiro:hasAnyPermissions="userInfo:view, userInfo:del"><!-- 权限或判断 -->
    You can see or delete users.
</p>
<a shiro:hasPermission="pp" href="createUser.html">Create a new User</a>

```



## 8.3 JWT

引入依赖

```xml
<!--JWT的依赖-->
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.4.0</version>
</dependency>
```

### 生成解析token

生成Token

```java
    Map<String,Object> map=new HashMap<>();

    Calendar instance=Calendar.getInstance();
    instance.add(Calendar.SECOND,20);

    System.out.println(instance.getTime());
    String token = JWT.create()
            //header
            .withHeader(map)      //可以不写
            //payload
            .withClaim("userID", 21)
            .withClaim("userName", "zcj")
            //过期时间
            .withExpiresAt(instance.getTime())
            //签名
            .sign(Algorithm.HMAC256("ZCJ@Token666"));
    System.out.println(token);
}
```

解析Token

```java
@Test
public void test(){
    //获得解析对象
    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("ZCJ@Token666")).build();
    //解析token
    DecodedJWT verify = jwtVerifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6InpjajIiLCJleHAiOjE2MTQ1OTkxMzQsInVzZXJJRCI6MjF9.Z0nmBBafpyQHOsVLMr-pTPI8YABfuAunJqDx9g35c0g");
    //获取携带信息
    System.out.println(verify.getClaim("userID").asInt());
    System.out.println(verify.getClaim("userName").asString());
    //获取过期时间
    System.out.println("过期时间"+verify.getExpiresAt());
}
```



### 工具类

```java
public class JWTUtils {

    private static final String SING="!!!!ZCJ123456789";

    public static String getToken(Map<String,String> map){
        Calendar instance=Calendar.getInstance();
        instance.add(Calendar.DATE,7);

        //添加信息
        JWTCreator.Builder builder = JWT.create();
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });

        String token =builder
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SING));
        return token;
    }

    public static DecodedJWT verify(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SING)).build();
        return jwtVerifier.verify(token);
    }
}
```



### 加入到控制层

```java
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    @ResponseBody
    public Map<String,Object> login(User user){
        Map<String,Object> map=new HashMap<>();

        try {
            User login = userService.login(user);

            //使用jwt
            Map<String,String> payload=new HashMap<>();
            payload.put("id",String.valueOf(login.getId()));
            payload.put("name",login.getName());
            //生成token
            String token = JWTUtils.getToken(payload);
            map.put("state",true);
            map.put("msg","认证成功");
            map.put("token",token);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state",false);
            map.put("msg","认证失败");
        }
        return map;
    }

    @GetMapping("/test")
    @ResponseBody
    public Map<String,Object> test(String token){
        Map<String ,Object> map=new HashMap<>();
        try {
            DecodedJWT verify = JWTUtils.verify(token);
            map.put("state",true);
            map.put("msg","请求成功");
            return map;
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            map.put("msg","无效签名");
        }catch (TokenExpiredException e){
            e.printStackTrace();
            map.put("msg","token过期");
        }catch (AlgorithmMismatchException e){
            e.printStackTrace();
            map.put("msg","认证不一致");
        }catch (Exception e){
            e.printStackTrace();
            map.put("msg","无效签名");
        }
        map.put("state",false);
        return map;
    }
}
```

![image-20210301215235039](file://D:\study\JAVAEE\JWT\img\image-20210301215235039.png?lastModify=1614676571)





### 添加到拦截器

```java
public class JWTInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");

        Map<String ,Object> map=new HashMap<>();
        try {
            DecodedJWT verify = JWTUtils.verify(token);
            return false;
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            map.put("msg","无效签名");
        }catch (TokenExpiredException e){
            e.printStackTrace();
            map.put("msg","token过期");
        }catch (AlgorithmMismatchException e){
            e.printStackTrace();
            map.put("msg","认证不一致");
        }catch (Exception e){
            e.printStackTrace();
            map.put("msg","无效签名");
        }
        map.put("state",false);
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JWTInterceptor())
                .addPathPatterns("/test")
                .excludePathPatterns("/login");
    }
}
```

控制层

```java
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    @ResponseBody
    public Map<String,Object> login(User user, HttpServletResponse response){
        Map<String,Object> map=new HashMap<>();

        try {
            User login = userService.login(user);
            //使用jwt
            Map<String,String> payload=new HashMap<>();
            payload.put("id",String.valueOf(login.getId()));
            payload.put("name",login.getName());
            //生成token
            String token = JWTUtils.getToken(payload);
            map.put("state",true);
            map.put("msg","认证成功");
            map.put("token",token);
            response.addHeader("token",token);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state",false);
            map.put("msg","认证失败");
        }
        return map;
    }

    @GetMapping("/test")
    @ResponseBody
    public Map<String,Object> test(){
        Map<String ,Object> map=new HashMap<>();
        map.put("state",true);
        map.put("msg","请求成功");
        return map;
    }
}
```

# 9.任务

## 9.1 异步任务

```java
@Service
public class AsyncService {

    public void hello(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("数据等待完成~");

    }
}

@Controller
public class AsyncController {
    @Autowired
    private AsyncService asyncService;

    @ResponseBody
    @GetMapping("/hello")
    public String hello(){
        asyncService.hello();
        return "ok";
    }
}
```

如果网页调用hello请求,程序便会卡在线程等待那里，**停止不前**



对于需要多线程的方法 可以使用**开启异步**

1.添加上**@EnableAsync**

```java
@EnableAsync
@SpringBootApplication
public class BootWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootWorkApplication.class, args);
    }

}
```

2.给需要异步的方法添加   **@Async**

```java
public void hello(){
    try {
        Thread.sleep(3000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    System.out.println("数据等待完成~");
}
```

程序便会为此方法**开启额外的线程**，保证运行流畅



## 9.2 邮件任务

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

导入**配置**信息

```properties
spring.mail.username=31999*@qq.com
spring.mail.password=laobhltghtctbgfb
spring.mail.host=smtp.qq.com
spring.mail.default-encoding=UTF-8
#开启加密验证
spring.mail.properties.mail.smtp.ssl.enable=true
```



**发送简单文件**

```java
@Autowired
JavaMailSender mailSender;

@Test
void contextLoads() {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setSubject("你好");
    mailMessage.setText("内容");
    mailMessage.setFrom("319991012@qq.com");
    mailMessage.setTo("2715099914@qq.com");
    mailSender.send(mailMessage);
}
```



**发送复杂文件**

```java
@Autowired
JavaMailSender mailSender;


@Test
void contextLoads2() throws MessagingException, IOException {
    //创建复杂邮件
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    //组装
    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,true);
    messageHelper.setSubject("你好");
    //        messageHelper.setText("内容");
    messageHelper.setText("<p style='color:red'>内容 <p>",true);  //开启html编码识别

    //附件
    messageHelper.addAttachment("1.jpg",new File("src/main/resources/static/img/faker.jpg"));

    messageHelper.setFrom("319991012@qq.com");
    messageHelper.setTo("2389928721@qq.com");

    mailSender.send(mimeMessage);
}
```



## 9.3 定时任务

项目开发中经常需要执行一些定时任务，比如需要在每天凌晨的时候，分析一次前一天的日志信息，Spring为我们提供了异步执行任务调度的方式，提供了两个接口。

- TaskExecutor接口       任务执行者
- TaskScheduler接口     任务调度者

两个注解：

- @EnableScheduling
- @Scheduled

**cron表达式：**

![图片](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7LUziamJeeiaLFt7YwxJtAgSMKLnW0ibMAiaR5yXOER51iaH9WTkrLhr0rSAnAJxJUM9c8eTGaCWXuYOibA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

![图片](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7LUziamJeeiaLFt7YwxJtAgSMfyibiaXGFm87zic2Ng3ICjicp4tlAia8MXDafQXZ9UZ7bsreJoTU9VWaBXg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**测试：**

1、这里写完定时任务之后，我们需要在主程序上增加**@EnableScheduling** 开启定时任务功能

```java
@EnableScheduling //开启基于注解的定时任务
@SpringBootApplication
public class SpringbootTaskApplication {

   public static void main(String[] args) {
       SpringApplication.run(SpringbootTaskApplication.class, args);
  }

}
```



2、创建一个ScheduledService

我们里面存在一个hello方法，他需要定时执行，怎么处理呢？

```java
@Service
public class ScheduledService {
   
   //秒   分   时     日   月   周几
   //0 * * * * MON-FRI
   @Scheduled(cron = "0 * * * * 0-7")    
   public void hello(){
       System.out.println("hello.....");
  }
}
```

**意味着每过一分钟就会执行这段代码**

![image-20210205144412747](img\21.png)



3、我们来详细了解下cron表达式；

http://www.bejson.com/othertools/cron/

4、常用的表达式

```
（1）0/2 * * * * ?   表示每2秒 执行任务
（1）0 0/2 * * * ?   表示每2分钟 执行任务
（1）0 0 2 1 * ?   表示在每月的1日的凌晨2点调整任务
（2）0 15 10 ? * MON-FRI   表示周一到周五每天上午10:15执行作业
（3）0 15 10 ? 6L 2002-2006   表示2002-2006年的每个月的最后一个星期五上午10:15执行作
（4）0 0 10,14,16 * * ?   每天上午10点，下午2点，4点
（5）0 0/30 9-17 * * ?   朝九晚五工作时间内每半小时
（6）0 0 12 ? * WED   表示每个星期三中午12点
（7）0 0 12 * * ?   每天中午12点触发
（8）0 15 10 ? * *   每天上午10:15触发
（9）0 15 10 * * ?     每天上午10:15触发
（10）0 15 10 * * ?   每天上午10:15触发
（11）0 15 10 * * ? 2005   2005年的每天上午10:15触发
（12）0 * 14 * * ?     在每天下午2点到下午2:59期间的每1分钟触发
（13）0 0/5 14 * * ?   在每天下午2点到下午2:55期间的每5分钟触发
（14）0 0/5 14,18 * * ?     在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
（15）0 0-5 14 * * ?   在每天下午2点到下午2:05期间的每1分钟触发
（16）0 10,44 14 ? 3 WED   每年三月的星期三的下午2:10和2:44触发
（17）0 15 10 ? * MON-FRI   周一至周五的上午10:15触发
（18）0 15 10 15 * ?   每月15日上午10:15触发
（19）0 15 10 L * ?   每月最后一日的上午10:15触发
（20）0 15 10 ? * 6L   每月的最后一个星期五上午10:15触发
（21）0 15 10 ? * 6L 2002-2005   2002年至2005年的每月的最后一个星期五上午10:15触发
（22）0 15 10 ? * 6#3   每月的第三个星期五上午10:15触发
```



# 10.分布式

在《分布式系统原理与范型》一书中有如下定义：“**分布式系统是若干独立计算机的集合，这些计算机对于用户来说就像单个相关系统”**；

分布式系统是由一组通过网络进行通信、为了完成共同的任务而协调工作的计算机节点组成的系统。分布式系统的出现是为了用廉价的、普通的机器完成单个计算机无法完成的计算、存储任务。其目的是**利用更多的机器，处理更多的数据**。

分布式系统（distributed system）是建立在网络之上的软件系统

**RPC**

RPC【Remote Procedure Call】是指远程过程调用，是一种进程间通信方式，他是一种技术的思想，而不是规范。



## **dubbo基本概念**

![图片](https://mmbiz.qpic.cn/mmbiz_png/uJDAUKrGC7JJjARRqcZibY4ZPv60renshLSMRQe7NJpvDFrQMChLxI3BqIYQXrZvfs28iadQ1dDB4p84ydyb3KtQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

**服务提供者**（Provider）：暴露服务的服务提供方，服务提供者在启动时，向注册中心注册自己提供的服务。

**服务消费者**（Consumer）：调用远程服务的服务消费方，服务消费者在启动时，向注册中心订阅自己所需的服务，服务消费者，从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。

**注册中心**（Registry）：注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者

**监控中心**（Monitor）：服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心

![image-20210205222634861](img\22.png)

cmd中 输入    **java -jar dubbo-admin-0.0.1-SNAPSHOT.jar**

即可在   **localhost7001**  中 即可监视 分布式项目

![image-20210205224734798](img\24.png)



## ZooKeeper

是一个[分布式](https://baike.baidu.com/item/分布式/19276232)的，开放源码的[分布式应用程序](https://baike.baidu.com/item/分布式应用程序/9854429)协调服务，是[Google](https://baike.baidu.com/item/Google)的Chubby一个[开源](https://baike.baidu.com/item/开源/246339)的实现，是Hadoop和[Hbase](https://baike.baidu.com/item/Hbase/7670213)的重要组件。它是一个为分布式应用提供一致性服务的软件，提供的功能包括：配置维护、域名服务、分布式同步、组服务等。

ZooKeeper的目标就是封装好复杂易出错的关键服务，将简单易用的接口和性能高效、功能稳定的系统提供给用户。

ZooKeeper包含一个简单的原语集，提供Java和C的接口。

![image-20210205222634861](img\22.png)

启动zkServer.cmd 即可 开启zookeeper



## SpringBoot项目集成使用

首先需要两个Springboot的项目

- boot-provider   提供者
- boot-consumer  消费者

都需要导入

```xml
<!-- Dubbo Spring Boot Starter -->
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>2.7.3</version>
</dependency>
<dependency>
    <groupId>com.github.sgroschupf</groupId>
    <artifactId>zkclient</artifactId>
    <version>0.1</version>
</dependency>
<!-- 引入zookeeper -->
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>2.12.0</version>
</dependency>
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>2.12.0</version>
</dependency>
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.4.14</version>
    <!--排除这个slf4j-log4j12-->
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```



### provider项目

```java
public interface TickerService {
    String getTicket();
}

----------

import org.apache.dubbo.config.annotation.Service;

@Service   //注意  @service是dubbo的service
public class TicketServiceImpl implements TickerService {
    @Override
    public String getTicket() {
        return "hello";
    }
}

```

- **@service是dubbo的service**



写入配置文件

```properties
server.port=8001

#当前应用名字
dubbo.application.name=provider-server
#注册中心地址
dubbo.registry.address=zookeeper://127.0.0.1:2181
#扫描指定包下服务
dubbo.scan.base-packages=com.jie.boot.service
```



### consumer项目

```java
package com.jie.boot.service;

public interface TickerService {
    String getTicket();
}

----------------------------------

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Reference //远程引用指定的服务，他会按照全类名进行匹配，看谁给注册中心注册了这个全类名
    TickerService ticketService;

    public void bugTicket(){
        String ticket = ticketService.getTicket();
        System.out.println("拿到的数据"+ticket);
    }
}
```

- TickerService接口可以在此项目中写在与提供者项目**项目**的路径下
- **@Service**  这个service是 提交给spring的  service注解
- **@Reference**   使用这个与提供者项目的提供的内容进行匹配

写入配置文件

```properties
server.port=8002

#当前应用名字
dubbo.application.name=consumer-server
#注册中心地址
dubbo.registry.address=zookeeper://127.0.0.1:2181
```





### 结果

即可在消费者项目中获取提供者项目提供的内容

![image-20210205224139247](img\23.png)

![image-20210205224734798](img\24.png)

# 11.第三方整合

## Swagger

- 号称世界上最流行的API框架
- Restful Api 文档在线自动生成器 => **API 文档 与API 定义同步更新**
- 直接运行，在线测试API
- 支持多种语言 （如：Java，PHP等）
- 官网：https://swagger.io/





```xml
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
```

### 开启

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {

}
```

放入编写类即可开启



### 配置Swagger-ui

编写配置类

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    //放入Docket
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo());
    }

    //Swagger的配置信息
    public ApiInfo apiInfo(){
        //作者信息
        Contact contact = new Contact("zcj", "http://39.98.69.145:8080/jie/", "319991012@qq.com");

        return new ApiInfo("SwaggerAPI文档",
                "Swagger练手",
                "1.0",
                "http://39.98.69.145:8080/jie/",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
}
```



访问 http://localhost:8080/swagger-ui.html

![image-20210204135336489](img\16.png)





### 配置扫描的条件

**enable**      扫描关闭按钮 默认为true

**apis** (RequestHandlerSelectors)  添加扫描方式

- basePackage 指定要扫描的包
- any  扫描全部
- none  都不扫描

**paths**  (PathSelectors.ant("/zcj/*"))   过滤路径

```java
//放入Docket
@Bean
public Docket docket(){
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            //.enable(false)       关闭扫描 默认为true
            .select()
            //RequestHandlerSelectors  配置要扫描的接口方式
            //basePackage 指定要扫描的包
            //any  扫描全部
            //none  都不扫描
            .apis(RequestHandlerSelectors.basePackage("com.jie.boot.controller"))
            //paths 过滤什么路径
            //.paths(PathSelectors.ant("/zcj/*"))
            .build();
}
```

**groupName**("zcj2")  分组

```java
    //放入Docket2
    @Bean
    public Docket docket2(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo2())
                .groupName("zcj2")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jie.boot.controller"))
                .build();
    }

    //Swagger2的配置信息
    public ApiInfo apiInfo2(){
        //作者信息
        Contact contact = new Contact("zcj", "http://39.98.69.145:8080/jie/", "319991012@qq.com");

        return new ApiInfo("SwaggerAPI文档",
                "Swagger练手2",
                "1.0",
                "http://39.98.69.145:8080/jie/",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
```

创建docket 再放入ApiInfo中 即可实现多个分组

![image-20210204180124951](img\17.png)



### Mdeol

只要控制层返回有实体类 便会在Model中显示

```java
@ResponseBody
@GetMapping("/user")
public User user(){
    return new User();
}
```

![image-20210204180629095](img\18.png)



### API注解



**@ApiModel**("用户实体类")              注释实体类

**@ApiModelProperty**("用户姓名")   注释类属性

```java
@ApiModel("用户实体类")
public class User {
    @ApiModelProperty("用户姓名")
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

![image-20210204211933268](img\19.png)



**@ApiOperation("hello方法")**  注释方法

**@ApiParam**("参数n")                注释参数

**@Api(tags = "hello控制层")**       可以用于注释类上

```java
@Api(tags = "hello控制层")
@Controller
public class HelloController {

    @ApiOperation("hello方法")
    @ResponseBody
    @GetMapping("/hello")
    public String hello(@ApiParam("参数n") int n){
        return "index";
    }

}
```

![image-20210204212621277](img\20.png)





## 整合Redis

