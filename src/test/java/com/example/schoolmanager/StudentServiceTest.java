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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@SuppressWarnings("null")
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        studentRepository.save(new Student("SV001", "Nguyễn Văn A", "a@gmail.com", "0901234567", "C2024A"));
        studentRepository.save(new Student("SV002", "Trần Thị B", "b@gmail.com", "0912345678", "C2024B"));
    }

    @Test
    @DisplayName("Kiểm tra lấy toàn bộ danh sách sinh viên - getAll()")
    void testGetAll() {
        List<Student> list = studentService.getAll();
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("Kiểm tra tìm kiếm sinh viên theo từ khóa - search(keyword)")
    void testSearch() {
        // Tìm theo tên
        List<Student> byName = studentService.search("Nguyễn");
        assertEquals(1, byName.size());
        assertEquals("Nguyễn Văn A", byName.get(0).getFullName());

        // Tìm theo mã sinh viên
        List<Student> byCode = studentService.search("SV002");
        assertEquals(1, byCode.size());
        assertEquals("Trần Thị B", byCode.get(0).getFullName());

        // Từ khóa rỗng -> trả về tất cả
        List<Student> all = studentService.search("");
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("Kiểm tra lấy sinh viên theo ID - getById(id)")
    void testGetById() {
        Student first = studentRepository.findAll().get(0);
        Student found = studentService.getById(first.getId());
        assertNotNull(found);
        assertEquals(first.getStudentCode(), found.getStudentCode());
    }

    @Test
    @DisplayName("Kiểm tra thêm mới sinh viên - save(student)")
    void testSave() {
        Student newStudent = new Student("SV003", "Lê Hoàng C", "c@gmail.com", "0923456789", "C2024C");
        Student saved = studentService.save(newStudent);
        assertNotNull(saved.getId());
        assertEquals("SV003", saved.getStudentCode());
        assertEquals("Lê Hoàng C", saved.getFullName());
    }

    @Test
    @DisplayName("Kiểm tra xóa sinh viên - delete(id)")
    void testDelete() {
        Student first = studentRepository.findAll().get(0);
        UUID id = first.getId();
        studentService.delete(id);

        assertThrows(RuntimeException.class, () -> {
            studentService.getById(id);
        });
    }
}
