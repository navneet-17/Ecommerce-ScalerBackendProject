package dev.navneet.userservice.services;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.navneet.userservice.configs.KafkaProducerClient;
import dev.navneet.userservice.dtos.SendEmailMessageDto;
import dev.navneet.userservice.dtos.UserDto;
import dev.navneet.userservice.exceptions.NotFoundException;
import dev.navneet.userservice.exceptions.UserAlreadyRegisteredException;
import dev.navneet.userservice.models.Role;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuthService {
    @Autowired
    private Environment env;

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final KafkaProducerClient kafkaProducerClient;
    private final ObjectMapper objectMapper;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       KafkaProducerClient kafkaProducerClient,
                       ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.kafkaProducerClient = kafkaProducerClient;
        this.objectMapper = objectMapper;
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

        // Check if there is already an active session for the user
        Optional<Session> activeSession = sessionRepository.findByUserIdAndStatus(user.getId(), SessionStatus.ACTIVE);
        if (activeSession.isPresent()) {
            UserDto userDto = new UserDto();
            userDto.setMessage("User is already logged in. Please log out before re-logging in.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(userDto);
        }

            System.out.println("Password Validation is successful");
            System.out.println("Creating  JWT token for the user");

//        String token = RandomStringUtils.randomAlphanumeric(30);

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
         userDto.setEmail(email);
         userDto.setMessage("Login was successful");

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + jwtToken);

        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto, headers, HttpStatus.OK);

        return response;
    }

    public ResponseEntity<String> logout(String token, Long userId) {
            Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

            if (sessionOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No active session was found for user " + userId);
            }

            Session session = sessionOptional.get();
            session.setStatus(SessionStatus.LOGGED_OUT);
            session.setLoggedOutAt(new Date());
            sessionRepository.save(session);
            System.out.println("User with user id " + userId + " has been successfully logged out");
            return ResponseEntity.ok("You have been successfully logged out");

    }

    public UserDto signUp(String email, String password) {

        // Check if the user is already registered
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new UserAlreadyRegisteredException("User with email " + email + " is already registered. Please log in.");
//            UserDto userDto = new UserDto();
//            userDto.setMessage("User with email " + email + " is already registered. Please log in.");
//            return userDto;
        }
        // If the user is not registered, proceed with registration
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        UserDto userDto = UserDto.from(savedUser);
        userDto.setMessage("You have been registered successfully. Use your email and password for login.");
        try{
            kafkaProducerClient.sendMessage("userSignUp", objectMapper.writeValueAsString(userDto));
            SendEmailMessageDto emailMessage = new SendEmailMessageDto();
            emailMessage.setTo(userDto.getEmail());
            emailMessage.setFrom("admin@scaler.com");
            emailMessage.setSubject("Welcome to Scaler, "+ userDto.getEmail());
            emailMessage.setBody("Thanks for creating an account with us. We look forward to you growing. \n\n Team Scaler");
            kafkaProducerClient.sendMessage("sendEmail", objectMapper.writeValueAsString(emailMessage));
        } catch (Exception e) {
            System.out.println("Something has gone wrong");
        }
        return userDto;
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

        //Validating the Token data
        try {
            @SuppressWarnings("deprecation")
            Jws<Claims> jwsClaims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            @SuppressWarnings("deprecation")
            Claims claims = jwsClaims.getBody();

            // Email Check
            String tokenEmail = claims.get("email", String.class);
            if (tokenEmail == null || !tokenEmail.equals(session.getUser().getEmail())) {
                System.out.println("Token email mismatch");
                return SessionStatus.EXPIRED;
            }

            // Token Expiration Check
            Date tokenExpiryAt = claims.get("expiryAt", Date.class);
            if (tokenExpiryAt == null || tokenExpiryAt.before(new Date())) {
                System.out.println("Token has already expired at " + tokenExpiryAt);
                return SessionStatus.EXPIRED;
            }

            // Roles Check
            Object rolesObj = claims.get("roles");
            List<Role> userRoles;
            if (rolesObj instanceof List<?>) {
                ObjectMapper objectMapper = new ObjectMapper();
                userRoles = objectMapper.convertValue(rolesObj, new TypeReference<List<Role>>() {});
            } else {
                System.out.println("Roles claim is not of type List<Role>");
                return SessionStatus.EXPIRED;
            }

            //convert the extracted <List> user roles and compare with the Set<roles> present in the db for user:
            Set<Role> tokenRolesSet = new HashSet<>(userRoles);
            Set<Role> sessionRolesSet = session.getUser().getRoles();

            if (!tokenRolesSet.equals(sessionRolesSet)){
                System.out.println("Token roles mismatch");
                return SessionStatus.EXPIRED;
            }

        } catch (Exception e) {
            // If token parsing or validation fails, consider the session as expired
            // TODO --> replace this Call to 'printStackTrace()' with  more robust logging using  'SLF4J and Logback' dependencies
            // TODO -->  and Configuring Logger: Create a logback.xml
            e.printStackTrace();
            return SessionStatus.EXPIRED;
        }
        return SessionStatus.ACTIVE;
    }
}
