package com.example.demo.controller;

import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.User_24133058;
import com.example.demo.service.UserService_24133058;
import javax.servlet.http.HttpSession;

@Controller
public class AuthController_24133058 {

    @Autowired
    private UserService_24133058 userService;

    @GetMapping("/login")
    public String showLogin() {
        return "web/login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password,
                              HttpSession session, Model model) {
        User_24133058 user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("account", user);
            if (user.isAdmin()) {
                return "redirect:/admin/home";
            }
            return "redirect:/home";
        }
        model.addAttribute("error", "Tài khoản hoặc mật khẩu không đúng, hoặc tài khoản chưa kích hoạt!");
        return "web/login";
    }

    @GetMapping("/register")
    public String showRegister() {
        return "web/register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute User_24133058 user, HttpSession session, Model model) {
        // Sinh ngẫu nhiên mã OTP 6 chữ số
        String otp = String.format("%06d", new Random().nextInt(999999));
        boolean success = userService.register(user, otp);
        if (success) {
            session.setAttribute("registerEmail", user.getEmail());
            session.setAttribute("otpCode", otp);
            return "redirect:/verify-otp";
        }
        model.addAttribute("error", "Username hoặc Email đã tồn tại trong hệ thống!");
        return "web/register";
    }

    @GetMapping("/verify-otp")
    public String showVerifyOtp() {
        return "web/verify-otp";
    }

    @PostMapping("/verify-otp")
    public String handleVerifyOtp(@RequestParam String otp, HttpSession session, Model model) {
        String email = (String) session.getAttribute("registerEmail");
        String sessionOtp = (String) session.getAttribute("otpCode");

        if (userService.verifyOtp(email, otp, sessionOtp)) {
            session.removeAttribute("otpCode");
            session.removeAttribute("registerEmail");
            model.addAttribute("msg", "Kích hoạt tài khoản thành công! Vui lòng đăng nhập.");
            return "web/login";
        }
        model.addAttribute("error", "Mã OTP không chính xác! Vui lòng kiểm tra lại email.");
        return "web/verify-otp";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}