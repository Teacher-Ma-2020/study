# Nginx笔记

**内容介绍**
**1、nginx基本概念。**

> (1) nginx是什么，能做什么事情
>
> (2) 反向代理。
>
> (3) 负载均衡。
>
> (4) 动静分离



**2、nginx 安装、常用命令和配置文件**

> (1)在liunx系统中安装nginx.
>
> (2) nginx常用命令。
>
> (3) nginx 配置文件。

**3、nginx 配置实例 1-反向代理**

**4、nginx 配置实例 2-负载均衡**

**5、nginx 配置实例 3-动静分离。**

**6、nginx配置高可用集群**

**7、nginx 原理。**

------





### 一、Nginx 简介

1、什么是NGINX

```
	Nginx ("engine x")是一个高性能的HTTP和反向代理服务器，特点是占有内存少，并发能
力强，事实上nginx的并发能力确实在同类型的网页服务器中表现较好
    Nginx专为性能优化而开发，性能是其最重要的考量，实现上非常注重效率，能经受高负载
的考验，有报告表明能支持高达50000个并发连接数。
```

#### **2、反向代理**

**a. 正向代理**
在客户端(浏览器)配置代理服务器，通过代理服务器进行互联网访问

```
{% asset_img image-20200606144302429.png  正向代理 %}
```

**b. 反向代理**
反向代理，其实客户端对代理是无感知的，因为客户端不需要任何配置就可以访问，我们只需要将请求发送到反向代理服务器，由反向代理服务器去选择目标服务器获取数据后,在返回给客户端，

**此时反向代理服务器和目标服务器对外就是一个服务器，暴露的是代理服务器地址，隐藏了真实服务器IP地址。**



