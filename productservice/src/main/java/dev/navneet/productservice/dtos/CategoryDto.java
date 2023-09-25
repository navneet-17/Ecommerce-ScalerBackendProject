package dev.navneet.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
public class CategoryDto {
    private String id;
    private String categoryName;
    private List<ProductDto> ProductList;
}




















