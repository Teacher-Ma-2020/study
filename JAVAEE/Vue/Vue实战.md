

[TOC]



# Vue实战

## 1. Vue 引言

> `渐进式` JavaScript 框架 

```markdown
# 渐进式
   1. 易用  html css javascript
   2. 高效  开发前端页面 非常高效 
   3. 灵活  开发灵活 多样性

# 总结
		Vue 是一个javascript 框架

# 后端服务端开发人员: 
		Vue 渐进式javascript框架: 让我们通过操作很少的DOM,甚至不需要操作页面中任何DOM元素,就很容易的完成数据和视图绑定  双向绑定 MVVM  
		
		注意: 日后在使用Vue过程中页面中不要在引入Jquery框架
		
		htmlcss--->javascript ----->jquery---->angularjs -----> Vue
 
 # Vue 作者
 		 尤雨溪     
```

-------



## 2. Vue入门

### 2.1	下载Vuejs

```html
//开发版本:
	<!-- 开发环境版本，包含了有帮助的命令行警告 -->
	<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>

//生产版本:
	<!-- 生产环境版本，优化了尺寸和速度 -->
	<script src="https://cdn.jsdelivr.net/npm/vue"></script>
//vue
    <script src="https://unpkg.com/vue@next"></script>
```

### 2.2 Vue第一个入门应用

```html
<body>

    你好
    <div id="app">
        {{msg.to}}
        <h3>name:{{user.name}}    msg:{{user.msg}}</h3>      
        <h4>{{lists}}--{{lists[0]}}</h4>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <script>
        const app=new Vue({
            el:"#app",
            data:{
                msg: "hello vue",
                user:{name:"zcj",msg:"123"},   //使用  .  访问
                lists:["a1","a2","a3","a4"]    //使用  下标 访问
            }
        })
    </script>
</body>
```

```markdown
# 总结:
			1.vue实例(对象)中el属性: 	代表Vue的作用范围  日后在Vue的作用范围内都可以使用Vue的语法
			2.vue实例(对象)中data属性: 用来给Vue实例绑定一些相关数据, 绑定的数据可以通过{{变量名}}在Vue作用范围内取出
			3.在使用{{}}进行获取data中数据时,可以在{{}}中书写表达式,运算符,调用相关方法,以及逻辑运算等
			4.el属性中可以书写任意的CSS选择器[jquery选择器],但是在使用Vue开发是推荐使用 id选择器
			5.el  id选择器使用 #   class选择器使用.  (.app)
```

------



## 3. v-text和v-html

### 3.1 v-text

> `v-text`:用来获取data中数据将数据以文本的形式渲染到指定标签内部             类似于javascript 中 innerText

```html
<body>
    你好
    <div class="app">
        <h3>{{msg}}，芜湖</h3>
        <h4 v-text="msg">芜湖</h4>    
    </div>

    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <script>
        const app=new Vue({
            el:".app",
            data:{
                msg: "hello vue",
            }
        })  
    </script>
</body>
```

```markdown
# 总结
			1.{{}}(插值表达式)和v-text获取数据的区别在于 
					a.使用v-text取值会将标签中原有的数据覆盖 使用插值表达式的形式不会覆盖标签原有的数据
					b.使用v-text可以避免在网络环境较差的情况下出现插值闪烁
```



### 3.2 v-html

> `v-html`:用来获取data中数据将数据中含有的html标签先解析

```html
<body>
    你好
    <div class="app">
        <h4 v-text="msg">芜湖</h4>
        <h4 v-html="msg">芜湖</h4>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <script>
        const app=new Vue({
            el:".app",
            data:{
                msg: "<a href=''>hello vue</a>",    //html 先解析再输出
            }
        })
    </script>
</body>
```

----





## 4.vue中事件绑定(v-on)

### 4.1 绑定事件基本语法

```html
<body>
    你好
    <div class="app">
        <h4 v-text="msg">芜湖</h4>
        <h4>年龄：{{age}}</h4>
        <input type="button" value="年龄+1" v-on:click="changeAge">
    </div>

    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <script>
        const app=new Vue({
            el:".app",
            data:{
                msg: "hello vue",
                age:23
            },
            methods:{                    //存放vue事件
                changeAge:function () {  //事件名
                    this.age++;
                }
            }
        })
    </script>
</body>
```

```markdown
# 总结:
		事件  事件源:发生事件dom元素  事件: 发生特定的动作  click....  监听器  发生特定动作之后的事件处理程序 通常是js中函数
		1.在vue中绑定事件是通过v-on指令来完成的 v-on:事件名 如  v-on:click
		2.在v-on:事件名的赋值语句中是当前时间触发调用的函数名
		3.在vue中事件的函数统一定义在Vue实例的methods属性中
		4.在vue定义的事件中this指的就是当前的Vue实例,日后可以在事件中通过使用this获取Vue实例中相关数据
```



### 4.2 Vue中事件的简化语法

```html
<body>
    你好
    <div class="app">
        <h4 v-text="msg">芜湖</h4>
        <h4>年龄：{{age}}</h4>
        <input type="button" value="年龄+1" v-on:click="changeAge">
        <input type="button" value="年龄-1" @click="reduceAge">
    </div>

    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <script>
        const app=new Vue({
            el:".app",
            data:{
                msg: "hello vue",
                age:23
            },
            methods:{                    //存放vue事件
                changeAge:function () {  //事件名
                    this.age++;
                },
                reduceAge:function () {
                    this.age--;
                }
            }
        })
    </script>
</body>
```

```markdown
# 总结:
			1.日后在vue中绑定事件时可以通过@符号形式 简化  v-on 的事件绑定
```



### 4.3 Vue事件函数两种写法

```javascript
changeAge:function () {  //事件名
    this.age++;
},
reduceAge() {
    this.age--;
}
```

```markdown
# 总结:
			1.在Vue中事件定义存在两种写法  一种是 函数名:function(){}  推荐    一种是  函数名(){} 推荐
```



### 4.4 Vue事件参数传递

```html
<body>
    你好
    <div class="app">
        <h4 v-text="msg">芜湖</h4>
        <h4>年龄：{{count}}</h4>

        <input type="button" value="改变count" @click="changeCount(13)">
    </div>

    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <script>
        const app=new Vue({
            el:".app",
            data:{
                count:1
            },
            methods:{                    //存放vue事件
                changeCount(count){
                    this.count=count;
                }
            }
        })
    </script>
</body>
```

```markdown
# 总结:
	1.在使用事件时,可以直接在事件调用出给事件进行参数传递,在事件定义出通过定义对应变量接收传递的参数
```

