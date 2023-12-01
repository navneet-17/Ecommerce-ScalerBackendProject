
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$$$ class-17: 11Oct Auth-1: Authentication v/s Authorization, oAuth $$$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
Authentication v/s Authorization:
* Authentication is the process of verifying the identity of a user.
* Authorization is the process of verifying what they have access to.

* 2 Factor Authentication:
  2FA is an extra layer of security used to make sure that people trying to gain access to an online account are who they say they are. First, a user will enter their username and a password. Then, instead of immediately gaining access, they will be required to provide another piece of information.

*2 types of authentication:
1. Basic Authentication
   Basic authentication is a simple authentication scheme built into the HTTP protocol.
2. Token Based Authentication
   Token-based authentication is a process where the user sends his credential to the server, server will validate the user details and generate a token which is sent as response to the users, and user store the token in client side and send this token on every request to the server to access protected resources.

OAuth:
OAuth is an open standard for access delegation, commonly used as a way for Internet users to grant websites or applications access to their information on other websites but without giving them the passwords.

* oAuth is mainly used for authorization purpose.
  --> Checking if the user is authorized to access the resource or not.
  --> It is a protocol that allows a user to grant limited access to their resources on one site, to another site, without having to expose their credentials.

* OAuth is an authorization protocol that contains an authentication step.
* OAuth is a protocol that utilizes tokens in order to access resources on behalf of a resource owner.
* OAuth can be used in conjunction with XACML where XACML is used for authorization and OAuth is used for authentication.

  Login with facebook, google, twitter, github, linkedin, etc. are all based on OAuth protocol.
  --> You can use your google account to login to any other website.
  --> Permissions are asked before giving access to the user's data.
  --> You can revoke the access anytime you want.

* Components of oAuth Protocol.
  There are 4 main components of OAuth protocol.
    1. Resource Owner
    2. Resource Server
    3. Client
    4. Authorization Server

  ##########################################################

  	1. Resource Owner
  		--> Resource Owner is the user who owns the resource and who authorizes the client to access the resources.
  		--> The resource owner can be a person or an application.

  	2. Resource Server
  		--> Resource Server is the server which hosts the protected resources. The resource owner can access the resources using the access token provided by the authorization server.
  		 	The resource server can accept the access token from the client and validate it and provide the resources if the access token is valid.

    3. Client
       --> Client is the application which wants to access the resources on behalf of the resource owner. The client first needs to obtain the permission from the resource owner and then request the authorization server to provide an access token. The client then uses this access token to access the protected resources hosted by the resource server.

    4. Authorization Server
       --> Authorization Server is the server which issues the tokens to the client after successfully authenticating the resource owner and getting authorization.
       The authorization server also provides an interface for the resource server to validate the access token.

  ##########################################################

    * OAuth 2.0 is a protocol that allows a user to grant limited access to their resources on one site, to another site, without having to expose their credentials.
    * OAuth 2.0 focuses on client developer simplicity while providing specific authorization flows for web applications, desktop applications, mobile phones, and living room devices.
    * OAuth 2.0 provides a single value, called an auth token, that represents both the user's identity and the application's authorization to act on the user's behalf.
    * OAuth 2.0 allows users to share specific data with an application while keeping their usernames, passwords, and other information private.
    * OAuth 2.0 is a complete rewrite of OAuth 1.0 from the ground up, sharing only overall goals and general user experience.

  ##########################################################

  JWT (JSON Web Token):
  * JWT is a compact and self-contained way for securely transmitting information between parties as a JSON object.
  * This information can be verified and trusted because it is digitally signed.

* JWT has 3 parts:
* Header
* Payload
* Signature
  ############

* Header:
    * The header typically consists of two parts: the type of the token, which is JWT, and the signing algorithm being used, such as HMAC SHA256 or RSA.
    * For example:
      {
      "alg": "HS256",
      "typ": "JWT"
      }

* Payload:
    * The second part of the token is the payload, which contains the claims.
    * Claims are statements about an entity (typically, the user) and additional data.
    * There are three types of claims: registered, public, and private claims.
    * For example:
      {
      "sub": "1234567890",
      "name": "John Doe",
      "admin": true
      }

