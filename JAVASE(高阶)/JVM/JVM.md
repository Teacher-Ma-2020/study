# 什么是JVM

**定义**

Java Virtual Machine，JAVA程序的**运行环境**（JAVA二进制字节码的运行环境）



 JVM JRE JDK的区别

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150422.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150422.png)



# 内存结构



## **整体架构**

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150440.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150440.png)



## 程序计数器

![image-20210328131157944](img\1.png)



**作用**：记住下一条jvm指令的执行地址

一条二进制字节码交给解释器，然后解释器转换成直接码交给cpu运行，程序计数器记住下一条jvm的执行地址，一条一条的交给解释器运行

**特点**：

- 线程私有   不同的线程会有自己的程序计数器，保证程序正确运行
- 不会存在内存溢出





## 虚拟机栈

### 定义

- 每次线程运行需要的**内存空间**，成为虚拟机栈
- 每个栈由多个**栈帧**组成
  - **栈帧**：对应着每次调用方法时所占用的内存
- 每个线程只能有一个**活动栈帧**，对应着当前正在执行的方法



代码

![image-20210328140551601](img\2.png)

- 当调用该方法时，就将该方法对应的栈帧压入虚拟机栈中
- 如果该方法调用了其他方法，则继续入栈
- 当调用结束后，依次从虚拟机栈中出栈



### **问题分析**

1. 垃圾回收是否涉及到栈内存

   **不需要**，因为虚拟机栈由一个个栈帧组成，在方法执行完毕后，对应的栈帧会自动出栈，所以无需垃圾回收 ，垃圾回收机制涉及的是堆内存

2. 栈内存分配的越大越好吗

   **不是** 因为物理内存是一定，

   栈内存越大，可以支持更多的递归调用、可执行线程数越少

3. 方法内的局部变量是否是线程安全的？

   如果方法内局部变量没有逃离方法的作用范围，则是**线程安全**的

   如果如果**局部变量引用了对象**，并逃离了方法的作用范围，则需要考虑线程安全问题



**栈内存溢出**

error：**StackOverflowError**

发生原因

- 虚拟机中，栈帧存放过多（无限递归）
- 每个栈帧占用过大





## 本地方法栈

一些带有**native关键字**的方法需要JAVA去调用c/c++的方法，因为java有时候没法直接与操作系统进行交互



## 堆（Heap）

通过new创建的对象 都会放在堆中



**特点**

- 线程共享，堆中对象都需要考虑线程安全的问题
- 有垃圾回收机制



**堆空间溢出** ：java.lang.OutOfMemoryError: Java heap space

**堆内存诊断**

- jps   查看系统中有哪些java进程
- jmap   查看堆内存占用情况   jmap -head  【进程id】
- jconsole   图像工具
- jvirsalvm  虚拟机可视化工具



![image-20210328163951663](img\3.png)



## 方法区

### 结构

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150547.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150547.png)

特点

- 线程共享
- 存有类相关的信息（类的信息、常量、静态变量,即时编译器编译后的代码等）
- 虚拟机被启动时创建
- 包含运行时常量池



**方法区内存溢出**

- 1.8 以前会导致永久代内存溢出
- 1.8 之后会导致元空间内存溢出



**元空间**：

- 1.8之后出现的概念    
- 元空间不是方法区 ，元空间是方法区的实现  
- 它存放与本地内存中



### **运行时常量池**

- 常量池
  - 就是一张表（如图中的constant pool），虚拟机指令根据这张常量表找到要执行的类名、方法名、参数类型、字面量信息
- 运行时常量池
  - 常量池是 *.class文件中的，当该**类被加载以后**，它的常量池信息就会**放入运行时常量池**，并把里面的**符号地址变为真实地址**

```java
//二进制字节码（类基本信息，常量池，类方法定义，包含虚拟机指令）
public class StackTest2 {
    public static void main(String[] args) {
        System.out.println("hello world");
    }
}
```

查看常量池

```java
javap -v  类名.class   //编译class类
```

![image-20210328175530913](img\5.png)

![image-20210328180753839](img\4.png)





查看运行时常量池

```java
//二进制字节码（类基本信息，常量池，类方法定义，包含虚拟机指令）
public class StackTest2 {
    public static void main(String[] args) {
        String s1="a";
        String s2="b";
        String s3="a"+"b";
        String s4=s1+s2;
        String s5="ab";
        String s6=s4.intern();

        System.out.println(s3==s4); //false
        System.out.println(s3==s5); //true
        System.out.println(s3==s6); //true

        String x2=new String("c")+new String("d");
        String x1="cd";
        System.out.println(x1==x2); //false;
    }
}
```

- 常量池（StringTable） ： 包含 a,b,ab

- 由于s3是常量相运算，所以直接得出ab的结果

- 而s4是变量相运算，需要转化为StringBuilder对象再调用append方法

- ##### intern方法 1.8

  调用字符串对象的intern方法，会将该字符串对象尝试放入到串池中

  - 如果串池中没有该字符串对象，则放入成功
  - 如果有该字符串对象，则放入失败

  无论放入是否成功，都会返回**串池中**的字符串对象

  **注意**：此时如果调用intern方法成功，堆内存与串池中的字符串对象是同一个对象；如果失败，则不是同一个对象





![image-20210329141326112](img\6.png)





# 垃圾回收



## 判断回收的方法



### **引用计数法**

- 当程序在引用该对象时，计数便会加一，不再引用时便会减一
- 当该对象计数为0时，便可以回收



弊端：循环引用时，两个对象的计数都为1，导致两个对象都无法被释放



考虑如下代码：

```java
class A {
    private B b;
    public void setB(B b) {
        this.b = b;
    }
}

class B {
    private A a = new A();
    public void setA(A a) {
        this.a = a;
    }
}

public void method() {
    A a = new A();
    B b = new B();
    a.setB(b);
    b.setA(a);
}
```

其内存图示如下

![img](https://pic1.zhimg.com/80/v2-3663b4c2a7c8eb93e9e19da45dc31ac8_720w.jpg)





### 可达性分析算法

- JVM中的垃圾回收器通过**可达性分析**来探索所有存活的对象
- 扫描堆中的对象，看能否沿着**GC Root对象**为起点的引用链找到该对象，如果**找不到，则表示可以回收**
- 可以作为GC Root的对象
  - 虚拟机栈（栈帧中的本地变量表）中引用的对象。　
  - 方法区中类静态属性引用的对象
  - 方法区中常量引用的对象
  - 本地方法栈中JNI（即一般说的Native方法）引用的对象



表示arraylist为 **gcRoot可以找到对象**

![image-20210330170531140](img\7.png)





### 五种引用

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150800.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150800.png)



