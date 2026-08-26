package com.example.schoolmanager.controller;

import com.example.schoolmanager.entity.Student;
import com.example.schoolmanager.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

    // 7️⃣ Lấy danh sách sinh viên (kết hợp Bài tập 3: Tìm kiếm theo tên)
    // URL: http://localhost:8080/students hoặc http://localhost:8080/students?keyword=Nguyen
    @GetMapping("/students")
    public String listStudents(
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {
        List<Student> students = studentService.searchStudents(keyword);
        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword != null ? keyword : "");
        return "students"; // templates/students.html
    }

    // 🎓 Bài tập 2: Trang chi tiết sinh viên
    // URL: http://localhost:8080/students/1
    @GetMapping("/students/{id:[0-9]+}")
    public String viewStudentDetail(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        return studentService.getStudentById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    return "student-detail"; // templates/student-detail.html
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sinh viên có ID: " + id);
                    return "redirect:/students";
                });
    }

    // Hiển thị form thêm sinh viên mới
    // URL: http://localhost:8080/students/new
    @GetMapping("/students/new")
    public String showCreateForm(Model model) {
        Student student = new Student();
        student.setGender("Nam"); // Giá trị mặc định
        model.addAttribute("student", student);
        model.addAttribute("pageTitle", "Thêm Sinh Viên Mới");
        return "student-form"; // templates/student-form.html
    }

    // Hiển thị form chỉnh sửa sinh viên
    // URL: http://localhost:8080/students/edit/1
    @GetMapping("/students/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        return studentService.getStudentById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    model.addAttribute("pageTitle", "Chỉnh Sửa Thông Tin Sinh Viên");
                    return "student-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sinh viên có ID: " + id);
                    return "redirect:/students";
                });
    }

    // Xử lý lưu thông tin sinh viên (Cả Thêm mới và Cập nhật)
    @PostMapping("/students/save")
    public String saveStudent(
            @Valid @ModelAttribute("student") Student student,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", student.getId() == null ? "Thêm Sinh Viên Mới" : "Chỉnh Sửa Thông Tin Sinh Viên");
            return "student-form";
        }

        boolean isNew = (student.getId() == null);
        studentService.saveStudent(student);

        String message = isNew ? "Thêm sinh viên mới thành công!" : "Cập nhật sinh viên thành công!";
        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/students";
    }

    // Xử lý xóa sinh viên
    // URL: http://localhost:8080/students/delete/1
    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa sinh viên ID " + id + " thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa sinh viên: " + e.getMessage());
        }
        return "redirect:/students";
    }
}
