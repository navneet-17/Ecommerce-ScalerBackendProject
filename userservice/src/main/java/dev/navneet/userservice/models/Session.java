package dev.navneet.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter
@Entity(name="sessions")
public class Session extends BaseModel{
        private String token;
        private Date createdAt;
        private Date expiringAt;
        private Date lastLoggedInAt;
        private Date loggedOutAt;

        @ManyToOne
        private User user;

      @Enumerated(EnumType.STRING)
       private SessionStatus status;
}
