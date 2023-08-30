package com.online_shopping_shaojin.online_shopping_shaojin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public String helloWorld(){
        return "HelloWorld";
    }
    @GetMapping("/echo/{para}")
    public String helloWorld(@PathVariable("para")String para){
        return para;
    }

}
