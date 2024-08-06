CREATE TABLE authorization
(
    id                            VARCHAR(255) NOT NULL,
    registered_client_id          VARCHAR(255) NULL,
    principal_name                VARCHAR(255) NULL,
    authorization_grant_type      VARCHAR(255) NULL,
    authorized_scopes             TEXT NULL,
    attributes                    TEXT NULL,
    state                         VARCHAR(500) NULL,
    authorization_code_value      TEXT NULL,
    authorization_code_issued_at  datetime NULL,
    authorization_code_expires_at datetime NULL,
    authorization_code_metadata   VARCHAR(255) NULL,
    access_token_value            TEXT NULL,
    access_token_issued_at        datetime NULL,
    access_token_expires_at       datetime NULL,
    access_token_metadata         TEXT NULL,
    access_token_type             VARCHAR(255) NULL,
    access_token_scopes           TEXT NULL,
    refresh_token_value           TEXT NULL,
    refresh_token_issued_at       datetime NULL,
    refresh_token_expires_at      datetime NULL,
    refresh_token_metadata        TEXT NULL,
    oidc_id_token_value           TEXT NULL,
    oidc_id_token_issued_at       datetime NULL,
    oidc_id_token_expires_at      datetime NULL,
    oidc_id_token_metadata        TEXT NULL,
    oidc_id_token_claims          TEXT NULL,
    user_code_value               TEXT NULL,
    user_code_issued_at           datetime NULL,
    user_code_expires_at          datetime NULL,
    user_code_metadata            TEXT NULL,
    device_code_value             TEXT NULL,
    device_code_issued_at         datetime NULL,
    device_code_expires_at        datetime NULL,
    device_code_metadata          TEXT NULL,
    CONSTRAINT pk_authorization PRIMARY KEY (id)
);

CREATE TABLE authorization_consent
(
    registered_client_id VARCHAR(255) NOT NULL,
    principal_name       VARCHAR(255) NOT NULL,
    authorities          VARCHAR(1000) NULL,
    CONSTRAINT pk_authorizationconsent PRIMARY KEY (registered_client_id, principal_name)
);

CREATE TABLE client
(
    id                            VARCHAR(255) NOT NULL,
    client_id                     VARCHAR(255) NULL,
    client_id_issued_at           datetime NULL,
    client_secret                 VARCHAR(255) NULL,
    client_secret_expires_at      datetime NULL,
    client_name                   VARCHAR(255) NULL,
    client_authentication_methods VARCHAR(1000) NULL,
    authorization_grant_types     VARCHAR(1000) NULL,
    redirect_uris                 VARCHAR(1000) NULL,
    post_logout_redirect_uris     VARCHAR(1000) NULL,
    scopes                        VARCHAR(1000) NULL,
    client_settings               VARCHAR(2000) NULL,
    token_settings                VARCHAR(2000) NULL,
    CONSTRAINT pk_client PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    `role` VARCHAR(255)          NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE sessions
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    token             VARCHAR(255)          NULL,
    created_at        datetime              NULL,
    expiring_at       datetime              NULL,
    last_logged_in_at datetime              NULL,
    logged_out_at     datetime              NULL,
    user_id           BIGINT                NULL,
    status            VARCHAR(255)          NULL,
    CONSTRAINT pk_sessions PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    email    VARCHAR(255)          NULL,
    password VARCHAR(255)          NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_roles
(
    roles_id BIGINT NOT NULL,
    users_id BIGINT NOT NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (roles_id, users_id)
);

ALTER TABLE sessions
    ADD CONSTRAINT FK_SESSIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (roles_id) REFERENCES roles (id);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (users_id) REFERENCES users (id);