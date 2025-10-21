package com.laundry.freshfoldlaundryapp.controller;

import com.laundry.freshfoldlaundryapp.model.User;
import com.laundry.freshfoldlaundryapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.security.SecureRandom;

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
            // Generate a secure 6-digit token
            String token = generateSecureToken();
            user.setToken(token);
            user.setTokenExpiry(LocalDateTime.now().plusMinutes(15)); // 15 minutes expiry
            user.setTokenUsed(false);
            userService.save(user);

            // Redirect to token display page with email
            return "redirect:/forgot-password/token-display?email=" + email;
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Email address not found.");
            return "redirect:/forgot-password";
        }
    }

    @GetMapping("/forgot-password/token-display")
    public String showTokenDisplay(@RequestParam("email") String email, Model model) {
        User user = userService.findByEmail(email);

        if (user != null && user.isTokenValid()) {
            model.addAttribute("token", user.getToken());
            model.addAttribute("email", email);
            return "token-display";
        } else {
            return "redirect:/forgot-password?error=expired";
        }
    }

    @GetMapping("/forgot-password/verify-token")
    public String showTokenVerification(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "verify-token";
    }

    @PostMapping("/forgot-password/verify-token")
    public String verifyToken(@RequestParam("email") String email,
                            @RequestParam("token") String enteredToken,
                            RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(email);

        if (user != null && user.isTokenValid() && user.getToken().equals(enteredToken)) {
            // Token is valid, redirect to reset password
            return "redirect:/reset-password?email=" + email + "&token=" + enteredToken;
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid or expired token. Please try again.");
            return "redirect:/forgot-password";
        }
    }

    private String generateSecureToken() {
        SecureRandom random = new SecureRandom();
        int token = 100000 + random.nextInt(900000); // 6-digit number
        return String.valueOf(token);
    }
}