#### 强引用

- 通过new出来的对象 为强引用   **Object object=new Object();**
- 只有GC Root**都不引用**该对象时，才会回收**强引用**对象

> 强引用对象什么时候失效

1. 生命周期结束 （作用域失效）

   ```java
   public void method(){
       Object o=new Object();
   }
   //方法执行结束后，强引用指向的Object对象 就会被回收
   ```

2. 置为null之后

   ```java
   Object=null
   ```

3. 除了上述两种情况外，其他情况Gc**都不会回收**强引用对象

代码

```java
虚拟机参数：  -Xmx20m  -XX:+PrintGCDetails -verbose:gc     堆空间设置20M
public static void main(String[] args) throws IOException {
    List<byte[]> list=new ArrayList<>();
    for (int i = 0; i < 5; i++) {
        System.out.println(i);
        list.add(new byte[4*1024*1024]);
    }
}
```





#### 软引用 （SoftReference）



当GC Root指向软引用对象时

1. 在**JVM内存不足时**，会**回收软引用所引用的对象**
2. 生命周期结束 （作用域失效）
3. 置为null之后



软引用**SoftReference类**

- 在当内存不够的时候，GC会回收SoftReference所引用的对象。所以，在memory sensitive的项目中将某些数据设置成SoftReference可以避免内存的溢出。
- 使用软引用对象 list和SoftReference是强引用，而SoftReference和byte数组则是软引用

```java
//        list->SoftReference->byte
List<SoftReference<byte[]>> list=new ArrayList<>();
for (int i=0;i<5; i++){
    SoftReference<byte[]> ref=new SoftReference<>(new byte[4*1024*1024]);
    System.out.println(ref.get());
    list.add(ref);
    System.out.println(list.size());
}
System.out.println("循环结束：" + list.size());
for (SoftReference<byte[]> ref : list) {
    System.out.println(ref.get());
}
```

回收视图

![image-20210330183749300](img\8.png)



#### 弱引用 （WeakReference）

当GC Root指向软引用对象时，在**内存不足时**，会**回收软引用所引用的对象**

- list->WeakReference->byte
- 通过list强引用**WeakReference**，然后利用WeakReference弱引用byte ，当内存不足时 垃圾回收清空弱引用的内存

```java
//        list->WeakReference->byte
        List<WeakReference<byte[]>> list=new ArrayList<>();
        for (int i=0;i<5; i++){
            WeakReference<byte[]> ref=new WeakReference<>(new byte[4*1024*1024]);
            System.out.println(ref.get());
            list.add(ref);
            System.out.println(list.size());
        }
        System.out.println("循环结束：" + list.size());
        for (WeakReference<byte[]> ref : list) {
            System.out.println(ref.get());
        }
    }
```









## 回收算法

### 标记-清除算法

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150813.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150813.png)

定义：标记清除算法顾名思义，是指在虚拟机的垃圾回收过程中，先采用标记算法确认可以回收的对象，然后垃圾收集器根据标识清除相应的内容，给堆内存清理出空间

- 清理空间不是字节清0，而是记录下内存的地址，下次分配内存时，直接进行**覆盖**内存

**缺点**：容易产生**大量的内存碎片**，过多的零碎的内存，无法满足内存的分配，虽然内存总和足够分配



### 标记-整理算法

- 用于老年代的清除

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150827.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150827.png)

标记清理算法 会将不被GC root引用的对象，清除其占用的内存空间，然后整理碎片的空间，可以有效的避免 **碎片内存过多的问题**

**缺点**：因为整体需要内存消耗时间，所以效率过低



### 复制算法

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150842.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150842.png)

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150856.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150856.png)

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150907.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150907.png)

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150919.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150919.png)



将内存分为等大小的两个区域，FROM和TO（TO中为空）。

先将被GC Root引用的对象从FROM放入TO中，再回收不被GC Root引用的对象。然后交换FROM和TO。这样也可以避免内存碎片的问题，但是会占用双倍的内存空间。





## 分代回收

内存分布

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150931.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150931.png)

- 内存分为**新生代、老年代**
- 而新生代中又包含 **伊甸园、幸存区Form、幸存区To**



### 回收流程

- **Minor GC**：当新生代的内存不足时，清理新生代。
- **Full GC**：当整体的内存不足时 是清理整个堆空间—包括年轻代和老年代。

新创建的对象被放在的**新生代的伊甸园**中

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150939.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150939.png)



- 当伊甸园的内存不足时，会进行一次垃圾回收，叫做 **Minor GC**
- Minor GC 会将**伊甸园和幸存区FROM**存活的对象**先**复制到 **幸存区 TO**中， 并让其**寿命加1**，再**交换两个幸存区**，使from装有内存，To为空

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150946.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150946.png)

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150955.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608150955.png)

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151002.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151002.png)

再次创建对象，若新生代的伊甸园又满了，则会**再次触发 Minor GC**（会触发 **stop the world**， 暂停其他用户线程，只让垃圾回收线程工作），这时不仅会回收伊甸园中的垃圾，**还会回收幸存区中的垃圾**，再将活跃对象复制到幸存区TO中。回收以后会交换两个幸存区，并让幸存区中的对象**寿命加1**

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151010.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151010.png)

如果幸存区中的对象的**寿命超过某个阈值**（最大为15，4bit），就会被**放入老年代**中

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151018.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151018.png)

如果新生代老年代中的内存都满了，就会先触发Minor Gc，再触发**Full GC**，扫描**新生代和老年代中**所有不再使用的对象并回收







## 相关VM参数

| 含义               | 参数                                                         |
| ------------------ | ------------------------------------------------------------ |
| 堆初始大小         | -Xms                                                         |
| 堆最大大小         | -Xmx 或 -XX:MaxHeapSize=size                                 |
| 新生代大小         | -Xmn 或 (-XX:NewSize=size + -XX:MaxNewSize=size )            |
| 幸存区比例（动态） | -XX:InitialSurvivorRatio=ratio 和 -XX:+UseAdaptiveSizePolicy |
| 幸存区比例         | -XX:SurvivorRatio=ratio                                      |
| 晋升阈值           | -XX:MaxTenuringThreshold=threshold                           |
| 晋升详情           | -XX:+PrintTenuringDistribution                               |
| GC详情             | -XX:+PrintGCDetails -verbose:gc                              |
| FullGC 前 MinorGC  | -XX:+ScavengeBeforeFullGC                                    |

