package com.example.schoolmanager.controller;

import com.example.schoolmanager.entity.Student;
import com.example.schoolmanager.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
@Tag(name = "Student API", description = "Các API CRUD Quản lý Sinh viên (LAB 03 - Phần A)")
public class StudentRestController {

    @Autowired
    private StudentService studentService;

    // 1. GET /api/students hoặc /api/students?keyword=...
    // Lấy tất cả sinh viên / Tìm kiếm sinh viên
    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả sinh viên hoặc tìm kiếm theo từ khóa", 
               description = "Tìm kiếm không phân biệt hoa thường trên các trường: studentCode, fullName, email, phone")
    public List<Student> listStudents(
            @Parameter(description = "Từ khóa tìm kiếm (mã SV, họ tên, email, sđt)")
            @RequestParam(required = false) String keyword) {
        return studentService.search(keyword);
    }

    // 2. GET /api/students/{id}
    // Lấy thông tin sinh viên theo ID (UUID)
    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin chi tiết sinh viên theo ID (UUID)")
    public Student getStudent(
            @Parameter(description = "UUID định danh của sinh viên")
            @PathVariable UUID id) {
        return studentService.getById(id);
    }

    // 3. POST /api/students
    // Thêm sinh viên mới
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Thêm sinh viên mới")
    public Student createStudent(@RequestBody Student student) {
        student.setId(null); // Đảm bảo tự tạo UUID mới nếu chưa có
        return studentService.save(student);
    }

    // 4. PUT /api/students/{id}
    // Cập nhật thông tin sinh viên
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin sinh viên theo ID")
    public Student updateStudent(
            @Parameter(description = "UUID của sinh viên cần cập nhật")
            @PathVariable UUID id,
            @RequestBody Student student) {
        student.setId(id);
        return studentService.save(student);
    }

    // 5. DELETE /api/students/{id}
    // Xóa sinh viên theo ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa sinh viên theo ID")
    public void deleteStudent(
            @Parameter(description = "UUID của sinh viên cần xóa")
            @PathVariable UUID id) {
        studentService.delete(id);
    }
}
