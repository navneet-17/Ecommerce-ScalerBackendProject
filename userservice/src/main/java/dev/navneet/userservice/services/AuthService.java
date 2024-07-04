package dev.navneet.userservice.services;

import dev.navneet.userservice.dtos.UserDto;
import dev.navneet.userservice.exceptions.JwtValidationException;
import dev.navneet.userservice.exceptions.NotFoundException;
import dev.navneet.userservice.models.Session;
import dev.navneet.userservice.models.SessionStatus;
import dev.navneet.userservice.repositories.SessionRepository;
import dev.navneet.userservice.repositories.UserRepository;
import dev.navneet.userservice.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AuthService {
    @Autowired
    private Environment env;

    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<UserDto> login(String email, String password) throws NotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new NotFoundException("User with email "+ email+ " is not found");
        }
        User user = userOptional.get();
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new NotFoundException("Entered password is incorrect");
        }
        else{
            System.out.println("Password Validation is successful");
        }
        String token = RandomStringUtils.randomAlphanumeric(30);

        //        *******  JWT implementation ********/
                //Scaler uses HS256 algorithm, we will also use the same for our JWT implementation
                // Get the secret key from the properties file
             String secretKeyString = env.getProperty("custom.jwt.secretKey");
            if (secretKeyString == null || secretKeyString.length() < 32) {
                throw new IllegalArgumentException("The JWT secret key must be at least 32 characters long.");
            }
            SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));

                SignatureAlgorithm alg = SignatureAlgorithm.HS256;


                Instant now = Instant.now();
                Instant expiry = now.plus(7, ChronoUnit.DAYS);

               Map<String, Object> jsonForJwt = new HashMap<>();
                jsonForJwt.put("email", user.getEmail());
                jsonForJwt.put("roles", user.getRoles());
                jsonForJwt.put("createdAt", now.getEpochSecond());
                jsonForJwt.put("expiryAt", expiry.getEpochSecond());

                String jwtToken = Jwts.builder()
                                            .claims(jsonForJwt)
                                            .signWith(secretKey, alg)
                                            .compact();
                System.out.println("Generated JWT Token: " + jwtToken+" for user: "+user.getEmail());
          /*****************************************************/
        // Session creation
        Session session = new Session();
        session.setStatus(SessionStatus.ACTIVE);
        session.setToken(jwtToken); // Use the generated JWT token
        session.setUser(user);
        session.setCreatedAt(Date.from(now)); // Set the Date field for creation
        session.setExpiringAt(Date.from(expiry)); // Set the Date field for expiry
        session.setLastLoggedInAt(new Date());
        sessionRepository.save(session);

         UserDto userDto = new UserDto();

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + jwtToken);

        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto, headers, HttpStatus.OK);

        return response;
    }

    public ResponseEntity<String> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No active session was found for user"+ userId);
        }

        Session session = sessionOptional.get();
        session.setStatus(SessionStatus.LOGGED_OUT);
        session.setLoggedOutAt(new Date());
        sessionRepository.save(session);
        System.out.println("User with user id"+ userId +" has been successfully logged out");
        return ResponseEntity.ok("You have been successfully logged out");
    }

    public UserDto signUp(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);
    }

       public ResponseEntity<Map<String, Object>> validateToken(String jwtToken) {
        return null;
    }

    public SessionStatus validate(String token, Long userId) {
        // get the secret key for the token validation
        String secretKeyString = env.getProperty("custom.jwt.secretKey");
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));

        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return SessionStatus.EXPIRED;
        }
        Session session = sessionOptional.get();

        if (session.getStatus() != SessionStatus.ACTIVE) {
            return SessionStatus.EXPIRED;
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Email Check
            String tokenEmail = claims.get("email", String.class);
            if (tokenEmail == null || !tokenEmail.equals(session.getUser().getEmail())) {
                return SessionStatus.EXPIRED;
            }

            // Token Expiration Check
            Long expiryAt = claims.get("expiryAt", Long.class);
            if (expiryAt == null || expiryAt < System.currentTimeMillis() / 1000) {
                return SessionStatus.EXPIRED;
            }
            // Roles Check (optional, if necessary)
            // List<String> tokenRoles = claims.get("roles", List.class);
            // if (tokenRoles == null || !tokenRoles.equals(session.getUser().getRoles())) {
            //        return SessionStatus.EXPIRED;
            // }

        } catch (Exception e) {
            // If token parsing or validation fails, consider the session as expired
            return SessionStatus.EXPIRED;
        }

        return SessionStatus.ACTIVE;
    }
}