-----





## 5.v-show v-if v-bind

### 5.1 v-show

> `v-show`:用来控制页面中某个标签元素是否展示        底层使用控制是 display 属性

```html
<div id="app">
    <!-- v-show: 用来控制标签展示还是隐藏的 -->
    <h2 v-show="false">芜湖!</h2>
    <h2 v-show="show">芜湖 这是vue中定义变量true!</h2>
    <input type="button" value="展示隐藏标签" @click="showmsg">
</div>
<!--引入vue.js-->
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script>
    const app = new Vue({
        el:"#app",
        data:{
            show:false,
        },
        methods:{
            showmsg(){
               this.show =  !this.show;
            }
        }
    })
</script>
```

```markdown
# 总结
			1.在使用v-show时可以直接书写boolean值控制元素展示,也可以通过变量控制标签展示和隐藏
			2.在v-show中可以通过boolean表达式控制标签的展示课隐藏
```



### 5.2 v-if 

> `v-if`: 用来控制页面元素是否展示                底层控制是DOM元素    操作DOM

```html
<div id="app">
    <h2 v-if="false">芜湖</h2>
    <h2 v-if="show">zcj</h2>
</div>
<!--引入vue.js-->
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script>
    const app = new Vue({
        el:"#app",
        data:{
            show:false
        },
        methods:{

        }
    });
</script>
```

if 和 show的区别

- if是添加和删除   show 是隐藏和显示
- v-if 的初始化较快，但切换代价高；v-show 初始化慢，但切换成本低
- v-if适合运营条件不大可能改变；
  v-show适合频繁切换。



### 5.3 v-bind

> `v-bind`: 用来绑定标签的属性从而通过vue动态修改标签的属性

```html
<body>
    <div v-bind:title="msg" id="app">              
        <img v-bind:border="b" src="../img/faker.jpg">

        <font v-bind:color="color">Vue</font>
    </div>

    <script  src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <script>
        const app=new Vue({
            el:"#app",
            data:{
                msg:"faker",                 //通过bind 改变标签
                color:"yellow",
                b:"5px"
            },
            methods:{
            }
        })
    </script>
</body>
```

### 5.4 v-bind 简化写法

> ​	vue为了方便我们日后绑定标签的属性提供了对属性绑定的简化写法如 `v-bind:属性名` 简化之后 `:属性名`

```html
<body>
<!--    简化-->
    <div :title="msg" id="app">
        <img :border="b" src="../img/faker.jpg">

        <font :color="color">Vue</font>
    </div>

    <script  src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <script>
        const app=new Vue({
            el:"#app",
            data:{
                msg:"faker",
                color:"yellow",
                b:"5px"
            },
            methods:{
            }
        })
    </script>
</body>
```

--------



## 6.v-for的使用

> `v-for`: 作用就是用来对对象进行遍历的(数组也是对象的一种)

```html
<body>
    <div class="app">
        <span v-for="(value,key,index) in user">
            {{index}}、{{key}}、{{value}} <br>
        </span>
        <span v-for="u in lists">
            {{u}}
        </span>
        <span v-for="user in users">
            {{user.name}}  {{user.age}}<br>
        </span>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <script>
        const app=new Vue({
            el:".app",
            data:{
                user:{name:"zcj",msg:"123"},
                lists:["a1","a2","a3","a4"],
                users:[
                    {name:"zcj",age:18},
                    {name:"zcj2",age:20}
                ]
            }
        })
    </script>
</body>
```

```markdown
# 总结
	1.在使用v-for的时候一定要注意加入:key 用来给vue内部提供重用和排序的唯一key 
```

----

综合训练

```html
<body>
    <div id="app">
        <input type="text" v-model="msg"> <input type="button" value="添加" @click="save">
        <br>
        <ul>
            <li v-for="item,index in lists">
                {{index+1}} {{item}} <a href="javascript:;" @click="deleteRow(index)">删除</a>
            </li>
        </ul>
        <br>
        <span>总数量：{{lists.length}}</span>
        <br><input  type="button" value="删除所有" v-show="lists.length!=0" @click="deleteAll">

    </div>

    <script  src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <script>
        const app=new Vue({
            el:"#app",
            data:{
                msg:"",
                lists:["芜湖","起飞"],
            },
            methods:{
                save(){
                    this.lists.push(this.msg);
                    this.msg="";
                },
                deleteRow(index){
                    this.lists.splice(index,1);
                },
                deleteAll(){
                    this.lists=[];
                }
            }
        })
    </script>
</body>
```





## 7 .v-model 双向绑定

> `v-model`: 作用用来绑定标签元素的值与vue实例对象中data数据保持一致,从而实现双向的数据绑定机制

```html
<body>
    <div id="app">
        <input type="text" v-model="message">
        <span>{{message}}</span>
        <input type="button" value="清空" @click="changeValue">
    </div>

    <script  src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <script>
        const app=new Vue({
            el:"#app",
            data:{
                message:""
            },
            methods:{
                changeValue(){
                    this.message="";
                }
            }
        })
    </script>
</body>
```

```markdown
# 总结
		1.使用v-model指令可以实现数据的双向绑定 
		2.所谓双向绑定 表单中数据变化导致vue实例data数据变化   vue实例中data数据的变化导致表单中数据变化 称之为双向绑定

# MVVM架构  双向绑定机制
	Model: 数据  Vue实例中绑定数据
	
	VM:   ViewModel  监听器

	View:  页面  页面展示的数据
```

-----





## 8. 事件修饰符

> `修饰符`: 作用用来和事件连用,用来决定事件触发条件或者是阻止事件的触发机制

```markdown
# 1.常用的事件修饰符
	.stop
	.prevent
	.capture
	.self
	.once
	.passive
```

**事件冒泡**

```html
<div id="app">
    <div class="aa" @click="divClick">
        <input type="button" value="按钮" @click="btnClick">
        <input type="button" value="按钮" @click="btnClick">
        <input type="button" value="按钮" @click="btnClick">
        <input type="button" value="按钮" @click="btnClick">
    </div>
</div>
```

如果点击了子div的事件  父div的事件也会执行



### 8.1 stop事件修饰符

> 用来阻止事件冒泡

```html
<body>
<div id="app">
    <div class="aa" @click="divClick">
    <input type="button" value="按钮" @click.stop="btnClick">
</div>
</div>
<script  src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script>
    const app=new Vue({
        el:"#app",
        data:{},
        methods:{
            btnClick(){
                alert("点击")
            },
            divClick(){
                alert("div点击")
            }
        }
    })
</script>
</body>
```



