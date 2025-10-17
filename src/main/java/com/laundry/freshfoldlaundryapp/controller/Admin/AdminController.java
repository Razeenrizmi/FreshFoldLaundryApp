package com.laundry.freshfoldlaundryapp.controller.Admin;


import com.laundry.freshfoldlaundryapp.DAO.AdminDAO;
import com.laundry.freshfoldlaundryapp.model.admin.Admin;
import com.laundry.freshfoldlaundryapp.service.admin.AdminService;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {

    private final AdminService adminService = new AdminService();

//    @Autowired
//    private CategoryService categoryService;

    @RequestMapping("/admin")
    public String index() {
        return "AdminLoginForm";
    }

    @GetMapping("/admin/register")
    public String showRegisterPage() {
        return "admin/AdminRegisterForm";
    }

    @PostMapping("/admin/register")
    public String register(@RequestParam String fullName, @RequestParam String userName, @RequestParam String password, @RequestParam String email, @RequestParam String phoneNumber, @RequestParam String role) {
        Admin admin = new Admin();
        admin.setFullName(fullName);
        admin.setUserName(userName);
        admin.setPassword(password);
        admin.setEmail(email);
        admin.setPhoneNumber(phoneNumber);
        admin.setRole(role);

        if (adminService.addAdmin(admin)) {
            return "admin/AdminDashboard"; // success
        } else {
            return "admin/AdminRegisterForm"; // fail
        }
    }

    @GetMapping("/admins")
    public String viewAdmin(Model model) {
        List<Admin> adminList = adminService.getAllAdmin();
        model.addAttribute("adminList", adminList);
        return "admin/AdminList";
    }

    // Admin Dashboard - doesn't require ID
    @GetMapping("/admin/admin/AdminDashboard")
    public String adminDashboard(Model model, Authentication authentication) {
        // Get the currently logged-in admin
        String username = authentication.getName();
        AdminDAO adminDAO = new AdminDAO();
        Admin admin = adminDAO.getAdminByUsername(username);

        if (admin != null) {
            model.addAttribute("admin", admin);
        }

        return "admin/AdminDasboard";  // Note: template has typo in filename
    }

    // method for view specific admin profile (renamed for clarity)
    @GetMapping("/admin/profile/{id}")
    public String viewAdminProfile(@PathVariable ("id") int id, Model model) {
        Admin admin = adminService.getAdminById(id);
        if (admin != null) {
            model.addAttribute("admin", admin);
            return "admin/AdminProfile";
        }

        return "redirect:/admins";
    }

    // method to handle admin deletion
    @GetMapping("/admin/delete/{id}")
    public String deleteAdmin(@PathVariable ("id") int id) {
        adminService.deleteAdmin(id);
        return "redirect:/admins";
    }

    // method to show edit form
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable ("id") int id, Model model) {
        Admin admin = adminService.getAdminById(id);
        if (admin != null) {
            model.addAttribute("admin", admin);
            return "admin/AdminEditForm";
        }
        return "redirect:/admins";
    }

    // method for handle admin update
    @PostMapping("/admin/update/{id}")
    public String updateAdmin(@PathVariable ("id") int id, @RequestParam String fullName, @RequestParam String userName, @RequestParam String password, @RequestParam String email, @RequestParam String phoneNumber, @RequestParam String role, @RequestParam String status){
        Admin admin = new Admin();
        admin.setAdminid(id);
        admin.setFullName(fullName);
        admin.setUserName(userName);
        admin.setPassword(password);
        admin.setEmail(email);
        admin.setPhoneNumber(phoneNumber);
        admin.setRole(role);
        admin.setStatus(status);

        adminService.updateAdmin(admin);
        return "redirect:/admins";
    }

    // Add logout mapping
    @PostMapping("/logout")
    public String logout() {
        // Invalidate session or perform logout operations
        return "redirect:/login";
    }

    // Removed duplicate category management endpoints - they exist in CategoryManagementController
}
