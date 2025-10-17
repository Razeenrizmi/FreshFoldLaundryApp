package com.laundry.freshfoldlaundryapp.dto.Admin;

import java.util.List;

public class CategoryManagementDTO {
    private int categoryId;
    private String categoryName;
    private String description;
    private String iconClass;
    private int clothesCount;
    private List<String> clothesNames;// Add this field
    private String imagePath;
    private String createdAt;


    // Getters and Setters
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconClass() { return iconClass; }
    public void setIconClass(String iconClass) { this.iconClass = iconClass; }

    public int getClothesCount() { return clothesCount; }
    public void setClothesCount(int clothesCount) { this.clothesCount = clothesCount; }

    public List<String> getClothesNames() { return clothesNames; }  // Add getter
    public void setClothesNames(List<String> clothesNames) { this.clothesNames = clothesNames; }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