### 8.2 prevent 事件修饰符

> 用来阻止标签的默认行为

```html
		<!--用来阻止事件的默认行为-->
<a href="http://www.baizhibest.com/" @click.prevent="aClick">百知教育</a>
```

### 8.3 self 事件修饰符

> 用来针对于当前标签的事件触发     ===========> 只触发自己标签的上特定动作的事件     只关心自己标签上触发的事件 不监听事件冒泡

```html
<div id="app">
    <div class="aa" @click.self="divClick">
        <input type="button" value="按钮" @click="btnClick">
        <input type="button" value="按钮" @click="btnClick">
        <input type="button" value="按钮" @click="btnClick">
        <input type="button" value="按钮" @click="btnClick">
    </div>
</div>
```

### 8.4 once 事件修饰符

> once 一次 作用:  就是让指定事件只触发一次

```html
    <!--
    .prevent : 用来阻止事件的默认行为
    .once    : 用来只执行一次特定的事件
    -->
    <a href="http://www.baizhibest.com/" @click.prevent.once="aClick">百知教育</a>
```

----





## 9. 按键修饰符

> 作用: 用来与键盘中按键事件绑定在一起,用来修饰特定的按键事件的修饰符

```markdown
# 按键修饰符
	.enter
	.tab
	.delete (捕获“删除”和“退格”键)
	.esc
	.space
	.up
	.down
	.left
	.right
```

### 9.1 enter 回车键

> 用来在触发回车按键之后触发的事件

```html
 <input type="text" v-model="msg" @keyup.enter="keyups">
```

### 9.2 tab 键

> 用来捕获到tab键执行到当前标签是才会触发

```html
<input type="text" @keyup.tab="keytabs">
```

----





## 10. Axios 基本使用

### 10.1 引言

> `Axios` 是一个异步请求技术,核心作用就是用来在页面中发送异步请求,并获取对应数据在页面中渲染       页面局部更新技术  Ajax

### 10.2 Axios 第一个程序

中文网站:https://www.kancloud.cn/yunye/axios/234845

安装: https://unpkg.com/axios/dist/axios.min.js

```html
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
```

#### 10.2.1 GET方式的请求

```js
	  //发送GET方式请求
    axios.get("http://localhost:8989/user/findAll?name=xiaochen").then(function(response){
        console.log(response.data);
    }).catch(function(err){
        console.log(err);
    });
```

#### 10.2.2 POST方式请求

```javascript
		//发送POST方式请求
    axios.post("http://localhost:8989/user/save",{
        username:"xiaochen",
        age:23,
        email:"xiaochen@zparkhr.com",
        phone:13260426185
    }).then(function(response){
        console.log(response.data);
    }).catch(function(err){
        console.log(err);
    });
```

#### 10.2.3 axios并发请求

> `并发请求`:  将多个请求在同一时刻发送到后端服务接口,最后在集中处理每个请求的响应结果

```js
 //1.创建一个查询所有请求
    function findAll(){
        return axios.get("http://localhost:8989/user/findAll?name=xiaochen");
    }

    //2.创建一个保存的请求
    function save(){
        return axios.post("http://localhost:8989/user/save",{
            username:"xiaochen",
            age:23,
            email:"xiaochen@zparkhr.com",
            phone:13260426185
        });
    }

    //3.并发执行
    axios.all([findAll(),save()]).then(
        axios.spread(function(res1,res2){  //用来将一组函数的响应结果汇总处理
            console.log(res1.data);
            console.log(res2.data);
        })
    );//用来发送一组并发请求
```

------

### 10.3 案例

```html
<body>
    <div id="app">
        <input type="text" v-model="name" @keyup="show" @keyup.enter="search"> <input type="button" value="搜索" @click="search"/><br>
        <span v-for="city in citys">
            <a href="" @click.prevent="insert(city)" >{{city}}</a>&nbsp;
        </span>
        <hr>
        <span v-show="isShow">{{name}}:{{message}}</span>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/vue"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script>
        function f() {
            axios.get("http://localhost:8080/hello")
        }
        const app=new Vue({
            el:"#app",
            data:{
                name:"",
                citys:["北京","上海","广州","深圳"],
                message:"",
                isShow:false
            },
            methods:{
                search(){
                    let _this=this;
                    axios.get("http://localhost:8080/find?name="+this.name).then(function (response){
                        console.log(response.data);
                        _this.message=response.data.message;
                        _this.isShow=true;
                    }).catch(function (err){
                        console.log(err)
                    })
                },
                show(){
                    this.isShow=false;
                },
                insert(city){
                    this.name=city;
                    this.search();
                }

            }
        })
    </script>
</body>
```



### 10.4 整合

```html
<script>
    const app=new Vue({
        el:"#app",
        data:{
            users:[
                {id:1,name:"zcj",phone:15272116776,age:18,sex:"男"},
                {id:2,name:"zcj",phone:15272116776,age:18,sex:"男"}
            ],
            user:{},
            searchName:""
        },
        methods:{
            //根据id删除用户
            deleteUser(id){
                let _this=this;
                if (window.confirm("是否删除")){
                    console.log(id);
                    axios.get("http://localhost:8080/deleteUser?id="+id).then(function (response){
                        console.log(response);
                        _this.findAll();
                    }).catch(function (err){
                        console.log(err);
                        _this.findAll();
                    })
                }
            },
            //注册并刷新
            saveUser(){
                let _this=this;
                console.log(this.user);
                axios.post("http://localhost:8080/saveUser",this.user).then(function (response){
                    if (response.data.msg){
                        _this.findAll();
                        _this.user={}
                    }else{
                        console.log(response.data);
                        _this.findAll();
                    }
                }).catch(function (err){
                    console.log(err);
                    _this.findAll();
                })
            },
            findUser(id){
                let _this=this;
                axios.get("http://localhost:8080/findUser?id="+id).then(function (response){
                    console.log(response.data);
                    _this.user=response.data;
                })
            },
            findName(){
                let _this=this;
                console.log(this.searchName);
                axios.get("http://localhost:8080/findName?name="+_this.searchName).then(function (response){
                    _this.users=response.data;
                })
            },
            //刷新
            findAll(){
                let _this=this;
                axios.get("http://localhost:8080/findAll").then(function (response){
                    console.log(response)
                    _this.users=response.data;
                }).catch(function (err){
                    console.log(err)
                })
            },
            //重置
            restUser(){
                this.user={}
            }
        },
        created(){
            console.log(111)
            this.findAll();
        }
    })
</script>
```

