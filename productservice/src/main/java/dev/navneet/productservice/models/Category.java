package dev.navneet.productservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;
@Setter @Getter@AllArgsConstructor@NoArgsConstructor
@Entity(name = "categories")
public class Category extends BaseModel {
    @Column
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public Category(String name) {
        this.name = name;
    }
}








