package dev.navneet.productservice.inheritanceDemo.singletable;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "st_student")
public class Student extends User {
    private double psp;
    private double attendance;
}
