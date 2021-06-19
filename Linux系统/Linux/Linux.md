



# **本地虚拟机   **



```properties
虚拟机本地IP:192.168.216.129
账户名:root
密码:123456
```

宝塔

```properties
外网面板地址: http://117.150.24.160:8888/12585ed9
内网面板地址: http://192.168.216.130:8888/
username: 319991012
password: abc4068321
```





# **阿里云服务器**

```properties
IP:39.98.69.145
账户名:root
密码:ZCJzcj4068321
```



**宝塔网页登陆网址**

```properties
外网面板地址: http://39.98.69.145:8888/69b9e0bb
内网面板地址: http://172.28.109.247:8888/69b9e0bb
username: 319991012
password: abc4068321
```

# 走进Linux



## 目录结构

Linux树状图结构

![image-20210113190658033](img\1.png)



文件属性

![image-20210113191928527](img\2.png)





## 关机 

在linux领域内大多用在服务器上，很少遇到关机的操作。

毕竟服务器上跑一个服务是永无止境的，除非 特殊情况下，不得已才会关机。 关机指令为：

**shutdown** 

```tcl
sync # 将数据由内存同步到硬盘中。
shutdown # 关机指令，你可以man shutdown 来看一下帮助文档。例如你可以运行如下命令关机：
shutdown –h 10 # 这个命令告诉大家，计算机将在10分钟后关机
shutdown –h now # 立马关机
shutdown –h 20:25 # 系统会在今天20:25关机
shutdown –h +10 # 十分钟后关机
shutdown –r now # 系统立马重启
shutdown –r +10 # 系统十分钟后重启
reboot # 就是重启，等同于 shutdown –r now
halt # 关闭系统，等同于shutdown –h now 和 poweroff
```

不管是重启系统还是关闭系统，首先要运行 **sync** 命令，把内存中的数据写到磁盘中。





## 常用的基本命令



**cd:切换目录**

```properties
cd :#切换目录命令!   
cd ../usr:#跳转上一级的目录文件


cd /home/jie:#绝对路径跳转   （无论在哪） 
cd ..:#返回上一级目录
cd ~:#回到当前的用户目录
pwd :#显示当前用户所在的目录
```



**ls: 列出目录**

```bash
ls :默认列出目录下的所有文件名
ls -a:参数: all ,查看全部的文件,包括隐藏文件
ls -l:列出目录下子目录和文件的详细信息
ls -t:按最后修改时间排序
ls -S:按文件大小排序。（大写的S）
ls -r:排序时按倒序。
ls -al:查看全部的文件包括隐藏文件的属性和权限
```



**mkdir 创建一个目录**

```bash
mkdir jie1  #创建文件夹
mkdir -p jie1/jie2/jie3 # 用-p递归创建层级目录
```

**rmdir 删除目录**  （只能删除空目录）

```bash
rmdir jie1 #删除目录
rmdir -p jie1/jie2/jie3 #递归删除目录
```

**touch**    创建文件

**echo**  写文件

```bash
echo "zcj" >>a    
```

**cp 复制文件**

```bash
cp panelExec.log jie1   #复制  中间为文件  后面为拷贝到的地址
cp panelExec.log ../home
```



**rm 移除文件或者目录**

```bash
-f #忽略不存在的文件,不会出现警告,强制删除!
-r #递归删除目录!
-i #互动,删除询问是否删除
rm -rf /*    #删库到跑路（十分危险 不要尝试）
```



**mv 移动文件或者目录|重命名文件**

```bash
-f #强制
-u #只替换已经更新过的文件
                                                                         mv panelExec.log jie1 #移动文件
mv panelExec.log p.log #重命名文件夹名
```

**tar  解压文件**

```bahs
tar -zxvf apache-tomcat-9.0.37.tar.gz
```

**firewall 防火墙、端口**

```bash
#查看防火墙信息
systemctl status firewalld
# 开启防火墙端口 8080
firewall-cmd --zone=public --add-port=8080/tcp --permanent
# 重启防火墙
systemctl restart firewalld.service
# 查看所有开启的端口
firewall-cmd --list-ports
```





## 基本属性

 Linux系统是一种典型的多用户系统,不同的用户处于不同的地位,拥有不同的权限。为了保护系统的安全性, Linux系统对不同的用户访问同一文件(包括目录文件)的权限做了不同的规定。在Linux中我们可以使用ll或者ls -l命令来显示一个文件的属性以及文件所属的用户和组,如:实例中, boot文件的第一个属性用"d"表示。 "d"在Linux中代表该文件是一 个目录文件。

 在Linux中第一个字符代表这个文件是目录、 文件或链接文件等等:



![image-20210114123003026](img\4.png)



- 当为[d]则是目录
- 当为[-]则是文件;
- 若是[l]则表示为链接文档( link file);
- 若是[b]则表示为装置文件里面的可供储存的接口设备(可随机存取装置) ;
- 若是[c]则表示为装置文件里面的串行端口设备,例如键盘、鼠标( 一次性读取装置)。

接下来的字符中,以三个为一组,且均为[rwx] 的三个参数的组合。

其中，[r]代表可读(read)、[w]代表可写(write)、[x]代表可执行(execute)。

要注意的是,这三个权限的位置不会改变,如果没有权限,就会出现减号[-]而已。

每个文件的属性由左边第一部分的10个字符来确定 (如下图) :
![在这里插入图片描述](img\3.png)





**因此, Linux系统按文件所有者、文件所有者同组用户和其他用户来规定了不同的文件访问权限。**





## 修改文件属性

1、chgrp :更改文件属组

```bash
chgrp [-R] 属组名文件名
```

-R :递归更改文件属组,就是在更改某个目录文件的属组时,如果加上R的参数,那么该目录下的所有文件的属组都会更改。
2、chown :更改文件属主,也可以同时更改文件属组

```bash
chown [-R] 属主名文件名
chown [-R] 属主名:属组名文件名

```

**3、chmod :更改文件9个属性 （重要）**

```bash
chmod [-R] xyz 文件或目录
```

Linux文件属性有两种设置方法,一种是数字（常用的是数字）, 一种是符号。

Linux文件的基本权限就有九个,分别是owner/group/others

三种身份各有自己的read/write/execute权限。

文件的权限字符为: [-rwxrwxrwx]，

这九个权限是三个一组的!其中,我们可以使用字来代表各个权限,各权限的分数对照表如下:

**r:4 w:2 x:1**

```bash
chmod 666 a  #可读可写不可执行 rw  6
```

```bash
chomd 777 a  #可读可写可执行 rwx 7  
```

![image-20210114150342716](img\6.png)

文件赋予所有用户可读可执行!



## 文件内容查看



Linux系统中使用以下命令来查看文件的内容:

●**cat**由第一行开始显示文件内容

![image-20210114161417082](img\7.png)

●**tac**从最后一行开始显示，可以看出tac是cat的倒着写!

![image-20210114161520862](img\8.png)

●**nl**显示的时候,顺道输出行号!

![image-20210114161709935](img\9.png)

●**more**一页一页的显示文件内容（空格表示翻页，enter代表向下看下一行）
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200630092344102.png#pic_center)

●**less**与more类似,但是比more更好的是,他可以往前翻页!

- 空格翻页，上下键代表上下翻动页面，退出q命令