* Signature:
    * To create the signature part you have to take the encoded header, the encoded payload, a secret, the algorithm specified in the header, and sign that.
    * For example if you want to use the HMAC SHA256 algorithm, the signature will be created in the following way:
      HMACSHA256(
      base64UrlEncode(header) + "." +
      base64UrlEncode(payload),
      secret)

        * The secret is a string which is known only to the server and client, and it will be used to encode and decode JWT token.

* We have data in the payload that we are trying to encrypt with the cryptographic algorithm mentioned in the header.

$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$$$ class-17: 13Oct Auth-2: Login Flow, Password Encoding, BCrypt, User Service  $$$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
Implementing an Authentication Service:
* Authentication is the process of verifying the identity of a user.
1.	One way is by Implementing all the authentication in a single Application:
    * This is not a good approach because it will be difficult to maintain the code.
    * It will be difficult to scale the application.
    * It will be difficult to add new features.
    * It will be difficult to add new authentication methods.
    * It will be difficult to add new authorization methods.
    * It will be difficult to add new security features.
    * It will be difficult to add new security features.
    * It will be difficult to add new security features.
    * It will be difficult to add new security

2.	However, in big systems the authentication is handled by a separate microservice.
   * The authentication service is responsible for authenticating the user and generating the token.
     * The authentication service is also responsible for validating the token and extracting the user information from the token.

We have already built the product service feature earlier, so now, let’s try to implement	 the user service – login/logout/signup etc.

How Login Flow works (Complete flow):
1.	Client send their details in the sign-up request.
2.	Server stores the details in the db.
3.	Server sends a sign-up is successful message to the client.
      [Steps 1-3 will go the user Service]
4.	Client tries to log in to the server with email/password.
      (From the client, encrypted password should be sent , e.g. Facebook example).
5.	Server validates the client login details with user service db.
6.	If the details are correct, it generates a JWT token and saves it in the sessions table.
7.	Server sends the token back to the client for future authentication
      There will be 2 tables –
      1. Users   which has all the user details.
      2. Sessions  which has all the user login related information.
         Now that the connection and authentication has been established, the client will send the requests for accessing the various resources.    
8.	Client will send the request to access a resource along with the generated JWT token that it had received from the server.
      (The token is typically sent in the HTTP Request Header – Authorization header, however it can be at any other request section as well)
9.	Server receives this token and t hen reaches out to the user service to validate this token.
10.	User service will check the db. and find out if the token is valid.
11.	If the token data is valid and the user has access to the resource, the server will send the requested data back to the client.
12.	Log-out request:
       The log-out request will be received by the user – service, it will again check the data that is sent by the client for the logout request.
13.	It will then delete the token from the session table and return a okay/success message back to the client.

***** Some points to keep in mind: **** 
•	We can store any data in the token, let’s store roles, so that they can be used for authorization purpose when the token is validated by the user service:
•	The tokens are stored locally in the browser which we can see from the application tab. It is not deleted when we close the browser:
•	Then token are only deleted when we clear the cookies and cache from the browser!
•	If we need to have some expiry time for any token, we can set it while creating the token itself:
•	We do not need the secret key / or the signature to open/read a JWT. Anyone can read a JWT without having the secret key. It will only need the secret key to validate the token, which is typically stored somewhere at the server side.
•	We can read the token data by simply putting the details in the JWT site
•	If we manually delete the cookies, we will be logged out, as the token is the only identifier that we have!

****** Storing Passwords in the db. ************
There are 3 ways of storing a password in the databases:
1. Storing the password as it is.
   	If someone gets access to
   	Chances are that the leaked passwords can be same for common websites for every user.
   	HaveIbeenPawned example:
   Safety guidelines for online passwords:
        1. Never ever use the same passwords in 2 different websites as leakage of one the website data will result in leaking the other as well.
        2.	As a developer, never save user passwords as it is in the database to prevent the user password leaking on database hacks.

2. Encrypt the password and store in the database.
   We encrypt the password, and then stored the encrypted password in the database.
   Issues with this approach:
   	A hacker creates an account on the system with his password.
   	His password will be stored on the database as encrypted string.
   	He gets access to the db. and checks his password that is stored as an encrypted password.
   	He searches for the entire db. for same encrypted value, and one finding one, he can know the real password.
   	He can also figure out the encoding technique by comparing real and encrypted passwords for the users.

    * A hacker can create 30 different accounts,  use the common passwords, and see their encrypted value in the database. Then he can search for the same encrypted values and figure out users who have the common passwords.
    * Chances are these users will also have the same password in the popular websites and he can get access to their accounts.

