package com.baizhi.sb.controller;

import com.baizhi.sb.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {



    //删除数据
    @CrossOrigin
    @DeleteMapping("delete")
    public Map<String,Object> delete(String id){
        Map<String, Object> map = new HashMap<>();
        System.out.println("id = " + id);
        map.put("success",true);
        return map;
    }


    //保存数据
    @CrossOrigin
    @PostMapping("save")
    public Map<String,Object> save(@RequestBody User user){
        Map<String, Object> map = new HashMap<>();
        System.out.println("user = " + user);
        map.put("success",true);
        return map;
    }

    //展示索引
    @CrossOrigin  //用来解决跨域问题
    @GetMapping("findAll")
    public List<User> findAll(String name){
        System.out.println("name = " + name);
        List<User> users = new ArrayList<>();
        users.add(new User("21","陈艳男","609937647@qq.com",23,"132xxx6185"));
        users.add(new User("22","小二黑","609937445@qq.com",24,"132xxx6186"));
        users.add(new User("23","小三","609937445@qq.com",24,"132xxx6186"));
        return users;
    }

}
