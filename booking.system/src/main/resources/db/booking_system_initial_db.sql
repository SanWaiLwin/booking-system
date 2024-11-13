CREATE DATABASE `booking_system`;
use `booking_system`;

DROP TABLE IF EXISTS `country`;
CREATE TABLE `country` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255), 
    created_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO booking_system.country (name,created_date,updated_date) VALUES
	 ('Myanmar','2024-11-13 03:08:43','2024-11-13 03:08:43'),
	 ('Singapore','2024-11-13 03:08:43','2024-11-13 03:08:43'),
	 ('Thailand','2024-11-13 03:08:43','2024-11-13 03:08:43'),
	 ('US','2024-11-13 03:08:43','2024-11-13 03:08:43');
 
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    phno VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    country VARCHAR(255),
    is_verified BOOLEAN DEFAULT FALSE,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;  

DROP TABLE IF EXISTS `packages`;
CREATE TABLE `packages` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    credits INT NOT NULL,
    price DOUBLE NOT NULL,  
    country_id BIGINT, 
    created_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_country FOREIGN KEY (country_id) REFERENCES country(id) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;  

INSERT INTO `packages` (name, credits, price, country_id, created_date, updated_date) 
VALUES
    ('Basic Package', 100, 19.99, 1, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Premium Package', 200, 39.99, 2, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Family Package', 300, 59.99, 3, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Pro Package', 500, 99.99, 4, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Basic Package', 100, 19.99, 1, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Family Package', 300, 59.99, 2, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Pro Package', 500, 99.99, 3, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Premium Package', 200, 39.99, 4, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Basic Package', 100, 19.99, 1, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Pro Package', 500, 99.99, 2, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Premium Package', 200, 39.99, 3, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Family Package', 300, 59.99, 4, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Basic Package', 100, 19.99, 1, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Pro Package', 500, 99.99, 2, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Premium Package', 200, 39.99, 3, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Family Package', 300, 59.99, 4, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Pro Package', 500, 99.99, 1, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Basic Package', 100, 19.99, 2, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Pro Package', 500, 99.99, 3, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Premium Package', 200, 39.99, 4, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Family Package', 300, 59.99, 1, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Basic Package', 100, 19.99, 2, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Pro Package', 500, 99.99, 3, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Basic Package', 100, 19.99, 4, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Premium Package', 200, 39.99, 1, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Family Package', 300, 59.99, 2, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Pro Package', 500, 99.99, 3, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Basic Package', 100, 19.99, 4, '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
    ('Pro Package', 500, 99.99, 1, '2024-11-13 14:30:54', '2024-11-13 14:30:54'); 

DROP TABLE IF EXISTS `purchased_package`;
CREATE TABLE `purchased_package` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    remaining_credits INT NOT NULL,   
    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    user_id BIGINT, 
    packages_id BIGINT, 
    created_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user1 FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE cascade,
    CONSTRAINT fk_packages FOREIGN KEY (packages_id) REFERENCES packages(id) ON DELETE cascade
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;  

DROP TABLE IF EXISTS `booking_class`;
CREATE TABLE `booking_class` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    country_id BIGINT,
    required_credits INT NOT NULL,
    available_slots INT NOT NULL, 
    start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiry_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_bc_country FOREIGN KEY (country_id) REFERENCES country(id) ON DELETE CASCADE
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci; 

INSERT INTO `booking_class` (name, country_id, required_credits, available_slots, start_date, expiry_date, created_date, updated_date) VALUES 
('Yoga Class', 1, 100, 30, '2024-11-10 12:30:54', '2024-11-10 14:30:54', '2024-11-05 14:30:54', '2024-11-05 14:30:54'),
('Pilates Basics', 2, 50, 45, '2024-11-13 10:30:54', '2024-11-13 12:30:54', '2024-11-09 14:30:54', '2024-11-09 14:30:54'),
('Advanced Spin', 3, 200, 15, '2024-11-10 08:30:54', '2024-11-10 10:30:54', '2024-11-06 14:30:54', '2024-11-06 14:30:54'),
('Boxing Fundamentals', 4, 75, 25, '2024-11-14 16:30:54', '2024-11-14 18:30:54', '2024-11-11 14:30:54', '2024-11-11 14:30:54'),
('CrossFit Intro', 1, 150, 10, '2024-11-11 13:30:54', '2024-11-11 15:30:54', '2024-11-07 14:30:54', '2024-11-07 14:30:54'),
('Strength Training', 2, 120, 35, '2024-11-06 11:30:54', '2024-11-06 13:30:54', '2024-11-03 14:30:54', '2024-11-03 14:30:54'),
('HIIT Cardio', 3, 80, 40, '2024-11-12 18:30:54', '2024-11-12 20:30:54', '2024-11-08 14:30:54', '2024-11-08 14:30:54'),
('Zumba Fun', 4, 60, 50, '2024-11-07 17:30:54', '2024-11-07 19:30:54', '2024-11-02 14:30:54', '2024-11-02 14:30:54'),
('Ballet Fit', 1, 90, 20, '2024-11-06 15:30:54', '2024-11-06 17:30:54', '2024-11-01 14:30:54', '2024-11-01 14:30:54'),
('Aerobics Blast', 2, 30, 60, '2024-11-15 07:30:54', '2024-11-15 09:30:54', '2024-11-12 14:30:54', '2024-11-12 14:30:54'),
('Bootcamp Circuit', 3, 180, 12, '2024-11-08 10:30:54', '2024-11-08 12:30:54', '2024-11-04 14:30:54', '2024-11-04 14:30:54'),
('Meditation Hour', 4, 10, 90, '2024-11-13 08:30:54', '2024-11-13 10:30:54', '2024-11-10 14:30:54', '2024-11-10 14:30:54'),
('Power Yoga', 1, 95, 28, '2024-11-10 11:30:54', '2024-11-10 13:30:54', '2024-11-06 14:30:54', '2024-11-06 14:30:54'),
('Swimming Basics', 2, 40, 75, '2024-11-17 09:30:54', '2024-11-17 11:30:54', '2024-11-13 14:30:54', '2024-11-13 14:30:54'),
('BodyPump', 3, 150, 13, '2024-11-18 17:30:54', '2024-11-18 19:30:54', '2024-11-14 14:30:54', '2024-11-14 14:30:54'),
('Jazzercise', 4, 70, 45, '2024-11-08 14:30:54', '2024-11-08 16:30:54', '2024-11-04 14:30:54', '2024-11-04 14:30:54'),
('Dance Cardio', 1, 55, 52, '2024-11-09 13:30:54', '2024-11-09 15:30:54', '2024-11-05 14:30:54', '2024-11-05 14:30:54'),
('Rowing Workout', 2, 130, 21, '2024-11-06 10:30:54', '2024-11-06 12:30:54', '2024-11-02 14:30:54', '2024-11-02 14:30:54'),
('Climbing Basics', 3, 110, 22, '2024-11-11 15:30:54', '2024-11-11 17:30:54', '2024-11-07 14:30:54', '2024-11-07 14:30:54'),
('Pilates Advanced', 4, 160, 7, '2024-11-14 19:30:54', '2024-11-14 21:30:54', '2024-11-10 14:30:54', '2024-11-10 14:30:54'),
('Core Blast', 1, 45, 54, '2024-11-05 08:30:54', '2024-11-05 10:30:54', '2024-11-01 14:30:54', '2024-11-01 14:30:54'),
('Mindfulness', 2, 25, 80, '2024-11-16 06:30:54', '2024-11-16 08:30:54', '2024-11-12 14:30:54', '2024-11-12 14:30:54'),
('Running Club', 3, 60, 65, '2024-11-12 11:30:54', '2024-11-12 13:30:54', '2024-11-08 14:30:54', '2024-11-08 14:30:54'),
('Stretch and Flex', 4, 15, 100, '2024-11-18 14:30:54', '2024-11-18 16:30:54', '2024-11-14 14:30:54', '2024-11-14 14:30:54'),
('Kickboxing', 1, 125, 18, '2024-11-10 10:30:54', '2024-11-10 12:30:54', '2024-11-06 14:30:54', '2024-11-06 14:30:54'),
('Cycling Class', 2, 85, 39, '2024-11-06 13:30:54', '2024-11-06 15:30:54', '2024-11-02 14:30:54', '2024-11-02 14:30:54'),
('Strength & Balance', 3, 140, 24, '2024-11-05 16:30:54', '2024-11-05 18:30:54', '2024-11-01 14:30:54', '2024-11-01 14:30:54'),
('Step Aerobics', 4, 35, 58, '2024-11-09 11:30:54', '2024-11-09 13:30:54', '2024-11-05 14:30:54', '2024-11-05 14:30:54'),
('TRX Suspension', 1, 115, 20, '2024-11-13 14:30:54', '2024-11-13 16:30:54', '2024-11-09 14:30:54', '2024-11-09 14:30:54'),
('Tabata Circuit', 2, 170, 11, '2024-11-14 09:30:54', '2024-11-14 11:30:54', '2024-11-10 14:30:54', '2024-11-10 14:30:54');

DROP TABLE IF EXISTS `booking`;
CREATE TABLE `booking` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT, 
    user_id BIGINT,
    booking_class_id BIGINT, 
    perchased_package_id BIGINT,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    expiry_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status INT NOT NULL,
    created_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_b_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE cascade,
    CONSTRAINT fk_b_class FOREIGN KEY (booking_class_id) REFERENCES booking_class(id) ON DELETE cascade
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci; 

DROP TABLE IF EXISTS `waiting_list`;
CREATE TABLE `waiting_list` (
    id BIGINT PRIMARY KEY AUTO_INCREMENT, 
    user_id BIGINT,
    booking_class_id BIGINT, 
    waiting_list_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    created_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_wl_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE cascade,
    CONSTRAINT fk_wl_class FOREIGN KEY (booking_class_id) REFERENCES booking_class(id) ON DELETE cascade
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;