3. Use Salting and then Encrypt the password and store in   the database.
    In this technique, instead of just encrypting the password, we can use an  additional random data and then store this unique value as password.
    Example: 2 people having the same password will have different values in the db. if their current timestamp is considered.
    This will fix the password leakage issue, and no one will be able to figure out the exact password as the salting contains random data.
    However, this leads to a new issue:
     How will a successful login be identified, as the current timestamp during login will be different from the timestamp during password registration.
    Here comes the BCrypt algorithm to the rescue!!

    BCrypt demo:
    Encrypting ‘iloveyou’ using Bcrypt:
    Generated value1: $2a$12$GXrcnjJO/SODxzSAsQAnquZ3rc7thrntOg2Wkpb9zKBk6z1WEYkry
    
    Encrypting ‘iloveyou’ using Bcrypt again:
    Generated value2: $2a$12$2hbpo/8.rs/lgxiH6BNvC.y5HZpqENCQrF6.c4w8C74ICu7Sm7z/G
    
    As we can see both generated values for the same password ‘iloveyou’ is different.
    Given a password string, Bcrypt will always generate a random different value.
    How will be decrypt then?
    Well, to check if the entered string is the actual password, we can chek if the generated hash value for the password string could have actually generated the actual string
    We will get a match if it actually was the password

    There is a very low probability for a false positive value, it’s like a bloom filter concept.
    e.g: there are 60 characters, so there are 65^60 different values – 14881137601979569321560402989572621828907729208758899422788344355044062656285734360211908692166755534583627776
    2 values matching the same string is next to impossible.
    Summary:
    BCrypt algorithm encrypts the password and adds some randomness to generate a unique hashed value.
    No one will be able to generate the real password but given a String the BCrypt algorithm can check if that string could have generated the hashed password value.
    
    Password at the client level itself will be encrypted using BCrypt and then the encrypted password will be sent to the database and saved as the hashed value.
    At the time of login, we send the password. At the database level, the BCrypt algorithm will check if the password string could have generated the hashed password that is stored in the database. It validates the login accordingly as per the match result.

$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$$$ class-18: 16Oct Auth-3:  User Service Implementation  $$$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
Every microservice in production has its own database
•	One service should not talk to the database of another microservice.
•	Each microservice should talk to the other microservices through an API.
•   Each microservice should have its own database and other microservices should not have access to its database.

** Reasons to use Monolithic Architecture **
-> Monolithic applications are simple to develop.
-> Monolithic applications are simple to test.
-> Monolithic applications are simple to deploy.
-> Monolithic applications are simple to scale.
-> Monolithic applications are simple to monitor.
-> Monolithic applications are simple to debug.
-> Monolithic applications are simple to secure.

# Microservices
** Microservices Architecture **
-> Microservices architecture is a style of software architecture that is structured as a collection of loosely coupled services.

** Reasons to use Microservices **
-> Each microservice is a separate application, and each can be written in a different programming language and use different data storage technologies.
-> Microservices are loosely coupled, so if one microservice fails, the others can continue to function.
-> Microservices are organized around business capabilities.
-> Microservices can be deployed independently.
-> Microservices are small, so they can be developed by small teams.
-> Microservices are easier to understand and maintain than monolithic applications.
-> Microservices can be scaled, tested  independently.
-> Each microservice should have its own database and other microservices should not have access to its database.
-> Each microservice should talk to the other microservices through an API.

** Microservices Architecture disadvantages **
-> Microservices are distributed systems, so they are more complex than monolithic applications.
-> Microservices are distributed systems, so they are more difficult to test, debug, monitor and secure than monolithic applications.
-> Microservices are slower than monolithic applications because they use remote procedure calls using HTTP Requests to communicate with each other.

Project Structure:
The user Service API is divided into 3 different controllers:
1.	AuthController
2.	RoleController
3.	UserController

