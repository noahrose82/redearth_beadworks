INSERT IGNORE INTO users (id, email, full_name, password_hash, role) VALUES
(1, 'admin@redearth.local', 'Admin User', '$2a$10$9YB0M5xXHq/1nqJv0gJcE.ybUEaAow5vTIsSfeJ9wJZQBBxw7GmUu', 'ADMIN'),
(2, 'customer@redearth.local', 'Customer User', '$2a$10$VvGfG4nQy9sK0m4fZlQf4e9o2M3At5O.8Jt4z4xG8bE8qB9glbDlu', 'CUSTOMER');

-- Admin123! and Customer123! (BCrypt hashes above)
