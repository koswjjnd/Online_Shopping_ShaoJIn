package com.online_shopping_shaojin.online_shopping_shaojin.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class User {
    private String userName;
    private String userEmail;
    private String phone;


}
