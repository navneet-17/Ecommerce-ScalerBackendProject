package dev.navneet.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GenericProductDto implements Serializable {
   // Changing the value to String to return the string value of UUIDs / Long
    private String id; // This is the id of the product in the database.
    private String title;
    private String description;
    private String image;
    private String category;
    private double price;

   // New field to include UserDto
   private UserDto user;

    // Custom toString method
    @Override
    public String toString() {
        return "GenericProductDto{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", user=" + (user != null ? user.toString() : "null") +
                '}';
    }

}