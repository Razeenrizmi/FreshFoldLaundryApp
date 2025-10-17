package com.laundry.freshfoldlaundryapp.controller.Admin;



import com.laundry.freshfoldlaundryapp.dto.Admin.CategoryManagementDTO;
import com.laundry.freshfoldlaundryapp.service.admin.CategoryManagementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CategoryManagementController {
    private CategoryManagementService categoryService = new CategoryManagementService();

    @GetMapping("/category-management")
    public String categoryManagement(Model model) {
        List<CategoryManagementDTO> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        return "admin/category-management";
    }

    @PostMapping("/category-management/add")
    public String addCategory(
            @RequestParam String categoryName,
            @RequestParam String description,
            @RequestParam String iconClass,
            Model model) {

        boolean success = categoryService.addCategory(categoryName, description, iconClass);

        if (success) {
            model.addAttribute("successMessage", "Category added successfully!");
        } else {
            model.addAttribute("errorMessage", "Failed to add category!");
        }

        List<CategoryManagementDTO> categoryList = categoryService.getAllCategories();
        model.addAttribute("categoryList", categoryList);
        return "redirect:/category-management";
    }
}
