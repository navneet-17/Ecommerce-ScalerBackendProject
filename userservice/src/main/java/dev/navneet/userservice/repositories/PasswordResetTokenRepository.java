package dev.navneet.userservice.repositories;

import dev.navneet.userservice.models.PasswordResetToken;
import dev.navneet.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByUserEmailAndToken(String email, String token);
}