--create table product
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    storage_capacity VARCHAR(20),
    color VARCHAR(30),
    price DECIMAL(10,2) NOT NULL,
    stock_quantity INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert iPhone 15 Pro models
INSERT INTO products (model, storage_capacity, color, price, stock_quantity, created_at, updated_at) VALUES
('iPhone 15 Pro', '128GB', 'Space Black', 999.00, 25, NOW(), NOW()),
('iPhone 15 Pro', '128GB', 'Silver', 999.00, 30, NOW(), NOW()),
('iPhone 15 Pro', '128GB', 'Gold', 999.00, 20, NOW(), NOW()),
('iPhone 15 Pro', '128GB', 'Deep Purple', 999.00, 15, NOW(), NOW()),

('iPhone 15 Pro', '256GB', 'Space Black', 1099.00, 20, NOW(), NOW()),
('iPhone 15 Pro', '256GB', 'Silver', 1099.00, 25, NOW(), NOW()),
('iPhone 15 Pro', '256GB', 'Gold', 1099.00, 18, NOW(), NOW()),
('iPhone 15 Pro', '256GB', 'Deep Purple', 1099.00, 12, NOW(), NOW()),

('iPhone 15 Pro', '512GB', 'Space Black', 1299.00, 15, NOW(), NOW()),
('iPhone 15 Pro', '512GB', 'Silver', 1299.00, 18, NOW(), NOW()),
('iPhone 15 Pro', '512GB', 'Gold', 1299.00, 10, NOW(), NOW()),
('iPhone 15 Pro', '512GB', 'Deep Purple', 1299.00, 8, NOW(), NOW()),

('iPhone 15 Pro', '1TB', 'Space Black', 1499.00, 10, NOW(), NOW()),
('iPhone 15 Pro', '1TB', 'Silver', 1499.00, 12, NOW(), NOW()),
('iPhone 15 Pro', '1TB', 'Gold', 1499.00, 5, NOW(), NOW()),
('iPhone 15 Pro', '1TB', 'Deep Purple', 1499.00, 3, NOW(), NOW()),

-- Insert iPhone 15 models
('iPhone 15', '128GB', 'Dynamic Island Blue', 799.00, 35, NOW(), NOW()),
('iPhone 15', '128GB', 'Space Black', 799.00, 40, NOW(), NOW()),
('iPhone 15', '128GB', 'Silver', 799.00, 30, NOW(), NOW()),
('iPhone 15', '128GB', 'Gold', 799.00, 25, NOW(), NOW()),

('iPhone 15', '256GB', 'Dynamic Island Blue', 899.00, 28, NOW(), NOW()),
('iPhone 15', '256GB', 'Space Black', 899.00, 32, NOW(), NOW()),
('iPhone 15', '256GB', 'Silver', 899.00, 22, NOW(), NOW()),
('iPhone 15', '256GB', 'Gold', 899.00, 18, NOW(), NOW()),

('iPhone 15', '512GB', 'Dynamic Island Blue', 1099.00, 20, NOW(), NOW()),
('iPhone 15', '512GB', 'Space Black', 1099.00, 25, NOW(), NOW()),
('iPhone 15', '512GB', 'Silver', 1099.00, 15, NOW(), NOW()),
('iPhone 15', '512GB', 'Gold', 1099.00, 12, NOW(), NOW()),

-- Insert iPhone 14 Pro models
('iPhone 14 Pro', '128GB', 'Space Black', 899.00, 15, NOW(), NOW()),
('iPhone 14 Pro', '128GB', 'Silver', 899.00, 20, NOW(), NOW()),
('iPhone 14 Pro', '128GB', 'Gold', 899.00, 12, NOW(), NOW()),
('iPhone 14 Pro', '128GB', 'Deep Purple', 899.00, 8, NOW(), NOW()),

('iPhone 14 Pro', '256GB', 'Space Black', 999.00, 12, NOW(), NOW()),
('iPhone 14 Pro', '256GB', 'Silver', 999.00, 15, NOW(), NOW()),
('iPhone 14 Pro', '256GB', 'Gold', 999.00, 10, NOW(), NOW()),
('iPhone 14 Pro', '256GB', 'Deep Purple', 999.00, 5, NOW(), NOW()),

('iPhone 14 Pro', '512GB', 'Space Black', 1199.00, 8, NOW(), NOW()),
('iPhone 14 Pro', '512GB', 'Silver', 1199.00, 10, NOW(), NOW()),
('iPhone 14 Pro', '512GB', 'Gold', 1199.00, 6, NOW(), NOW()),
('iPhone 14 Pro', '512GB', 'Deep Purple', 1199.00, 4, NOW(), NOW()),

('iPhone 14 Pro', '1TB', 'Space Black', 1399.00, 5, NOW(), NOW()),
('iPhone 14 Pro', '1TB', 'Silver', 1399.00, 7, NOW(), NOW()),
('iPhone 14 Pro', '1TB', 'Gold', 1399.00, 3, NOW(), NOW()),
('iPhone 14 Pro', '1TB', 'Deep Purple', 1399.00, 2, NOW(), NOW()),

-- Insert iPhone 14 models
('iPhone 14', '128GB', 'Space Black', 699.00, 25, NOW(), NOW()),
('iPhone 14', '128GB', 'Silver', 699.00, 30, NOW(), NOW()),
('iPhone 14', '128GB', 'Gold', 699.00, 20, NOW(), NOW()),
('iPhone 14', '128GB', 'Deep Purple', 699.00, 15, NOW(), NOW()),

('iPhone 14', '256GB', 'Space Black', 799.00, 20, NOW(), NOW()),
('iPhone 14', '256GB', 'Silver', 799.00, 25, NOW(), NOW()),
('iPhone 14', '256GB', 'Gold', 799.00, 15, NOW(), NOW()),
('iPhone 14', '256GB', 'Deep Purple', 799.00, 12, NOW(), NOW()),

('iPhone 14', '512GB', 'Space Black', 999.00, 15, NOW(), NOW()),
('iPhone 14', '512GB', 'Silver', 999.00, 18, NOW(), NOW()),
('iPhone 14', '512GB', 'Gold', 999.00, 10, NOW(), NOW()),
('iPhone 14', '512GB', 'Deep Purple', 999.00, 8, NOW(), NOW());
