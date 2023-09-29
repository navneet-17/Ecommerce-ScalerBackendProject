package dev.navneet.productservice.services;

import dev.navneet.productservice.models.Category;
import dev.navneet.productservice.models.Product;
import dev.navneet.productservice.repositories.CategoryRepository;
import dev.navneet.productservice.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository){
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
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
//    public List<String> getProductTitles(List<String> categoryUUIDs) {
//        List<UUID> uuids = new ArrayList<>();
//        // Create a List of UUIds from String UUIds passed in the Request Body
//        for (String uuid: categoryUUIDs) {
//            uuids.add(UUID.fromString(uuid));
//        }
//        // get the Categories from the database for the UUIDs passed in Request
//        List<Category> categories = categoryRepository.findAllById(uuids);
//        // get the products for the categories -->  this will cause the N+1 problem
//        List<Product> products = productRepository.findAllByCategoryIn(categories);
//        List<String> titles = new ArrayList<>();
//        for (Product p: products) {
//            titles.add(p.getTitle());
//        }
//        return titles;
//    }

//    public List<String> getProductTitles(String uuid){
//        Optional<Category> optionalCategory = categoryRepository.findById(UUID.fromString(uuid));
//        if(optionalCategory.isEmpty()){
//            return null;
//        }
//        Category category = optionalCategory.get();
//        List<String> titles  = new ArrayList<>();
//        category.getProducts().forEach(
//                product -> titles.add(product.getTitle())
//        );
//        return titles;
//    }

    public List<String> getProductTitles(List<String> categoryUUIDs) {
        List<UUID> uuids = new ArrayList<>();
        for (String uuid: categoryUUIDs) {
            uuids.add(UUID.fromString(uuid));
        }
        List<Category> categories = categoryRepository.findAllById(uuids);
        List<Product> products = productRepository.findAllByCategoryIn(categories);
        List<String> titles = new ArrayList<>();
        for (Product p: products) {
            titles.add(p.getTitle());
        }
        return titles;
    }
}