- 幸存区比例：**伊甸园在新生代中的比例** ，如果比例为8 ，新生代10M，分配为  伊甸园8M，幸存者From、幸存者To 为各1M

- 晋升阈值：幸存者Form 转移到 老年代 的条件次数

  





## 代码分析

```java
//虚拟机指令：-Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails -verbose:gc -XX:-ScavengeBeforeFullGC
public class GcTest {
    private static final int _512Kb=512*1024;
    private static final int _1Mb=1024*1024;
    private static final int _6Mb=6*1024*1024;
    private static final int _7Mb=7*1024*1024;
    private static final int _8Mb=8*1024*1024;
    private static final int _10Mb=10*1024*1024;

    // -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails -verbose:gc -XX:-ScavengeBeforeFullGC
    public static void main(String[] args) {
    }

}
```

![image-20210401192458517](img\9.png)



**放置7M内存**

```java
// -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails -verbose:gc -XX:-ScavengeBeforeFullGC
public static void main(String[] args) {
    byte[] bytes = new byte[_7Mb];
}
```

![image-20210401194755159](img\10.png)

- 7M放置在伊甸园



**放置8M内存**

![image-20210401194937180](img\11.png)

**大对象处理**：当遇到较大的对象时，就算伊甸园为空也无法容纳该对象时，会直接晋升老年代



![image-20210401195222079](img\12.png)

当MinorGC和 FullGC都不够清楚内存时，会报**OOM异常** 提示超过堆内存



![image-20210401195747438](img\13.png)

线程内存溢出

- 某个线程的内存溢出而抛异常（out of memory），不会让其他线程结束
- 这是因为当一个线程**抛出OOM异常后，它所占据的内存资源会被全部释放掉**，从而不会影响其他线程的运行，**进程依然正常**





## 垃圾回收器

### 串行

- 单线程
- 内存较小，个人电脑（CPU核数较少）

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151027.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151027.png)

**安全点：**让其他线程在这个点停下来，以免垃圾回收时移动对象地址，使其他线程收到影响

因为是串行的，所以只有一个垃圾回收线程。且在该线程执行回收工作时，其他线程进入**阻塞**状态



#### Serial 收集器

Serial收集器是最基本的、发展历史最悠久的收集器

**特点：**单线程、简单高效（与其他收集器的单线程相比），采用**复制算法**。对于限定单个CPU的环境来说，Serial收集器由于没有线程交互的开销，专心做垃圾收集自然可以获得最高的单线程手机效率。收集器进行垃圾回收时，必须暂停其他所有的工作线程，直到它结束（Stop The World）



#### ParNew 收集器

ParNew收集器其实就是Serial收集器的多线程版本

**特点**：多线程、ParNew收集器默认开启的收集线程数与CPU的数量相同，在CPU非常多的环境中，可以使用-XX:ParallelGCThreads参数来限制垃圾收集的线程数。和Serial收集器一样存在Stop The World问题



#### Serial Old 收集器

Serial Old是Serial收集器的老年代版本

**特点**：同样是单线程收集器，采用**标记-整理算法**



------



### 吞吐量优先

- 多线程
- 堆内存较大，多核CPU
- 单位时间内，STW（stop the world，停掉其他所有工作线程）**总时间最短**
- **JDK1.8默认使用**的垃圾回收器

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151039.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151039.png)

#### Parallel Scavenge 收集器

与吞吐量关系密切，故也称为吞吐量优先收集器

**特点**：属于新生代收集器也是采用**复制算法**的收集器（用到了新生代的幸存区），又是并行的多线程收集器（与ParNew收集器类似）

该收集器的目标是达到一个可控制的吞吐量。还有一个值得关注的点是：**GC自适应调节策略**（与ParNew收集器最重要的一个区别）

**GC自适应调节策略**：Parallel Scavenge收集器可设置-XX:+UseAdptiveSizePolicy参数。当开关打开时**不需要**手动指定新生代的大小（-Xmn）、Eden与Survivor区的比例（-XX:SurvivorRation）、晋升老年代的对象年龄（-XX:PretenureSizeThreshold）等，虚拟机会根据系统的运行状况收集性能监控信息，动态设置这些参数以提供最优的停顿时间和最高的吞吐量，这种调节方式称为GC的自适应调节策略。

Parallel Scavenge收集器使用两个参数控制吞吐量：

- XX:MaxGCPauseMillis 控制最大的垃圾收集停顿时间
- XX:GCRatio 直接设置吞吐量的大小



#### **Parallel Old 收集器**

是Parallel Scavenge收集器的老年代版本

**特点**：多线程，采用**标记-整理算法**（老年代没有幸存区）





### 响应时间优先

- 多线程
- 堆内存较大，多核CPU
- 尽可能让**单次**STW (**清理垃圾的时间**) 变短（尽量不影响其他线程运行）

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151052.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151052.png)

#### CMS 收集器

Concurrent Mark Sweep，一种以获取**最短回收停顿时间**为目标的**老年代**收集器

**特点**：基于**标记-清除算法**实现。并发收集、低停顿，但是会产生内存碎片

**应用场景**：适用于注重服务的响应速度，希望系统停顿时间最短，给用户带来更好的体验等场景下。如web程序、b/s服务

**CMS收集器的运行过程分为下列4步：**

**初始标记**：标记GC Roots能直接到的对象。速度很快但是**仍存在Stop The World问题**

**并发标记**：进行GC Roots Tracing 的过程，找出存活对象且用户线程可并发执行

**重新标记**：为了**修正并发标记期间**因用户程序继续运行而导致标记产生变动的那一部分对象的标记记录。仍然存在Stop The World问题

**并发清除**：对标记的对象进行清除回收

CMS收集器的内存回收过程是与用户线程一起**并发执行**的





### G1回收器

**定义**：

Garbage First

JDK 9以后默认使用，而且替代了CMS 收集器

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200909201212.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200909201212.png)

**适用场景**

- 同时注重吞吐量和低延迟（响应时间）
- 超大堆内存（内存大的），会将堆内存划分为多个**大小相等**的区域
- 整体上是**标记-整理**算法，两个区域之间是**复制**算法





### 不同收集器的MinorGC、FullGc



# 类加载与字节码



```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello world");
    }
}
```

以下是字节码文件

