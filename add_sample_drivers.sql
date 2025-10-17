-- Add sample drivers to the database for testing coordinator dashboard
USE freshfold_db;

-- Insert sample drivers if they don't exist
INSERT IGNORE INTO drivers (name, contact) VALUES
('John Smith', '+1-555-0101'),
('Maria Garcia', '+1-555-0102'),
('David Wilson', '+1-555-0103'),
('Sarah Johnson', '+1-555-0104'),
('Michael Brown', '+1-555-0105');

-- Check if drivers were inserted
SELECT 'Sample drivers added successfully:' as message;
SELECT id, name, contact FROM drivers;
