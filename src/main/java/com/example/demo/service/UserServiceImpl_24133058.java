package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dao.UserDao_24133058;
import com.example.demo.model.User_24133058;

@Service
public class UserServiceImpl_24133058 implements UserService_24133058 {

    @Autowired
    private UserDao_24133058 userDao;

    @Autowired
    private EmailService_24133058 emailService;

    @Override
    public User_24133058 login(String username, String password) {
        User_24133058 user = userDao.findByUsername(username);
        if (user != null && user.getPassword().equals(password) && user.isActive()) {
            return user;
        }
        return null;
    }

    @Override
    public boolean register(User_24133058 user, String otpCode) {
        if (userDao.findByUsername(user.getUsername()) != null || userDao.findByEmail(user.getEmail()) != null) {
            return false;
        }
        user.setActive(false);
        user.setAdmin(false);
        userDao.insert(user);
        emailService.sendOtpEmail(user.getEmail(), otpCode);
        return true;
    }

    @Override
    public boolean verifyOtp(String email, String inputOtp, String sessionOtp) {
        if (inputOtp != null && inputOtp.equals(sessionOtp)) {
            userDao.activateAccount(email);
            return true;
        }
        return false;
    }

    @Override
    public User_24133058 getByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public void save(User_24133058 user) {
        if (userDao.findByUsername(user.getUsername()) != null) {
            userDao.update(user);
        } else {
            userDao.insert(user);
        }
    }

    @Override
    public void delete(String username) {
        userDao.delete(username);
    }

    @Override
    public List<User_24133058> findAllPage(int page, int pageSize) {
        return userDao.findAllPage(page, pageSize);
    }

    @Override
    public int countUsers() {
        return userDao.countUsers();
    }
}