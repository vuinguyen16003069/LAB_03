package com.example.schoolmanager;

import com.example.schoolmanager.entity.Student;
import com.example.schoolmanager.repository.StudentRepository;
import com.example.schoolmanager.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        studentRepository.save(new Student("Nguyễn Văn A", 20, "a@gmail.com", "Nam"));
        studentRepository.save(new Student("Trần Thị B", 21, "b@gmail.com", "Nữ"));
    }

    @Test
    @DisplayName("Kiểm tra lấy toàn bộ danh sách sinh viên")
    void testGetAllStudents() {
        List<Student> list = studentService.getAllStudents();
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("Kiểm tra tìm kiếm sinh viên theo tên")
    void testSearchStudents() {
        List<Student> result = studentService.searchStudents("Nguyễn");
        assertEquals(1, result.size());
        assertEquals("Nguyễn Văn A", result.get(0).getName());
    }

    @Test
    @DisplayName("Kiểm tra thêm mới sinh viên")
    void testSaveStudent() {
        Student newStudent = new Student("Lê Văn C", 22, "c@gmail.com", "Nam");
        Student saved = studentService.saveStudent(newStudent);
        assertNotNull(saved.getId());
        assertEquals("Lê Văn C", saved.getName());
    }

    @Test
    @DisplayName("Kiểm tra cập nhật sinh viên")
    void testUpdateStudent() {
        List<Student> list = studentService.getAllStudents();
        Student first = list.get(0);
        first.setName("Nguyễn Văn A - Updated");
        Student updated = studentService.updateStudent(first.getId(), first);
        assertEquals("Nguyễn Văn A - Updated", updated.getName());
    }

    @Test
    @DisplayName("Kiểm tra xóa sinh viên")
    void testDeleteStudent() {
        List<Student> list = studentService.getAllStudents();
        Integer id = list.get(0).getId();
        studentService.deleteStudent(id);
        Optional<Student> deleted = studentService.getStudentById(id);
        assertTrue(deleted.isEmpty());
    }
}
