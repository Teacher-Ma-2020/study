# IO流

## 简介

- I/O是Input/Output的缩写，I/O技术是非常实用的技术，用于处理设备之间的数据传输。如读/写文件，网络通讯等。
- Java程序中，对于数据的输入/输出操作以“流(stream)”的方式进行。
- java.io包下提供了各种“流”类和接口，用以获取不同种类的数据，并通过标准的方法输入或输出数据。

●**输入input:**读取外部数据（磁盘、光盘等存储设备的数据）到程序（内存）中。
●**输出output:**将程序（内存)数据输出到磁盘、光盘等存储设备中。





## 分类

![image-20201202172405233](img\1.png)

![image-20201202175328590](img\2.png)

- 按操作数据单位不同分为:字节流(8 bit)，字符流(16 bit)

  - 字节流可用于**任何类型的对象**，包括二进制对象，
  - 而字符流**只能处理字符**或者字符串

- 按数据流的流向不同分为:输入流，输出流

  - 输出流**传输完毕**后，需要调用flush方法清空缓存
  - 也可以直接调用close()，**关闭**并且**清空缓存**

- 按流的角色的不同分为:**节点流，处理流**

  



**IO流体系**

![image-20201204162511382](D:\study\JAVASE(高阶)\IO流\img\6.png)





------



## File类

- 是指某**文件、目录**的**实例**，创建实例时，指定路径和文件名
  - 路径可以是绝对路径，也可以是相对路径
  - 如果只指定了文件名，则文件会默认创建在项目的根目录下

**常用方法**

```java
//创建file实例
File f1=new File("D:/log");
//判断是否有这个文件，或者目录
if (!f1.exists()){
    ~~~~
}

//以文件的格式创建
f1.createNewFile();
//以目录的格式创建
f1.mkdir();

//获取上一级路径
String parent = f1.getParent();
//获取绝对路径
String absolutePath = f1.getAbsolutePath();

//获取文件名                    

//删除文件
if (f1.exists()){
    f1.delete();
}

//获取文件名
System.out.println("文件名为: " + file.getName());

//判断是否是一个目录
System.out.println(file.isDirectory());

//判断是否是一个文件
System.out.println(file.isFile());

//获取文件的大小，单位为字节
System.out.println(file.length());

File f1=new File("D:");
//获取该目录下的所有子文件
File[] files = f1.listFiles();
for(File f:files){
    System.out.println(f.getName());
}
```







- File类实例通过**构造方法**传入指定的输入流或者输出流

```java
File file=new File("src/main/java/com/jie/io2/zcj.txt");
```



说明：

1. 如果**输入流**指定的file实例**没有在硬盘中找到**，那么会**报异常**
2. 如果**输出流**指定的file实例没有创建，则会创建此文件
3. 如果输出流有该文件，可以指定是，覆盖还是在末尾继续添加
   - 构造器是:**Filewriter(file,false) / Filewriter(file)**:  对原有有文件进行覆盖
   - 构造器是:**Filewriter(file,true)**:  不会对原有文件覆盖，而是在原来的文件上进行添加







## 字符流

- **字符流只能处理字符或者字符串**
- 不能读取图片、声音、视频等，只能处理字符文件



### FilerReader

> 操作

1. 创建**file类**的实例

2. 创建**FileReader流的实例**

3. 读入的**操作**

4. **资源的关闭**

   

> **将hello.txt 读入程序并且输出**



**通过数组依次输出**