```java
@RestController
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/findAll")
    public List<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/findName")
    public List<User> findName(String name){
        return userService.findName(name);
    }

    @PostMapping("/saveUser")
    public Map<String,Object> saveUser(@RequestBody User user){
        Map<String,Object> map=new HashMap<>();
        map.put("msg",true);
        try {
            if (user.getId()==0){
                userService.saveUser(user);
            }else{
                userService.updateUser(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @GetMapping("/deleteUser")
    public int deleteUser(int id){
        return userService.deleteUser(id);
    }

    @GetMapping("/findUser")
    public User findUser(int id){
        return userService.findUser(id);
    }

}
```





## 11. Vue 生命周期

> `生命周期钩子`   ====>  `生命周期函数`

![img](Vue实战.assets/lifecycle.png)

```markdown
# Vue生命周期总结
		1.初始化阶段
				beforeCreate(){ //1.生命周期中第一个函数,该函数在执行时Vue实例仅仅完成了自身事件的绑定和生命周期函数的初始化工作,Vue实例中还没有 Data el methods相关属性
            console.log("beforeCreate: "+this.msg);
        },
        created(){ //2.生命周期中第二个函数,该函数在执行时Vue实例已经初始化了data属性和methods中相关方法
            console.log("created: "+this.msg);
        },
        beforeMount(){//3.生命周期中第三个函数,该函数在执行时Vue将El中指定作用范围作为模板编译
            console.log("beforeMount: "+document.getElementById("sp").innerText);
        },
        mounted(){//4.生命周期中第四个函数,该函数在执行过程中,已经将数据渲染到界面中并且已经更新页面
            console.log("Mounted: "+document.getElementById("sp").innerText);
        }
        
		2.运行阶段
			 	beforeUpdate(){//5.生命周期中第五个函数,该函数是data中数据发生变化时执行 这个事件执行时仅仅是Vue实例中data数据变化页面显示的依然是原始数据
            console.log("beforeUpdate:"+this.msg);
            console.log("beforeUpdate:"+document.getElementById("sp").innerText);
        },
        updated(){    //6.生命周期中第六个函数,该函数执行时data中数据发生变化,页面中数据也发生了变化  页面中数据已经和data中数据一致
            console.log("updated:"+this.msg);
            console.log("updated:"+document.getElementById("sp").innerText);
        },
        
		3.销毁阶段
			 	beforeDestory(){//7.生命周期第七个函数,该函数执行时,Vue中所有数据 methods componet 都没销毁

        },
        destoryed(){ //8.生命周期的第八个函数,该函数执行时,Vue实例彻底销毁

        }
```



```html
<body>
<div id="app">

    <span id="sp">{{ msg }}</span>

    <input type="button" @click="changeData" value="修改数据">

</div>
<!--引入vue-->
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script>
    const app = new Vue({
        el: "#app",
        data: {
            msg:"百知教育"
        },
        methods: {
            changeData(){
                this.msg = Math.random();
            }
        },
        beforeCreate(){ //1.生命周期中第一个函数,该函数在执行时Vue实例仅仅完成了自身事件的绑定和生命周期函数的初始化工作,Vue实例中还没有 Data el methods相关属性
            console.log("beforeCreate: "+this.msg);
        },
        created(){ //2.生命周期中第二个函数,该函数在执行时Vue实例已经初始化了data属性和methods中相关方法
            console.log("created: "+this.msg);
        },
        beforeMount(){//3.生命周期中第三个函数,该函数在执行时Vue将El中指定作用范围作为模板编译
            console.log("beforeMount: "+document.getElementById("sp").innerText);
        },
        mounted(){//4.生命周期中第四个函数,该函数在执行过程中,已经将数据渲染到界面中并且已经更新页面
            console.log("Mounted: "+document.getElementById("sp").innerText);
        },
        beforeUpdate(){//5.生命周期中第五个函数,该函数是data中数据发生变化时执行 这个事件执行时仅仅是Vue实例中data数据变化页面显示的依然是原始数据
            console.log("beforeUpdate:"+this.msg);
            console.log("beforeUpdate:"+document.getElementById("sp").innerText);
        },
        updated(){    //6.生命周期中第六个函数,该函数执行时data中数据发生变化,页面中数据也发生了变化  页面中数据已经和data中数据一致
            console.log("updated:"+this.msg);
            console.log("updated:"+document.getElementById("sp").innerText);
        },
        beforeDestory(){//7.生命周期第七个函数,该函数执行时,Vue中所有数据 methods componet 都没销毁

        },
        destoryed(){ //8.生命周期的第八个函数,该函数执行时,Vue实例彻底销毁

        }

    });
</script>
</body>
```





----



## 12. Vue中组件(Component)

### 12.1 组件作用

组件作用: 用来减少Vue实例对象中代码量,日后在使用Vue开发过程中,可以根据 不能业务功能将页面中划分不同的多个组件,然后由多个组件去完成整个页面的布局,便于日后使用Vue进行开发时页面管理,方便开发人员维护。

### 12.2 组件使用

#### 12.2.1 全局组件注册

`说明:全局组件注册给Vue实例,日后可以在任意Vue实例的范围内使用该组件`

```html
<body>
    <div id="app">
        <login></login>  
    </div>
    <script>
        Vue.component('login',{
            template:'<div><h1>用户登陆</h1></div>'   //配置全局组件
        });
        const app=new Vue({
            el:"#app"
        })
    </script>
</body>
```

```html
<div id="app">
    <user-login></user-login>
</div>
<script>
    Vue.component('userLogin',{                   //驼峰命名 使用时加上-
        template:'<div><h1>用户登陆</h1></div>'    
    });
    const app=new Vue({
        el:"#app"
    })
</script>
```


```markdown
# 注意:
			1.Vue.component用来开发全局组件 参数1: 组件的名称  参数2: 组件配置{}  template:''用来书写组件的html代码  template中必须有且只有一个root元素
			2.使用时需要在Vue的作用范围内根据组件名使用全局组件
			3.如果在注册组件过程中使用 驼峰命名组件的方式 在使用组件时 必须将驼峰的所有单词小写加入-线进行使用
```





#### 12.2.2 局部组件注册

`说明:通过将组件注册给对应Vue实例中一个components属性来完成组件注册,这种方式不会对Vue实例造成累加`

- 第一种开发方式

```html
<body>
    <div id="app">
        <login></login>
    </div>
    <script>
        const app=new Vue({
            el:"#app",
            data:{},
            methods:{},
            components:{
                login:{
                    template:'<div><h1>用户登陆</h1></div>'
                }
            }
        })
    </script>
</body>
```

