package dev.navneet.userservice.repositories;

import dev.navneet.userservice.models.Session;
import dev.navneet.userservice.models.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static dev.navneet.userservice.models.SessionStatus.ACTIVE;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByTokenAndUser_Id(String token, Long userId);
    Optional<Session> findByUserIdAndStatus( Long userId, SessionStatus SessionStatus);
}