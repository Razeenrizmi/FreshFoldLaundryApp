package com.laundry.freshfoldlaundryapp.controller;

import com.laundry.freshfoldlaundryapp.model.User;
import com.laundry.freshfoldlaundryapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ResetPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam(value = "email", required = false) String email, Model model) {
        // For now, just show the form - token functionality removed
        model.addAttribute("email", email);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handlePasswordReset(@RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      @RequestParam("confirmPassword") String confirmPassword,
                                      RedirectAttributes redirectAttributes) {

        // Validate the passwords
        if (password == null || password.isEmpty() || !password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/reset-password?email=" + email;
        }

        // Find the user by email
        User user = userService.findByEmail(email);

        // Check if the user exists
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "No user found with this email address.");
            return "redirect:/forgot-password";
        }

        // Update the user's password with the hashed password
        user.setPassword(passwordEncoder.encode(password));
        userService.save(user);

        redirectAttributes.addFlashAttribute("success", "Your password has been reset successfully. Please log in with your new password.");
        return "redirect:/login";
    }
}
