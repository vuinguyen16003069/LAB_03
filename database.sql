-- ==========================================================
-- LAB 03: Hướng dẫn CRUD API Sinh viên dùng Spring Boot và SQL Server
-- Script khởi tạo Database quanlysinhvien và dữ liệu mẫu (Bước 1)
-- ==========================================================

-- 1. Tạo Database quanlysinhvien (nếu chưa có)
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'quanlysinhvien')
BEGIN
    CREATE DATABASE quanlysinhvien;
END
GO

USE quanlysinhvien;
GO

-- 2. Tạo bảng students
IF OBJECT_ID('students', 'U') IS NOT NULL
BEGIN
    DROP TABLE students;
END
GO

CREATE TABLE [dbo].[students](
    [id] [uniqueidentifier] NOT NULL,
    [student_code] [nvarchar](50) NOT NULL,
    [full_name] [nvarchar](255) NOT NULL,
    [email] [nvarchar](255) NOT NULL,
    [phone] [varchar](255) NULL,
    [class_name] [varchar](255) NULL,
    PRIMARY KEY (id)
);
GO

-- 3. Thêm dữ liệu mẫu (10 sinh viên chuẩn từ tài liệu LAB 03)
INSERT [dbo].[students] ([id], [student_code], [full_name], [email], [phone], [class_name]) VALUES (N'e78a09ed-6a65-48ce-be7e-410f6de63c89', N'SV007', N'Đặng Nhật Anh', N'g@gmail.com', N'0966789012', N'C2024A');
INSERT [dbo].[students] ([id], [student_code], [full_name], [email], [phone], [class_name]) VALUES (N'0c8ee03c-184b-4d12-a8b4-6234f1cb531f', N'SV004', N'Phạm Minh Dũng', N'd@gmail.com', N'0933456789', N'C2024A');
INSERT [dbo].[students] ([id], [student_code], [full_name], [email], [phone], [class_name]) VALUES (N'99f77886-b335-4d11-94bf-6240d2383114', N'SV005', N'Hoàng Thị Em', N'e@gmail.com', N'0944567890', N'C2024B');
INSERT [dbo].[students] ([id], [student_code], [full_name], [email], [phone], [class_name]) VALUES (N'75c9818a-fbda-421e-b397-654869d0e9ac', N'SV008', N'Bùi Thảo H', N'h@gmail.com', N'0977890123', N'C2024B');
INSERT [dbo].[students] ([id], [student_code], [full_name], [email], [phone], [class_name]) VALUES (N'5b599794-18e8-4a45-a6c2-755dcaf3881a', N'SV001', N'Nguyễn Văn A', N'a@gmail.com', N'0901234567', N'C2024A');
INSERT [dbo].[students] ([id], [student_code], [full_name], [email], [phone], [class_name]) VALUES (N'd4e253c6-948e-4500-b823-87d5d3fde96d', N'SV009', N'Ngô Quốc I', N'i@gmail.com', N'0988901234', N'C2024C');
INSERT [dbo].[students] ([id], [student_code], [full_name], [email], [phone], [class_name]) VALUES (N'422ff893-970a-47b5-b0b3-950bd08cc96b', N'SV002', N'Trần Thị B', N'b@gmail.com', N'0912345678', N'C2024B');
INSERT [dbo].[students] ([id], [student_code], [full_name], [email], [phone], [class_name]) VALUES (N'49c77b9e-e56b-49a3-b89d-a2fb6b578c02', N'SV003', N'Lê Hoàng C', N'c@gmail.com', N'0923456789', N'C2024C');
INSERT [dbo].[students] ([id], [student_code], [full_name], [email], [phone], [class_name]) VALUES (N'67a64038-d7a0-47f7-b536-bcc0b1d50364', N'SV010', N'Mai Lan K', N'k@gmail.com', N'0999012345', N'C2024A');
INSERT [dbo].[students] ([id], [student_code], [full_name], [email], [phone], [class_name]) VALUES (N'35685adf-d811-491e-96c9-d858e5474c9d', N'SV006', N'Vũ Đức F', N'f@gmail.com', N'0955678901', N'C2024C');
GO

-- 4. Kiểm tra dữ liệu vừa thêm
SELECT * FROM students;
GO
