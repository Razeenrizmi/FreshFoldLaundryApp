package com.laundry.freshfoldlaundryapp.model.admin;

import java.time.LocalDateTime;

public class Category {
    private int categoryId;
    private String name;
    private String imagePath;
    private LocalDateTime createdAt;

    // Constructors
    public Category() {
    }

    public Category(int categoryId, String name, String imagePath, LocalDateTime createdAt) {
        this.categoryId = categoryId;
        this.name = name;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
