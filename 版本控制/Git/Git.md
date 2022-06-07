

# Git

Git 是一个免费的、开源的分布式版本控制系统，可以快速高效地处理从小型到大型的各种 项目。



**工作机制**

![img](https://img-blog.csdn.net/20171129100040582?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbWF5Zmxh/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

１、在工作目录中添加、修改文件；

２、将需要进行版本管理的文件放入暂存区域；

３、将**暂存区域**的文件**提交到git仓库**。

３、将本地仓库**提交到远程服务器**。



## 常用命令

| 命令名称                             | 作用               |
| ------------------------------------ | ------------------ |
| git config --global user.name 用户名 | 设置用户签名       |
| git config --global user.email 邮箱  | 设置用户签名       |
| **git init**                         | 初始化本地库       |
| **git status**                       | 查看本地库状态     |
| **git add 文件名**                   | 添加到暂存区       |
| **git commit -m "日志信息" 文件名**  | 提交到本地库       |
| **git reflog**                       | 查看历史记录       |
| **git log**                          | 查看历史记录(详细) |
| **git reset --hard 版本号**          | 版本穿梭           |



1. 通过**git init**在某文件夹初始化本地库
2. **git add ** 向暂存区添加文件
3. **git commit -m "日志信息" 文件名**   向本地库提交
4. **git reflog** 查看本地库的历史信息
5. **git reset --hard 版本号**   切换版本--对本地库内容进行切换







## 分支

**什么是分支** 

在版本控制过程中，同时推进多个任务，为每个任务，我们就可以创建每个任务的单独分支。

![image-20210512194855062](img\1.png)

**分支的好处**

同时并行推进多个功能开发，提高开发效率。 各个分支在开发过程中，如果某一个分支开发失败，不会对其他分支有任何影响。失败 的分支删除重新开始即可

| 命令名称             | 作用                         |
| -------------------- | ---------------------------- |
| git branch 分支名    | 创建分支                     |
| git branch -v        | 查看分支                     |
| git checkout 分支名  | 切换分支                     |
| git merge 分支名     | 把指定的分支合并到当前分支上 |
| git branch -D 分支名 | 删除本地分支                 |





### 创建分支

```java
git branch hot-fix  //创建hot-fix分支
```

![image-20210512204838989](img\2.png)

```java
git checkout hot-fix  //切换到热修复分支上
git commit -m "hot-fix-commit" hello.txt   //在该分支上提交
```

![image-20210512204918711](img\3.png)



在master分支上运行 **git merge hot-fix** 进行合并

![image-20210512205737287](img\4.png)











## 远程仓库

远程仓库**保存**着本地仓库提交的代码

### 连接

![image-20210515154429955](img\19.png)



**远程仓库操作**

| 命令名称                                 | 作用                                                      |
| ---------------------------------------- | --------------------------------------------------------- |
| **git remote -v**                        | 查看本地所有远程地址别名                                  |
| **git remote add 别名  远程地址**        | 起别名                                                    |
| **git push 别名 分支**                   | **推送本地分支到远程仓库**                                |
| **git clone 远程地址**                   | 将远程仓库的内容克隆到本地                                |
| **git pull 远程库地址别名  远程分支名**  | 将远程仓库对于分支最新内容拉下来后与 当前本地分支直接合并 |
| **git push**  远程地址  --delete develop | 删除远程分支（同时也会删除本地分支）                      |



> 创建远程仓库

![image-20210514135805869](img\10.png)

![image-20210514135842308](img\11.png)

git remote -v   查看本地远程库

**git remote add**   blog_git（别名）   ....（git地址）  添加别名

![image-20210514142019199](img\12.png)

**git push  blog_git**(别名)  master  (分支)   **推送本地分支到远程仓库**

![image-20210514142831111](img\13.png)



### 免密SSH

**获取SSH免密**  

```java
ssh-keygen -t rsa -C  [登录名] //319991012@qq.com
```

查看 

```java
# Compiled class file
*.class

# Log file
*.log

# BlueJ files
*.ctxt

# Mobile Tools for Java (J2ME)
.mtj.tmp/

# Package Files #
*.jar
*.war
*.nar
*.ear
*.zip
*.tar.gz
*.rar

# virtual machine crash logs, see 
http://www.java.com/en/download/help/error_hotspot.xml
hs err pid*

.classpath
.project
.settings
target
.idea
*.iml
```

在.gitconfig 文件中引用忽略配置文件（此文件在 Windows 的家目录中）

```java
[user]
	email = 319991012@qq.com
	name = zcj
[core]
	excludesfile = C:\Users\lenovo\git.ignore
     //注意：这里要使用“正斜线（/）”，不要使用“反斜线（\）
```



### 团队协作

![](D:\study\Git\img\8.png)



### 跨团队协助

![](D:\study\Git\img\9.png)



## IDEA整合Git

![image-20210515165835611](img\22.png)





### **提交版本**

![image-20210515183949384](img\23.png)



### **版本切换**

![image-20210515184129592](img\24.png)



### **分支**

![image-20210515184408982](img\25.png)

![image-20210516103637164](img\26.png)

 

### **提交、拉取**

![image-20210516153458371](img\27.png)

- **github**建议使用 **免密SSH**
- **gitee**建议使用 **https**



**Gitee操作同上**



