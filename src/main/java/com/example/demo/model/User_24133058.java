package com.example.demo.model;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User_24133058 implements Serializable {
    private String username;
    private String password;
    private String phone;
    private String fullname;
    private String email;
    private boolean admin;
    private boolean active;
    private String images;
}