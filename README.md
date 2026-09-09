# LAB 03: CRUD API Sinh Viên Dùng Spring Boot & SQL Server

Dự án hoàn chỉnh Quản lý Sinh viên chuẩn theo hướng dẫn tại **LAB 03**, sử dụng **Spring Boot 3.4.3**, **Spring Data JPA**, **Microsoft SQL Server**, **Swagger OpenAPI UI**, **Giao diện HTML (Phần B)** và **Giao diện AdminLTE 4 (Phần C)**.

---

## 🎯 Mục Tiêu Bài Học & Kết Quả Đạt Được (100% Yêu Cầu)

- [x] **Phần A. Tạo các API cơ bản kết nối bảng sinh viên trong SQL Server**:
  - Cơ sở dữ liệu: `quanlysinhvien` trên SQL Server.
  - Bảng `students`: Khóa chính `id uniqueidentifier` (Java: `UUID`), `student_code`, `full_name`, `email`, `phone`, `class_name`.
  - Nạp đầy đủ 10 sinh viên mẫu chuẩn UUID (`SV001` đến `SV010`) từ tài liệu.
  - Xây dựng chuẩn 5 REST API CRUD tại `/api/students`:
    - `GET /api/students` & `/api/students?keyword=...` : Lấy danh sách hoặc tìm kiếm đa trường.
    - `GET /api/students/{id}` : Lấy chi tiết sinh viên theo ID.
    - `POST /api/students` : Thêm sinh viên mới.
    - `PUT /api/students/{id}` : Cập nhật sinh viên theo ID.
    - `DELETE /api/students/{id}` : Xóa sinh viên theo ID.
  - Tích hợp tài liệu API trực quan **Swagger UI** (`springdoc-openapi-starter-webmvc-ui` 2.8.13) tại `http://localhost:8080/swagger-ui/index.html`.
- [x] **Phần B. Trang web HTML cơ bản quản lý sinh viên**:
  - Giao diện `students.html` tại `http://localhost:8080/students`.
  - Hiển thị danh sách sinh viên, tìm kiếm theo từ khóa.
  - Tích hợp modal Thêm mới, Chỉnh sửa, Xem chi tiết và Xóa sinh viên.
- [x] **Phần C. Trang web quản lý sinh viên bằng framework AdminLTE 4**:
  - Giao diện hiện đại chuẩn **AdminLTE v4** (Bootstrap 5) tại `http://localhost:8080/admin`.
  - Sidebar điều hướng, Header Navbar, Breadcrumb, các widget thống kê (Info box).
  - Tương tác CRUD 100% với REST API ở Phần A thông qua AJAX / Fetch API (không cần tải lại trang).
  - Hộp thoại xác nhận và thông báo SweetAlert2 chuyên nghiệp.

---

## 🛠️ Công Nghệ Sử Dụng

- **Ngôn ngữ**: Java 17 / 21+
- **Framework**: Spring Boot 3.4.3 (Spring Web, Spring Data JPA, Spring Validation, Thymeleaf)
- **Tài liệu API**: SpringDoc OpenAPI UI 2.8.13 (Swagger UI)
- **Cơ sở dữ liệu**:
  - **Chính thức**: Microsoft SQL Server (Database `quanlysinhvien`)
  - **Dev/Fallback & Testing**: H2 Database (hỗ trợ kiểu UUID, tự động nạp 10 sinh viên mẫu)
- **Giao diện**:
  - **Phần B**: HTML5, Thymeleaf, Bootstrap 5.3.3, Bootstrap Icons
  - **Phần C**: Framework AdminLTE 4, SweetAlert2, AJAX Fetch API

---

## 🗄️ Cấu Trúc Bảng CSDL (`database.sql`)

```sql
CREATE DATABASE quanlysinhvien;
GO
USE quanlysinhvien;
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
```

Dữ liệu mẫu gồm 10 sinh viên (`SV001` đến `SV010`) được định nghĩa sẵn trong tệp [database.sql](database.sql) và [data.sql](src/main/resources/data.sql).

---

## 🚀 Hướng Dẫn Khởi Chạy Ứng Dụng

### Cách 1: Chạy trực tiếp (Sử dụng dữ liệu mẫu tự động)
Dự án đã được cấu hình sẵn để có thể chạy ngay lập tức mà không cần cài đặt phức tạp:
```bash
./mvnw spring-boot:run
```
*(Trên Windows PowerShell: `.\mvnw.cmd spring-boot:run`)*

### Cách 2: Kết nối trực tiếp Microsoft SQL Server
1. Mở SQL Server Management Studio (SSMS) hoặc `sqlcmd` và thực thi tệp `database.sql`:
   ```bash
   sqlcmd -S localhost -U sa -P 123123 -C -i database.sql
   ```
2. Khởi chạy ứng dụng với profile `sqlserver`:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=sqlserver
   ```

---

## 🌐 Danh Sách Các Đường Dẫn (Endpoints)

### 1. 🖥️ Giao diện Người dùng
| Chức năng | Đường dẫn (URL) | Mô tả |
| :--- | :--- | :--- |
| **Giao diện Phần B** | `http://localhost:8080/students` | Trang web HTML cơ bản quản lý sinh viên |
| **Giao diện Phần C** | `http://localhost:8080/admin` | Trang quản trị framework **AdminLTE 4** |
| **Swagger UI** | `http://localhost:8080/swagger-ui/index.html` | Tài liệu tương tác và thử nghiệm REST API |

### 2. 🔌 REST API JSON (Phần A - `/api/students`)
| STT | Method | API Endpoint | Chức năng |
| :---: | :---: | :--- | :--- |
| 1 | `GET` | `/api/students`<br>`/api/students?keyword=...` | Lấy danh sách tất cả sinh viên / Tìm kiếm đa trường (mã SV, họ tên, email, SĐT) |
| 2 | `GET` | `/api/students/{id}` | Lấy chi tiết sinh viên theo UUID |
| 3 | `POST` | `/api/students` | Thêm sinh viên mới (Body JSON) |
| 4 | `PUT` | `/api/students/{id}` | Cập nhật thông tin sinh viên theo UUID (Body JSON) |
| 5 | `DELETE` | `/api/students/{id}` | Xóa sinh viên theo UUID |

---

## 🧪 Kiểm Thử Tự Động (Unit & Integration Tests)

Chạy toàn bộ bộ test kiểm thử tự động của dự án:
```bash
./mvnw clean test
```
Bộ test bao gồm:
- `StudentServiceTest`: Kiểm tra các phương thức nghiệp vụ (`getAll`, `search`, `getById`, `save`, `delete`).
- `StudentRestControllerTest`: Kiểm thử tự động toàn bộ 5 REST API HTTP request và response JSON.
