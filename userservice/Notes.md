
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



