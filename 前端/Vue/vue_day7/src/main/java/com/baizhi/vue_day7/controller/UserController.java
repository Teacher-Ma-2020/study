package com.baizhi.vue_day7.controller;

import com.baizhi.vue_day7.enity.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @GetMapping("findAll")
    public List<User> findAll(){
        System.out.println("查询所有....");

        List<User> list = Arrays.asList(
                new User("21","xiaochen",23,new Date()),
                new User("22","小三",24,new Date()),
                new User("23","小明",25,new Date())
        );
        return list;
    }
}
