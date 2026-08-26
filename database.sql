-- ==========================================================
-- LAB 03: Hướng dẫn CRUD API Sinh viên dùng Spring Boot và SQL Server
-- Script khởi tạo Database và dữ liệu mẫu
-- ==========================================================

-- 1. Tạo Database school (nếu chưa có)
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'school')
BEGIN
    CREATE DATABASE school;
END
GO

USE school;
GO

-- 2. Tạo bảng students (Bao gồm các cột cơ bản và cột giới tính cho bài tập mở rộng)
IF OBJECT_ID('students', 'U') IS NOT NULL
BEGIN
    DROP TABLE students;
END
GO

CREATE TABLE students (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    age INT NOT NULL,
    email NVARCHAR(100) NOT NULL,
    gender NVARCHAR(10) NULL -- Bài tập 1: Thêm cột giới tính (Nam / Nữ / Khác)
);
GO

-- 3. Thêm dữ liệu mẫu ban đầu
SET IDENTITY_INSERT students ON;

INSERT INTO students (id, name, age, email, gender) VALUES
(1, N'Nguyễn Văn A', 20, 'a@gmail.com', N'Nam'),
(2, N'Trần Thị B', 21, 'b@gmail.com', N'Nữ'),
(3, N'Lê Văn C', 19, 'c@gmail.com', N'Nam'),
(4, N'Phạm Thị D', 22, 'd@gmail.com', N'Nữ'),
(5, N'Hoàng Văn E', 20, 'e@gmail.com', N'Nam');

SET IDENTITY_INSERT students OFF;
GO

-- 4. Kiểm tra dữ liệu vừa thêm
SELECT * FROM students;
GO
