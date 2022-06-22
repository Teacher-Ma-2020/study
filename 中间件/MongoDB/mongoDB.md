# 介绍

**官方**

- mongodb是一个文档数据库，旨在方便应用的开发和编辑

**百度百科**

- mongodb是一个**基于分布式文件存储的数据库**，由C++编写，旨在为web应用提供可扩展高性能数据库存储解决方案

mongodb是一个非关系型文档数据库



**特点**

- 面向集合存储，易存储对象类型的数据
- 支持查询，以及动态查询
- 文件存储格式为BSON(一种JSON的扩展)
- 支持复制和故障恢复和分片
- 支持事务



# 安装（docker）

```shell
#拉取镜像
docker pull mongo:5.0.5
#启动mongo容器
docker run --name mongo -d -p 27017:27017 mongo:5.0.5
#进入mongo
docker exec -it 5f213 bash
```



# 概念

**库** <DataBase>

- mongo的库与传统的mysql关系库概念相同，用来通过不同库隔离不同应用的数据

**集合** <Collection>

- 集合存于数据库中，一个库中可以创建多个集合，每个集合没有固定的结构

**文档** <Document>

文档数据是一组组kv对，**即BSON**



**传统**关系型数据库 对比 **MongoDB**

![image-20220610094039883](img\1.png)



# 基本操作

## 库操作

**查看所有数据库**

```shell
show dbs 
show databases  
```

- 只能查看的包含内容数据库
- 无法show 内容为空的数据库

**创建数据库**

```shell
use 库名  #创建并切换
```

**查看当前库**

```json
db
```

**删除数据库**

```shell
db.dropDatabase()
```



## 集合操作

**查看库中的所有集合**

```shell
show tables 
show collections
```

**创建集合**

```shell
db.createCollection('集合名称'，[options])
```

- options选项可选

| 字段   | 类型    | 描述                                                         |
| ------ | ------- | ------------------------------------------------------------ |
| capped | boolean | (可选)如果为true,则创建固定集合。固定集合是指有着固定大小的集合，当达到最大值时，它会自动覆盖最早的文档。**当该值为true时，必须指定size参数**。 |
| size   | 数值    | (可选)为固定集合指定一个字节最大值，如果capped为true,则需要指定该字段 |
| max    | 数值    | （可选）指定固定集合中包含文档的最大数量                     |

```shell
db.createCollection('products',{max:100,capped:true,size:5000})
```

- 该集合字节大小超过5000，或者文档条数超过100，则覆盖最早的文档



**集合中插入数据**

```shell
db.[集合名].insert([数据])
db.zzz.insert({name:'zzz'})
```

- 若集合不存在，则会自动创建该集合





## 文档操作

**插入文档**

- 单条文档

```shell
db.集合名称.insert({"name":"zcj","age":18})
```

- 多条文档

```shell
db.zcjList.insert([
    {"name":"zcj","age":"18"},
    {"name":"zcj2","age":"22"}
])
```

- 脚本方式

![image-20220610105540414](img\2.png)

- 查看文档内容

  ```shell
  db.zcjList.find()
  ```

  ![image-20220610111514564](img\3.png)

**删除文档**

- 根据唯一ID删除（传入自动生成 或者 自己给的id值）

```shell
db.zcjList.remove({_id:ObjectId("62a2b063b0330e9b96eb1c01")})
```

- 根据条件删除 (删除集合中name为‘zcj’的所有数据)

```shell
 db.zcjList.remove({"name":"zcj"})
```

- 删除集合中的所有

```shell
db.zcjList.remove({})
```



**更新文档**

```mong
db.库名.update({条件},{修改})
db.zcjList.update({"age":"19"},{"name":"zcj_10"})
```

- 该修改不会把某个字段修改，而是将该文档删除后，再添加该字段

![image-20220610143220398](img\4.png)



```shell
db.zcjList.update({"name":"zcj_8"},{$set:{"bir":"1119"}});
```

- 用过$set可以保留原始数据

![image-20220610144008444](img\5.png)



## 文档查询

MongoDB查询文档使find（）方法

```shell
db.集合名称.find(query,projection)
```

- query:可选，使用查询操作符指定的查询条件
- projection：可选，返回指定的值

```shell
db.集合名称.find().protty() #格式化显示，更美观
```

![image-20220610145631617](img\6.png)

```shell
db.zcjList.find({age:{$lte:18}}) #查询age小于等于18
db.zcjList.find({age:18,name:"zcj_1"})  #条件and查询
```



**or条件查询**

MongoDB OR 条件语句使用了关键字 **$or**,语法格式如下：

> 类似于 WHERE 语句：WHERE key1=value1 or key2=value2

```sql
> db.集合名称.find(
   {
      $or: [
         {key1: value1}, {key2:value2}
      ]
   }
).pretty()
```



**AND 和 OR 联合**

> 类似SQL语句为：'where age >50 AND (name = '编程不良人' OR name = 'MongoDB')'

```sql
> db.集合名称.find({"age": {$gt:50}, $or: [{"name": "编程不良人"},{"name": "MongoDB"}]}).pretty();
```

**数组查询**

