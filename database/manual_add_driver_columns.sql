-- Manual Database Update Script for Driver Assignment Columns
-- Run this script in your MySQL client or phpMyAdmin to add the missing columns

USE freshfold_db;

-- First, let's check if the columns already exist
SELECT COLUMN_NAME
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'freshfold_db'
AND TABLE_NAME = 'Orders'
AND COLUMN_NAME IN ('pickup_driver_id', 'delivery_driver_id');

-- Add the columns if they don't exist
-- Note: Remove IF NOT EXISTS if your MySQL version doesn't support it
ALTER TABLE Orders
ADD COLUMN pickup_driver_id BIGINT DEFAULT NULL,
ADD COLUMN delivery_driver_id BIGINT DEFAULT NULL;

-- Add foreign key constraints (optional, but recommended)
-- Make sure the drivers table exists first
ALTER TABLE Orders
ADD CONSTRAINT fk_pickup_driver
    FOREIGN KEY (pickup_driver_id) REFERENCES drivers(id) ON DELETE SET NULL;

ALTER TABLE Orders
ADD CONSTRAINT fk_delivery_driver
    FOREIGN KEY (delivery_driver_id) REFERENCES drivers(id) ON DELETE SET NULL;

-- Verify the changes
DESCRIBE Orders;

-- Check if drivers table exists and has data
SELECT COUNT(*) as driver_count FROM drivers;
SELECT * FROM drivers LIMIT 5;