```
0000000 ca fe ba be 00 00 00 34 00 23 0a 00 06 00 15 09 
0000020 00 16 00 17 08 00 18 0a 00 19 00 1a 07 00 1b 07 
0000040 00 1c 01 00 06 3c 69 6e 69 74 3e 01 00 03 28 29 
0000060 56 01 00 04 43 6f 64 65 01 00 0f 4c 69 6e 65 4e 
0000100 75 6d 62 65 72 54 61 62 6c 65 01 00 12 4c 6f 63 
0000120 61 6c 56 61 72 69 61 62 6c 65 54 61 62 6c 65 01 
0000140 00 04 74 68 69 73 01 00 1d 4c 63 6e 2f 69 74 63 
0000160 61 73 74 2f 6a 76 6d 2f 74 35 2f 48 65 6c 6c 6f 
0000200 57 6f 72 6c 64 3b 01 00 04 6d 61 69 6e 01 00 16 
0000220 28 5b 4c 6a 61 76 61 2f 6c 61 6e 67 2f 53 74 72 
0000240 69 6e 67 3b 29 56 01 00 04 61 72 67 73 01 00 13 
0000260 5b 4c 6a 61 76 61 2f 6c 61 6e 67 2f 53 74 72 69 
0000300 6e 67 3b 01 00 10 4d 65 74 68 6f 64 50 61 72 61 
0000320 6d 65 74 65 72 73 01 00 0a 53 6f 75 72 63 65 46 
0000340 69 6c 65 01 00 0f 48 65 6c 6c 6f 57 6f 72 6c 64
0000360 2e 6a 61 76 61 0c 00 07 00 08 07 00 1d 0c 00 1e 
0000400 00 1f 01 00 0b 68 65 6c 6c 6f 20 77 6f 72 6c 64 
0000420 07 00 20 0c 00 21 00 22 01 00 1b 63 6e 2f 69 74 
0000440 63 61 73 74 2f 6a 76 6d 2f 74 35 2f 48 65 6c 6c 
0000460 6f 57 6f 72 6c 64 01 00 10 6a 61 76 61 2f 6c 61 
0000500 6e 67 2f 4f 62 6a 65 63 74 01 00 10 6a 61 76 61 
0000520 2f 6c 61 6e 67 2f 53 79 73 74 65 6d 01 00 03 6f 
0000540 75 74 01 00 15 4c 6a 61 76 61 2f 69 6f 2f 50 72 
0000560 69 6e 74 53 74 72 65 61 6d 3b 01 00 13 6a 61 76 
0000600 61 2f 69 6f 2f 50 72 69 6e 74 53 74 72 65 61 6d 
0000620 01 00 07 70 72 69 6e 74 6c 6e 01 00 15 28 4c 6a 
0000640 61 76 61 2f 6c 61 6e 67 2f 53 74 72 69 6e 67 3b 
0000660 29 56 00 21 00 05 00 06 00 00 00 00 00 02 00 01 
0000700 00 07 00 08 00 01 00 09 00 00 00 2f 00 01 00 01 
0000720 00 00 00 05 2a b7 00 01 b1 00 00 00 02 00 0a 00 
0000740 00 00 06 00 01 00 00 00 04 00 0b 00 00 00 0c 00 
0000760 01 00 00 00 05 00 0c 00 0d 00 00 00 09 00 0e 00 
0001000 0f 00 02 00 09 00 00 00 37 00 02 00 01 00 00 00 
0001020 09 b2 00 02 12 03 b6 00 04 b1 00 00 00 02 00 0a 
0001040 00 00 00 0a 00 02 00 00 00 06 00 08 00 07 00 0b 
0001060 00 00 00 0c 00 01 00 00 00 09 00 10 00 11 00 00 
0001100 00 12 00 00 00 05 01 00 10 00 00 00 01 00 13 00 
0001120 00 00 02 00 14Copy
```

根据 JVM 规范，**类文件结构**如下

```
u4 			 magic
u2             minor_version;    
u2             major_version;    
u2             constant_pool_count;    
cp_info        constant_pool[constant_pool_count-1];    
u2             access_flags;    
u2             this_class;    
u2             super_class;   
u2             interfaces_count;    
u2             interfaces[interfaces_count];   
u2             fields_count;    
field_info     fields[fields_count];   
u2             methods_count;    
method_info    methods[methods_count];    
u2             attributes_count;    
attribute_info attributes[attributes_count];
```



## javap工具



使用 **javap -v HelloWorld.class** 编译class文件

```java
D:\work\JAVA\Project_javaSEplus\JVM\src>javap -v   com.jie.clas.HelloWorld
Classfile /D:/work/JAVA/Project_javaSEplus/JVM/src/com/jie/clas/HelloWorld.class
  Last modified 2021-4-4; size 438 bytes
  MD5 checksum f26608fe11843e89d265a1b4e7a42c86
  Compiled from "HelloWorld.java"            
public class com.jie.clas.HelloWorld
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #6.#15         // java/lang/Object."<init>":()V
   #2 = Fieldref           #16.#17        // java/lang/System.out:Ljava/io/PrintStream;
   #3 = String             #18            // Hello world
   #4 = Methodref          #19.#20        // java/io/PrintStream.println:(Ljava/lang/String;)V
   #5 = Class              #21            // com/jie/clas/HelloWorld
   #6 = Class              #22            // java/lang/Object
   #7 = Utf8               <init>
   #8 = Utf8               ()V
   #9 = Utf8               Code
  #10 = Utf8               LineNumberTable
  #11 = Utf8               main
  #12 = Utf8               ([Ljava/lang/String;)V
  #13 = Utf8               SourceFile
  #14 = Utf8               HelloWorld.java
  #15 = NameAndType        #7:#8          // "<init>":()V
  #16 = Class              #23            // java/lang/System
  #17 = NameAndType        #24:#25        // out:Ljava/io/PrintStream;
  #18 = Utf8               Hello world
  #19 = Class              #26            // java/io/PrintStream
  #20 = NameAndType        #27:#28        // println:(Ljava/lang/String;)V
  #21 = Utf8               com/jie/clas/HelloWorld
  #22 = Utf8               java/lang/Object
  #23 = Utf8               java/lang/System
  #24 = Utf8               out
  #25 = Utf8               Ljava/io/PrintStream;
  #26 = Utf8               java/io/PrintStream
  #27 = Utf8               println
  #28 = Utf8               (Ljava/lang/String;)V
{
  public com.jie.clas.HelloWorld();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 3: 0

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=1, args_size=1
         0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #3                  // String Hello world
         5: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         8: return
      LineNumberTable:
        line 5: 0
        line 6: 8
}
SourceFile: "HelloWorld.java"
```



