# Mysql



## SQL语句分类

- ​	DQL（数据查询语言）: 查询语句，凡是select语句都是DQL。
- ​	DML（数据操作语言）：insert delete update，对表当中的数据进行增删改。
- ​	DDL（数据定义语言）：create drop alter，对表结构的增删改。
- ​	TCL（事务控制语言）：commit提交事务，rollback回滚事务。(TCL中的T是Transaction)
- ​	DCL（数据控制语言）: grant授权、revoke撤销权限等。



## Sql常用命令

### 基本操作

DQL执行顺序

```sql
select		    5
		..
	from		1	
		..
	where		2
		..
	group by	3
		..
	having		4
		..
	order by	6
		..
```



```sql
select ename,sal*12 '年薪' from emp
select * from emp where sal BETWEEN 1000 and 2000  -- 相当于 1000<=sal<=2000
select * from emp where sal>2000
SELECT * from emp where COMM is not null
SELECT * from emp where JOB ='SALESMAN' or JOB='MANAGER'

SELECT * from emp where JOB in ('SALESMAN','MANAGER')

select e.ENAME,e.DEPTNO,(select dept.DNAME from dept where dept.DEPTNO=e.DEPTNO) from  emp e;
```



**模糊查询**

```sql
SELECT * from emp where ENAME like '_A%';
```

**排序**

```sql
SELECT * from emp ORDER BY SAL DESC;
```

**去重**

```sql
select DISTINCT job from emp



## 分组查询

当一条sql语句没有group by的话，整张表的数据会自成一组。


聚合函数

- ​	count 计数
- ​	sum 求和
- ​	avg 平均值
- ​	max 最大值
- ​	min 最小值

​```sql
SELECT  sum(COMM) FROM emp        -- sum自动忽略null

SELECT  count(*) FROM emp   -- 查询多少条数据
SELECT  count(comm) FROM emp    -- 查询comm不为null的数据
```



```sql
SELECT job,MAX(sal) FROM emp GROUP BY JOB   -- 查询每个职业的最大值

-- 过滤小于于2000
SELECT job,MAX(sal) FROM emp GROUP BY JOB HAVING MAX(sal) >2000
SELECT job,MAX(sal) FROM emp where sal>2000 GROUP BY JOB 
```

**nuion 表拼接**

```sql
select  * from emp where emp.JOB ="MANAGER" 
UNION 
select * from emp where emp.JOB ="SALESMAN"
```

**limit分页**

```sql
-- 查询前3
select * from emp ORDER BY emp.SAL DESC limit 0,3
select * from emp ORDER BY emp.SAL DESC limit 3


select * from emp ORDER BY emp.SAL DESC limit 6.3
```



**插入语句**

```sql
insert into t_student(no,name,sex,classno,birth) values(1,'zhangsan','1','gaosan1ban', '1950-10-12');
```

**修改**

```sql
update dept set loc = 'SHANGHAI', dname = 'RENSHIBU' where deptno = 10;
```

**删除**

```sql
语法格式：
		delete from 表名 where 条件;
		
		
delete from dept1 where deptno = 10;

delete from dept1;  -- 删除全部数据
```







### 多表查询

内连接：

- ​	等值连接
- ​	非等值连接
- ​	自连接

外连接：

- ​	左外连接（左连接）
- ​	右外连接（右连接）

```sql
-- 展示笛卡尔积
select * from emp,dept

-- 避免笛卡尔积（只是显示的为有效数据，查询的实际的还是笛卡尔积的数量）
select * from emp,dept where emp.DEPTNO = dept.DEPTNO   

```

**内连接**

- 两个表的数据必须相互对应 否则不显示

```sql
-- 内连接 （inner可省略）
select e.ENAME, d.DNAME from emp e JOIN dept d on e.DEPTNO=d.DEPTNO
select e.ENAME, d.DNAME from emp e inner JOIN dept d on e.DEPTNO=d.DEPTNO 

