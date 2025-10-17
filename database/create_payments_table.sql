-- Create the missing payments table for FreshFold Laundry App
-- This table is required by PaymentManagementDAO

USE freshfold_db;

-- Create payments table
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

-- Insert some sample payment data for testing
INSERT INTO payments (order_id, payment_method, amount, status, transaction_id, card_type, payment_notes) VALUES
(1, 'Credit Card', 25.50, 'Completed', 'TXN123456789', 'Visa', 'Payment processed successfully'),
(2, 'PayPal', 18.75, 'Completed', 'PP987654321', NULL, 'PayPal payment confirmed'),
(3, 'Cash', 32.00, 'Pending', NULL, NULL, 'Cash on delivery'),
(4, 'Debit Card', 45.25, 'Completed', 'TXN456789123', 'MasterCard', 'Payment completed'),
(5, 'Credit Card', 22.50, 'Failed', 'TXN789123456', 'Visa', 'Payment declined - insufficient funds');
