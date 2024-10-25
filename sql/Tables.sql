CREATE TABLE users
(
    id                  BINARY(16) PRIMARY KEY,
    aadhaar_number_hash VARCHAR(255) NOT NULL UNIQUE,
    password            VARCHAR(255) NOT NULL,
    name                VARCHAR(100) NOT NULL,
    email               VARCHAR(255) NOT NULL UNIQUE,
    phone_number        VARCHAR(15),
    date_of_birth       DATE,
    address             VARCHAR(255),
    blood_group         VARCHAR(3),
    emergency_contact   VARCHAR(15),
    allergies           VARCHAR(255),
    height_cm           DECIMAL(5, 2),
    weight_kg           DECIMAL(5, 2),
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE institutions
(
    registration_number VARCHAR(20) PRIMARY KEY,
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

CREATE INDEX idx_users_aadhaar_hash ON users (aadhaar_number_hash);
CREATE INDEX idx_institutions_email ON institutions (email);
