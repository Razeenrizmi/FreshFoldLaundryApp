-- FreshFold Laundry App Database Schema
-- This file contains all the table definitions needed for the application

USE freshfold_db;

-- Drop tables if they exist (in reverse dependency order)
DROP TABLE IF EXISTS SpecialRequest;
DROP TABLE IF EXISTS OrderItem;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Customer;
DROP TABLE IF EXISTS Clothes;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS drivers;

-- Create User table for authentication
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'CUSTOMER',
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(20),
    address TEXT,
    token VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Category table
CREATE TABLE Category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    image_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Clothes table
CREATE TABLE Clothes (
    cloth_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    category_id INT NOT NULL,
    image_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES Category(category_id) ON DELETE CASCADE
);

-- Create Customer table
CREATE TABLE Customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    phone_no VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Orders table
CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    customer_name VARCHAR(100),
    customer_phone VARCHAR(20),
    customer_address TEXT,
    special_instructions TEXT,
    pickup_date DATE,
    pickup_time TIME,
    delivery_date DATE,
    delivery_time TIME,
    pickup_driver_id BIGINT,
    delivery_driver_id BIGINT,
    service_type VARCHAR(50) NOT NULL,
    pickup_datetime DATETIME NOT NULL,
    delivery_datetime DATETIME NOT NULL,
    order_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (pickup_driver_id) REFERENCES drivers(id) ON DELETE SET NULL,
    FOREIGN KEY (delivery_driver_id) REFERENCES drivers(id) ON DELETE SET NULL
);

-- Create OrderItem table
CREATE TABLE OrderItem (
    order_id INT NOT NULL,
    cloth_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    PRIMARY KEY (order_id, cloth_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (cloth_id) REFERENCES Clothes(cloth_id) ON DELETE CASCADE
);

-- Create SpecialRequest table
CREATE TABLE SpecialRequest (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE
);

-- Create Drivers table
CREATE TABLE drivers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert sample categories
INSERT INTO Category (name, image_path) VALUES
('Shirts', '/images/Shirt.jpg'),
('T-Shirts', '/images/T-shirt.jpg'),
('Trousers', '/images/Trouser.jpg'),
('Denims', '/images/Denims.jpg'),
('Blouses', '/images/Blouse.jpg'),
('Suits', '/images/Suits.jpg'),
('Bed Sheets', '/images/BedSheets.jpg'),
('Curtains', '/images/Curtains.jpg');

-- Insert sample clothes items
INSERT INTO Clothes (name, unit_price, category_id, image_path) VALUES
-- Shirts
('Formal Shirt', 15.00, 1, '/images/Shirt.jpg'),
('Casual Shirt', 12.00, 1, '/images/Shirt.jpg'),
-- T-Shirts
('Cotton T-Shirt', 8.00, 2, '/images/T-shirt.jpg'),
('Polo T-Shirt', 10.00, 2, '/images/T-shirt.jpg'),
-- Trousers
('Formal Trousers', 18.00, 3, '/images/Trouser.jpg'),
('Casual Trousers', 15.00, 3, '/images/Trouser.jpg'),
-- Denims
('Jeans', 20.00, 4, '/images/Denims.jpg'),
('Denim Jacket', 25.00, 4, '/images/Denims.jpg'),
-- Blouses
('Silk Blouse', 22.00, 5, '/images/Blouse.jpg'),
('Cotton Blouse', 18.00, 5, '/images/Blouse.jpg'),
-- Suits
('Business Suit', 45.00, 6, '/images/Suits.jpg'),
('Wedding Suit', 60.00, 6, '/images/Suits.jpg'),
-- Bed Sheets
('Single Bed Sheet', 12.00, 7, '/images/BedSheets.jpg'),
('Double Bed Sheet', 18.00, 7, '/images/BedSheets.jpg'),
-- Curtains
('Window Curtain', 25.00, 8, '/images/Curtains.jpg'),
('Heavy Curtain', 35.00, 8, '/images/Curtains.jpg');

-- Insert sample admin user
INSERT INTO user (email, password, role, first_name, last_name) VALUES
('admin@freshfold.com', '$2a$10$DowJonesPassword123Hash', 'admin', 'Admin', 'User'),
('staff@freshfold.com', '$2a$10$DowJonesPassword123Hash', 'staff', 'Staff', 'Member'),
('customer@freshfold.com', '$2a$10$DowJonesPassword123Hash', 'customer', 'John', 'Doe');

-- Insert sample drivers
INSERT INTO drivers (name, contact) VALUES
('John Smith', '+1-555-0101'),
('Sarah Johnson', '+1-555-0102'),
('Mike Davis', '+1-555-0103'),
('Lisa Wilson', '+1-555-0104');

COMMIT;
