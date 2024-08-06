package dev.navneet.userservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.navneet.userservice.security.CustomSpringUserDetails;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter@Setter
@Entity(name = "users")
@JsonDeserialize(as = User.class)
public class User extends BaseModel{
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();
}