- 第二种开发方式

  ```html
  <body>
      <div id="app">
          <login></login>
      </div>
  
  <!--    配置组件-->
      <template id="loginTemplate"><h1>用户登陆</h1></template>
      <script>
          const login={
              template: '#loginTemplate'  //使用id绑定
          }
  
          const app=new Vue({
              el:"#app",
              data:{},
              methods:{},
              components:{
                  login:login
              }
          })
      </script>
  </body>
  ```



### 12.3 Prop的使用

`作用:props用来给组件传递相应静态数据或者是动态数据的`

#### 12.3.1 通过在组件上声明静态数据传递给组件内部

```html
<body>
    <div id="app">
<!--        传入值-->
        <login user-name="zcj" age="23"></login>
    </div>

    <script>
        const login={
            template: "<div><h1>欢迎{{userName}},{{age}}</h1></div>",
            //接收值
            props:['userName','age']
        }

        const app=new Vue({
            el:"#app",
            data:{},
            methods:{},
            components:{
                login:login
            }
        })
    </script>
</body>
```

```markdown
# 总结:
			1.使用组件时可以在组件上定义多个属性以及对应数据
			2.在组件内部可以使用props数组生命多个定义在组件上的属性名 日后可以在组件中通过{{ 属性名 }} 方式获取组件中属性值
```

#### 12.3.2 通过在组件上声明动态数据传递给组件内部

```html
<body>
    <div id="app">
<!--        与vue中的动态数据绑定-->
        <login :name="userName" :age="userAge"></login>
    </div>

    <script>
        const login={
            template: "<div><h1>欢迎{{name}},{{age}}</h1></div>",
            props: ['name','age']
        }
        const app=new Vue({
            el:"#app",
            data:{
                userName:"zcj",
                userAge:23
            },
            methods:{},
            components:{
                login:login
            }
        })
    </script>
</body>
```



#### 12.3.3 prop的单向数据流

`单向数据流:所有的 prop 都使得其父子 prop 之间形成了一个**单向下行绑定**：父级 prop 的更新会向下流动到子组件中，但是反过来则不行。`

> 所有的 prop 都使得其父子 prop 之间形成了一个**单向下行绑定**：父级 prop 的更新会向下流动到子组件中，但是反过来则不行。这样会防止从子组件意外改变父级组件的状态，从而导致你的应用的数据流向难以理解。
>
> 额外的，每次父级组件发生更新时，子组件中所有的 prop 都将会刷新为最新的值。这意味着你**不**应该在一个子组件内部改变 prop。如果你这样做了，Vue 会在浏览器的控制台中发出警告

```html
<body>
    <div id="app">
<!--        与vue中的动态数据绑定-->
        <login :username="username"></login>
    </div>

    <script>
        const login={
            template: "<div><h1>欢迎{{name}}</h1></div>",
            data(){
                return {
                    name:this.username  //绑定后使用
                }
            },
            props: ['username']  //接收外部传值
        }
        const app=new Vue({
            el:"#app",
            data:{
                username: "ZCJ"
            },
            methods:{},
            components:{
                login
            }
        })
    </script>
</body>
```





### 12.4 组件中定义数据和事件使用

##### 1. 组件中定义属于组件的数据

```js
//组件声明的配置对象
    const login = {
        template:'<div><h1>{{ msg }} 百知教育</h1><ul><li v-for="item,index in lists">{{ index }}{{ item }}</li></ul></div>',
        data(){   //使用data函数方式定义组件的数据   在templatehtml代码中通过插值表达式直接获取
            return {
                msg:"hello",
                lists:['java','spring','springboot']
            }//组件自己内部数据
        }
    }
```

##### 2.组件中事件定义

```js
 const login={
            template:'<div><h1>芜湖</h1>  <ul @click="change"><li v-for="item in lists">{{item}}</li></ul></div>',
            data(){
                return {
                    msg:"hello",
                    lists:['java','spring','springboot']
                }
            },
            methods: {
                change(){
                    alert("wuhu")
                }
            }
        }
```

```markdown
# 总结	
		1.组件中定义事件和直接在Vue中定义事件基本一致 直接在组件内部对应的html代码上加入@事件名=函数名方式即可
		2.在组件内部使用methods属性用来定义对应的事件函数即可,事件函数中this 指向的是当前组件的实例
```



### 12.5 向子组件中传递事件并在子组件中调用改事件

`在子组件中调用传递过来的相关事件必须使用 this.$emit('函数名') 方式调用`

```html
<body>
    <div id="app">
        <login :username="username" @change="change"></login>   
    </div>

    <script>
        const login={
            template: "<div><h1 >欢迎{{name}} <input type='button' value='点击' @click='change'></h1></div>",
            data(){
                return {
                    name:this.username  //绑定后使用
                }
            },
            props: ['username'],  //接收外部传值
            methods: {
                change(){
                    this.$emit("change");
                }
            }
        }
        const app=new Vue({
            el:"#app",
            data:{
                username: "ZCJ"
            },
            methods:{
                change(){
                    alert("芜湖")
                }
            },
            components:{
                login
            }
        })
    </script>
</body>
```

-----



## 13.Vue中路由(VueRouter)

### 13.1 路由

`路由:根据请求的路径按照一定的路由规则进行请求的转发从而帮助我们实现统一请求的管理`

### 13.2 作用

`用来在vue中实现组件之间的动态切换`

### 13.3 使用路由

1. ##### 引入路由

   ```html
   <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
   <script src="https://unpkg.com/vue-router/dist/vue-router.js"></script>
   ```

2. ##### 创建组件对象

   ```js
   //声明组件模板
   const login = {
     template:'<h1>登录</h1>'
   };
   
   const register = {
     template:'<h1>注册</h1>'
   };
   
   ```

3. ##### 定义路由对象的规则

   ```js
    //创建路由对象
   const router = new VueRouter({
     routes:[
       {path:'/login',component:login},   //path: 路由的路径  component:路径对应的组件
       {path:'/register',component:register}
     ]
   });
   ```

   

4. ##### 将路由对象注册到vue实例

   ```js
   const app = new Vue({
     el: "#app",
     data: {
       username:"小陈",
     },
     methods: {},
     router:router   //设置路由对象
   });
   ```

   

5. ##### 在页面中显示路由的组件

   ```html
   <!--显示路由的组件-->
   <router-view></router-view>
   ```

6. ##### 根据连接切换路由

   ```html
   <a href="#/login">点我登录</a>
   <a href="#/register">点我注册</a>
   ```
   
