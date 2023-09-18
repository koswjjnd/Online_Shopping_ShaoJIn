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
//不用RestConrtoller是因为要用view连着html
public class UserController {

    Map<String, User> users = new HashMap<>();
    @Resource(name="Zhangsan") //从resource里给我一个具体的user，如果config里有不同的bean，她会取zhangsan的这个
    private User defaultUser;

    @PostMapping("/users")
//    下面的这个String返回的是view的名字，resultMap作为这里的方法的最后一个参数，是专门用来和前端即userdetail的html来进行交互的，这是springboot这个生态提供的
    public String createUser(@RequestParam("name") String name,
                             @RequestParam("email") String email,
                             Map<String, User> resultMap) {
        User user = User.builder()
                .userName(name)
                .userEmail(email)
                .build();
//        用build的函数的好处是你可以把你想要的user的attribute写上，其他的attribute可写可不写
        users.put(name, user);
        resultMap.put("user", user);//这里的“user”就是用来在html里进行交互的
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