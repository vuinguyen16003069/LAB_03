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
        studentRepository.save(new Student("Nguyễn Văn A", 20, "a@gmail.com", "Nam"));
        studentRepository.save(new Student("Trần Thị B", 21, "b@gmail.com", "Nữ"));
    }

    @Test
    @DisplayName("REST API GET /api/students - Lấy toàn bộ danh sách JSON")
    void testGetAllStudentsApi() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Nguyễn Văn A")));
    }

    @Test
    @DisplayName("REST API GET /api/students?keyword=Trần - Tìm kiếm sinh viên")
    void testSearchStudentsApi() throws Exception {
        mockMvc.perform(get("/api/students").param("keyword", "Trần"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Trần Thị B")));
    }

    @Test
    @DisplayName("REST API POST /api/students - Tạo sinh viên mới")
    void testCreateStudentApi() throws Exception {
        Student newStudent = new Student("Phạm Văn C", 23, "c@gmail.com", "Nam");

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Phạm Văn C")));
    }

    @Test
    @DisplayName("REST API PUT /api/students/{id} - Cập nhật sinh viên")
    void testUpdateStudentApi() throws Exception {
        Student student = studentRepository.findAll().get(0);
        student.setName("Nguyễn Văn A - Edited");

        mockMvc.perform(put("/api/students/" + student.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Nguyễn Văn A - Edited")));
    }

    @Test
    @DisplayName("REST API DELETE /api/students/{id} - Xóa sinh viên")
    void testDeleteStudentApi() throws Exception {
        Student student = studentRepository.findAll().get(0);

        mockMvc.perform(delete("/api/students/" + student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Đã xóa sinh viên")));
    }
}