7. 完整代码

   ```html
   <head>
       <meta charset="UTF-8">
       <title>Title</title>
       <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
       <script src="https://unpkg.com/vue-router/dist/vue-router.js"></script>
   </head>
   <body>
   <div id="app">
       <a href="#/login">登陆</a>
       <a href="#/register">注册</a>
   
       <router-view></router-view>
   </div>
   
   <script>
       const login={
           template:'<h1>登陆</h1>'
       }
       const register={
           template: '<h1>注册</h1>'
       }
   
       const router=new VueRouter({
           routes:[
               {path:'/login',component:login},  //与路由对象绑定
               {path:"/register",component: register}
           ]
       })
   
       const app=new Vue({
           el:"#app",
           data:{
           },
           methods:{
   
           },
           router:router  //设置路由对象
       })
   </script>
   </body>
   ```



### 13.4 router-link使用

`作用:用来替换我们在切换路由时使用a标签切换路由`

`好处:就是可以自动给路由路径加入#不需要手动加入`

```html
<router-link to="/login">登陆</router-link>
<router-link to="/register">注册</router-link>
```

通过tag加样式

```html
	<router-link to="/login" tag="button">我要登录</router-link>
    <router-link to="/register" tag="button">点我注册</router-link>
```

```markdown
# 总结:
	1.router-link 用来替换使用a标签实现路由切换 好处是不需要书写#号直接书写路由路径
	2.router-link to属性用来书写路由路径   tag属性:用来将router-link渲染成指定的标签
```

### 13.5 默认路由

`作用:用来在第一次进入界面是显示一个默认的组件`

```js
const router = new VueRouter({
  routes:[
    //{ path:'/',component:login},
    { path:'/',redirect:'/login'},  //redirect: 用来当访问的是默认路由 "/" 时 跳转到指定的路由展示  推荐使用
    { path:'/login', component:login},
    { path:'/register', component:register},
  ]
});
```



### 13.6 路由中参数传递

- 第一种方式传递参数 传统方式

1. 通过?号形式拼接参数

   ```html
    <router-link to="/login?id=21&name=zhangsan">我要登录</router-link>
   ```

2. 组件中获取参数

   ```js
   const login = {
     template:'<h1>用户登录</h1>',
     data(){return {}},
     methods:{},
     created(){
       console.log("==="+this.$route.query.id+"==="+this.$route.query.name);
     }
   };
   ```

- 第二种方式传递参数 restful

1. 通过使用路径方式传递参数

   ```js
   <router-link to="/register/24/张三">我要注册</router-link>
   var router = new VueRouter({
     routes:[
       {path:'/register/:id/:name',component:register}   //定义路径中获取对应参数
     ]
   });
   ```

   

2. 组件中获取参数

   ```js
   const register = {
     template:'<h1>用户注册{{ $route.params.name }}</h1>',
     created(){
       console.log("注册组件中id:   "+this.$route.params.id+this.$route.params.name);
     }
   };
   ```





### 13.7 嵌套路由

1. ##### 声明最外层和内层路由

   ```html
   <body>
   <div id="app">
       <router-link to="/product">商品</router-link>
       <router-view></router-view>
   </div>
   <!--子路由显示-->
   <template id="product">
       <div>
           <h1>商品管理</h1>
           <router-link to="/product/productAdd">商品添加</router-link>
           <router-link to="/product/productUpdate">商品修改</router-link>
           <router-view></router-view>
       </div>
   </template>
   
   <script>
       const product={
           template:"#product"
       }
       const productAdd={
           template: '<h2>商品添加</h2>'
       }
       const productUpdate={
           template: '<h2>商品修改</h2>'
       }
   
       const router=new VueRouter({
           routes:[
               {
                   path:'/product',
                   component:product,
                   children:[        //表示子路由
                       {path:'productAdd',component:productAdd},
                       {path:'productUpdate',component:productUpdate}
                   ]
               }
           ]
       })
   
       const app=new Vue({
           el:"#app",
           data:{
           },
           methods:{
           },
           router  //设置路由对象
       })
   </script>
   </body>
   ```

   

2. ##### 创建路由对象含有嵌套路由

   ```js
   const router=new VueRouter({
           routes:[
               {
                   path:'/product',
                   component:product,
                   children:[        //表示子路由
                       {path:'productAdd',component:productAdd},
                       {path:'productUpdate',component:productUpdate}
                   ]
               }
           ]
       })
   ```

   

3. ##### 注册路由对象

   ```js
   const app = new Vue({
       el: "#app",
       data: {},
       methods: {},
       router,//定义路由对象
   });
   ```

4. 测试路由

   ```html
   <div id="app">
       <router-link to="/product">商品</router-link>
       <router-view></router-view>
</div>
   <!--子路由显示-->
   <template id="product">
       <div>
           <h1>商品管理</h1>
           <router-link to="/product/productAdd">商品添加</router-link>
           <router-link to="/product/productUpdate">商品修改</router-link>
           <router-view></router-view>
       </div>
   </template>
   ```
   
   ---
   

### 13.8项目

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <script src="js/vue.js"></script>
    <script src="https://unpkg.com/vue-router/dist/vue-router.js"></script>
    <script src="js/axios.min.js"></script>
</head>
<body>
    <div id="app">
        <div class="container">
            <div class="row" style="margin-top: 70px">
                <div class="col-md-10 col-md-offset-1">
                    <ul class="nav nav-pills nav-justified">
                        <li role="presentation" :class="show=='home'?'active':''"><a href="#/home" @click="change('home')">Home</a></li>
                        <li role="presentation" :class="show=='user'?'active':''"><a href="#/user" @click="change('user')">Profile</a></li>
                        <li role="presentation" :class="show=='student'?'active':''"><a href="#/student" @click="change('student')">Messages</a></li>
                    </ul>
                </div>
            </div>
            <div class="row">
                <div class="col-md-offset-1">
                    <router-view></router-view>
                </div>
            </div>
        </div>
    </div>
    <template id="home">
        <div>
            <div class="jumbotron" style="margin-top: 50px">
                <h1>Hello, world!</h1>
                <p>...</p>
                <p><a class="btn btn-primary btn-lg" href="#" role="button">Learn more</a></p>
            </div>
        </div>
    </template>
    <template id="user">
        <div>
            <table class="table table-striped" style="margin-top: 50px">
                <tr>
                    <th>id</th>
                    <th>name</th>
                    <th>phone</th>
                    <th>age</th>
                    <th>sex</th>
                    <th>password</th>
                    <th>操作</th>
                </tr>
                <tr v-for="item in users">
                    <th>{{item.id}}</th>
                    <th>{{item.name}}</th>
                    <th>{{item.phone}}</th>
                    <th>{{item.age}}</th>
                    <th>{{item.sex}}</th>
                    <th>{{item.passowrd}}</th>
                    <th><a href="" class="btn btn-default">修改</a> <a href="" class="btn btn-danger">删除</a></th>
                </tr>
            </table>
        </div>
    </template>
    <template id="student">
        <div>
            <h1>学生信息</h1>
        </div>
    </template>

    <script>
        const home={
            template:"#home"
        }
        const user={
            template:"#user",
            data(){
                return{
                    users:[]
                }
            },
            methods: {

            },
            created(){
                let _this=this;
                axios.get("http://localhost:8080/findAll").then(function (response){
                    console.log(response);
                    _this.users=response.data;
                })
            }
        }
        const student={
            template:"#student"
        }
        const router = new VueRouter({
            routes:[
                {path: "/",redirect:"/home"},
                {path:"/home",component:home},
                {path:"/user",component:user},
                {path:"/student",component:student}
            ]
        })
        const app=new Vue({
            el:"#app",
            data:{
                show:'home'
            },
            methods:{
                change(value){
                    this.show=value;
                }
            },
            router
        });
    </script>