●**/** 查找字符串   向下查询  **使用/**   例如(/a)，向上查询**使用?**   例如(?a)   



●**head**     

```bash
head -n 20 panelExec.log    #只看头几行 通过-n参数来控制显示几行
```

●**tail**   

```bash
 tail -n 20 panelExec.log  #只看尾巴几行 通过-n参数来控制显示几行
```



你可以使用man 命令来查看各个命令的使用文档,如: man [命令]。                                                                                                                                                                                                                                



## 硬链接和软链接

Linux链接分为两种：硬链接、软链接

**硬链接**  （深拷贝）： ln  [源文件           ]    [创建文件]

A—B,假设B是A的硬链接，那么他们两个指向了同一个文件!允许一个文件拥有多个路径，用户可以通过这种机制硬链接到一个重要文件上，防止误删

**软链接**  （浅拷贝）：ln -s  [源文件]   [创建文件]

类似Windows下的快捷方式，删除源文件，快捷方式也就访问不了

![image-20210114205122570](img\10.png)

​                                                                





## VIM编辑器

Vim是从Vi发展出来的一个文本编辑器。代码补完、编译及错误跳转等方便编程的功能特别丰富,在程序员中被广泛使用。

尤其是Linux中,必须要会使用Vim (查看内容, 编辑内容,保存内容! )



三种**使用方式**:

基本上Vi/Vim共分为三种模式,分别是

- 命令模式( Command mode) 
- 输入模式( Insert mode )
- 底线命令模式( Lastline mode )

这三种模式的作用分别是:



**命令模式**

用户刚刚启动Vi/Vim ,便进入了命令模式。

此状态下敲击键盘动作会被Vim识别为命令,而非输入字符。比如我们此时按下i ,并不会输入一个字符, i被当作了一个命令。

以下是常用的几个命令:

```bash
i  #切换到输入模式,以输入字符。

x  #删除当前光标所在处的字符。

:  #切换到底线命令模式,以在最底一行输入命令。
```



若想要编辑文本:启动Vim,进入了命令模式,按下i,切换到输入模式。

```bash
vim jie.txt      #若有该文件 则进入Vim编辑   若无 则创建 该文件
```





**输入模式**

在命令模式下按下 i 就进入了输入模式。
在输入模式中，可以使用以下按键：

```bash
字符按键以及Shift组合  #输入字符
ENTER                #换行
BACK SPACE           #删除光标前一个字符
DEL，删除键，         #删除光标后一个字符
方向键，              #在文本中移动光标
HOME/END，           #移动光标到行首/行尾
Page Up/Page Down   #上/下翻页
Insert，            #切换光标为输入/替换模式，光标将变成竖线/下划线
ESC，               #退出输入模式，切换到命令模式
```



**底线命令模式**

在命令模式下按下 :（英文冒号）就进入了底线命令模式。

底线命令模式可以输入单个或多个字符的命令，可用的命令非常多。
在底线命令模式中，基本的命令有

- q 退出程序
- w 保存文件
- wq 退出并且保存
- set nu  显示行号
- set nonu  取消行号

按ESC键可随时退出底线命令模式。





## 账号管理

Linux系统是一个多用户多任务的分时操作系统,任何一个要使用系统资源的用户,都必须首先向系统管理员申请一个账号,然后以这个账号的身份进入系统。



每个用户账号都拥有一个唯一的用户名和各自的口令。

实现用户账号的管理,要完成的工作主要有如下几个方面:
 ●用户账号的添加、删除与修改。
 ●用户口令的管理。
 ●用户组的管理。



**添加用户  useradd**  

useradd 命令 添加用户

useradd -选项 用户名

- -m:自动创建这个用户的主目录/home/cqh

  ```bashu
  useradd -m zcj 
  ```

- -G:给用户分配组

  ```bash
  useradd -G root zcj2
  ```

添加的用户存在于   **/etc/passwd**



**删除用户 userdel**

```bash
userdel -r zcj3 #删除用户的时候把它的目录也删除掉
```



**修改用户 usermod**

```bash
 usermod -d /tmp/jie1 zcj
```

修改完毕后查看配置文件 将cqh用户下的目录修改为jie1



**切换用户**

切换用户的命令为:  **su**

 ```bash
su zcj
 ```

从普通用户切换到root用户,还可以使用命令:

```bash
exit 或者 Ctrl+D     
```

​                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     4.在切换用户时,如果想在切换用户之后使用新用户的工作环境,可以在su和username之间加,例如:             

**设置密码**

我们一般通过root创建用户的时候!要配置密码!
如果是超级用户

```bash
passwd zcj(用户名) 
```

普通用户

```bash
passwd zcj(用户名)           #还需要输入之前的密码 才能设置新密码           
```



冻结账户

一旦冻结,这个人就登录不上系统了!

```bash
passwd -l zcj #锁定之后这个用户就不能再登录了
passwd -d zcj #把密码清空 这样也能防止用户登录

```





## 用户组


用户组的管理涉及用户组的添加、删除和修改。组的增加、删除和修改实际上就是对/etc/group文件的更新。

**创建一个用户组 groupadd**                           

```bash
groupadd zcjgroup

groupadd -g 520 zcjgroup2  #可以指定id  若不指定则会自增
```



**删除用户组 groupdel**

```bash
groupdel zcjgroup2
```



**修改用户组的权限和名字 groupmod**

```bash
group -g 666 -n newcqh cqh #修改cqh用户组id为666 重命名为newcqh 
```

**用户切换用户组**

```bash
$ newgrp root #   将当前用户所属组切换为root
```



**拓展 用户账户文件的查看(了解即可) 

**/etc/shadow**
登录口令:把真正的加密后的用户口令字存放到/etc/shadow文件中,保证我们密码的安全性!
用户组的所有信息都存放在/etc/group文件中。



## 磁盘管理

- **df** (列出文件系统整体的磁盘使用量) 

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200703084335994.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L29rRm9ycmVzdDI3,size_16,color_FFFFFF,t_70#pic_center)

- **du** (检查磁盘空间使用量)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200703084403486.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L29rRm9ycmVzdDI3,size_16,color_FFFFFF,t_70#pic_center)

