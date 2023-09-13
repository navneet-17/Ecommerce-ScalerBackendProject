package dev.navneet.productservice.models;

import jakarta.persistence.Entity;

@Entity(name = "categories")
public class Category extends BaseModel {
    private String name;
}