</body>
</html>
```





## 14. Vue CLI 脚手架

### 14.1 什么是CLI

命令行界面（英语：command-line interface，缩写：*CLI*）是在图形用户界面得到普及之前使用最为广泛的用户界面，它通常不支持鼠标，用户通过键盘输入指令，计算机接收到指令后，予以执行。也有人称之为字符用户界面（CUI）

### 14.2 什么是Vue CLI

Vue CLI 是一个基于 Vue.js 进行快速开发的完整系统。使用Vue 脚手架之后我们开发的页面将是一个完整系统(项目)。

### 14.3 Vue CLI优势

- 通过 `vue-cli` 搭建交互式的项目脚手架。bootstrap css js jquery js     通过执行命令方式下载相关依赖
- 通过 `@vue/cli` + `@vue/cli-service-global` 快速开始零配置原型开发    vue页面 vuejs  vuerouter        axios(一条命令)
- 一个运行时依赖 (`@vue/cli-service`)，该依赖：
  - 可升级；  一条命令
  - 基于 webpack 构建，并带有合理的默认配置；  webpack  项目打包方式     编译好的项目源码===>部署到服务器上直接使用
  - 可以通过项目内的配置文件进行配置；               默认配置文件,通过修改默认配置文件达到自己想要的项目环境            
  - 可以通过插件进行扩展。                                       vue v-charts  elementui 
- 一个丰富的官方插件集合，集成了前端生态中最好的工具。Nodejs(tomcat)  Vue VueRouter webpack yarn
- 一套完全图形化的创建和管理 Vue.js 项目的用户界面

### 14.4 Vue CLI安装

##### 1. 环境准备

```markdown
#1.下载nodejs
		http://nodejs.cn/download/
			windows系统:   .msi  安装包(exe)指定安装位置   .zip(压缩包)直接解压缩指定目录
		  mac os 系统:   .pkg  安装包格式自动配置环境变量  .tar.gz(压缩包)解压缩安装到指定名

#2.配置nodejs环境变量
	windows系统:
		1.计算上右键属性---->  高级属性 ---->环境变量 添加如下配置:
				NODE_HOME=  nodejs安装目录
        PATH    = xxxx;%NODE_HOME%
    2.macos 系统
    		推荐使用.pkg安装直接配置node环境
 
#3.验证nodejs环境是否成功
		node -v 
		
#4.npm介绍
		node package mangager    nodejs包管理工具       前端主流技术  npm 进行统一管理
			maven 管理java后端依赖   远程仓库(中心仓库)      阿里云镜像
			npm   管理前端系统依赖    远程仓库(中心仓库)      配置淘宝镜像
		
#5.配置淘宝镜像
	  npm config set registry https://registry.npm.taobao.org
	  npm config get registry

#6.配置npm下载依赖位置
	 windows:
		npm config set cache "E:\work\node\npm_cache"
		npm config set prefix "E:\work\node\npm_global"
	 mac os:
	 	npm config set cache "/Users/chenyannan/dev/nodereps"
		npm config set prefix "/Users/chenyannan/dev/nodereps"

#7.验证nodejs环境配置
	npm config ls
	
    ; userconfig /Users/chenyannan/.npmrc
    cache = "/Users/chenyannan/dev/nodereps"
    prefix = "/Users/chenyannan/dev/nodereps"
    registry = "https://registry.npm.taobao.org/"

```

##### 2.安装脚手架

```markdown
#0.卸载脚手架
	npm uninstall -g @vue/cli  //卸载3.x版本脚手架
	npm uninstall -g vue-cli  //卸载2.x版本脚手架

#1.Vue Cli官方网站
		https://cli.vuejs.org/zh/guide/

#2.安装vue Cli
		npm install -g vue-cli

```

##### 3.第一个vue脚手架项目

```markdown
# 1.创建vue脚手架第一个项目
	vue init webpack 项目名
# 2.创建第一个项目
	hello     ------------->项目名
    -build  ------------->用来使用webpack打包使用build依赖
    -config ------------->用来做整个项目配置目录
    -node_modules  ------>用来管理项目中使用依赖
    -src					 ------>用来书写vue的源代码[重点]
    	+assets      ------>用来存放静态资源 [重点]
      components   ------>用来书写Vue组件 [重点]
      router			 ------>用来配置项目中路由[重点]
      App.vue      ------>项目中根组件[重点]
      main.js      ------>项目中主入口[重点]
    -static        ------>其它静态
    -.babelrc      ------> 将es6语法转为es5运行
    -.editorconfig ------> 项目编辑配置
    -.gitignore    ------> git版本控制忽略文件
    -.postcssrc.js ------> 源码相关js
    -index.html    ------> 项目主页
    -package.json  ------> 类似与pom.xml 依赖管理  jquery 不建议手动修改
    -package-lock.json ----> 对package.json加锁
    -README.md         ----> 项目说明文件

# 3.如何运行在项目的根目录中执行
		npm start 运行前端系统

# 4.如何访问项目
		http://localhost:8081    

# 5.Vue Cli中项目开发方式
	 注意: 一切皆组件   一个组件中   js代码  html代码  css样式
	 
	 	1. VueCli开发方式是在项目中开发一个一个组件对应一个业务功能模块,日后可以将多个组件组合到一起形成一个前端系统
	 	2. 日后在使用vue Cli进行开发时不再书写html,编写的是一个个组件(组件后缀.vue结尾的文件),日后打包时vue cli会将组件编译成运行的html文件	  
