package com.online_shopping_shaojin.online_shopping_shaojin.controller;

import java.util.HashMap;
import java.util.Map;

import com.online_shopping_shaojin.online_shopping_shaojin.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
public class UserController {

    Map<String, User> users = new HashMap<>();
    @Resource(name="Zhangsan")
    private User defaultUser;

    @PostMapping("/users")
    public String createUser(@RequestParam("name") String name,
                             @RequestParam("email") String email,
                             Map<String, User> resultMap) {
        User user = User.builder()
                .userName(name)
                .userEmail(email)
                .build();
        users.put(name, user);
        resultMap.put("user", user);
        return "user_detail";
    }
    @GetMapping("/users/{name}")
    public String getUser(@PathVariable("name") String name,
                          Map<String, User> resultMap ){
        User user = users.getOrDefault(name, defaultUser);
        resultMap.put("user", user);
        return "user_detail";
    }


}