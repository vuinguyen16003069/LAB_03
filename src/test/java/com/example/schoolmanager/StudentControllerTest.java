package com.example.schoolmanager;

import com.example.schoolmanager.entity.Student;
import com.example.schoolmanager.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("null")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        studentRepository.save(new Student("SV001", "Nguyễn Văn A", "a@gmail.com", "0901234567", "C2024A"));
        studentRepository.save(new Student("SV002", "Trần Thị B", "b@gmail.com", "0912345678", "C2024B"));
    }

    @Test
    @DisplayName("Giao diện: GET / - Điều hướng sang /students")
    void testHomeRedirect() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));
    }

    @Test
    @DisplayName("Giao diện Phần B: GET /students - Trả về view students.html với danh sách")
    void testListStudentsView() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attribute("students", hasSize(2)))
                .andExpect(model().attributeExists("newStudent"));
    }

    @Test
    @DisplayName("Giao diện Phần B: GET /students?keyword=SV001 - Lọc danh sách trên View")
    void testListStudentsWithKeyword() throws Exception {
        mockMvc.perform(get("/students").param("keyword", "SV001"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attribute("students", hasSize(1)));
    }

    @Test
    @DisplayName("Giao diện Phần C: GET /admin - Trả về view admin-students.html")
    void testAdminDashboardView() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-students"))
                .andExpect(model().attributeExists("totalStudents"))
                .andExpect(model().attribute("totalStudents", is(2)));
    }

    @Test
    @DisplayName("Thao tác Phần B: POST /students/add - Thêm sinh viên từ modal")
    void testAddStudentModal() throws Exception {
        mockMvc.perform(post("/students/add")
                        .param("studentCode", "SV003")
                        .param("fullName", "Lê Văn C")
                        .param("email", "c@gmail.com")
                        .param("phone", "0934567890")
                        .param("className", "C2024A"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @DisplayName("Thao tác Phần B: POST /students/edit/{id} - Chỉnh sửa sinh viên từ modal")
    void testEditStudentModal() throws Exception {
        Student student = studentRepository.findAll().get(0);

        mockMvc.perform(post("/students/edit/" + student.getId())
                        .param("studentCode", student.getStudentCode())
                        .param("fullName", "Nguyễn Văn A Updated")
                        .param("email", student.getEmail())
                        .param("phone", student.getPhone())
                        .param("className", student.getClassName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @DisplayName("Thao tác Phần B: GET /students/delete/{id} - Xóa sinh viên qua link")
    void testDeleteStudentAction() throws Exception {
        Student student = studentRepository.findAll().get(0);

        mockMvc.perform(get("/students/delete/" + student.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"))
                .andExpect(flash().attributeExists("successMessage"));
    }
}
