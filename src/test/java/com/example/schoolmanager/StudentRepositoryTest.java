package com.example.schoolmanager;

import com.example.schoolmanager.entity.Student;
import com.example.schoolmanager.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        studentRepository.save(new Student("SV007", "Đặng Nhật Anh", "g@gmail.com", "0966789012", "C2024A"));
        studentRepository.save(new Student("SV004", "Phạm Minh Dũng", "d@gmail.com", "0933456789", "C2024A"));
        studentRepository.save(new Student("SV005", "Hoàng Thị Em", "e@gmail.com", "0944567890", "C2024B"));
    }

    @Test
    @DisplayName("JPA: Tìm kiếm không phân biệt chữ hoa/thường theo Mã Sinh Viên")
    void testFindByStudentCodeCaseInsensitive() {
        List<Student> result = studentRepository
                .findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                        "sv007", "sv007", "sv007", "sv007");
        assertEquals(1, result.size());
        assertEquals("SV007", result.get(0).getStudentCode());
        assertEquals("Đặng Nhật Anh", result.get(0).getFullName());
    }

    @Test
    @DisplayName("JPA: Tìm kiếm theo Họ và Tên có dấu tiếng Việt")
    void testFindByFullNameVietnamese() {
        List<Student> result = studentRepository
                .findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                        "Minh Dũng", "Minh Dũng", "Minh Dũng", "Minh Dũng");
        assertEquals(1, result.size());
        assertEquals("SV004", result.get(0).getStudentCode());
    }

    @Test
    @DisplayName("JPA: Tìm kiếm theo phần đuôi Email")
    void testFindByEmailDomain() {
        List<Student> result = studentRepository
                .findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                        "gmail.com", "gmail.com", "gmail.com", "gmail.com");
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("JPA: Tìm kiếm theo Số Điện Thoại")
    void testFindByPhoneNumber() {
        List<Student> result = studentRepository
                .findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                        "0944", "0944", "0944", "0944");
        assertEquals(1, result.size());
        assertEquals("SV005", result.get(0).getStudentCode());
    }

    @Test
    @DisplayName("JPA: Tìm kiếm chuỗi không khớp bất kỳ trường nào -> trả về mảng rỗng")
    void testFindNoMatch() {
        List<Student> result = studentRepository
                .findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                        "XYZ999", "XYZ999", "XYZ999", "XYZ999");
        assertTrue(result.isEmpty());
    }
}
