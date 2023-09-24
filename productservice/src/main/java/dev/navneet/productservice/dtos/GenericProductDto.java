package dev.navneet.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericProductDto {
//    private Long id; // This is the id of the product in the database.
    // Changing the value to String to return the string value of UUIDs
    private String id;
    private String title;
    private String description;
    private String image;
    private String category;
    private double price;
}