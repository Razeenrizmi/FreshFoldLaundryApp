package com.laundry.freshfoldlaundryapp.controller;

import com.laundry.freshfoldlaundryapp.dto.UserProfileDto;
import com.laundry.freshfoldlaundryapp.model.User;
import com.laundry.freshfoldlaundryapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String showProfilePage(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            // Handle case where user is not found, maybe redirect to login or error page
            return "redirect:/login";
        }

        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setFirstName(user.getFirstName());
        profileDto.setLastName(user.getLastName());
        profileDto.setPhoneNumber(user.getPhoneNumber());
        profileDto.setAddress(user.getAddress());

        model.addAttribute("profileDto", profileDto);
        model.addAttribute("role", user.getRole());
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute UserProfileDto profileDto, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if (user != null) {
            user.setFirstName(profileDto.getFirstName());
            user.setLastName(profileDto.getLastName());
            user.setPhoneNumber(profileDto.getPhoneNumber());
            user.setAddress(profileDto.getAddress());
            userService.save(user);

            if (user.getRole().equals("CUSTOMER")) {
                return "redirect:/customer-dashboard";
            } else if (user.getRole().equals("STAFF")) {
                return "redirect:/staff-dashboard";
            } else {
                return "redirect:/profile";
            }
        }
            return "redirect:/profile";
        }

}