```sql
-- 测试数据
> db.集合名称.insert({ "_id" : 11, "age" : 29, "likes" : [ "看电视", "读书xx", "美女" ], "name" : "不良人_xx_11" })
-- 执行数组查询
> db.users.find({likes:"看电视"})
-- $size 按照数组长度查询
> db.users.find({likes:{$size:3}});
```

**排序**

```sql
> db.集合名称.find().sort({name:1,age:1}),
- 1 升序  -1 降序

#类似于 SQL 语句为: 'limit start,rows'
```

**分页**

```sql
> db.集合名称.find().sort({条件}).skip(start).limit(rows);

#类似于 SQL 语句为: 'limit start,rows'
```

**总条数**

```sql
> db.集合名称.count();
> db.集合名称.find({"name":"编程不良人"}).count();

#类似于 SQL 语句为: 'select count(id) from ....'
```

**去重**

```sql
> db.集合名称.distinct('字段')

#类似于 SQL 语句为: 'select distinct name from ....'
```

**指定返回字段**

```sql
> db.集合名称.find({条件},{name:1,age:1}) 
- 参数2: 1 返回  0 不返回    `注意:1和0不能同时使用`
```

**$type**

$type操作符是基于BSON类型来检索集合中匹配的数据类型，并返回结果。

MongoDB 中可以使用的类型如下表所示：

![img](https://img-blog.csdnimg.cn/bf29cabd24534432b55a62c2199f578d.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAzphMTM6Y,size_20,color_FFFFFF,t_70,g_se,x_16)

如果想获取 “col” 集合中 title 为 String 的数据，你可以使用以下命令：

```sql
db.col.find({"title" : {$type : 2}}).pretty();
或
db.col.find({"title" : {$type : 'string'}}).pretty();
```

如果想获取 “col” 集合中 tags 为 Array 的数据，你可以使用以下命令：

```sql
db.col.find({"tags" : {$type : 'array'}}).pretty();
```



# 索引

- 索引通常能够极大的提高查询效率
- 索引存储在一个易于遍历读取的数据集合中，索引是对数据库表中一列或多列的值进行排序的一种结构。

创建索引

```sql
> db.集合名称.createIndex(keys, options)
> db.集合名称.createIndex({"title":1,"description":-1})
```

`说明: 语法中 Key 值为你要创建的索引字段，1 为指定按升序创建索引，如果你想按降序来创建索引指定为 -1 即可。`

createIndex() 接收可选参数，可选参数列表如下：

| Parameter            | Type          | Description                                                  |
| -------------------- | ------------- | ------------------------------------------------------------ |
| `background`         | Boolean       | 建索引过程会阻塞其它数据库操作，background可指定以后台方式创建索引，即增加 “background” 可选参数。 “background” 默认值为**false**。 |
| `unique`             | Boolean       | 建立的索引是否唯一。指定为true创建唯一索引。默认值为**false**. |
| `name`               | string        | 索引的名称。如果未指定，MongoDB的通过连接索引的字段名和排序顺序生成一个索引名称。 |
| `sparse`             | Boolean       | 对文档中不存在的字段数据不启用索引；这个参数需要特别注意，如果设置为true的话，在索引字段中不会查询出不包含对应字段的文档.。默认值为 **false**. |
| `expireAfterSeconds` | integer       | 指定一个以秒为单位的数值，完成 TTL设定，设定集合的生存时间。 |
| `v`                  | index version | 索引的版本号。默认的索引版本取决于mongod创建索引时运行的版本。 |
| `weights`            | document      | 索引权重值，数值在 1 到 99,999 之间，表示该索引相对于其他索引字段的得分权重。 |
| `default_language`   | string        | 对于文本索引，该参数决定了停用词及词干和词器的规则的列表。 默认为英语 |
| `language_override`  | string        | 对于文本索引，该参数指定了包含在文档中的字段名，语言覆盖默认的language，默认值为 language. |

1、查看集合索引

```sql
> db.集合名称.getIndexes()
```

2、查看集合索引大小

```sql
> db.集合名称.totalIndexSize()
```

3、删除集合所有索引

```sql
> db.集合名称.dropIndexes()
```

4、删除集合指定索引

```sql
> db.集合名称.dropIndex("索引名称")
```

# 聚合

Mongo中的聚合主要用于处理数据（如平均值、求和等），并返回计算后的数据

现在我们通过以上集合计算每个作者所写的文章数，使用aggregate()计算结果如下：

```sql
> db.集合名称.aggregate([{$group : {_id : "$by_user", num_tutorial : {$sum : 1}}}])
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/feb65967974d423fa2acb6472411d369.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBAzphMTM6Y,size_20,color_FFFFFF,t_70,g_se,x_16)



# 整合springBoot

引入依赖

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

编写配置

```properties
# mongodb 没有开启任何安全协议
# mongodb(协议)://121.5.167.13(主机):27017(端口)/baizhi(库名)
spring.data.mongodb.uri=mongodb://121.5.167.13:27017/baizhi


# mongodb 存在密码
#spring.data.mongodb.host=tx.chenyn.cn
#spring.data.mongodb.port=27017
#spring.data.mongodb.database=baizhi
#spring.data.mongodb.username=root
#spring.data.mongodb.password=root
```

面试造航母

