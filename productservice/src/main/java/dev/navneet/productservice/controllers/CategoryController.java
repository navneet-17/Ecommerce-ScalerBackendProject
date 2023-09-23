package dev.navneet.productservice.controllers;

import dev.navneet.productservice.dtos.CategoryDto;
import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.dtos.GetProductTitlesRequestDto;
import dev.navneet.productservice.dtos.ProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import dev.navneet.productservice.models.Category;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.services.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private CategoryService categoryService;
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }
    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable("id") String uuid) {
        List<Product> products = categoryService.getCategoryById(uuid).getProducts();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product: products) {
            ProductDto productDto = new ProductDto();
            productDto.setDescription(product.getDescription());
            productDto.setTitle(product.getTitle());
            productDto.setImage(product.getImage());
            productDto.setPrice(product.getPrice());
            productDtos.add(productDto);
        }
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryName(categoryService.getCategoryById(uuid).getName());
        categoryDto.setProductList(productDtos);
        return categoryDto;
    }

    @GetMapping("/titles/{uuid}")
    public List<String> getProductTitles(@PathVariable("uuid") String uuid) {
        return categoryService.getProductTitles(uuid);
    }



}
//    public List<ProductDto> getCategory(@PathVariable("id") String uuid) {
//        return productDtos;