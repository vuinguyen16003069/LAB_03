-- ==========================================================
-- LAB 03: Khởi tạo bảng students & Dữ liệu mẫu (UUID H2 & SQL Server Compatible)
-- ==========================================================

DROP TABLE IF EXISTS students;

CREATE TABLE students (
    id UUID PRIMARY KEY,
    student_code VARCHAR(50) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    class_name VARCHAR(255)
);

INSERT INTO students (id, student_code, full_name, email, phone, class_name) VALUES ('e78a09ed-6a65-48ce-be7e-410f6de63c89', 'SV007', 'Đặng Nhật Anh', 'g@gmail.com', '0966789012', 'C2024A');
INSERT INTO students (id, student_code, full_name, email, phone, class_name) VALUES ('0c8ee03c-184b-4d12-a8b4-6234f1cb531f', 'SV004', 'Phạm Minh Dũng', 'd@gmail.com', '0933456789', 'C2024A');
INSERT INTO students (id, student_code, full_name, email, phone, class_name) VALUES ('99f77886-b335-4d11-94bf-6240d2383114', 'SV005', 'Hoàng Thị Em', 'e@gmail.com', '0944567890', 'C2024B');
INSERT INTO students (id, student_code, full_name, email, phone, class_name) VALUES ('75c9818a-fbda-421e-b397-654869d0e9ac', 'SV008', 'Bùi Thảo H', 'h@gmail.com', '0977890123', 'C2024B');
INSERT INTO students (id, student_code, full_name, email, phone, class_name) VALUES ('5b599794-18e8-4a45-a6c2-755dcaf3881a', 'SV001', 'Nguyễn Văn A', 'a@gmail.com', '0901234567', 'C2024A');
INSERT INTO students (id, student_code, full_name, email, phone, class_name) VALUES ('d4e253c6-948e-4500-b823-87d5d3fde96d', 'SV009', 'Ngô Quốc I', 'i@gmail.com', '0988901234', 'C2024C');
INSERT INTO students (id, student_code, full_name, email, phone, class_name) VALUES ('422ff893-970a-47b5-b0b3-950bd08cc96b', 'SV002', 'Trần Thị B', 'b@gmail.com', '0912345678', 'C2024B');
INSERT INTO students (id, student_code, full_name, email, phone, class_name) VALUES ('49c77b9e-e56b-49a3-b89d-a2fb6b578c02', 'SV003', 'Lê Hoàng C', 'c@gmail.com', '0923456789', 'C2024C');
INSERT INTO students (id, student_code, full_name, email, phone, class_name) VALUES ('67a64038-d7a0-47f7-b536-bcc0b1d50364', 'SV010', 'Mai Lan K', 'k@gmail.com', '0999012345', 'C2024A');
INSERT INTO students (id, student_code, full_name, email, phone, class_name) VALUES ('35685adf-d811-491e-96c9-d858e5474c9d', 'SV006', 'Vũ Đức F', 'f@gmail.com', '0955678901', 'C2024C');