## 字节码指令



### 图解方法执行流程

代码

```java
public class Demo3_1 {    
	public static void main(String[] args) {        
		int a = 10;        
		int b = Short.MAX_VALUE + 1;        
		int c = a + b;        
		System.out.println(c);   
    } 
}
```

```java
D:\work\JAVA\Project_javaSEplus\JVM\src\com\jie\clas>javap -v HelloWorld.class
Classfile /D:/work/JAVA/Project_javaSEplus/JVM/src/com/jie/clas/HelloWorld.class
  Last modified 2021-4-4; size 451 bytes
  MD5 checksum 9259d5a0b5aea880b7554ff9bb75d5cf
  Compiled from "HelloWorld.java"
public class com.jie.clas.HelloWorld
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #7.#16         // java/lang/Object."<init>":()V
   #2 = Class              #17            // java/lang/Short
   #3 = Integer            32768
   #4 = Fieldref           #18.#19        // java/lang/System.out:Ljava/io/PrintStream;
   #5 = Methodref          #20.#21        // java/io/PrintStream.println:(I)V
   #6 = Class              #22            // com/jie/clas/HelloWorld
   #7 = Class              #23            // java/lang/Object
   #8 = Utf8               <init>
   #9 = Utf8               ()V
  #10 = Utf8               Code
  #11 = Utf8               LineNumberTable
  #12 = Utf8               main
  #13 = Utf8               ([Ljava/lang/String;)V
  #14 = Utf8               SourceFile
  #15 = Utf8               HelloWorld.java
  #16 = NameAndType        #8:#9          // "<init>":()V
  #17 = Utf8               java/lang/Short
  #18 = Class              #24            // java/lang/System
  #19 = NameAndType        #25:#26        // out:Ljava/io/PrintStream;
  #20 = Class              #27            // java/io/PrintStream
  #21 = NameAndType        #28:#29        // println:(I)V
  #22 = Utf8               com/jie/clas/HelloWorld
  #23 = Utf8               java/lang/Object
  #24 = Utf8               java/lang/System
  #25 = Utf8               out
  #26 = Utf8               Ljava/io/PrintStream;
  #27 = Utf8               java/io/PrintStream
  #28 = Utf8               println
  #29 = Utf8               (I)V
{
  public com.jie.clas.HelloWorld();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 3: 0

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=4, args_size=1
         0: bipush        10
         2: istore_1
         3: ldc           #3                  // int 32768
         5: istore_2
         6: iload_1
         7: iload_2
         8: iadd
         9: istore_3
        10: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
        13: iload_3
        14: invokevirtual #5                  // Method java/io/PrintStream.println:(I)V
        17: return
      LineNumberTable:
        line 5: 0
        line 6: 3
        line 7: 6
        line 8: 10
        line 9: 17
}
SourceFile: "HelloWorld.java"
```



**常量池载入运行时常量池**

常量池也属于方法区，只不过这里单独提出来了

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151317.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151317.png)

**方法字节码载入方法区**

（stack=2，locals=4） 对应操作数栈有2个空间（每个空间4个字节），局部变量表中有4个槽位

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151325.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151325.png)



> **执行引擎开始执行方法区字节码**



**bipush 10**

- 将一个 byte 压入操作数栈

  （其长度会补齐 4 个字节），类似的指令还有

  - sipush 将一个 short 压入操作数栈（其长度会补齐 4 个字节）
  - ldc 将一个 int 压入操作数栈
  - ldc2_w 将一个 long 压入操作数栈（**分两次压入**，因为 long 是 8 个字节）
  - 这里小的数字都是和字节码指令存在一起，**超过 short 范围的数字存入了常量池**

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151336.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151336.png)



**istore 1**

将操作数栈栈顶元素弹出，放入局部变量的表中 slot 1

对应代码中的

```java
a = 10
```

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151346.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151346.png)



**ldc #3**

读取运行时常量池中#3，即32768(超过short最大值范围的数会被放到运行时常量池中)，将其加载到操作数栈中

注意 Short.MAX_VALUE 是 32767，所以 32768 = Short.MAX_VALUE + 1 实际是在编译期间计算好的

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151421.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151421.png)



**istore 2**

将操作数栈中的元素弹出，放到局部变量表的2号位置

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151432.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151432.png)



**iload1 iload2**

将局部变量表中1号位置和2号位置的元素放入操作数栈中

- 因为只能在操作数栈中执行运算操作

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151450.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151450.png)

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151459.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151459.png)



**iadd**

将操作数栈中的两个元素**弹出栈**并相加，结果在压入操作数栈中

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151508.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151508.png)

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151523.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151523.png)



**istore 3**

将操作数栈中的元素弹出，放入局部变量表的3号位置

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151547.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151547.png)

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151555.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151555.png)



**getstatic #4**

在运行时常量池中找到#4，发现是一个对象

在堆内存中找到该对象，并将其**引用**放入操作数栈中

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151605.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151605.png)

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151613.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151613.png)

**iload 3**

将局部变量表中3号位置的元素压入操作数栈中

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151624.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151624.png)

**invokevirtual 5**

找到常量池 #5 项，定位到方法区 java/io/PrintStream.println:(I)V 方法

生成新的栈帧（分配 locals、stack等）

传递参数，执行新栈帧中的字节码

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151632.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151632.png)

执行完毕，弹出栈帧

清除 main 操作数栈内容

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151640.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200608151640.png)

**return**
完成 main 方法调用，弹出 main 栈帧，程序结束



### 分析 i++

```java
public class HelloWorld {
    public static void main(String[] args) {
        int a=10;
        int b=a++;
        System.out.println(b);
    }
}
```

```java
public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=3, args_size=1
         0: bipush        10
         2: istore_1
         3: iload_1
         4: iinc          1, 1
         7: istore_2
         8: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
        11: iload_2
        12: invokevirtual #3                  // Method java/io/PrintStream.println:(I)V
        15: return
      LineNumberTable:
        line 5: 0
        line 6: 3
        line 7: 8
        line 8: 15
}
SourceFile: "HelloWorld.java"
```

分析：

- 注意iinc指令是直接在局部变量slot上进行运算
- a++和++a的区别是**先执行iload**还是**先执行iinc**



### 判断

```java
public static void main(String[] args) {
    int a=2;
    if (a==1){
        System.out.println(1);
    }else{
        System.out.println(2);
    }
}
```

