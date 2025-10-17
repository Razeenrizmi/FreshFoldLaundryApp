-- Add driver assignment columns to Orders table
USE freshfold_db;

-- Add driver assignment columns to Orders table
ALTER TABLE Orders
ADD COLUMN IF NOT EXISTS pickup_driver_id BIGINT,
ADD COLUMN IF NOT EXISTS delivery_driver_id BIGINT;

-- Add foreign key constraints to reference the drivers table
-- Note: These will only be added if the drivers table exists
ALTER TABLE Orders
ADD CONSTRAINT IF NOT EXISTS fk_pickup_driver
    FOREIGN KEY (pickup_driver_id) REFERENCES drivers(id) ON DELETE SET NULL;

ALTER TABLE Orders
ADD CONSTRAINT IF NOT EXISTS fk_delivery_driver
    FOREIGN KEY (delivery_driver_id) REFERENCES drivers(id) ON DELETE SET NULL;

-- Verify the changes
DESCRIBE Orders;
