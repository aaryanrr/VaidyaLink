CREATE TABLE users
(
    id                  BINARY(16) PRIMARY KEY,
    aadhaar_number_hash VARCHAR(255)  NOT NULL UNIQUE,
    password            VARCHAR(255)  NOT NULL,
    name                VARCHAR(1024) NOT NULL,
    email               VARCHAR(1024) NOT NULL UNIQUE,
    phone_number        VARCHAR(1024) NOT NULL,
    date_of_birth       DATE          NOT NULL,
    address             VARCHAR(1024) NOT NULL,
    blood_group         VARCHAR(1024) NOT NULL,
    emergency_contact   VARCHAR(1024) NOT NULL,
    allergies           VARCHAR(1024) NOT NULL,
    height_cm           DECIMAL(5, 2) NOT NULL,
    weight_kg           DECIMAL(5, 2) NOT NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE institutions
(
    registration_number VARCHAR(20) PRIMARY KEY,
    institution_name    VARCHAR(255) NOT NULL,
    email               VARCHAR(255) NOT NULL UNIQUE,
    password            VARCHAR(255) NOT NULL,
    license_file_path   VARCHAR(255) NOT NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE tokens
(
    id                  BINARY(16) PRIMARY KEY,
    user_id             BINARY(16),
    institution_reg_num VARCHAR(20),
    token               VARCHAR(512) NOT NULL UNIQUE,
    expiry_date         TIMESTAMP    NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_institution FOREIGN KEY (institution_reg_num) REFERENCES institutions (registration_number) ON DELETE CASCADE
);

CREATE TABLE access_requests
(
    id               BINARY(16) PRIMARY KEY,
    institution_name VARCHAR(255) NOT NULL,
    aadhaar_number   VARCHAR(255) NOT NULL,
    data_category    VARCHAR(255) NOT NULL,
    time_period      DATE         NOT NULL,
    action_required  VARCHAR(255) NOT NULL,
    approved         BOOLEAN   DEFAULT FALSE,
    requested_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE INDEX idx_users_aadhaar_hash ON users (aadhaar_number_hash);
CREATE INDEX idx_institutions_email ON institutions (email);
CREATE INDEX idx_users_email ON users (email);
