package com.laundry.freshfoldlaundryapp.controller;

import com.laundry.freshfoldlaundryapp.dto.UserRegistrationDto;
import com.laundry.freshfoldlaundryapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto registrationDto, Model model) {
        boolean registrationSuccess = userService.registerNewUser(registrationDto);
        if (registrationSuccess) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Email already in use. Please use a different email.");
            return "register";
        }
    }
}