# LAB 03: CRUD API Sinh viên dùng Spring Boot & SQL Server

Dự án hoàn chỉnh Quản lý Sinh viên chuẩn Production sử dụng **Spring Boot 3.4.3**, **Spring Data JPA**, **Microsoft SQL Server**, và **Thymeleaf UI + Bootstrap 5**.

---

## 🎯 Mục tiêu bài học & Tính năng đã hoàn thành

- [x] **Kết nối CSDL SQL Server**: Tự động ánh xạ Entity JPA với bảng `students` trong SQL Server database `school`.
- [x] **Mô hình 3 lớp chuẩn**: `Controller` → `Service` → `Repository` → `SQL Server`.
- [x] **Giao diện Web Thymeleaf (HTML5 + Bootstrap 5)**:
  - Trang danh sách sinh viên hiện đại với badge giới tính và hiệu ứng hover.
  - Thẻ thông báo Flash Message (Thành công / Thất bại).
  - Modal / Confirm dialog khi xóa sinh viên.
- [x] **Full REST API (JSON)**: Cung cấp đầy đủ API CRUD (`GET`, `POST`, `PUT`, `DELETE`).
- [x] **Hoàn thành 100% 4 Bài tập mở rộng**:
  1. **Thêm cột Giới tính (`gender`)**: Hỗ trợ Nam, Nữ, Khác trên cả DB, Form, Web UI và REST API.
  2. **Trang Chi tiết sinh viên (`student-detail.html`)**: Giao diện Card hồ sơ sinh viên chuyên nghiệp tại `/students/{id}`.
  3. **Tìm kiếm theo tên (`search by name`)**: Tìm kiếm sinh viên thời gian thực / theo từ khóa tại `/students?keyword=...` và REST API `/api/students?keyword=...`.
  4. **Thiết kế Bootstrap 5 & Icons**: Giao diện chuẩn UI/UX, responsive, sử dụng Google Fonts (Plus Jakarta Sans).

---

## 🛠️ Công nghệ sử dụng

- **Java 17 / Java 25**
- **Spring Boot 3.4.3** (Spring Web, Spring Data JPA, Spring Validation, Thymeleaf)
- **Database**: Microsoft SQL Server Express / Developer (Kèm H2 Database cho Unit Test)
- **Frontend**: Thymeleaf, Bootstrap 5.3.3, Bootstrap Icons 1.11.3
- **Build Tool**: Maven (`mvnw`)

---

## 🗄️ Cấu trúc CSDL (`database.sql`)

```sql
CREATE DATABASE school;
GO
USE school;

CREATE TABLE students (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    age INT NOT NULL,
    email NVARCHAR(100) NOT NULL,
    gender NVARCHAR(10) NULL
);

INSERT INTO students (name, age, email, gender) VALUES
(N'Nguyễn Văn A', 20, 'a@gmail.com', N'Nam'),
(N'Trần Thị B', 21, 'b@gmail.com', N'Nữ'),
(N'Lê Văn C', 19, 'c@gmail.com', N'Nam');
```

---

## 🚀 Hướng dẫn khởi chạy dự án

### 1. Thêm CSDL vào SQL Server
Mở **SSMS** (SQL Server Management Studio) hoặc dùng `sqlcmd` chạy tệp `database.sql`:
```bash
sqlcmd -S localhost -U sa -P 123123 -C -i database.sql
```

### 2. Cấu hình `application.properties`
Đảm bảo thông tin SQL Server trùng khớp:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=school;encrypt=true;trustServerCertificate=true;
spring.datasource.username=sa
spring.datasource.password=123123
```

### 3. Chạy ứng dụng
Dùng lệnh Maven Wrapper:
```bash
./mvnw spring-boot:run
```

---

## 🌐 Các Đường Dẫn (Endpoints)

### 🖥️ Giao diện Web (Thymeleaf UI)
- `http://localhost:8080/students` : Danh sách sinh viên & Tìm kiếm
- `http://localhost:8080/students/1` : Chi tiết sinh viên ID=1
- `http://localhost:8080/students/new` : Form thêm sinh viên mới
- `http://localhost:8080/students/edit/1` : Form chỉnh sửa sinh viên ID=1

### 🔌 REST API (JSON)
- `GET /api/students` : Lấy tất cả sinh viên
- `GET /api/students?keyword=Nguyen` : Tìm kiếm sinh viên theo tên
- `GET /api/students/1` : Lấy chi tiết sinh viên ID=1
- `POST /api/students` : Tạo sinh viên mới (Body JSON)
- `PUT /api/students/1` : Cập nhật sinh viên ID=1 (Body JSON)
- `DELETE /api/students/1` : Xóa sinh viên ID=1

---

## 🧪 Chạy Kiểm Thử Tự Động (Unit Tests)

Dự án bao gồm bộ kiểm thử tự động sử dụng database H2 in-memory:
```bash
./mvnw clean test
```
Result: `Tests run: 10, Failures: 0, Errors: 0, Skipped: 0`