```java
@Test
public void testFileTest2(){
    //1.file类的实例
    File file=new File("hello.txt");

    //2.FileReader流的实例
    FileReader fr=null;
    try {
        fr=new FileReader(file);

        //3.读入的操作
        char[] chars=new char[5];
        int len;
        while ((len=fr.read(chars))!=-1){
            for (int i = 0; i < len; i++) {
                System.out.print(chars[i]);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }finally {
        try {
            //4.资源的关闭
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

说明点:

- **字符流**使用数组读取时，需要用到**Char[]数组**，字节流才是Byte[]数组
- 异常的处理:为了保证流资源**一定执行关闭**操作。需要使用try-catch-finally处理
- 指定的File文件如果**不存在**，输入流则会报异常
  - 如果输出流指定的文件不存在，则会创建此文件



#### read方法说明

- 调用read方法如果是使用的read(),会返回**该次读取的Ascll码值**，当读到结尾时，会返回-1，**读取速度很慢**

  - ```java
    int data1;
    while ((data1=fr.read())!=-1){
        System.out.print((char)data1);
    }
    ```

- 如果使用的**read方法传入数组**时，会返回该次读取的长度，效率会增加

  - 读取的数据会存储在数组中

  - ```java
    byte []num=new byte[1024];
    int len;
    while ((len=fileReader.read(num))!=-1){
        for (int i = 0; i < len; i++) {
            System.out.print(num[len]);
        }
    }
    ```





### FileWriter

说明:

1.写出操作，对应的File可以不存在的。并不会报异常

2.File对应的硬盘中的文件如果不存在:

- **在输出的过程中，会自动创建此文件FiLe对应的硬盘中的文件**

如果存在:

- 如果流使用的构造器是:**Filewriter(file,false) / Filewriter(file)**:对原有有文件进行覆盖
- 如果流使用的构造器是:**Filewriter(file,true)**:不会对原有文件覆盖，而是在原来的文件上进行添加

```java
@Test
public void testFileWriter() throws IOException {
    //1.file类的实例
    File file=new File("hello.txt");
    //fileWriter对象 用于文件的写出
    FileWriter fw=new FileWriter(file,true);
    //写出
    fw.write("芜湖\n");
    fw.write("起飞\n");
    //
    fw.close();
}
```



### **复制文本文件**

代码如下：

```java
@Test
public void testFileCopy(){
     //1.创建file
    File file=new File("泛型.md");
    File file_copy=new File("泛型_copy.md");


    FileReader fr=null;
    FileWriter fw=null;
    try {
        fr=new FileReader(file);
        fw=new FileWriter(file_copy);
        char []num=new char[5];
        int len;
        while((len=fr.read(num))!=-1){
            fw.write(num,0,len);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }finally {
        try {
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```





## 字节流

- **字节流可用于任何类型的对象，包括二进制对象**
- 字节流使用数组读取时，需要使用Byte[]  数组，而字符流使用的Char[]



### FileInputStream 

- 用于读取硬盘中的任何文件，将其加载到内存中（程序中）

- 传入File文件，对其进行操作

  

**代码如下**：

```java
File file=new File("src/main/java/com/jie/io2/zcj.txt");  //创建file实例
FileInputStream inputStream= null;
try {
    inputStream = new FileInputStream(file);
    byte []num=new byte[1024];
    int len;
    while ((len=inputStream.read(num))!=-1){
        System.out.print(new String(num,0,len));
    }
} catch (IOException e) {
    e.printStackTrace();
}finally {
    try {
        inputStream.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```



FileInputStream类的其他方法   (了解即可)

1. int **available()**   返回流当中剩余的没有读到的字节数量   
2. long **skip(long n)**  跳过几个字节不读



使用字节流读取文本文件，可能会出现乱码

```java
@Test
public void FileInputTest() throws IOException {
    //文件
    File file=new File("hello.txt");
    //字符流
    FileInputStream inputStream=new FileInputStream(file);
    //读文件
    byte[]buffer=new byte[10];
    int len;
    while ((len=inputStream.read(buffer))!=-1){
        String str=new String(buffer,0,len);
        System.out.print(str);
    }
    inputStream.close();
}
```



![image-20201203142411474](img\3.png)





### FileOutputStream

- **字节输出流**

- 负责写 从内存到硬盘

- 可以选择是覆盖，还是在末尾添加

  - ```java
    outputStream=new FileOutputStream(file_copy); //覆盖
    outputStream=new FileOutputStream(file_copy,true);//末尾添加
    ```

> **字节流复制文件**

```java
public static void main(String[] args) {
    //指定被复制文件
    File file=new File("泛型.md");
    //指定复制文件
    File file_copy=new File("src/main/java/com/jie/io2/泛型_copy.md");
    FileInputStream inputStream= null;
    FileOutputStream outputStream=null;
    try {
        inputStream = new FileInputStream(file);
        outputStream=new FileOutputStream(file_copy);
        //创建字节数组
        byte []num=new byte[1024];
        int len;
        //读取
        while ((len=inputStream.read(num))!=-1){
            //书写
            outputStream.write(num,0,len);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }finally {
        //分开关闭  防止其中一个关闭了，另一个无法关闭
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```


------



## 缓冲流

也称为处理流，对文件或者其他目标频繁的操作，效率低，性能差。**缓冲流目的**是**提高程序读取**和**写出的性能**。缓冲流也分为字节缓冲流和字符缓冲流。



### BufferedInputStream

但是如果只是使用字节流，效率会很慢，所以我们需要用到**缓冲流**

```java
    public void FileBuffer(String srcPath,String destPath) throws IOException{
        //文件
        File file=new File(srcPath);
        File file_copy=new File(destPath);
        //字节流
        FileInputStream inputStream=new FileInputStream(file);
        FileOutputStream outputStream=new FileOutputStream(file_copy);
        //缓冲流
        BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream);
        BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(outputStream);
        //复制
        byte[] buffer=new byte[1024];
        int len;
        while ((len=bufferedInputStream.read(buffer))!=-1){
            bufferedOutputStream.write(buffer,0,len);
        }
        //关闭
        bufferedOutputStream.close();
        bufferedInputStream.close();
//        关闭外层流的同时 内层流也会自动关闭  可以省略
//        outputStream.close();
//        inputStream.close();
    }
```



**时间对比**

![image-20201203162302474](img\4.png)







### BufferedReader

**字符缓冲流**

- 可以直接调用**readLine()方法读取文本文件**，内置缓冲区

```java
    @Test
    public void FileBufferReader() throws IOException{
        //文件
        File file=new File("hello.txt");
        File file_copy=new File("hello_copy2.txt");
        //字节流
        FileReader reader=new FileReader(file);
        FileWriter writer=new FileWriter(file_copy);
        //缓冲流
        BufferedReader bufferedReader=new BufferedReader(reader);
        BufferedWriter bufferedWriter=new BufferedWriter(writer);
        //复制
        String data;
        while ((data=bufferedReader.readLine())!=null){
            bufferedWriter.write(data);
            bufferedWriter.newLine();
        }
        //关闭
        bufferedWriter.close();
        bufferedReader.close();
//        关闭外层流的同时 内层流也会自动关闭  可以省略
//        outputStream.close();
//        inputStream.close()
    }
```





## 转换流

**转换流提供了在字节流和字符流之间的转换**

Java API提供了两个转换流:

- lnputStreamReader:将**InputStream转换为Reader**
- OutputStreamWriter:将**OutputStream转换为Writer**



> 优点

- 当字节流中的数据**都是字符时**，转成字符流操作更高效。

- 很多时候我们使用转换流来处理文件乱码问题。实现编码和解码的功能。




**作用:**提供字节流与字符流之间的转换

- 解码:字节、字节数组--->字符数组、字符串
- 编码:字符数组、字符串--->字节、字节数组

4.字符集



![image-20201204145329446](img\5.png)





代码如下

```java
@Test
public void test1() throws IOException {
    FileInputStream fs=new FileInputStream("hello.txt");//字节流
    InputStreamReader is=new InputStreamReader(fs,"gbk");//将字节流转换为字符流
    BufferedReader bf=new BufferedReader(is);

    char[]buffer=new char[20];
    int len;
    while ((len=bf.read(buffer))!=-1){
        for (int i = 0; i < len; i++) {
            System.out.print(buffer[i]);
        }
    }
    bf.close();
    is.close();
    fs.close();
}
```





## 数据流

- **为了方便地操作Java语言的基本数据类型和String的数据，可以使用数据流。**
- 这个流可以将**数据连同数据的类型**一并写入文件中



**数据写出流**

```java
@Test
public void test1() throws IOException {
    //写出数据流
    DataOutputStream ds=new DataOutputStream(new FileOutputStream("hello_data.txt"));
    ds.writeUTF("愿你历经千帆\n" +
            "归来仍是少年\n");
    ds.writeInt(10);
    ds.writeBoolean(false);
    ds.flush();
    ds.close();
}
```

**数据写入流（读取顺序必须和写出流相同）**

```java
@Test
public void test2() throws IOException {
    //写入数据流
    DataInputStream ds=new DataInputStream(new FileInputStream("hello_data.txt"));

    //读取的顺序必须和写入的顺序相同
    System.out.println(ds.readUTF());
    System.out.println(ds.readInt());
    System.out.println(ds.readBoolean());
    ds.close();
}
```



## 标准输出流

PrintStream: 标准的字节输出流，**默认输出到控制台**

```java
//我们可以自定义一个PrintStream，然后修改输出方向，使输出指向"log"文件
PrintStream stream1 = new PrintStream(new FileOutputStream("log"));
System.setOut(stream1);
//此时的输出会放到log文件中
System.out.println("hello world");
System.out.println("芜湖");
```
可以通过标准输出流**编写日志Logger类**

```java
public class Logger {
    public static void log(String msg)  {
        PrintStream printStream= null;
        try {
            printStream = new PrintStream(new FileOutputStream("log.txt",true));
            System.setOut(printStream);
            Date date=new Date();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");
            String time = simpleDateFormat.format(date);
            System.out.println(time+": "+msg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            printStream.close();
        }
    }

    public static void main(String[] args) {
        Logger.log("zcj日志");
        Logger.log("我把梦想卖了");
        Logger.log("换成了材米油盐");
    }
}
```



## 对象流

**ObjectInputStream和OjbectOutputSteam**

用于存储和读取**基本数据类型**数据或**对象**的处理流。它的强大之处就是可以把Java中的对象写入到数据源中，也能把对象从数据源中还原回来。

- 序列化:用ObjectOutputStream类**保存**基木类型数据或对象的机制

- 反序列化:用ObjectlnputStream类**读取**基本类型数据或对象的机制








**存入自定义对象需要**

- 需要实现接口: **Serializable**
- 当前类提供一个全局常量: **serialVersionUID**
  - 如果没有提供，虚拟机会为序列化的对象生成**默认的序列化版本号**
- 除了当前Person类需要实现Serializable接口之外，还必须保证其内部所有属性也必须是可序列化的。
- 对象流**不能**序列化static和transient修饰的成员变量



代码如下：

自定义类

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person implements Serializable {
    private static final long serialVersionUID = -684979447075L;
    String name;
    int id;
}
```

对象写出

```java
@Test
//序列化
public void ObjectStream() throws IOException {
    //字节流创建
    FileOutputStream output=new FileOutputStream("hello_object.txt");
    //创建对象写出流
    ObjectOutputStream outputStream=new ObjectOutputStream(output);
    outputStream.writeObject(new String("芜湖起飞"));
    outputStream.writeObject(new String("芜湖起飞2"));
    outputStream.writeObject(new Date());
    outputStream.writeObject(new Person("asd",1));
    //关闭
    outputStream.close();
    output.close();
}
```



对象写入

```java
@Test
//反序列化
public void ObjectStream2() throws Exception {
    //字节流创建
    FileInputStream inputStream=new FileInputStream("hello_object.txt");
    //创建对象写入流
    ObjectInputStream objectInputStream=new ObjectInputStream(inputStream);
    //反序列化

    //按照顺序读取
    Object o = objectInputStream.readObject();
    System.out.println(o);
    Object o1 = objectInputStream.readObject();
    System.out.println(o1.toString());
    Object o2 = objectInputStream.readObject();
    System.out.println(o2);
    Object o3 = objectInputStream.readObject();
    System.out.println(o3);

    //关闭
    objectInputStream.close();
    inputStream.close();
}
```



### 序列号作用

1. 当文件中的**对象进行反序列化**时，会先判断文件中**类名**和程序中类名是否一致

2. 如果相同则再比较**文件中对象的序列号**，和项目中类的序列号是否相同

   - 如果不相同则会报异常

   - ```java
     java.io.InvalidClassException: com.jie.io.Person; 
     local class incompatible: stream classdesc serialVersionUID = -7564189118422739644, local class serialVersionUID = -2265004783317520704
     ```

     

> 为什么会导致文件中**类的序列号和项目中类的序列号不同呢**？

- 当存储实现了**Serializable**接口的对象时，如果该对象没有设置序列号，Jvm会给这个对象**分配一个默认**的序列号
- 但当该类的结构**被改变**时，该类的默认的序号也会发生改变
- 如果不设置**固定序列号**，当类的结构改变时,就会出现**磁盘和内存**中类 序列号不同的情况

```java
java.io.InvalidClassException: com.jie.io.Person; 
local class incompatible: stream classdesc serialVersionUID = -7564189118422739644, 
local class serialVersionUID = -2265004783317520704  //序列号不同
```

所以需要给可序列的类给定一个**固定**的序列号，使磁盘和内存中的类的序列号始终相同





## Properties+IO 

- 用于读取配置文件
- Properties.load 可以根据文件中的  **等号、空格、分号** 等分割，然后放置Key-Value对



一般默认的配置文件的后缀都是 **.properties** 

```properties
#该文件名为  mybatis.properties
username=root
password=123456
url=localhost3306
Driver : mysql
```

可以通过**Properties集合**去读取这个配置文件

```java
public static void main(String[] args) throws Exception {
    //读取配置文件
    FileReader fileReader=new FileReader("mybatis.properties");
    Properties properties=new Properties();
    //通过properties调用load 读取输入流
    properties.load(fileReader);   
    //{url=localhost3306, password=123456, Driver=mysql, username=root}
    //输出
    System.out.println(properties.getProperty("Driver"));
    System.out.println(properties.getProperty("url"));
    System.out.println(properties.getProperty("username"));
    System.out.println(properties.getProperty("password"));
    fileReader.close();
}
```