```

##### 4.如何开发Vue脚手架

`注意:在Vue cli 中一切皆组件`

------

## 15.在脚手架中使用axios

### 15.1 安装axios

```markdown
# 1.安装axios
	npm install axios --save-dev

# 2.配置main.js中引入axios
	import axios from 'axios';

	Vue.prototype.$http=axios;

# 3.使用axios
	在需要发送异步请求的位置:this.$http.get("url").then((res)=>{}) this.$http.post("url").then((res)=>{})
```

---

**跳转**

this.$router.push("/user");

**监听（刷新界面）**

```javascript
watch:{
  $route:{
    handler: function (val, oldVal){
      if (val.path=='/user'){
        this.findAll();
      }
    },
    deep: true
  }
}
```



##  安装element-ui

接下来我们引入element-ui组件（[element.eleme.cn](https://element.eleme.cn/#/zh-CN/component/installation)），这样我们就可以获得好看的vue组件，开发好看的博客界面。

![图片](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/91fafb839956455087f4fe20da4c420e~tplv-k3u1fbpfcp-zoom-1.image)

命令很简单：

```plain
# 切换到项目根目录
cd vueblog-vue
# 安装element-ui
cnpm install element-ui --save
复制代码
```

然后我们打开项目src目录下的main.js，引入element-ui依赖。

```plain
import Element from 'element-ui'
import "element-ui/lib/theme-chalk/index.css"
Vue.use(Element)
复制代码
```

这样我们就可以愉快得在官网上选择组件复制代码到我们项目中直接使用啦。





## Makerdown博客编辑（发表）

我们点击发表博客链接调整到/blog/add页面，这里我们需要用到一个markdown编辑器，在vue组件中，比较好用的是mavon-editor，那么我们直接使用哈。先来安装mavon-editor相关组件：

### 安装mavon-editor

基于Vue的markdown编辑器mavon-editor

```plain
cnpm install mavon-editor --save
```

然后在main.js中全局注册：

```javascript
// 全局注册
import Vue from 'vue'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
// use
Vue.use(mavonEditor)
```

mavon-editor因为已经全局注册，所以我们直接使用组件即可：

```plain
<mavon-editor v-model="editForm.content"/>
```

效果如下： ![图片](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/0b4a0adf4450494590e16911d5d2b257~tplv-k3u1fbpfcp-zoom-1.image)



### 博客详情

博客详情中需要回显博客信息，然后有个问题就是，后端传过来的是博客内容是markdown格式的内容，我们需要进行渲染然后显示出来，这里我们使用一个插件markdown-it，用于解析md文档，然后导入github-markdown-c，所谓md的样式。

方法如下：

```plain
# 用于解析md文档
cnpm install markdown-it --save
# md样式
cnpm install github-markdown-css
复制代码
```

然后就可以在需要渲染的地方使用：

- views\BlogDetail.vue

```html
<template>
  <div class="m-container">
    <Header></Header>
    <div class="mblog">
      <h2>{{ blog.title }}</h2>
      <el-link icon="el-icon-edit" v-if="ownBlog"><router-link :to="{name: 'BlogEdit', params: {blogId: blog.id}}">编辑</router-link></el-link>
      <el-divider></el-divider>
      <div class="content markdown-body" v-html="blog.content"></div>
    </div>
  </div>
</template>
<script>
  import 'github-markdown-css/github-markdown.css' // 然后添加样式markdown-body
  import Header from "@/components/Header";
  export default {
    name: "BlogDetail",
    components: {
      Header
    },
    data() {
      return {
        blog: {
          userId: null,
          title: "",
          description: "",
          content: ""
        },
        ownBlog: false
      }
    },
    methods: {
      getBlog() {
        const blogId = this.$route.params.blogId
        const _this = this
        this.$axios.get('/blog/' + blogId).then((res) => {
          console.log(res)
          console.log(res.data.data)
          _this.blog = res.data.data
          var MarkdownIt = require('markdown-it'),
            md = new MarkdownIt();
          var result = md.render(_this.blog.content);
          _this.blog.content = result
          // 判断是否是自己的文章，能否编辑
          _this.ownBlog =  (_this.blog.userId === _this.$store.getters.getUser.id)
        });
      }
    },
    created() {
      this.getBlog()
    }
  }
</script>
复制代码
```

具体逻辑还是挺简单，初始化create()方法中调用getBlog()方法，请求博客详情接口，返回的博客详情content通过markdown-it工具进行渲染。

再导入样式：

```plain
import 'github-markdown.css'
```

然后在content的div中添加class为markdown-body即可哈。 效果如下：

```html
<div class="markdown-body" v-html="ruleForm.content"></div>
```



![图片](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/7c76e68611984712b46646f964de0dea~tplv-k3u1fbpfcp-zoom-1.image)

另外标题下添加了个小小的编辑按钮，通过ownBlog （判断博文作者与登录用户是否同一人）来判断按钮是否显示出来。

## 路由权限拦截

页面已经开发完毕之后，我们来控制一下哪些页面是需要登录之后才能跳转的，如果未登录访问就直接重定向到登录页面，因此我们在src目录下定义一个js文件：

- src\permission.js

```javascript
import router from "./router";
// 路由判断登录 根据路由配置文件的参数
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requireAuth)) { // 判断该路由是否需要登录权限
    const token = localStorage.getItem("token")
    console.log("------------" + token)
    if (token) { // 判断当前的token是否存在 ； 登录存入的token
      if (to.path === '/login') {
      } else {
        next()
      }
    } else {
      next({
        path: '/login'
      })
    }
  } else {
    next()
  }
})
复制代码
```

通过之前我们再定义页面路由时候的的meta信息，指定requireAuth: true，需要登录才能访问，因此这里我们在每次路由之前（router.beforeEach）判断token的状态，觉得是否需要跳转到登录页面。

```css
{
  path: '/blog/add', // 注意放在 path: '/blog/:blogId'之前
  name: 'BlogAdd',
  meta: {
    requireAuth: true
  },
  component: BlogEdit
}
复制代码
```

然后我们再main.js中import我们的permission.js

```css
import './permission.js' // 路由拦截
```






## 16.Vue Cli脚手架项目打包和部署

```markdown
# 1.在项目根目录中执行如下命令:
	  vue run build
		注意:vue脚手架打包的项目必须在服务器上运行不能直接双击运行

# 2.打包之后当前项目中变化
	 	在打包之后项目中出现dist目录,dist目录就是vue脚手架项目生产目录或者说是直接部署目录
# 3.
```

