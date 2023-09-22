package dev.navneet.productservice.models;

import jakarta.persistence.*;
import lombok.*;

@Builder @Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Entity(name = "products")
public class Product extends BaseModel {
    private String title;
    private String description;
    private String image;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne (cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Price price;
}