1.	AuthController has 4 APIs :
      login, logout, signup and validate.
      DTOs:
      Login:
      Request  LoginRequestDto: email, password
      Response  UserDTO: email and HashSet of roles.
      User will have a password as well.

    Logout:
    Request  LogoutRequestDto: token, userId
    User id is used to make each session unique as we are not  using JWT at the moment
    Response 
    
    Sign-up:
    Request  SignUpRequestDto: email, password
    
    Response 
    
    Validate:
    Request  ValidateTokenRequestDto: token, userId
    
    Response 


2. RoleController has 1 API:
   createRole:
   Request  createRoleRequestDto: name
   Creates a role for the role name passed.
   Response 

3.	UserController has 2 APIs:
      getUserDetails:
      Request  gets the User Details for the user Id  passed.
      Response  UserDto

setUserRoles:
Request  setUserRolesRequestDto: name
Creates the list of rolesIds for the user passed
Response 
Going into the details for the Controller methods:
1.	AuthController
      Login:
      	Check if the user is present, if not present, throw and error.
      	If present, validate the entered password.

2.	RoleController
3.	UserController

Creating the BCrypt Password Encoder:
Now wherever in the project we need BCrypt Password Encoder, Spring will autowire this and inject the Bean.
@Component is used to mark a class as a bean so that the Spring container can find it and register it as a bean.
@Component is a generic annotation and it can be used to mark any class as a Spring bean.
So we can create a class and mark it as @Component and it will be registered as a bean in the Spring container. And now we can inject this bean into other classes using @Autowired. This is the simplest way to create a bean in Spring.

So now when we are doing sign-up, instead of storing the password in the database, we will use BCryptPasswordEncoder to encode the password and then store the encoded password in the database.

Spring Security puts auth on all requests and we are getting the forbidden error.
Encrypted password:

******* BCryptPasswordEncoder Implementation: *********

Create a folder Security and add Spring Security class in it. 
@Configuration
public class SpringSecurity {
// TODO: implement Spring Security Filter Chain here

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

Now we can use this bean in the AuthService class.

@Service
public class AuthService {
private UserRepository userRepository;
private SessionRepository sessionRepository;
private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

// use the BCrypt Password Encoder to encode the password before saving it in the database:

    public UserDto signUp(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));

        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);
    }
}

// Validate the login password using BCrypt Password Encoder:
public ResponseEntity<UserDto> login(String email, String password) throws NotFoundException {
Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new NotFoundException("User with email "+ email+ " is not found");
        }

        // check if the password is valid using BCrypt Password Encoder
        User user = userOptional.get();
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new NotFoundException("Entered password is invalid");
        }
************ Generate token to be passed to the client on a successful login: ************
// Generate token to be passed to the client on a successful login:
// For now, we will generate a basic random string instead of a JWT token.
// we will use RandomStringUtils from Apache Commons Lang library to generate a random string.

// Add the dependency in the pom.xml file:
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.13.0</version>
        </dependency>

public ResponseEntity<UserDto> login(String email, String password) throws NotFoundException {
Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new NotFoundException("User with email "+ email+ " is not found");
        }
        User user = userOptional.get();
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new NotFoundException("Entered password is invalid");
        }
//        if (!user.getPassword().equals(password)) {
//            throw new NotFoundException("Entered password is invalid");
//        }

// Generate the token 
String token = RandomStringUtils.randomAlphanumeric(30);
Session session = new Session();
session.setStatus(SessionStatus.ACTIVE);
session.setToken(token);
session.setUser(user);
sessionRepository.save(session);

        UserDto userDto = new UserDto();

// Pass the token to the client as cookie in the response header
        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);

        ResponseEntity<UserDto> response = new ResponseEntity<>(userDto, headers, HttpStatus.OK);

        return response;
    }

$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$$$ class-19: 23Oct Auth-4: JWT Implementations  $$$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
JWT Implementation:
JWT is a compact and self-contained way for securely transmitting information between parties as a JSON object.
This information can be verified and trusted because it is digitally signed.
We need to decide on which JWT library to use.
There are many libraries available for JWT, but we will use the jjwt library.
--> The jjwt library is a Java library that can be used to create and verify JWTs.
--> It is a simple library with no external dependencies. It is the most popular Java library for JWT.
     GitHub Url link: https://github.com/jwtk/jjwt#example-jws-hs
