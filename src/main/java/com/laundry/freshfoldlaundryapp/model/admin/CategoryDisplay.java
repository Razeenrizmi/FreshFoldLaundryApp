package com.laundry.freshfoldlaundryapp.model.admin;

import java.util.List;

public class CategoryDisplay {
    private int categoryId;
    private String categoryName;
    private String description;
    private String iconClass;
    private int clothesCount;
    private List<String> clothesNames;

    // Constructors
    public CategoryDisplay() {
    }

    public CategoryDisplay(int categoryId, String categoryName, String description, String iconClass, int clothesCount, List<String> clothesNames) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.iconClass = iconClass;
        this.clothesCount = clothesCount;
        this.clothesNames = clothesNames;
    }

    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public int getClothesCount() {
        return clothesCount;
    }

    public void setClothesCount(int clothesCount) {
        this.clothesCount = clothesCount;
    }

    public List<String> getClothesNames() {
        return clothesNames;
    }

    public void setClothesNames(List<String> clothesNames) {
        this.clothesNames = clothesNames;
    }
}