- du -sm /* 

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200703084435661.png#pic_center)



**mount  挂载**

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200703084513927.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L29rRm9ycmVzdDI3,size_16,color_FFFFFF,t_70#pic_center)

卸载：umount-f [挂载位置] 强制卸载



## 进程管理

什么是进程

1、在Linux中 ,每一个程序都是有自己的一个进程,每一个进程都有一个id号!
2、每一个进程呢,都会有一个父进程!
3、进程可以有两种存在方式:**前台**!**后台**运行!
4、一般的话**服务都是后台运行**的，基本的程序都是**前台运行**的



命令

**ps** :查看当前系统中正在执行的各种进程的信息！

```bash
ps  -a       #显示当前终端所有的进程信息
ps  -u       #以用户的信息显示进程
ps  -x       #显示后台运行进程的参数
ps -aux      #查看所有进程
```



 **ps -xx|grep   [name]**  : 查看指定进程   

```bash
ps -aux|grep mysql  查看mysql相关进程
ps -aux|grep redis  查看redis相关进程
# | 在Linux中这个叫管道符   A|B

```


**ps-ef** ：可以查看到父进程的信息

```bash
ps -ef|grep mysql  
```

**pstree**:  查看进程树
-p 显示父id
-u 显示用户组

```bash
pstree -pu   #查看所有进程的进程树
pstree -pu|grep mysql   #查看mysql的进程树
```

  ![image-20210115182518015](img\12.png)

**kill** :结束进程

```bash
kill -9 [pid]  #当程序死循环时 可以强制结束该进程
```





# 安装与部署

## JDK安装

安装Java环境

```bash
java-version #检查当前系统是否存在java环境！
rpm -ivh jdk-8u271-linux-x64.rpm    #安装
rpm -qa|grep jdk  # 检测JDK版本信息
```

卸载

```bash
rpm -e --nodeps jdk #强制卸载
```



**使用rpm和yum方式安装不需要配置环境变量**，使用压缩包需要配置。



1. 配置环境变量

   ```bash
   JAVA_HOME=/usr/java/jdk1.8.0_261-i586
   CLASSPATH=%JAVA_HOME%/lib;%JAVA_HOME%/jre/lib
   PATH=$JAVA_HOME/bin;$JAVA_HOME/jre/bin
   export PATH CLASSPATH JAVA_HOME
   ```

   让这个配置文件生效！ `source /etc/profile`

2. 发布项目

   ```bash
   #查看防火墙信息
   systemctl status firewalld
   # 开启防火墙端口 8080
   firewall-cmd --zone=public --add-port=8080/tcp --permanent
   # 重启防火墙
   systemctl restart firewalld.service
   # 查看所有开启的端口
   firewall-cmd --list-ports
   ```



## Tomcat安装

ssm项目打包方式为war包 需要放在Tomcat中运行！

1. 解压这个文件

   ```bash
   tar -zxvf apache-tomcat-9.0.37.tar.gz
   ```

2. 启动tomcat测试 

   ```bash
   # 执行 
   ./startup.sh
   # 停止 
   ./shutdown.sh 
   ```



## Docker安装

1. 检测CenOS7

   ```bash
   cat /etc/redhat-release
   ```

2. 安装我们的准备环境

   ```bash
   yum -y install 包名   # yum install 安装命令 -y 所有的提示都为y
   # 安装gcc相关
   yum -y install gcc
   yum -y install gcc-c++
   ```

3. 卸载之前的版本

   ```bash
   # 官网版本
   yum remove docker \
             docker-client \
             docker-client-latest \
             docker-common \
             docker-latest \
             docker-latest-logrotate \
             docker-logrotate \
             docker-engine
   ```

4. 安装需要的软件包

   ```bash
   yum install -y yum-utils device-mapper-persistent-data 
   ```

5. 设置stable镜像仓库

   ```bash
   # 正确推荐使用国内的
   yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
   ```

6. 更新yum软件包索引

   ```bash
   yum makecache fast
   ```

7. 安装Docker CE

   ```bash
   yum -y install docker-ce docker-ce-cli containerd.io
   ```

8. 启动docker

   ```b
   systemctl start docker
   ```

9. 测试

   ```bash
   docker version
   
   docker run hello-world
   
   docker images
   ```



**出现错误**

如果出现打开不了docker

```bash
sudo service docker start
```

如果启动不了hello world

```bash
systemctl stop docker

echo "DOCKER_OPTS=\"\$DOCKER_OPTS --registry-mirror=http://f2d6cb40.m.daocloud.io\"" | sudo tee -a /etc/default/docker

service docker restart
```

