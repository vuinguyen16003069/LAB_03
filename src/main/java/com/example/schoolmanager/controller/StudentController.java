package com.example.schoolmanager.controller;

import com.example.schoolmanager.entity.Student;
import com.example.schoolmanager.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Điều hướng trang chủ về danh sách sinh viên
    @GetMapping("/")
    public String home() {
        return "redirect:/students";
    }

    // ==========================================================
    // PHẦN B: Trang web HTML cơ bản quản lý sinh viên (/students)
    // ==========================================================
    @GetMapping("/students")
    public String listStudents(
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {
        List<Student> students = studentService.search(keyword);
        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        model.addAttribute("newStudent", new Student());
        return "students"; // templates/students.html
    }

    // Xem chi tiết sinh viên (Trang riêng)
    @GetMapping("/students/{id}")
    public String viewStudentDetail(@PathVariable("id") @NonNull UUID id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Student student = studentService.getById(id);
            model.addAttribute("student", student);
            return "student-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sinh viên có ID: " + id);
            return "redirect:/students";
        }
    }

    // Trang form thêm sinh viên mới
    @GetMapping("/students/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("pageTitle", "Thêm Sinh Viên Mới");
        return "student-form";
    }

    // Trang form chỉnh sửa sinh viên
    @GetMapping("/students/edit/{id}")
    public String showEditForm(@PathVariable("id") @NonNull UUID id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Student student = studentService.getById(id);
            model.addAttribute("student", student);
            model.addAttribute("pageTitle", "Chỉnh Sửa Thông Tin Sinh Viên");
            return "student-form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sinh viên có ID: " + id);
            return "redirect:/students";
        }
    }

    // Lưu thông tin từ student-form (Thêm mới hoặc Cập nhật)
    @PostMapping("/students/save")
    public String saveStudentForm(@ModelAttribute("student") Student student, RedirectAttributes redirectAttributes) {
        try {
            boolean isNew = (student.getId() == null);
            studentService.save(student);
            redirectAttributes.addFlashAttribute("successMessage", isNew ? "Thêm sinh viên mới thành công!" : "Cập nhật sinh viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/students";
    }

    // Thêm sinh viên mới qua modal form (Phần B)
    @PostMapping("/students/add")
    public String addStudent(
            @ModelAttribute("newStudent") Student student,
            RedirectAttributes redirectAttributes) {
        try {
            student.setId(null);
            studentService.save(student);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm sinh viên mới thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi thêm sinh viên: " + e.getMessage());
        }
        return "redirect:/students";
    }

    // Cập nhật thông tin sinh viên qua modal form (Phần B)
    @PostMapping("/students/edit/{id}")
    public String editStudent(
            @PathVariable("id") @NonNull UUID id,
            @ModelAttribute Student student,
            RedirectAttributes redirectAttributes) {
        try {
            student.setId(id);
            studentService.save(student);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sinh viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật sinh viên: " + e.getMessage());
        }
        return "redirect:/students";
    }

    // Xóa sinh viên qua link/button (Phần B)
    @GetMapping("/students/delete/{id}")
    public String deleteStudent(
            @PathVariable("id") @NonNull UUID id,
            RedirectAttributes redirectAttributes) {
        try {
            studentService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa sinh viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa sinh viên: " + e.getMessage());
        }
        return "redirect:/students";
    }

    // ==========================================================
    // PHẦN C: Trang web quản lý sinh viên bằng framework AdminLTE 4
    // ==========================================================
    @GetMapping({"/admin", "/admin/students"})
    public String adminStudents(Model model) {
        List<Student> students = studentService.getAll();
        model.addAttribute("totalStudents", students.size());
        return "admin-students"; // templates/admin-students.html
    }
}