********************************************************************************************
The token generation that we have implemented earlier is not secure.
If we look at our implementation, we are generating a random string and sending it to the client:
    _String token = RandomStringUtils.randomAlphanumeric(30);
    Session session = new Session();
    session.setStatus(SessionStatus.ACTIVE);
    session.setToken(token);
    session.setUser(user);
    sessionRepository.save(session);_

The issue with this approach is that the only way for us to validate the token is to check if the token is present in the database.
If the token is present in the database, we will consider it as a valid token.
No client can self-validate ths token as validation will require db access and server's db access is obviously not available to the client!

So, the way to solve this issue is to use JWT token; where the client can self-validate the token without the server's help. 
As of now, if we are using a random token, we will need to have minimum of 2 db calls to validate the token:
1.	To check if the token is present in the db and check if the token is valid.
2. To get the user details from the db - including the authorization for various resources.

With JWT, we can do both the above steps in a single step. We know that inside the JWT token, we can store any data that we want. So, we can store the user details inside the JWT token itself. So, when the client sends the token to the server, the server can validate the token and extract the user details from the token itself. So, we will not need to make a db call to get the user details.

**** Implementing the JWT using jjwt library: ****
-We will use the jjwt library to generate and validate the JWT token.

GitHub Url link: https://github.com/jwtk/jjwt/blob/master/README.md

-Add the dependency in the pom.xml file mentioned in the readme file of the jjwt library at the GitHub link mentioned above:
<dependency>
<groupId>io.jsonwebtoken</groupId>
<artifactId>jjwt-api</artifactId>
<version>0.12.3</version>
</dependency>
<dependency>
<groupId>io.jsonwebtoken</groupId>
<artifactId>jjwt-impl</artifactId>
<version>0.12.3</version>
<scope>runtime</scope>
</dependency>
<dependency>
<groupId>io.jsonwebtoken</groupId>
<artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson if Gson is preferred -->
<version>0.12.3</version>
<scope>runtime</scope>
</dependency>
<!-- Uncomment this next dependency if you are using:
     - JDK 10 or earlier, and you want to use RSASSA-PSS (PS256, PS384, PS512) signature algorithms.  
     - JDK 10 or earlier, and you want to use EdECDH (X25519 or X448) Elliptic Curve Diffie-Hellman encryption.
     - JDK 14 or earlier, and you want to use EdDSA (Ed25519 or Ed448) Elliptic Curve signature algorithms.    
     It is unnecessary for these algorithms on JDK 15 or later.
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk18on</artifactId> or bcprov-jdk15to18 on JDK 7
    <version>1.76</version>
    <scope>runtime</scope>
</dependency>
-->

Post adding the dependency, we will create a class called JwtUtil.java in the Security folder.
-- We will use the same algorithm that Scaler uses - HS256 (HMAC using SHA-256). 

// Create a test key suitable for the desired HMAC-SHA algorithm:
MacAlgorithm alg = Jwts.SIG.HS512; //or HS384 or HS256
SecretKey key = alg.key().build();

String message = "Hello World!";
byte[] content = message.getBytes(StandardCharsets.UTF_8);

// Create the compact JWS:
String jws = Jwts.builder().content(content, "text/plain").signWith(key, alg).compact();

// Parse the compact JWS:
content = Jwts.parser().verifyWith(key).build().parseSignedContent(jws).getPayload();

assert message.equals(new String(content, StandardCharsets.UTF_8));

// Once the JWS is parsed, we can inspect its header and signature:
Jws<Claims> jws = Jwts.parser().verifyWith(key).parseClaimsJws(jws);
assert jws.getHeader().getAlgorithm().equals(alg.name());
assert jws.getSignature().equals(jws.getSignature());

// We can also parse the JWS into a JWT and inspect its body:
Jwt<Header, Claims> jwt = Jwts.parser().verifyWith(key).parse(jws.getCompactSerialization());
assert jwt.getBody().getSubject().equals("Joe");

// We can also use the builder to construct a JWS from scratch:
jws = Jwts.builder().header("alg", "HS256").claim("name", "Joe").signWith(key).build();

