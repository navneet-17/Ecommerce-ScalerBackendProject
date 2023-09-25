package dev.navneet.productservice.services;

import dev.navneet.productservice.models.Category;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    CategoryService categoryService;

    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public Category getCategoryById(String uuid){
        Optional<Category> optionalCategory = categoryRepository.findById(UUID.fromString(uuid));
       if(optionalCategory.isEmpty()){
           return null;
       }
       Category category = optionalCategory.get();
        List<Product> products = category.getProducts();
        return category;
    }

    // this method will demonstrate the N+1 problem
    public List<String> getProductTitles(String uuid){
        Optional<Category> optionalCategory = categoryRepository.findById(UUID.fromString(uuid));
        if(optionalCategory.isEmpty()){
            return null;
        }
        Category category = optionalCategory.get();
        List<String> titles  = new ArrayList<>();

        category.getProducts().forEach(
                product -> titles.add(product.getTitle())
        );

        return titles;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

}
