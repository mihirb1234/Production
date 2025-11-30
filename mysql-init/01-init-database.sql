-- Database initialization script for Memes Commerce
-- This script runs when the MySQL container starts for the first time

-- Create database if it doesn't exist (handled by docker-compose environment)
-- CREATE DATABASE IF NOT EXISTS memesdb;
-- USE memesdb;

-- Create additional users or permissions if needed
-- The main user is created via docker-compose environment variables

-- Example table structure (uncomment and modify as needed)
/*
-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Memes table
CREATE TABLE IF NOT EXISTS memes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    image_url VARCHAR(500),
    price DECIMAL(10,2),
    user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Insert sample data (optional)
INSERT IGNORE INTO users (username, email) VALUES
('admin', 'admin@memescommerce.com'),
('testuser', 'test@example.com');
*/

-- Add any additional database configuration here
-- For example, setting timezone, character set, etc.

-- Ensure proper character set
ALTER DATABASE memesdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