// Once a user logs out, we can invalidate the token by deleting it from the database.
// We can also set an expiry time for the token while creating it.
 This is done by setting the expiration time in the claims while creating the token.

// We can also set the claims while creating the token.
// We can set below fields, for example in the claims while creating the token:
// user details,  expiry time , roles,  permissions,  scopes, audience, issuer, subject, id, other custom claims.

Implementation:
We will create a class called JwtUtil.java in the Security folder.
We will create a method called generateToken() which will take the user details and generate the token.
** Generating the token:
public String generateToken(User user) {
// We will use the same algorithm that Scaler uses - HS256 (HMAC using SHA-256).
// Create a test key suitable for the desired HMAC-SHA algorithm:
MacAlgorithm alg = Jwts.SIG.HS256; //or HS384 or HS512.
SecretKey key = alg.key().build();
}

** Complete code:*********************************************
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtExample {
    public static void main(String[] args) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", "John Doe");
        claims.put("email", "john.doe@example.com");
        claims.put("createdAt", new Date()); // Replace this with the actual creation date

        // Set expiration date to one week from now
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 7);
        claims.put("expirationDate", calendar.getTime());

        String secretKey = "yourSecretKey"; // Replace with your actual secret key

        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration((Date) claims.get("expirationDate"))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        System.out.println("Generated JWT Token: " + jwtToken);
    }
}
******************************

************ Validating the JWT Token: ************
We will create a method called validateToken() which will take the token and validate it.
1. Extract the Token from the Request
2. Retrieve the JWT token from the incoming request header or wherever you have it stored.
3. Parse and Verify the Token:
4. Use the jjwt library to parse and verify the token. Ensure that the token has not expired and that it was signed with the correct secret key.
5. Extract the User Details from the Token:
6. Handle Verification Failures:
7. Implement error handling in case the token validation fails. This could include returning an error response to the client or taking appropriate action based on your application's requirements.

Here's an example of how you can implement the validate method in your Auth Controller:

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
// Replace with your actual secret key
    private final String secretKey = "yourSecretKey"; 

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestBody String jwtToken) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Parse and verify the token
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken)
                    .getBody();

            // Check expiration date
            Date expirationDate = claims.getExpiration();
            Date now = new Date();
            if (expirationDate.before(now)) {
                response.put("message", "Token has expired");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // Token is valid, you can access claims like claims.get("name"), claims.get("email"), etc.
            response.put("message", "Token is valid");
            return ResponseEntity.ok(response);

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            // Handle token validation errors
            response.put("message", "Invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
*************************************************************************
We actually make an implementation to generate the JWT token from the Security key that we have defined in the application.properties file.
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

Next we implement the token validation in the validate method:
//        *******  JWT token validation ********/

import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    // Your secret key
    private final String secretKey = "yourSecretKey"; // Replace with your actual secret key

    public ResponseEntity<Map<String, Object>> validateToken(String jwtToken) {
        Map<String, Object> response = new HashMap<>();

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken)
                    .getBody();

            Date expirationDate = claims.get("expiryAt", Date.class);
            Date now = new Date();
            if (expirationDate.before(now)) {
                throw new JwtValidationException("Token has expired");
            }

            response.put("message", "Token is valid");
            return ResponseEntity.ok(response);

        } catch (JwtValidationException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            response.put("message", "Invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
import io.jsonwebtoken.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    // Your secret key
    private final String secretKey = "yourSecretKey"; // Replace with your actual secret key

    public ResponseEntity<Map<String, Object>> validateToken(String jwtToken) {
        Map<String, Object> response = new HashMap<>();

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken)
                    .getBody();

            Date expirationDate = claims.get("expiryAt", Date.class);
            Date now = new Date();
            if (expirationDate.before(now)) {
                throw new JwtValidationException("Token has expired");
            }

            response.put("message", "Token is valid");
            return ResponseEntity.ok(response);

        } catch (JwtValidationException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            response.put("message", "Invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$$$ class-20: 25Oct Auth-5: JWT Decoding, Cookies, CSRF  $$$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$$$ class-21: 27Oct Auth-6: Spring Security_OAuth2 Authorization Server  $$$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
$$$ class-22: 30Oct Auth-7: Finishing our Authentication Service  $$$
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

