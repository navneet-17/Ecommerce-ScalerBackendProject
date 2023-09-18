package dev.navneet.productservice.inheritanceDemo;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity(name="ms_mentor")
public class Mentor extends User{
    private double averageRating;
}
