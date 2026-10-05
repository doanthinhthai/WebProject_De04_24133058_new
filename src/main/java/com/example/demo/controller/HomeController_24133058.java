package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController_24133058 {

    @GetMapping(value = {"/", "/home"})
    public String home() {
        return "web/home";
    }

    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin/home";
    }
}