package dev.navneet.productservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class RoleDto {
    private Long id;
    private String role;

    @Override
    public String toString() {
        return "{" +
                "roleId=" + id + '\''+
                "roleName='" + role + '\'' +
                '}';
    }
}
