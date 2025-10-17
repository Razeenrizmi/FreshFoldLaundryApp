package com.laundry.freshfoldlaundryapp.model.order;

public class Category {
    private Integer categoryId;
    private String name;
    private String imagePath;

    public Category() {}

    public Category(Integer categoryId, String name, String imagePath) {
        this.categoryId = categoryId;
        this.name = name;
        this.imagePath = imagePath;
    }

    // Getters and Setters
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}