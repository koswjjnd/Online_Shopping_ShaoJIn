package com.online_shopping_shaojin.online_shopping_shaojin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.online_shopping_shaojin.online_shopping_shaojin.db.mappers") //在这指定了蓝鸟头的接口
public class OnlineShoppingShaoJInApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineShoppingShaoJInApplication.class, args);
    }

}
