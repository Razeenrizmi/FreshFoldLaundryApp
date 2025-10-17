-- FreshFold Laundry App - Missing Tables Schema
-- This file creates only the missing tables needed for the browse functionality

USE freshfold_db;

-- Create Category table if it doesn't exist
CREATE TABLE IF NOT EXISTS Category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    image_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Clothes table if it doesn't exist
CREATE TABLE IF NOT EXISTS Clothes (
    cloth_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    category_id INT NOT NULL,
    image_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES Category(category_id) ON DELETE CASCADE
);

-- Create Customer table if it doesn't exist
CREATE TABLE IF NOT EXISTS Customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    phone_no VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create Orders table if it doesn't exist
CREATE TABLE IF NOT EXISTS Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    service_type VARCHAR(50) NOT NULL,
    pickup_datetime DATETIME NOT NULL,
    delivery_datetime DATETIME NOT NULL,
    order_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE
);

-- Create OrderItem table if it doesn't exist
CREATE TABLE IF NOT EXISTS OrderItem (
    order_id INT NOT NULL,
    cloth_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    PRIMARY KEY (order_id, cloth_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (cloth_id) REFERENCES Clothes(cloth_id) ON DELETE CASCADE
);

-- Create SpecialRequest table if it doesn't exist
CREATE TABLE IF NOT EXISTS SpecialRequest (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE
);

-- Insert sample categories (only if table is empty)
INSERT IGNORE INTO Category (name, image_path) VALUES
('Shirts', '/images/Shirt.jpg'),
('T-Shirts', '/images/T-shirt.jpg'),
('Trousers', '/images/Trouser.jpg'),
('Denims', '/images/Denims.jpg'),
('Blouses', '/images/Blouse.jpg'),
('Suits', '/images/Suits.jpg'),
('Bed Sheets', '/images/BedSheets.jpg'),
('Curtains', '/images/Curtains.jpg');

-- Insert sample clothes items (only if table is empty)
INSERT IGNORE INTO Clothes (name, unit_price, category_id, image_path) VALUES
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

COMMIT;