select e.ENAME,e.SAL,s.GRADE from emp e JOIN salgrade s on e.SAL BETWEEN s.LOSAL and s.HISAL 
```

**外连接( 左、右)**

- 以左或者右的表进行查询，不匹配会返回null

```sql
select * from emp LEFT JOIN dept on emp.DEPTNO=dept.DEPTNO
```





**自连接**

```sql
select e1.ENAME  员工,e2.ENAME 领导 from emp e1 ,emp e2 where e1.MGR=e2.EMPNO
```



**区别**

**内连接：**

- 假设A和B表进行连接，使用内连接的话，凡是A表和B表能够匹配上的记录查询出来，这就是内连接。
- AB两张表没有主副之分，两张表是平等的。

**外连接：**

- 假设A和B表进行连接，使用外连接的话，AB两张表中有**一张表是主表**，**一张表是副表**
- 主要查询主表中的数据，捎带着查询副表，当副表中的数据没有和主表中的数据匹配上，副表自动模拟出NULL与之匹配。

外连接的分类？
	左外连接（左连接）：表示左边的这张表是主表。
	右外连接（右连接）：表示右边的这张表是主表。



## 表操作

**建表**

```sql
	create table t_student(
		no bigint,
		name varchar(255),
		sex char(1),
		classno varchar(255),
		birth char(10)
	);
```

**删表**

```sql
drop table  t_student;
drop table if exists t_student;   -- 当这个表存在的话删除。
```







## 约束

什么是约束？常见的约束有哪些呢？

​	在创建表的时候，可以给表的字段添加相应的约束，添加约束的目的是为了保证表中数据的合法性、有效性、完整性。

​	常见的约束有哪些呢？

- 非空约束(not null)：约束的字段不能为NULL
- 唯一约束(unique)：约束的字段**不能重复**，但**可以为null**（null可以重复）
- 主键约束(primary key)：约束的字段既不能为NULL，也不能重复（简称PK）
- 外键约束(foreign key)：...（简称FK）
- 检查约束(check)：注意Oracle数据库有check约束，但是mysql没有，目前mysql不支持该约束。

```sql
-- 主表
create table t_class(
    cno int,
    cname varchar(255),
    primary key(cno)
);

