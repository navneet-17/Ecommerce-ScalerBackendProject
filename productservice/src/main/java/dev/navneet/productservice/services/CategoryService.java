package dev.navneet.productservice.services;

import dev.navneet.productservice.models.Category;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CategoryService {
     Category getCategoryById(String uuid);
//     List<String> getProductTitles(String categoryUUIDs);
     List<String> getProductTitles(List<String> categoryUUIDs);
}
