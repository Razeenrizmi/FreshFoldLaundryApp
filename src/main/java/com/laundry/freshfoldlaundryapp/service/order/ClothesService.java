package com.laundry.freshfoldlaundryapp.service.order;

import com.laundry.freshfoldlaundryapp.model.order.Category;
import com.laundry.freshfoldlaundryapp.model.order.Clothes;
import com.laundry.freshfoldlaundryapp.repository.order.CategoryRepository;
import com.laundry.freshfoldlaundryapp.repository.order.ClothesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.List;

@Service
public class ClothesService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ClothesRepository clothesRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Clothes> getClothesByCategory(Integer categoryId) {
        return clothesRepository.findByCategoryId(categoryId);
    }

    public Clothes getClothById(Integer clothId) {
        return clothesRepository.findById(clothId);
    }
}