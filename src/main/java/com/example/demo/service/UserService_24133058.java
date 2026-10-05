package com.example.demo.service;

import java.util.List;
import com.example.demo.model.User_24133058;

public interface UserService_24133058 {
    User_24133058 login(String username, String password);
    boolean register(User_24133058 user, String otpCode);
    boolean verifyOtp(String email, String inputOtp, String sessionOtp);
    User_24133058 getByUsername(String username);
    void save(User_24133058 user);
    void delete(String username);
    List<User_24133058> findAllPage(int page, int pageSize);
    int countUsers();
}