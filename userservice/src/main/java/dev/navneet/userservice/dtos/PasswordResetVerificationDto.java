package dev.navneet.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class PasswordResetVerificationDto {
    private String email;
    private String resetCode;
    private String newPassword;
}
