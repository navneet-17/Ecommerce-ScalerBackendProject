# User Service

The **User Service** is a microservice responsible for handling user authentication, registration, login, and authorization. It provides JWT-based token authentication and leverages **Spring Security**, **Bcrypt** for password encryption, and **OAuth2** for third-party logins (Google, Facebook, etc.).

## Key Features

1. **User Registration (Sign-Up)**:
   - Registers new users by securely storing their credentials using **Bcrypt** encryption with salting.
   - User data includes a hashed password, email, and roles (for authorization purposes).
2. **User Login**:

   - Allows users to log in using their email and password.
   - Validates the user's credentials against the stored hashed password in the database.
   - Upon successful login, a **JWT (JSON Web Token)** is generated and returned to the client.
   - **JWT** is used for subsequent requests to authenticate users.

3. **Token-Based Authentication**:
   - After login, all requests are validated through the **JWT token**, ensuring stateless authentication.
   - The JWT contains user-related data like roles, which is used for authorization.
   - Tokens are stored locally on the client (e.g., browser cookies) and validated on the server using **HS256** algorithm for signature validation.
4. **Authorization with OAuth 2.0**:

   - The service supports third-party logins using **OAuth2**, enabling users to log in via Google, Facebook, etc.
   - OAuth 2.0 protocol is implemented for authorization, allowing the system to access user information from third-party services while keeping passwords secure.

5. **Password Security**:
   - Passwords are **salted** and **hashed** using the **Bcrypt** algorithm, ensuring each password is uniquely encrypted, even if two users have the same password.
   - Password validation is done using **Bcrypt's** match functionality.
6. **Session Management**:

   - Manages user sessions by storing JWT tokens in a **sessions** table for tracking user login activity.
   - Supports tracking `last_logged_in_at` and `logged_out_at` for more granular session control.

7. **Logout**:

   - Users can log out by invalidating their JWT token, which is removed from the sessions table.

8. **Role-Based Access Control (RBAC)**:
   - Roles are assigned to users for handling permissions, and role-based access is enforced across the system.
   - The token contains user roles to ensure that the correct level of access is granted when a resource is requested.

## API Endpoints

### AuthController

- **POST /signup**: Registers a new user.
- **POST /login**: Authenticates user and returns a JWT token.
- **POST /logout**: Invalidates the current session by deleting the JWT token.
- **POST /validateToken**: Validates if a provided JWT token is still active and valid.

### RoleController

- **POST /role**: Assigns a role to a user.

### UserController

- **GET /user/{id}**: Fetches the user details by user ID.
- **POST /setUserRoles**: Assigns roles to a user.

## Authentication Flow

1. **Sign-Up**:

   - Client sends user details (email, password) to the server.
   - Password is **hashed** using **Bcrypt** and stored in the database.
   - Upon success, a "sign-up successful" message is returned to the client.

2. **Login**:

   - Client sends email and password to the login endpoint.
   - The server verifies the password using **Bcrypt** and generates a **JWT** upon successful login.
   - The **JWT** is returned to the client and stored in their browser's cookies or local storage.

3. **Accessing Resources**:
   - Client sends requests with the **JWT** included in the **Authorization** header.
   - The server validates the token and grants access if the user is authorized.
4. **Logout**:
   - Client sends a logout request.
   - The server invalidates the **JWT** by removing it from the sessions table.

## Security

- **JWT Token**:

  - Securely transmits user data between the client and server.
  - Includes claims such as the user's ID, roles, and token expiration.
  - The token's integrity is validated using **HS256**.

- **OAuth 2.0**:

  - Allows users to log in via third-party providers like Google and Facebook without sharing passwords.
  - Authorization is handled via **OAuth** flows, keeping user data secure.

- **Bcrypt**:

  - Ensures that passwords are hashed and salted to prevent attacks like rainbow table or dictionary attacks.

- **CSRF Protection**:
  - Ensures that cookies are sent only for requests originating from the same domain, protecting against cross-site request forgery attacks.

## Technologies Used

- **Spring Security**: For handling authentication and authorization mechanisms.
- **Bcrypt**: For securely hashing passwords.
- **JWT (JSON Web Tokens)**: For token-based authentication.
- **OAuth 2.0**: For third-party login support.
- **MySQL**: For storing user data and session tokens.
- **Redis**: For caching authentication data (if implemented).

## Improvements

- **Caching with Redis**: Implement caching for token validation to reduce the load on the authentication service by storing tokens in **Redis**.
- **Token Expiry and Refresh**: Implement token expiration handling and refresh tokens to ensure secure access over longer sessions.
- **OAuth Enhancements**: Expand the OAuth2 implementation to allow logins from other providers and streamline the flow for external authorization.

## References

- [JJWT GitHub](https://github.com/jwtk/jjwt) - JSON Web Token library for token creation and verification.
- [Spring Authorization Server](https://docs.spring.io/spring-authorization-server/docs/current/reference/html5/) - Documentation for building authorization servers with Spring.
