package dev.navneet.productservice.controllers;

import dev.navneet.productservice.dtos.GenericProductDto;
import dev.navneet.productservice.exceptions.NotFoundException;
import dev.navneet.productservice.models.Category;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @GetMapping("{id}")
    public Category getCategoryById(@PathVariable("id") String uuid) {
        return null;
    }
}
