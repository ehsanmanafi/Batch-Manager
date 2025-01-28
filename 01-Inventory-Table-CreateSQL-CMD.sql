CREATE TABLE Inventory (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           item_id VARCHAR(255) NOT NULL,
                           item_name VARCHAR(255) NOT NULL,
                           quantity INT NOT NULL,
                           unit_price DECIMAL(10, 2) NOT NULL,
                           total_price DECIMAL(10, 2) NOT NULL
);