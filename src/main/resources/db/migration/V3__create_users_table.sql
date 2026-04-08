-- File: V3__create_user_profiles.sql
USE users_schema;

CREATE TABLE user_profiles (
                               user_profile_id BIGINT PRIMARY KEY,
                               auth_id BIGINT,
                               full_name VARCHAR(255) NOT NULL,
                               date_of_birth DATE,
                               gender VARCHAR(20),
                               nationality VARCHAR(100),
                               phone_number VARCHAR(20),
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP

);
