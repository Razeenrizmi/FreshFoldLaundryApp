-- Insert default admin user
-- Password is 'admin123' encoded with BCrypt
INSERT INTO admins (fullName, userName, password, email, phoneNumber, role, status)
VALUES ('System Administrator', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXIGf5CQBmNkqVSZGQ2LWiDuwBm', 'admin@freshfold.com', '1234567890', 'ADMIN', 'ACTIVE');

