package dev.navneet.productservice.inheritanceDemo.singletable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Entity(name="st_mentor")
@DiscriminatorValue("3")
public class Mentor extends User {
    private double averageRating;
}
