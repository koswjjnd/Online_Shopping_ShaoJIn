package com.online_shopping_shaojin.online_shopping_shaojin.configuration;

import com.online_shopping_shaojin.online_shopping_shaojin.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration//给出一个具体的User 通过注册user bean到了spring系统里面
public class UserConfig {
    @Bean(name="Nobody")
    public User userProvider(){ //这一行的User决定了我是user bean，和controller之间进行连接，如果说有很多user bean，就现在下面这种情况，那就根据name来决定选哪一个
        return User.builder()
                .userName("Nobody")
                .userEmail("NobodyEmail")
                .build();
    }
    @Bean(name="Zhangsan")
    public User userProvider2(){
        return User.builder()
                .userName("Zhangsan")
                .userEmail("ZhangsanEmail")
                .build();
    }
}
