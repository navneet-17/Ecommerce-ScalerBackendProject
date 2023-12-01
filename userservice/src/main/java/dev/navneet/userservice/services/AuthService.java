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
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
            throw new NotFoundException("Entered password is invalid");
        }
        String token = RandomStringUtils.randomAlphanumeric(30);

        //        *******  JWT implementation ********/
                //Scaler uses HS256 algorithm, we will also use the same for our JWT implementation
                String secretKeyString = env.getProperty("custom.jwt.secretKey");
                SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));

                SignatureAlgorithm alg = SignatureAlgorithm.HS256;
               SecretKey key = Keys.secretKeyFor(alg);

               Map<String, Object> jsonForJwt = new HashMap<>();
                jsonForJwt.put("email", user.getEmail());
                jsonForJwt.put("roles", user.getRoles());
                jsonForJwt.put("createdAt", new Date());
                jsonForJwt.put("expiryAt", new Date(LocalDate.now().plusDays(7).toEpochDay()));

                String jwtToken = Jwts.builder()
                                            .claims(jsonForJwt)
                                            .signWith(key, alg)
                                            .compact();
                System.out.println("Generated JWT Token: " + jwtToken+" for user: "+user.getEmail());
          /*****************************************************/

        Session session = new Session();
        session.setStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDto userDto = new UserDto();

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);

        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto, headers, HttpStatus.OK);

        return response;
    }

    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null;
        }
        Session session = sessionOptional.get();
        session.setStatus(SessionStatus.LOGGED_OUT);
        sessionRepository.save(session);
        return ResponseEntity.ok().build();
    }

    public UserDto signUp(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);
    }

    // Your secret key
    private final String secretKey = "yourSecretKey"; // Replace with your actual secret key

    public ResponseEntity<Map<String, Object>> validateToken(String jwtToken) {
        return null;
    }
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(jwtToken)
//                    .getBody();
//
//            Date expirationDate = claims.get("expiryAt", Date.class);
//            Date now = new Date();
//            if (expirationDate.before(now)) {
//                throw new JwtValidationException("Token has expired");
//            }
//
//            response.put("message", "Token is valid");
//            return ResponseEntity.ok(response);
//
//        } catch (JwtValidationException e) {
//            response.put("message", e.getMessage());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//        } catch (Exception e) {
//            response.put("message", "Invalid token");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//        }
//    }

    public SessionStatus validate(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return SessionStatus.EXPIRED;
        }

        Session session = sessionOptional.get();
        if (session.getStatus() != SessionStatus.ACTIVE) {
            return SessionStatus.EXPIRED;
        }

        Jwts.parser().build();
        return SessionStatus.ACTIVE;
    }
}
