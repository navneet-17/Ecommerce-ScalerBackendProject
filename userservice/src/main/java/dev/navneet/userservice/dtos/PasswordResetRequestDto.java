package dev.navneet.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequestDto {
    private String email;
}

