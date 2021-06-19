package com.baizhi.sb.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("weather")
public class WeatherController {

    private static Map<String,String> weathers;

    static{
        weathers = new HashMap<>();
        weathers.put("北京","城市:北京,天气晴朗,阳关充足!");
        weathers.put("深圳","城市:深圳,多云转晴,没有雨!");
        weathers.put("内蒙古","城市:内蒙古,中到大雪,局部地区有大风!");
        weathers.put("上海","城市:上海,大风非常大,多云转晴!");
    }


    @GetMapping("find")
    @CrossOrigin
    public String findByCity(String cityName){
        System.out.println("cityName = " + cityName);
        return weathers.get(cityName);
    }



}
