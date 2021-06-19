

# SpringPlus



## AOP

### 方法测试类

```java
public class MathTest {
    public int div(int x,int j){
        return x/j;
    }
}
```

### 切面

```java
@Aspect  //声明为切入类
public class LogAspects {
    //抽取公共类切入点
    @Pointcut("execution(public int com.jie.aop.MathTest.div(..))")
    public void pointCut(){ };

    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint){   //
        Object[] args = joinPoint.getArgs();
        System.out.println("除法运行~~~:"+joinPoint.getSignature().getName()+" "+"运行参数为"+ Arrays.asList(args));
    }

    @After("execution(public int com.jie.aop.MathTest.div(..))")
    public void logEnd(JoinPoint joinPoint){
        System.out.println("除法结束~~:"+joinPoint.getSignature().getName()+" "+joinPoint.getArgs());
    }

    @AfterThrowing(value = "pointCut()",throwing = "exception")
    public void Log(Exception exception){
        System.out.println("异常为：");
        exception.printStackTrace();
    }
}
```



**@Aspect**  声明该类为切面



**@Pointcut("execution(public int com.jie.aop.MathTest.div(..))")**   声明切入点

**JoinPoint joinPoint**  切入点信息 

**joinPoint.getArgs();**  切入方法的参数

**joinPoint.getSignature().getName()**  切入的方法名



### IOC容器类

```java
@EnableAspectJAutoProxy
@Configuration
public class MainConfigOfAop {
    @Bean
    public MathTest getMainConfigOfAop(){
        return new MathTest();
    }

    @Bean
    public LogAspects getLogAspects(){
        return new LogAspects();
    }
}
```



**@EnableAspectJAutoProxy**  开启Aop动态代理





## IOC