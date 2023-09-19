package dev.navneet.productservice.inheritanceDemo.singletable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
    @Getter
    @Setter
    @Entity(name = "st_ta")
    @DiscriminatorValue("2")
    public class TA extends User {
        private double averageRating;
    }

