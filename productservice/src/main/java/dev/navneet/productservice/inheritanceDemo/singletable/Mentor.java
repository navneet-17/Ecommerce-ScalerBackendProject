package dev.navneet.productservice.inheritanceDemo.singletable;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity(name="st_mentor")
public class Mentor extends User {
    private double averageRating;
}
