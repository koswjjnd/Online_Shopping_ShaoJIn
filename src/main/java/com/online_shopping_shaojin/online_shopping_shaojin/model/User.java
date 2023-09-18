package com.online_shopping_shaojin.online_shopping_shaojin.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
//data可以帮我写好了get和set的所有方法
@Builder
//builder可以帮我们创建一个新class instance
@AllArgsConstructor
//帮我写了构造函数
public class User {
    private String userName;
    private String userEmail;
    private String phone;


}