```java
    Code:
      stack=2, locals=2, args_size=1
         0: iconst_2
         1: istore_1
         2: iload_1
         3: iconst_1
         4: if_icmpne     17
         7: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
        10: iconst_1
        11: invokevirtual #3                  // Method java/io/PrintStream.println:(I)V
        14: goto          24
        17: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
        20: iconst_2
        21: invokevirtual #3                  // Method java/io/PrintStream.println:(I)V
        24: return
```



### 循环

```java
    public static void main(String[] args) {
        int a = 0;
        while (a < 10) {
            a++;
        }
    }
```

```java    Code:
      stack=2, locals=2, args_size=1
         0: iconst_0
         1: istore_1
         2: iload_1
         3: bipush        10
         5: if_icmpge     14
         8: iinc          1, 1
        11: goto          2
        14: return
```



### 构造方法

> **cinit()V**

```java
public class Demo3 {
	static int i = 10;

	static {
		i = 20;
	}

	static {
		i = 30;
	}

	public static void main(String[] args) {
		System.out.println(i); //结果为30
	}
}
```

编译器会按**从上至下**的顺序，收集所有 static 静态代码块和静态成员赋值的代码，**合并**为一个特殊的方法 cinit()V ：

```java
stack=1, locals=0, args_size=0
         0: bipush        10
         2: putstatic     #3                  // Field i:I
         5: bipush        20
         7: putstatic     #3                  // Field i:I
        10: bipush        30
        12: putstatic     #3                  // Field i:I
        15: return
```



> **init()V**

```java
public class Demo4 {
	private String a = "s1";

	{
		b = 20;
	}

	private int b = 10;

	{
		a = "s2";
	}

	public Demo4(String a, int b) {
		this.a = a;
		this.b = b;
	}

	public static void main(String[] args) {
		Demo4 d = new Demo4("s3", 30);
		System.out.println(d.a);
		System.out.println(d.b);
	}
}
```

编译器会按**从上至下**的顺序，收集所有 {} 代码块和成员变量赋值的代码，**形成新的构造方法**，但**原始构造方法**内的代码**总是在后**

```java
Code:
     stack=2, locals=3, args_size=3
        0: aload_0
        1: invokespecial #1                  // Method java/lang/Object."<init>":()V
        4: aload_0
        5: ldc           #2                  // String s1
        7: putfield      #3                  // Field a:Ljava/lang/String;
       10: aload_0
       11: bipush        20
       13: putfield      #4                  // Field b:I
       16: aload_0
       17: bipush        10
       19: putfield      #4                  // Field b:I
       22: aload_0
       23: ldc           #5                  // String s2
       25: putfield      #3                  // Field a:Ljava/lang/String;
       //原始构造方法在最后执行
       28: aload_0
       29: aload_1
       30: putfield      #3                  // Field a:Ljava/lang/String;
       33: aload_0
       34: iload_2
       35: putfield      #4                  // Field b:I
       38: return
```



### 方法调用

```java
public class HelloWorld {
    public HelloWorld() {

    }

    private void test1() {

    }

    private final void test2() {

    }

    public void test3() {

    }

    public static void test4() {

    }

    public static void main(String[] args) {
        HelloWorld demo5 = new HelloWorld();
        demo5.test1();
        demo5.test2();
        demo5.test3();
        HelloWorld.test4();
    }
}
```

字节码

```bash
Code:
      stack=2, locals=2, args_size=1
         0: new           #2                  // class com/jie/clas/HelloWorld
         3: dup
         4: invokespecial #3                  // Method "<init>":()V
         7: astore_1
         8: aload_1
         9: invokespecial #4                  // Method test1:()V
        12: aload_1
        13: invokespecial #5                  // Method test2:()V
        16: aload_1
        17: invokevirtual #6                  // Method test3:()V
        20: invokestatic  #7                  // Method test4:()V
        23: return
```

- 私有、构造、被final修饰的方法，在调用时都使用**invokespecial**指令
- 普通成员方法在调用时，使用**invokespecial**指令。因为编译期间无法确定该方法的内容，只有在运行期间才能确定
- 静态方法在调用时使用**invokestatic**指令



**分析**

- new 是创建【对象】，给对象分配堆内存，执行成功会将【**对象引用**】压入操作数栈
- dup 是赋值操作数栈栈顶的内容，本例即为【**对象引用**】，为什么需要两份引用呢，一个是要配合 invokespecial 调用该对象的构造方法 “init”:()V （会消耗掉栈顶一个引用），另一个要 配合 astore_1 赋值给局部变量
- 终方法（ﬁnal），私有方法（private），构造方法都是由 invokespecial 指令来调用，属于静态绑定
- 普通成员方法是由 invokevirtual 调用，属于**动态绑定**，即支持多态 成员方法与静态方法调用的另一个区别是，执行方法前是否需要【对象引用】







### 异常

```java
public static void main(String[] args) {
    int i=0;
    try {
        i=10;
    }catch (Exception e){
        i=20;
    }finally {
        i=30;
    }
}
```

```java
 Code:
      stack=1, locals=4, args_size=1
         0: iconst_0
         1: istore_1
         2: bipush        10
         4: istore_1
         5: bipush        30
         7: istore_1
         8: goto          27  //跳转
        11: astore_2
        12: bipush        20
        14: istore_1
        15: bipush        30
        17: istore_1
        18: goto          27
        21: astore_3
        22: bipush        30
        24: istore_1
        25: aload_3
        26: athrow
        27: return
      Exception table:          //异常监控
         from    to  target type
             2     5    11   Class java/lang/Exception
             2     5    21   any
            11    15    21   any
```







## 类加载阶段

### 加载

- 将类的字节码载入**方法区**（1.8后为元空间，在本地内存中）中，内部采用 C++ 的 instanceKlass 描述 java 类，它的重要 ﬁeld 有：

  - _java_mirror 即 java 的类镜像，例如对 String 来说，它的镜像类就是 String.class，作用是把 klass 暴露给 java 使用
- _super 即父类
  - _ﬁelds 即成员变量
- _methods 即方法
  - _constants 即常量池
  - _class_loader 即类加载器
  - _vtable 虚方法表
  - _itable 接口方法
  
- 如果这个类还有父类没有加载，**先加载父类**

- 加载和链接可能是**交替运行**的



**类会加载到元空间中（方法区的实现）**

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200611205050.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200611205050.png)

 

### 链接

#### **验证**

验证类是否符合JVM规范，安全性检查



#### **准备**

为static变量分配空间，设置默认值

