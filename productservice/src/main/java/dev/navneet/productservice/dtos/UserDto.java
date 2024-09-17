package dev.navneet.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter@Setter
public class UserDto {
    private String email;
    private Set<RoleDto> roles = new HashSet<>();

    @Override
    public String toString() {
        return " \n User: " +
                "email='" + email + '\'' +
                ", \n roles=" + (roles != null && !roles.isEmpty() ? roles.toString() : "[]") +
                '}';
    }

}