-- 从表
create table t_student(
    sno int,
    sname varchar(255),
    classno int,   -- 外键约束
    primary key(sno),
    foreign key(classno) references t_class(cno)
);
```

外键值可以为NULL？

- 外键**可以为NULL**。
  	

外键字段引用其他表的某个字段的时候，被引用的字段必须是主键吗？

- 注意：被引用的字段不一定是主键，但至少具有**unique约束**（唯一约束）



## 存储引擎

```sql
CREATE TABLE `t_x` (
    `id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 建表的时候可以指定存储引擎，也可以指定字符集。
```

> **MyISAM**

MyISAM这种存储引擎**不支持事务。**

MyISAM是mysql最常用的存储引擎，但是这种引擎不是默认的。

MyISAM采用三个文件组织一张表：
				xxx.frm（存储格式的文件）
				xxx.MYD（存储表中数据的文件）
				xxx.MYI（存储表中索引的文件）

- **优点**：可被压缩，节省存储空间。并且可以转换为只读表，提高检索效率。
- **缺点**：不支持事务。



> **InnoDB** (默认)

优点：支持事务、行级锁、外键等。这种存储引擎数据的安全得到保障。
			

表的结构存储在xxx.frm文件中
		数据存储在tablespace这样的表空间中（逻辑概念），无法被压缩，无法转换成只读。
		这种InnoDB存储引擎在MySQL数据库崩溃之后提供自动恢复机制。
		InnoDB支持级联删除和级联更新。



> **MEMORY**

缺点：不支持事务。数据容易丢失。因为所有数据和索引都是存储在内存当中的。

优点：查询速度最快。

以前叫做HEPA引擎。





## 事务

事务（Transaction）



**1、什么是事务？**

一个事务是一个**完整的业务逻辑单元**，不可再分。

```sql
-- 比如：银行账户转账，从A账户向B账户转账10000.需要执行两条update语句：
update t_act set balance = balance - 10000 where actno = 'act-001';
update t_act set balance = balance + 10000 where actno = 'act-002';
```

以上两条DML语句**必须同时成功**，或者同时失败，不允许出现一条成功，一条失败。

要想保证以上的两条DML语句同时成功或者同时失败，那么就需要使用数据库的“事务机制”。



**2、和事务相关的语句只有：DML语句。（insert delete update）**

- 因为它们这三个语句都是和数据库表当中的“数据”相关的。
- 事务的存在是为了保证数据的完整性，安全性。



**3、事务的特性**

​		事务包括四大特性：ACID

- A: **原子性**：事务是最小的工作单元，不可再分。
- C: **一致性**：事务必须保证多条DML语句同时成功或者同时失败。
- I：**隔离性**：事务A与事务B之间具有隔离。
- D：**持久性**：持久性说的是最终数据必须持久化到硬盘文件中，事务才算成功的结束。



> 回滚

```sql
start TRANSACTION;  -- 开启事务

INSERT INTO student(NAME)  VALUES('zcj3'),('zcj4');

ROLLBACK;  -- 回滚
```

> 提交

```sql
start TRANSACTION;  -- 开启事务
INSERT INTO student(NAME)  VALUES('zcj3'),('zcj2');
COMMIT;  -- 提交
```

> 设置 回滚点S

```sql
start TRANSACTION;

INSERT INTO student(NAME)  VALUES('zcj33'),('zcj44');

SAVEPOINT n;  -- 保存事务点

INSERT INTO student(NAME)  VALUES('zcj55'),('zcj66');
ROLLBACK n;  -- 回滚到n   
```



> 事务级别

**uncommitted**;

两个客户端中，一个客户端在事务中操作数据，即使未提交，另一个客户端也能访问



**committed**;

一个客户端在事务中，未提交的数据，另一个客户端无妨访问



**repeatable read;**

**可重复读**(Repeated Read)：可重复读。在同一个事务内的查询都是事务开始时刻一致的，InnoDB默认级别。在事务中，读取的数据的始终不变



**Serializable**

串行读(Serializable)：完全串行化的读，每次读都需要获得表级共享锁，**读写相互都会阻塞**





## 索引

索引就相当于一本书的目录，通过目录可以快速的找到对应的资源。



> 索引为什么可以提高检索效率呢？

- 其实最根本的原理是**缩小了扫描的范围**。
- **主键**和**唯一约束**自动添加索引



索引虽然可以提高检索效率，但是**不能随意的添加索引**，因为索引也是数据库当中的对象，也需要数据库不断的维护。是有维护成本的。

- **表中的数据经常被修改这样就不适合添加索引**，因为数据一旦修改，索引需要重新排序，进行维护。
- 索引采用B+树 左小右大





**添加索引**

- create index 索引名称 on 表名(字段名);
- **CREATE INDEX emp_sal on emp(sal);** 

**删除索引**

- drop index 索引名称 on 表名;
- **DROP INDEX emp_sal_index on emp**

**索引的实现原理**
		通过B Tree缩小扫描范围，底层索引进行了排序，分区，索引会携带数据在表中的“物理地址”，最终通过索引检索到数据之后，获取到关联的物理地址，通过物理地址定位表中的数据，效率是最高的。

select ename from emp where ename = 'SMITH';
通过索引转换为：
select ename from emp where 物理地址 = 0x3;



**索引的分类**
	单一索引：给单个字段添加索引
	复合索引: 给多个字段联合起来添加1个索引
	主键索引：主键上会自动添加索引
	唯一索引：有unique约束的字段上会自动添加索引
	....

**索引什么时候失效**
	select ename from emp where ename like '%A%';

​	**模糊查询的时候**，第一个通配符使用的是%，这个时候索引是失效的。



**未加索引前**   （查询全部）

```sql
 EXPLAIN select ename,sal from emp where ename = 'SMITH';  
```

![image-20210511170047370](img\1.png)

**加了索引后  （部分查询）**

![image-20210511170915764](img\2.png)







## 视图

站在不同的角度去看到数据。（同一张表的数据，通过不同的角度去看待）。

```sql
create view myview as select empno,ename from emp; -- 创建视图

drop view myview;  -- 删除视图
```

```sql
	mysql> select * from myview;
	+-------+--------+
	| empno | ename  |
	+-------+--------+
	|  7369 | SMITH  |
	|  7499 | ALLEN  |
	|  7521 | WARD   |
	|  7566 | JONES  |
	|  7654 | MARTIN |
	|  7698 | BLAKE  |
	|  7782 | CLARK  |
	|  7788 | SCOTT  |
	|  7839 | KING   |
	|  7844 | TURNER |
	|  7876 | ADAMS  |
	|  7900 | JAMES  |
	|  7902 | FORD   |
	|  7934 | MILLER |
	+-------+--------+

	create table emp_bak as select * from emp;
	create view myview1 as select empno,ename,sal from emp_bak;
	update myview1 set ename='hehe',sal=1 where empno = 7369; // 通过视图修改原表数据。
	delete from myview1 where empno = 7369; // 通过视图删除原表数据。
```



**视图的作用**

​		视图可以隐藏表的实现细节。保密级别较高的系统，数据库只对外提供相关的视图，java程序员，只对视图对象进行CRUD。





## 三范式

第一范式：任何一张表都应该有**主键**，并且每一个字段**原子性不可再分**

第二范式：建立在第一范式的基础之上，所有非主键字段完全依赖主键，**不能**产生**部分依赖。**

第三范式：建立在第二范式的基础之上，所有非主键字段直接依赖主键，**不能**产生**传递依赖。**

