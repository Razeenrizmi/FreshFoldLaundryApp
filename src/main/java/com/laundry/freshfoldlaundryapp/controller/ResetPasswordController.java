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
    public String showResetPasswordForm(@RequestParam(value = "email", required = false) String email,
                                      @RequestParam(value = "token", required = false) String token,
                                      Model model) {
        // Validate token if provided
        if (email != null && token != null) {
            User user = userService.findByEmail(email);
            if (user == null || !user.isTokenValid() || !user.getToken().equals(token)) {
                model.addAttribute("error", "Invalid or expired token. Please request a new password reset.");
                return "forgot-password";
            }
        }

        model.addAttribute("email", email);
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handlePasswordReset(@RequestParam("email") String email,
                                      @RequestParam(value = "token", required = false) String token,
                                      @RequestParam("password") String password,
                                      @RequestParam("confirmPassword") String confirmPassword,
                                      RedirectAttributes redirectAttributes) {

        // Validate the passwords
        if (password == null || password.isEmpty() || !password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/reset-password?email=" + email + (token != null ? "&token=" + token : "");
        }

        // Find the user by email
        User user = userService.findByEmail(email);

        // Check if the user exists
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "No user found with this email address.");
            return "redirect:/forgot-password";
        }

        // If token is provided, validate it
        if (token != null && (!user.isTokenValid() || !user.getToken().equals(token))) {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired token. Please request a new password reset.");
            return "redirect:/forgot-password";
        }

        // Update the user's password with the hashed password
        user.setPassword(passwordEncoder.encode(password));

        // Mark token as used and clear it
        if (token != null) {
            user.setTokenUsed(true);
            user.clearToken();
        }

        userService.save(user);

        // Success message and redirect to login
        redirectAttributes.addFlashAttribute("message", "Password has been reset successfully. Please login with your new password.");
        return "redirect:/login";
    }
}
