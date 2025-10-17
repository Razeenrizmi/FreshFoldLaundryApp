-- Insert sample categories and clothes data for FreshFold Laundry App
USE freshfold_db;

-- Insert Categories
INSERT INTO Category (name, image_path) VALUES
('Shirts', '/images/categories/shirts.jpg'),
('Pants', '/images/categories/pants.jpg'),
('Dresses', '/images/categories/dresses.jpg'),
('Jackets', '/images/categories/jackets.jpg'),
('Underwear', '/images/categories/underwear.jpg'),
('Bedding', '/images/categories/bedding.jpg'),
('Towels', '/images/categories/towels.jpg'),
('Delicates', '/images/categories/delicates.jpg');

-- Insert Clothes for Shirts category (category_id = 1)
INSERT INTO Clothes (name, unit_price, category_id, image_path) VALUES
('Dress Shirt', 25.00, 1, '/images/clothes/dress_shirt.jpg'),
('T-Shirt', 15.00, 1, '/images/clothes/tshirt.jpg'),
('Polo Shirt', 20.00, 1, '/images/clothes/polo_shirt.jpg'),
('Button-Down Shirt', 22.00, 1, '/images/clothes/button_down.jpg');

-- Insert Clothes for Pants category (category_id = 2)
INSERT INTO Clothes (name, unit_price, category_id, image_path) VALUES
('Dress Pants', 30.00, 2, '/images/clothes/dress_pants.jpg'),
('Jeans', 25.00, 2, '/images/clothes/jeans.jpg'),
('Khakis', 20.00, 2, '/images/clothes/khakis.jpg'),
('Shorts', 15.00, 2, '/images/clothes/shorts.jpg');

-- Insert Clothes for Dresses category (category_id = 3)
INSERT INTO Clothes (name, unit_price, category_id, image_path) VALUES
('Evening Dress', 45.00, 3, '/images/clothes/evening_dress.jpg'),
('Casual Dress', 30.00, 3, '/images/clothes/casual_dress.jpg'),
('Summer Dress', 25.00, 3, '/images/clothes/summer_dress.jpg'),
('Formal Dress', 50.00, 3, '/images/clothes/formal_dress.jpg');

-- Insert Clothes for Jackets category (category_id = 4)
INSERT INTO Clothes (name, unit_price, category_id, image_path) VALUES
('Suit Jacket', 40.00, 4, '/images/clothes/suit_jacket.jpg'),
('Blazer', 35.00, 4, '/images/clothes/blazer.jpg'),
('Winter Coat', 50.00, 4, '/images/clothes/winter_coat.jpg'),
('Light Jacket', 30.00, 4, '/images/clothes/light_jacket.jpg');

-- Insert Clothes for Underwear category (category_id = 5)
INSERT INTO Clothes (name, unit_price, category_id, image_path) VALUES
('Underwear', 10.00, 5, '/images/clothes/underwear.jpg'),
('Bras', 15.00, 5, '/images/clothes/bras.jpg'),
('Socks', 8.00, 5, '/images/clothes/socks.jpg'),
('Undershirts', 12.00, 5, '/images/clothes/undershirts.jpg');

-- Insert Clothes for Bedding category (category_id = 6)
INSERT INTO Clothes (name, unit_price, category_id, image_path) VALUES
('Bed Sheets', 20.00, 6, '/images/clothes/bed_sheets.jpg'),
('Pillowcases', 10.00, 6, '/images/clothes/pillowcases.jpg'),
('Comforter', 35.00, 6, '/images/clothes/comforter.jpg'),
('Blanket', 25.00, 6, '/images/clothes/blanket.jpg');

-- Insert Clothes for Towels category (category_id = 7)
INSERT INTO Clothes (name, unit_price, category_id, image_path) VALUES
('Bath Towel', 15.00, 7, '/images/clothes/bath_towel.jpg'),
('Hand Towel', 10.00, 7, '/images/clothes/hand_towel.jpg'),
('Beach Towel', 20.00, 7, '/images/clothes/beach_towel.jpg'),
('Kitchen Towel', 8.00, 7, '/images/clothes/kitchen_towel.jpg');

-- Insert Clothes for Delicates category (category_id = 8)
INSERT INTO Clothes (name, unit_price, category_id, image_path) VALUES
('Silk Blouse', 40.00, 8, '/images/clothes/silk_blouse.jpg'),
('Lace Garment', 35.00, 8, '/images/clothes/lace_garment.jpg'),
('Cashmere Sweater', 50.00, 8, '/images/clothes/cashmere_sweater.jpg'),
('Wool Garment', 45.00, 8, '/images/clothes/wool_garment.jpg');
