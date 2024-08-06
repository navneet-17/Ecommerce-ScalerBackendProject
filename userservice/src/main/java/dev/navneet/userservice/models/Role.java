package dev.navneet.userservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.navneet.userservice.security.CustomSpringGrantedAuthority;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@Entity(name = "roles")
@JsonDeserialize(as = Role.class)
public class Role extends BaseModel{
private String role;

}
