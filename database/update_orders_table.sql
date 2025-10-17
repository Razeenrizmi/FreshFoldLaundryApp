-- Update Orders table to add missing columns for dashboard functionality
USE freshfold_db;

-- Add missing columns to Orders table
ALTER TABLE Orders
ADD COLUMN IF NOT EXISTS price DECIMAL(10, 2) DEFAULT 0.00,
ADD COLUMN IF NOT EXISTS cloth_type VARCHAR(255) DEFAULT 'Mixed Items';

-- Update existing orders with default values if needed
UPDATE Orders SET price = 0.00 WHERE price IS NULL;
UPDATE Orders SET cloth_type = 'Mixed Items' WHERE cloth_type IS NULL;
