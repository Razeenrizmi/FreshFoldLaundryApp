-- Create missing tables for admin management functionality
-- This resolves the PaymentManagementDAO errors and other admin features

USE freshfold_db;

-- Create payments table (required by PaymentManagementDAO)
CREATE TABLE IF NOT EXISTS payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'Pending',
    transaction_id VARCHAR(100),
    card_type VARCHAR(20),
    payment_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE
);

-- Create users table (alias/view for user table to match DAO expectations)
-- The PaymentManagementDAO expects 'users' but your schema has 'user'
CREATE VIEW IF NOT EXISTS users AS
SELECT id as user_id, CONCAT(first_name, ' ', last_name) as name, email, role,
       first_name, last_name, phone_number, address, created_at, updated_at
FROM user;

-- Create orders table (lowercase 'orders' to match DAO expectations)
-- Your schema has 'Orders' but DAOs might expect 'orders'
CREATE VIEW IF NOT EXISTS orders AS
SELECT order_id, customer_id, customer_name, customer_phone, customer_address,
       special_instructions, pickup_date, pickup_time, delivery_date, delivery_time,
       pickup_driver_id, delivery_driver_id, service_type, pickup_datetime,
       delivery_datetime, order_time, status, created_at, updated_at
FROM Orders;

-- Insert sample payment data for testing
INSERT IGNORE INTO payments (order_id, payment_method, amount, status, transaction_id, card_type, payment_notes) VALUES
(1, 'Credit Card', 25.50, 'Completed', 'TXN123456789', 'Visa', 'Payment processed successfully'),
(2, 'PayPal', 18.75, 'Completed', 'PP987654321', NULL, 'PayPal payment confirmed'),
(3, 'Cash', 32.00, 'Pending', NULL, NULL, 'Cash on delivery'),
(4, 'Debit Card', 45.25, 'Completed', 'TXN456789123', 'MasterCard', 'Payment completed'),
(5, 'Credit Card', 22.50, 'Failed', 'TXN789123456', 'Visa', 'Payment declined - insufficient funds');