- 存储在？（方法区，还是堆）
- static变量在分配空间和赋值是在两个阶段完成的，**分配空间**在准备阶段完成，**赋值**在初始阶段完成
- 如果static是final的**基本类型，以及字符串常量**，那准备阶段就确定赋值了
- 如果static是final的**引用类型**，赋值仍然在初始化阶段



![image-20210405154213209](img\14.png)



如果static是final的**基本类型，以及字符串常量**，那准备阶段就确定赋值了

![image-20210405154624638](img\15.png)



如果static是final的**引用类型**，赋值仍然在初始化阶段

```java
public class HelloWorld {
    static int a;
    static final int b=1;
    static final Object o=new Object();  //仍然在初始化阶段才会赋值
}
```



#### 解析

- 将常量池中的**符号引用**解析为**直接引用**
- 解析之前，符号未分配在具体的内中，解析之后才分配了具体的内存

------



- 未解析时，常量池中的看到的对象仅是符号，未真正的存在于内存中

```java
public class Demo1 {
   public static void main(String[] args) throws IOException, ClassNotFoundException {
      ClassLoader loader = Demo1.class.getClassLoader();
      //只加载不解析
      Class<?> c = loader.loadClass("com.nyima.JVM.day8.C");
      //用于阻塞主线程
      System.in.read();
   }
}

class C {
   D d = new D();
}

class D {

}
```

- 打开HSDB
  - 可以看到此时只加载了类C

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200611223153.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200611223153.png)

查看类C的常量池，可以看到类D**未被解析**，只是存在于常量池中的符号

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200611230658.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200611230658.png)

------



- 解析以后，会将常量池中的符号引用解析为直接引用

```java
public class Demo1 {
   public static void main(String[] args) throws IOException, ClassNotFoundException {
      ClassLoader loader = Demo1.class.getClassLoader();
      //只加载不解析
      new C(); //解析
      //用于阻塞主线程
      System.in.read();
   }
}

class C {
   D d = new D();
}

class D {

}
```

- 可以看到，此时已加载并解析了类C和类D

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200611223441.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200611223441.png)

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200613104723.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200613104723.png)





### 初始化

初始化阶段就是在**执行类构造器clinit()方法的过程**，虚拟机会保证这个类的『构造方法』的线程安全

- clinit()方法是由编译器自动收集类中的所有类变量的**赋值动作和静态语句块**(static{}块)中的语句合并产生的



**发生的时机**

类的初始化**是懒惰的**，以下情况会初始化

- main方法所在的类，总会被首先初始化
- 首次访问这个类的**静态变量或静态方法**
- 子类初始化，如果父类还没初始化，会引发父类的初始化
- 子类访问父类的静态变量，只会触发父类的初始化
- Class.forName
- **new 会导致初始化**

一下情况不会导致初始化

- 访问类的**static final静态常量**（基本类型和字符串）
- 类对象.class不会触发初始化
- 创建该类对象的数组
- 类加载器的loadClass方法
- Class.forNamed的参数2为false时

**验证类是否被初始化，可以看该类的静态代码块是否被执行**



### 类加载器

- Java虚拟机团队设计团队有意把类加载阶段中的 "**通过一个类的全限定名来描述该类的二进制字节流**" 这个动作放到java虚拟机外部去实现，以便让应用程序自己决定如何去获取所需的类
- 实现这个动作的代码被称为 "**类加载器**"

对于任意一个类，都必须由加载它的**类加载器**和这个**类本身**一起共同确立其在Java虚拟机中的唯一性，每一个类加载器，都拥有一个独立的类名称空间。这句话可以表达得更通俗一些：**比较两个类是否“相等”，只有在这两个类是由同一个类加载器加载的前提下才有意义**，否则，即使这两个类来源于同一个Class文件，被同一个Java虚拟机加载，只要加载它们的类加载器不同，那这两个类就必定不相等



以JDK 8为例

| 名称                                      | 加载的类              | 说明                            |
| ----------------------------------------- | --------------------- | ------------------------------- |
| Bootstrap ClassLoader（启动类加载器）     | JAVA_HOME/jre/lib     | 无法直接访问                    |
| Extension ClassLoader(拓展类加载器)       | JAVA_HOME/jre/lib/ext | 上级为Bootstrap，**显示为null** |
| Application ClassLoader(应用程序类加载器) | classpath             | 上级为Extension                 |
| 自定义类加载器                            | 自定义                | 上级为Application               |





#### 启动类加载器

- **优先级最高的类加载器**
- 负责加载  **JAVA_HOME/jre/lib** 下的类



#### 扩展类加载器

- 优先级第二的类加载器
- 负责加载 **JAVA_HOME/jre/lib/ext**  下的类



#### 应用程序类加载器

- 优先级第三的类加载器
- 负责加载 **classpath** 下的类   一般是自定义类型



#### 双亲委派机制

所谓的双亲委派机制，就是指**调用类加载器的loadClass方法时的规则**

- 这里用 **上级委派** 来形容似乎更为合适，因为它们并没有继承关系
  - 首先查看这个类是否被加载过
  - 如果没有，就询问该类 **是否属于该加载器的上级加载**（**应用程序类加载器 ->扩展类加载器->启动类加载器**）
  - 如果该类不属于于上级类加载器加载   则再由下级加载器加载

loadClass源码

```java
protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    synchronized (getClassLoadingLock(name)) {
        // 首先查找该类是否已经被该类加载器加载过了
        Class<?> c = findLoadedClass(name);
        //如果没有被加载过
        if (c == null) {
            long t0 = System.nanoTime();
            try {
                //查看它是否有上级的类加载器
                if (parent != null) {
                    //如果有则调用上级的类加载器
                    c = parent.loadClass(name, false);
                } else {   
                    //如果等于null了（启动类加载器返回的就是null)   就是用启动类加载器 启动该类
                    c = findBootstrapClassOrNull(name);
                }
            } catch (ClassNotFoundException e) {
                //捕获异常，但不做任何处理
            }

            if (c == null) {
                //如果还是没有找到，先让拓展类加载器调用findClass方法去找到该类，如果还是没找到，就抛出异常
                //然后让应用类加载器去找classpath下找该类
                long t1 = System.nanoTime();
                c = findClass(name);

                // 记录时间
                sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                sun.misc.PerfCounter.getFindClasses().increment();
            }
        }
        if (resolve) {
            //解析该class
            resolveClass(c);
        }
        return c;
    }
}
```



#### 自定义类加载器

