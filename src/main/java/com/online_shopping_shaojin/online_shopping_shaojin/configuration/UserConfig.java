package com.online_shopping_shaojin.online_shopping_shaojin.configuration;

import com.online_shopping_shaojin.online_shopping_shaojin.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    @Bean(name="Nobody")
    public User userProvider(){
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
