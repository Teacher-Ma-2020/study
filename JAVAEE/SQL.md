

# 数据库

## 创建数据库

```sql
create database sql_test
```

## 删除表

```sql
drop table person;
```



# 表修改

## 增加列

```sql
alter table person add birthday DATE;
```

## 修改列的类型

```sql
alter table user modify userid varchar(10);
```

## 修改列的名称

```sql
alter table user change userid user varchar(10);
```



# 添加约束



## 主键约束

```sql
create database web;

create table Students(
    sname varchar(10),
    sex varchar(10),
    age int,
    phone int
);

alter table students add primary key (sname)
```



## 外键约束

```sql
create table nclass(
      id int primary key,
			name varchar(10)
);


create table student(
       id int primary key,
			 name varchar(10),
			 class_id int,
			 constraint pk_student foreign key(class_id) references nclass(id)
);
```

```sql
alter table book add constraint pk_person foreign key (person_id) references person(id);
```



## 非空约束

```sql
alter table person add constraint pk_p unique (id);
```

## 自增约束

```mysql
alter table 【数据库名.】表名称 modify 字段名 数据类型 auto_increment;
alter table web.user modify id int auto_increment;
```



# CRUD



## 插入

```sql
INSERT INTO USERs(id,na,PASSWORDs,email)  VALUES('4','张三','123546','asd');
INSERT INTO USERs(id,na,PASSWORDs,email)  VALUES('5','张三','123546','asd');
INSERT INTO USERs(id,na,PASSWORDs,email)  VALUES('6','张4','1235467','as');
INSERT INTO USERs(id,na,PASSWORDs,email)  VALUES('7','张5','12354678','a');
```



## 查询

```sql
SELECT * from users
```

## 改

```sql
update smbms.smbms_user set userPassword='000000' where id>1;
```

## 模糊查询

```sql
select * from smbms.smbms_user u,smbms.smbms_role r where u.userRole=r.id and u.userName like '%李%';
```



## 删除

```sql
DELETE FROM 表名称 WHERE 列名称 = 值，
```

