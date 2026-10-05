package com.example.demo.dao;

import java.util.List;
import com.example.demo.model.User_24133058;

public interface UserDao_24133058 {
    User_24133058 findByUsername(String username);
    User_24133058 findByEmail(String email);
    void insert(User_24133058 user);
    void update(User_24133058 user);
    void delete(String username);
    void activateAccount(String email);
    List<User_24133058> findAllPage(int page, int pageSize);
    int countUsers();
}