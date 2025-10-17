package com.laundry.freshfoldlaundryapp.controller;

import com.laundry.freshfoldlaundryapp.model.User;
import com.laundry.freshfoldlaundryapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(email);

        if (user != null) {
            String token = UUID.randomUUID().toString();
            user.setToken(token); // Set the token on the user object.
            userService.save(user); // Save the updated user to the database.

            // Redirect directly to reset-password page with token
            return "redirect:/reset-password?token=" + token;
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Email address not found.");
            return "redirect:/forgot-password";
        }
    }
}
