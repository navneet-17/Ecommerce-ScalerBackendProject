package dev.navneet.productservice.services;

import dev.navneet.productservice.models.Category;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService{
    private CategoryRepository categoryRepository;

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
}
