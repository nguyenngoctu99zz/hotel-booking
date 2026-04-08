-- File: V4__create_hotel_inventory.sql
USE hotel_schema;

CREATE TABLE hotel_profiles (
                                hotel_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                hotel_name VARCHAR(255) NOT NULL,
                                address VARCHAR(500) NOT NULL,
                                city VARCHAR(100) NOT NULL,
                                description TEXT,
                                star_rating INT,
                                hotline VARCHAR(20),
                                checkin_time TIME,
                                checkout_time TIME,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE room_types (
                            room_type_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            max_adult INT NOT NULL,
                            max_child INT NOT NULL,
                            bed_type VARCHAR(100),
                            room_size DECIMAL(10,2),
                            base_price DECIMAL(15,2) NOT NULL,
                            description TEXT
);

CREATE TABLE rooms (
                       room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       hotel_id BIGINT NOT NULL,
                       room_type_id BIGINT NOT NULL,
                       room_number VARCHAR(20) NOT NULL,
                       floor_no INT,
                       room_status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
                       CONSTRAINT fk_room_hotel
                           FOREIGN KEY (hotel_id) REFERENCES hotel_profiles(hotel_id)
                               ON DELETE CASCADE,
                       CONSTRAINT fk_room_type
                           FOREIGN KEY (room_type_id) REFERENCES room_types(room_type_id)
);

CREATE TABLE room_availability (
                                   room_available_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   room_id BIGINT NOT NULL,
                                   available_date DATE NOT NULL,
                                   availability_status VARCHAR(30) NOT NULL,
                                   price DECIMAL(15,2) NOT NULL,
                                   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   CONSTRAINT fk_room_availability_room
                                       FOREIGN KEY (room_id) REFERENCES rooms(room_id)
                                           ON DELETE CASCADE
);
