package com.example.schoolmanager;

import com.example.schoolmanager.entity.Student;
import com.example.schoolmanager.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("null")
class StudentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        studentRepository.save(new Student("SV001", "Nguyễn Văn A", "a@gmail.com", "0901234567", "C2024A"));
        studentRepository.save(new Student("SV002", "Trần Thị B", "b@gmail.com", "0912345678", "C2024B"));
    }

    @Test
    @DisplayName("API 1: GET /api/students - Lấy toàn bộ danh sách JSON")
    void testGetAllStudentsApi() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].studentCode", is("SV001")))
                .andExpect(jsonPath("$[0].fullName", is("Nguyễn Văn A")));
    }

    @Test
    @DisplayName("API 1b: GET /api/students?keyword=Trần - Tìm kiếm sinh viên")
    void testSearchStudentsApi() throws Exception {
        mockMvc.perform(get("/api/students").param("keyword", "Trần"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].studentCode", is("SV002")))
                .andExpect(jsonPath("$[0].fullName", is("Trần Thị B")));
    }

    @Test
    @DisplayName("API 2: GET /api/students/{id} - Lấy chi tiết sinh viên theo ID")
    void testGetStudentByIdApi() throws Exception {
        Student student = studentRepository.findAll().get(0);

        mockMvc.perform(get("/api/students/" + student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(student.getId().toString())))
                .andExpect(jsonPath("$.studentCode", is(student.getStudentCode())))
                .andExpect(jsonPath("$.fullName", is(student.getFullName())));
    }

    @Test
    @DisplayName("API 3: POST /api/students - Tạo sinh viên mới")
    void testCreateStudentApi() throws Exception {
        Student newStudent = new Student("SV003", "Lê Hoàng C", "c@gmail.com", "0923456789", "C2024C");

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.studentCode", is("SV003")))
                .andExpect(jsonPath("$.fullName", is("Lê Hoàng C")));
    }

    @Test
    @DisplayName("API 4: PUT /api/students/{id} - Cập nhật thông tin sinh viên")
    void testUpdateStudentApi() throws Exception {
        Student student = studentRepository.findAll().get(0);
        student.setFullName("Nguyễn Văn A - Cập nhật");

        mockMvc.perform(put("/api/students/" + student.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is("Nguyễn Văn A - Cập nhật")));
    }

    @Test
    @DisplayName("API 5: DELETE /api/students/{id} - Xóa sinh viên")
    void testDeleteStudentApi() throws Exception {
        Student student = studentRepository.findAll().get(0);

        mockMvc.perform(delete("/api/students/" + student.getId()))
                .andExpect(status().isOk());
    }
}
