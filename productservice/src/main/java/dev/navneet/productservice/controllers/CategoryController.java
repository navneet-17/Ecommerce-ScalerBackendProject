package dev.navneet.productservice.controllers;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import dev.navneet.productservice.models.Category;
import dev.navneet.productservice.services.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private CategoryService categoryService;
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }
    @GetMapping("{id}")
    public String getCategoryById(@PathVariable("id") String uuid) {
        Category category = categoryService.getCategoryById(uuid);
        System.out.println("Fetched Category Name is : "+ category.getName());
        return category.getName();
    }
}