**使用场景**

- 想加载非classpath的其他路径下的类文件
- 通过接口来使用实现，希望解耦时，常用在框架设计
- 这些类希望予以隔离，不同应用的同名类都可以加载，不冲突，常见于 tomcat 容器

**步骤**

- 继承ClassLoader父类
- 要遵从双亲委派机制，重写 ﬁndClass 方法
  - 不是重写loadClass方法，否则不会走双亲委派机制
- 读取类文件的字节码
- 调用父类的 deﬁneClass 方法来加载类
- 使用者调用该类加载器的 loadClass 方法



### 运行时优化

当一段代码被**重复调用**时，jvm会对代码进行**运行时优化**

#### 分层编译

JVM 将执行状态分成了 5 个层次：

- 0层：解释执行，用解释器将字节码翻译为机器码44
- 1层：使用 C1 **即时编译器**编译执行（不带 proﬁling）
- 2层：使用 C1 即时编译器编译执行（带基本的profiling）
- 3层：使用 C1 即时编译器编译执行（带完全的profiling）
- 4层：使用 C2 即时编译器编译执行

proﬁling 是指在运行过程中收集一些程序执行状态的数据，例如【方法的调用次数】，【循环的 回边次数】等

> 即时编译器（JIT）与解释器的区别

- 解释器
  - 将字节码**解释**为机器码，下次即使遇到相同的字节码，仍会执行重复的解释
  - 是将字节码解释为针对所有平台都通用的机器码
- 即时编译器
  - 将一些字节码**编译**为机器码，**并存入 Code Cache**，下次遇到相同的代码，直接执行，无需再编译
  - 根据平台类型，生成平台特定的机器码

**对于大部分的不常用的代码，我们无需耗费时间将其编译成机器码，而是采取解释执行的方式运行**；另一方面，对于仅占据小部分的热点代码，我们则可以将其编译成机器码，以达到理想的运行速度。 执行效率上简单比较一下 Interpreter < C1 < C2，总的目标是发现热点代码（hotspot名称的由 来），并优化这些热点代码





#### 方法内联

**内联函数**

内联函数就是在程序编译时，编译器将程序中出现的内联函数的调用表达式用内联函数的函数体来直接进行替换

**JVM内联函数**

C++是否为内联函数由自己决定，Java由**编译器决定**。Java不支持直接声明为内联函数的，如果想让他内联，你只能够向编译器提出请求: 关键字**final修饰** 用来指明那个函数是希望被JVM内联的，如

```java
public final void doSomething() {  
        // to do something  
}
```

总的来说，一般的函数都不会被当做内联函数，只有声明了final后，编译器才会考虑是不是要把你的函数变成内联函数

JVM内建有许多运行时优化。首先**短方法**更利于JVM推断。流程更明显，作用域更短，副作用也更明显。如果是长方法JVM可能直接就跪了。

第二个原因则更重要：**方法内联**

如果JVM监测到一些**小方法被频繁的执行**，它会把方法的调用替换成方法体本身，如：

```java
private int add4(int x1, int x2, int x3, int x4) { 
		//这里调用了add2方法
        return add2(x1, x2) + add2(x3, x4);  
    }  

    private int add2(int x1, int x2) {  
        return x1 + x2;  
    }
```

方法调用被替换后

```java
private int add4(int x1, int x2, int x3, int x4) {  
    	//被替换为了方法本身
        return x1 + x2 + x3 + x4;  
    }
```

#### 反射优化

```java
public class Reflect1 {
   public static void foo() {
      System.out.println("foo...");
   }

   public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
      Method foo = Demo3.class.getMethod("foo");
      for(int i = 0; i<=16; i++) {
         foo.invoke(null);
      }
   }
}
```

foo.invoke 前面 0 ~ 15 次调用使用的是 MethodAccessor 的 NativeMethodAccessorImpl 实现

invoke方法源码

```java
@CallerSensitive
public Object invoke(Object obj, Object... args)
    throws IllegalAccessException, IllegalArgumentException,
       InvocationTargetException
{
    if (!override) {
        if (!Reflection.quickCheckMemberAccess(clazz, modifiers)) {
            Class<?> caller = Reflection.getCallerClass();
            checkAccess(caller, clazz, obj, modifiers);
        }
    }
    //MethodAccessor是一个接口，有3个实现类，其中有一个是抽象类
    MethodAccessor ma = methodAccessor;             // read volatile
    if (ma == null) {
        ma = acquireMethodAccessor();
    }
    return ma.invoke(obj, args);
}
```

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200614133554.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200614133554.png)

会由DelegatingMehodAccessorImpl去调用NativeMethodAccessorImpl

NativeMethodAccessorImpl源码

```java
class NativeMethodAccessorImpl extends MethodAccessorImpl {
    private final Method method;
    private DelegatingMethodAccessorImpl parent;
    private int numInvocations;

    NativeMethodAccessorImpl(Method var1) {
        this.method = var1;
    }
	
	//每次进行反射调用，会让numInvocation与ReflectionFactory.inflationThreshold的值（15）进行比较，并使使得numInvocation的值加一
	//如果numInvocation>ReflectionFactory.inflationThreshold，则会调用本地方法invoke0方法
    public Object invoke(Object var1, Object[] var2) throws IllegalArgumentException, InvocationTargetException {
        if (++this.numInvocations > ReflectionFactory.inflationThreshold() && !ReflectUtil.isVMAnonymousClass(this.method.getDeclaringClass())) {
            MethodAccessorImpl var3 = (MethodAccessorImpl)(new MethodAccessorGenerator()).generateMethod(this.method.getDeclaringClass(), this.method.getName(), this.method.getParameterTypes(), this.method.getReturnType(), this.method.getExceptionTypes(), this.method.getModifiers());
            this.parent.setDelegate(var3);
        }

        return invoke0(this.method, var1, var2);
    }

    void setParent(DelegatingMethodAccessorImpl var1) {
        this.parent = var1;
    }

    private static native Object invoke0(Method var0, Object var1, Object[] var2);
}
//ReflectionFactory.inflationThreshold()方法的返回值
private static int inflationThreshold = 15;
```

- 一开始if条件不满足，就会调用本地方法invoke0
- 随着numInvocation的增大，当它大于ReflectionFactory.inflationThreshold的值16时，就会本地方法访问器替换为一个运行时动态生成的访问器，来提高效率
  - 这时会从反射调用变为**正常调用**，即直接调用 Reflect1.foo()

[![img](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200614135011.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20200614135011.png)


