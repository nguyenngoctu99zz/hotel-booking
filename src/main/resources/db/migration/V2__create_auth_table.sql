-- File: V2__create_auth_and_refresh_token.sql
USE auth_schema;

CREATE TABLE auth (
                      auth_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(100) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL,
                      access_token_version  INT,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      account_status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE refresh_tokens (
                                refresh_token_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                token_value TEXT NOT NULL,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                expired_at TIMESTAMP NOT NULL,
                                auth_id BIGINT NOT NULL,
                                CONSTRAINT fk_refresh_token_auth
                                    FOREIGN KEY (auth_id) REFERENCES auth(auth_id)
                                        ON DELETE CASCADE
);