-- Seed Offices
INSERT IGNORE INTO offices (name, address) VALUES
('Văn phòng Hà Nội', 'Tầng 10, Tòa nhà Keangnam, Phạm Hùng, Nam Từ Liêm, Hà Nội'),
('Văn phòng TP.HCM', 'Tầng 15, Tòa nhà Bitexco, Quận 1, TP.HCM'),
('Văn phòng Đà Nẵng', 'Tầng 5, Tòa nhà FPT, Quận Ngũ Hành Sơn, Đà Nẵng'),
('Văn phòng Tokyo', 'Shibuya Sky Building, Tokyo, Japan'),
('Văn phòng Osaka', 'Umeda Business Center, Osaka, Japan');

-- Seed Admin User (password: 123456)
INSERT IGNORE INTO employees (name, username, email, password, phone_number, gender, address, position, role, office_id, created_at, updated_at) VALUES
('Nguyễn Văn Admin', 'admin', 'admin@urban.vn', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', '0901234567', 'MALE', '123 Đường ABC, Hà Nội', 'MANAGER', 'ADMIN', 1, NOW(), NOW());