![反向代理](https://nishigouzi.github.io/2020/06/07/Nginx%E7%AC%94%E8%AE%B0/image-20200606150932688.png)

#### 3、负载均衡

单个服务器解决不了，我们增加服务器的数量，然后将请求分发到各个服务器上,将原先请求集中到单个服务器上的情况改为将请求分发到多个服务器上,将负载分

发到不同的服务器，也就是我们所说的负载均衡

![负载均衡](https://nishigouzi.github.io/2020/06/07/Nginx%E7%AC%94%E8%AE%B0/image-20200606154956614.png)

#### 4、动静分离

为了加快网站的解析速度，可以把动态页面和静态页面由不同的服务器来解析，加快解析速度。降低原来单个服务器的压力。

![动静分离](https://nishigouzi.github.io/2020/06/07/Nginx%E7%AC%94%E8%AE%B0/image-20200606155620164.png)





### 二、Nginx 安装

![](https://nishigouzi.github.io/2020/06/07/Nginx%E7%AC%94%E8%AE%B0/image-20200606173451847.png)



### 三、nginx操作命令

在 windows 系统中访问 linux 中 nginx，默认不能访问的，因为防火墙问题 

（1）关闭防火墙 

（2）开放访问的端口号，80 端口 



端口号

```bash
#查看开放的端口号 
firewall-cmd --list-all 

#设置开放的端口号 
firewall-cmd --zone=public --add-port=6379/tcp --permanent

#重启防火墙 
firewall-cmd --reload

#强行停止：
pkill -9 nginx
```



首先要进入 nginx 目录中 

```bash
cd /usr/local/nginx/sbin
```



```bash
#1、查看 nginx 版本号 
./nginx -v 

#2、启动 nginx  
./nginx 

#3、停止 nginx  
./nginx -s stop 

#4、重新加载 nginx 
./nginx -s reload
```







### 四、nginx.conf配置文件

进入配置文件

```bash
cd /usr/local/nginx/conf/     
```



配置文件中的内容 包含三部分内容 

（1）**全局块**：配置服务器整体运行的配置指令 比如 worker_processes 1;处理并发数的配置 

（2）**events 块**：

events 块涉及的指令主要影响 Nginx 服务器与用户的网络连接，常用的设置包括是否开启对多 work process 下的网络连接进行序列化，是否允许同时接收多个网络连接，选取哪种事件驱动模型来处理连接请求，每个 word process 可以同时支持的最大连接数等。 

上述例子就表示每个 work process 支持的最大连接数为 1024. 这部分的配置对 Nginx 的性能影响较大，在实际中应该灵活配置



（3）**http 块** 还包含两部分： http 全局块 server 块

![image-20210321170130647](img\1.png)







### 五、反向代理

**1、实现效果**

使用nginx反向代理，根据访问的路径跳转到不同端口的服务中。

 **访问不同的界面 跳到tomcat的界面**

![image-20210321173452842](img\2.png)



**2、准备工作**

> (1) 准备两个tomcat服务器，一个8080端口，一个8081端口
>
> (2) 创建文件夹和测试页面。



**3、具体nginx配置**

```
$ vi /usr/local/nginx/conf/nginx.conf
```

![image-20210322120428565](img\3.png)

> (1) 找到nginx配置文件，进行反向代理配置。
>
> (2) 开放对外访问的端口号81
>
> (3) 重启nginx服务器，使配置文件生效

当访问  http://192.168.216.132:81/jie/ 时 

便会跳到代理的地址  即为127.0.0.1:8080



**4、最终测试**

![image-20210322111211750](img\4.png)





**实现效果**

使用nginx反向代理  根据不同的路径跳到不同的端口服务中

![image-20210322115320899](D:\study\Nginx\img\5.png)



首先配置两个tomcat服务器

![image-20210322115153118](D:\study\Nginx\img\6.png)



配置nginx配置文件

![image-20210322120324367](img\7.png)



**配置监听端口 和路径**   即可实现 跳转到另外端口的效果

![image-20210322123409764](img\8.png)



**5、补充`location`部分**

location指令说明。
该指令用于匹配URL。。
语法如下:

```
location [ = | ~ | ~* | ^~] uri {

}
1、=: 用于不含正则表达式的uri前，要求请求字符串与uri严格匹配，如果匹配成功，
	就停止继续向下搜索并立即处理该请求
2、~: 用于表示uri包含正则表达式，并且区分大小写
3、~*: 用于表示uri包含正则表达式，并且不区分大小写
4、^~: 用于不含正则表达式的uri前，要求Nginx服务器找到标识uri和请求字
	符串匹配度最高的location后，立即使用此location处理请求，而不再使用location
	块中的正则uri和请求字符串做匹配
注意: 如果uri包含正则表达式，则必须要有~或者~*标识。
```







### 六、 负载均衡

> 将**许多个请求**，平均分配到不同的服务器中

**1、实现效果**

> (1) 浏览器地址栏输入地址http://192.168.xxx.xxx/edu/index.html, 负载均衡效果，平均到配到8080和8081端口中，

**2、准备工作**

> (1) 准备两台tomcat服务器，一 台8080， 一台8081
>
> (2) 在两台tomcat里面webapps目录中，创建名称是edu文件夹，在edu文件夹中创建
> 页面index.html,用于测试。

**3、在nginx的配置文件中进行负载均衡的配置**

配置负载均衡指向的地址和端口号

![image-20210322134755960](img\9.png)

只要访问http://192.168.216.132:81/edu/

便会平均分配到两个不同的tomcat服务器中



**4、效果**

![负载均衡](https://nishigouzi.github.io/2020/06/07/Nginx%E7%AC%94%E8%AE%B0/image-20200607124828732.png) ![负载均衡](https://nishigouzi.github.io/2020/06/07/Nginx%E7%AC%94%E8%AE%B0/image-20200607124843736.png)



**负载分配策略**

在linux下有Nginx、LVS、 Haproxy 等等服务可以提供负载均衡服务，而且Nginx提供了几种分配方式(策略):。

- **1、轮询(默认)**

  每个请求按时间顺序逐一分配到不 同的后端服务器，如果后端服务器down掉，能自动剔除

- **2、weight**
  weight代表权重默认为1,权重越高被分配的客户端越多。
  指定轮询几率，weight和访问比率成正比，用于后端服务器性能不均的情况。例如: 。

![image-20210322141228348](img\10.png)

- **3、ip hash**

  每个请求按访问ip的hash结果分配,

  这样每个访客固定访问一个后端服务器,可以解诀session的问题。例如:

  ```
  upstream server pool{
    ip_ hash
    server 192.168.5.21:80
    server 192.168.5.22:80
  }
  ```



- **4、fair (第三方)**
  

**需要安装**

  按后端服务器的响应时间来分配请求，响应时间短的优先分配

  ```
  upstream server_pool 
  	server 192.168.5.21:80;
  	server 192.168.5.22:80;
  	fair;
  }
  ```





### 七、动静分离

![动静分离](https://nishigouzi.github.io/2020/06/07/Nginx%E7%AC%94%E8%AE%B0/image-20200607153752466.png)

```
通过location指定不同的后缀名实现不同的请求转发。通过expires参数设置，可以使浏览器缓存过期时间，减少与服务器之前的请求和流量。具体Expires定义: 是给一个资源设定一个过期时间，也就是说无需去服务端验证，直接通过浏览器自身确认是否过期即可，所以不会产生额外的流量。此种方法非常适合不经常变动的资源。(如果经常更新的文件，不建议使用Expires来缓存)，如果设置3d, 表示在这3天之内访问这个URL, 发送一个请求，比对服务器该文件最后更新时间没有变化，则不会从服务器抓取，返回状态码304,如果有修改，则直接从服务器重新下载，返回状态码200。。
```

**2、准备工作**

> (1) 在liunx系统中准备静态资源，用于进行访问
>
> /data/image 图片文件夹
>
> /data/www html文件夹

**3、具体配置**

> (1) 在nginx配置文件中进行配置

![动静分离](img\11.png)

- root为根目录
- 将根目录下的image设置为静态文件
- 将根目录下的www设为动态文件



4、实际测试

使用image访问静态文件

```
http://192.168.1.112/image/1.jpg
```

![img](https://nishigouzi.github.io/2020/06/07/Nginx%E7%AC%94%E8%AE%B0/image-20200607200424689.png)

上图是因为`autoindex on`这个设置生效的

**动态访问**

使用www访问动态文件

```bash
http://192.168.216.132:81/www/a.html
```

![image-20210322151105519](img\12.png)









### 六、Nginx配置高可用集群



**1、什么是nginx高可用**

![img](https://nishigouzi.github.io/2020/06/07/Nginx%E7%AC%94%E8%AE%B0/image-20200607205253505.png)

> (1) 需要两台nginx服务器。
> (2) 需要keepalived
> (3) 需要虚拟ip

**2、配置高可用的准备工作**

> (1) 需要两台服务器192.168.17.129 和192.168.17.1314
> (2) 在两台服务器安装nginx.
> (3) 在两合服务器安装keepalived.

**3、在两台服务器安装keepalived**
使用yum命令进行安装

```
$ yum install keepalived
$ rpm -q -a keepalived    #查看是否已经安装上
```

默认安装路径: /etc/keepalived

安装之后，在etc里面生成目录keepalived, 有配置文件keepalived.conf

**4、完成高可用配置(主从配置)**

（1）修改keepalived的配置文件`keepalived.conf`为：

```
global_defs {
	notification_email {
	  acassen@firewall.loc
	  failover@firewall.loc
	  sysadmin@firewall.loc
	}
	notification_email_from Alexandre.Cassen@firewall.loc
	smtp_ server 192.168.17.129
	smtp_connect_timeout 30
	router_id LVS_DEVEL	# LVS_DEVEL这字段在/etc/hosts文件中看；通过它访问到主机
}

vrrp_script chk_http_ port {
	script "/usr/local/src/nginx_check.sh"
	interval 2   # (检测脚本执行的间隔)2s
	weight 2  #权重，如果这个脚本检测为真，服务器权重+2
}

vrrp_instance VI_1 {
	state BACKUP   # 备份服务器上将MASTER 改为BACKUP
	interface ens33 //网卡名称
	virtual_router_id 51 # 主、备机的virtual_router_id必须相同
	priority 100   #主、备机取不同的优先级，主机值较大，备份机值较小
	advert_int 1	#每隔1s发送一次心跳
	authentication {	# 校验方式， 类型是密码，密码1111
        auth type PASS
        auth pass 1111
    }
	virtual_ipaddress { # 虛拟ip
		192.168.17.50 // VRRP H虛拟ip地址
	}
}
```

（2）在路径/usr/local/src/ 下新建检测脚本 nginx_check.sh

nginx_check.sh

```
#! /bin/bash
A=`ps -C nginx -no-header | wc - 1`
if [ $A -eq 0];then
	/usr/local/nginx/sbin/nginx
	sleep 2
	if [`ps -C nginx --no-header| wc -1` -eq 0 ];then
		killall keepalived
	fi
fi
```

(3) 把两台服务器上nginx和keepalived启动

```
$ systemctl start keepalived.service		#keepalived启动
$ ps -ef I grep keepalived		#查看keepalived是否启动
```

**5、最终测试**

(1) 在浏览器地址栏输入虚拟ip地址192.168.17.50

(2) 把主服务器(192.168.17.129) nginx和keealived停止，再输入192.168.17.50.

```
$ systemctl stop keepalived.service  #keepalived停止
```

### 七、Nginx原理解析

**1、master和worker**

![img](https://nishigouzi.github.io/2020/06/07/Nginx%E7%AC%94%E8%AE%B0/image-20200607222133869.png)

**2、worker如何进行工作的**

![img](https://nishigouzi.github.io/2020/06/07/Nginx%E7%AC%94%E8%AE%B0/image-20200607222501486.png)

**3、一个master和多个woker的好处**
(1) 可以使用nginx -s reload热部署。

```
首先，对于每个worker进程来说，独立的进程，不需要加锁，所以省掉了锁带来的开销，
同时在编程以及问题查找时，也会方便很多。其次,采用独立的进程，可以让互相之间不会
影响，一个进程退出后，其它进程还在工作，服务不会中断，master进程则很快启动新的
worker进程。当然，worker进程的异常退出，肯定是程序有bug了，异常退出，会导致当
前worker.上的所有请求失败，不过不会影响到所有请求，所以降低了风险。
```

**4、设置多少个woker合适**

```
Nginx同redis类似都采用了io多路复用机制，每个worker都是一个独立的进程， 但每个进
程里只有一个主线程，通过异步非阻塞的方式来处理请求，即使是 千上万个请求也不在话
下。每个worker的线程可以把一个cpu的性能发挥到极致。所以worker数和服务器的cpu
数相等是最为适宜的。设少了会浪费cpu,设多了会造成cpu频繁切换上下文带来的损耗。
# 设置worker数量
worker.processes 4 

# work绑定cpu(4work绑定4cpu)
worker_cpu_affinity 0001 0010 0100 1000

# work绑定cpu (4work绑定8cpu中的4个)
worker_cpu_affinity 0000001 00000010 00000100 00001000
```

**5、连接数worker_ connection**

```
这个值是表示每个worker进程所能建立连接的最大值，所以，一个nginx 能建立的最大连接数，应该是worker.connections * worker processes。当然，这里说的是最大连接数，对于HTTP 请求本地资源来说，能够支持的最大并发数量是worker.connections * worker processes,如果是支持http1.1的浏览器每次访问要占两个连接，所以普通的静态访问最大并发数是: worker.connections * worker.processes / 2, 而如果是HTTP作为反向代理来说，最大并发数量应该是worker.connections * worker_proceses/4. 因为作为反向代理服务器，每个并发会建立与客户端的连接和与后端服务的连接，会占用两个连接.
```

> 第一个: 发送请求，占用了woker的几个连接数?
> 答案: 2或者4个。
>
> 第二个: nginx有一个master,有四个woker,每个woker支持最大的连接数1024,支持的最大并发数是多少?
> 答案：普通的静态访问最大并发数是: worker connections * worker processes /2，
> 而如果是HTTP作为反向代理来说，最大并发数量应该是worker connections * worker processes/4