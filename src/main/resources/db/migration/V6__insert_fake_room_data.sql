USE hotel_schema;

INSERT INTO hotel_profiles (
    hotel_name,
    address,
    city,
    description,
    star_rating,
    hotline,
    checkin_time,
    checkout_time
)
VALUES
    (
        'Sunrise Hotel',
        '123 Vo Nguyen Giap',
        'Da Nang',
        'Luxury hotel near the beach',
        5,
        '0905123456',
        '14:00:00',
        '12:00:00'
    );


INSERT INTO room_types (
    name,
    max_adult,
    max_child,
    bed_type,
    room_size,
    base_price,
    description
)
VALUES
    ('Standard Room', 2, 1, 'Queen Bed', 25.00, 500000, 'Standard room'),
    ('Deluxe Room', 2, 2, 'King Bed', 35.00, 800000, 'Deluxe room'),
    ('Family Suite', 4, 2, '2 Queen Beds', 60.00, 1500000, 'Family suite');

INSERT INTO rooms (
    hotel_id,
    room_type_id,
    room_number,
    floor_no,
    room_status
)
SELECT
    h.hotel_id,
    rt.room_type_id,
    room_data.room_number,
    room_data.floor_no,
    1
FROM hotel_profiles h
         JOIN (
    SELECT 'Standard Room' AS type_name, '101' AS room_number, 1 AS floor_no
    UNION ALL SELECT 'Standard Room', '102', 1
    UNION ALL SELECT 'Deluxe Room', '201', 2
    UNION ALL SELECT 'Deluxe Room', '202', 2
    UNION ALL SELECT 'Family Suite', '301', 3
    UNION ALL SELECT 'Family Suite', '302', 3
) room_data
         JOIN room_types rt ON rt.name = room_data.type_name
    LIMIT 6;


INSERT INTO room_availability (
    room_id,
    available_date,
    availability_status,
    price
)
WITH RECURSIVE date_series AS (
    SELECT DATE('2026-05-01') AS dt
UNION ALL
SELECT DATE_ADD(dt, INTERVAL 1 DAY)
FROM date_series
WHERE dt < '2026-07-31'
    )
SELECT
    r.room_id,
    ds.dt,
    1,
    CASE
        WHEN DAYOFWEEK(ds.dt) IN (1, 7) THEN
            CASE rt.name
                WHEN 'Standard Room' THEN 600000
                WHEN 'Deluxe Room' THEN 950000
                WHEN 'Family Suite' THEN 1800000
                END
        ELSE
            rt.base_price
        END
FROM rooms r
         JOIN room_types rt ON r.room_type_id = rt.room_type_id
         CROSS JOIN date_series ds;