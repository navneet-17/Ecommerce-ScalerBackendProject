package dev.navneet.productservice.dtos;
import lombok.Getter;
import lombok.Setter;
import dev.navneet.productservice.models.Category;
import dev.navneet.productservice.models.Price;

@Getter@Setter
public class ProductDto {
    private String title;
    private String description;
    private String image;
    private Price price;
}
