package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.User_24133058;
import com.example.demo.service.UserService_24133058;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController_24133058 {

    @Autowired
    private UserService_24133058 userService;

    @GetMapping
    public String listUsers(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 6;
        int totalUsers = userService.countUsers();
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

        List<User_24133058> list = userService.findAllPage(page, pageSize);

        model.addAttribute("users", list);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "admin/user-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User_24133058());
        model.addAttribute("isEdit", false);
        return "admin/user-form";
    }

    @GetMapping("/edit/{username}")
    public String showEditForm(@PathVariable String username, Model model) {
        User_24133058 user = userService.getByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("isEdit", true);
        return "admin/user-form";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User_24133058 user) {
        userService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable String username) {
        userService.delete(username);
        return "redirect:/admin/users";
    }
}