package dev.navneet.userservice.dtos;

import dev.navneet.userservice.models.Role;
import dev.navneet.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter@Setter
public class UserDto {
    private String email;
    private Set<Role> roles = new HashSet<>();
    private String message;

    public static UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}

