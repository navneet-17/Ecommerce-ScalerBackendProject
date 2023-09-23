package dev.navneet.productservice.services;

import dev.navneet.productservice.models.Category;
import org.springframework.web.bind.annotation.PathVariable;

public interface CategoryService {
     Category getCategoryById(String uuid);
}
