package com.baizhi.sb.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("city")
public class CityController {


    @GetMapping("find")
    @CrossOrigin
    public Map<String,String> findWeatherByCity(String name){
        Map<String, String> map = new HashMap<>();
        String weathers = getWeathers(name);
        map.put("message",weathers);
        return map;
    }

    //返回对应城市天气
    public String getWeathers(String name){
        Map<String,String> weathers =  new HashMap<>();
        weathers.put("北京","晴转多云,空气清新!");
        weathers.put("上海","多云转晴,空气质量不错!");
        weathers.put("深圳","中到暴雨,空气也很好!");
        weathers.put("广州","局部地区大风,空气也算不错!");
        weathers.put("天津","离北京比较近,和北京差不过");
        return weathers.get(name);
    }
}
