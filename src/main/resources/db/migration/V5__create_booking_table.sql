-- File: V5__create_bookings_and_histories.sql
USE booking_schema;

CREATE TABLE bookings (
                          booking_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          booking_code VARCHAR(50) NOT NULL,
                          room_id BIGINT NOT NULL,
                          user_profile_id BIGINT NOT NULL,
                          checkin_date DATE NOT NULL,
                          checkout_date DATE NOT NULL,
                          total_nights INT NOT NULL,
                          total_amount DECIMAL(15,2) NOT NULL,
                          booking_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP

);

CREATE TABLE booking_histories (
                                   booking_history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   booking_id BIGINT NOT NULL,
                                   from_status VARCHAR(30),
                                   to_status VARCHAR(30) NOT NULL,
                                   changed_by VARCHAR(30) NOT NULL,
                                   note TEXT,
                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   changed_by_user_id BIGINT,
                                   CONSTRAINT fk_booking_history_booking
                                       FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
                                           ON DELETE CASCADE
);