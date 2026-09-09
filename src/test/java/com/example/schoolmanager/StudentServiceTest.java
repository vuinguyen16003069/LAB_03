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
    @DisplayName("Kiểm tra tìm kiếm sinh viên theo họ tên - search(keyword)")
    void testSearchByName() {
        List<Student> byName = studentService.search("Nguyễn");
        assertEquals(1, byName.size());
        assertEquals("Nguyễn Văn A", byName.get(0).getFullName());
    }

    @Test
    @DisplayName("Kiểm tra tìm kiếm sinh viên theo mã SV - search(keyword)")
    void testSearchByStudentCode() {
        List<Student> byCode = studentService.search("SV002");
        assertEquals(1, byCode.size());
        assertEquals("Trần Thị B", byCode.get(0).getFullName());
    }

    @Test
    @DisplayName("Kiểm tra tìm kiếm sinh viên theo email - search(keyword)")
    void testSearchByEmail() {
        List<Student> byEmail = studentService.search("b@gmail.com");
        assertEquals(1, byEmail.size());
        assertEquals("SV002", byEmail.get(0).getStudentCode());
    }

    @Test
    @DisplayName("Kiểm tra tìm kiếm sinh viên theo số điện thoại - search(keyword)")
    void testSearchByPhone() {
        List<Student> byPhone = studentService.search("0901234567");
        assertEquals(1, byPhone.size());
        assertEquals("SV001", byPhone.get(0).getStudentCode());
    }

    @Test
    @DisplayName("Kiểm tra tìm kiếm với từ khóa rỗng hoặc khoảng trắng -> trả về tất cả")
    void testSearchNullOrWhitespace() {
        assertEquals(2, studentService.search("").size());
        assertEquals(2, studentService.search("   ").size());
        assertEquals(2, studentService.search(null).size());
    }

    @Test
    @DisplayName("Kiểm tra tìm kiếm không tìm thấy kết quả nào -> trả về mảng rỗng")
    void testSearchNotFound() {
        List<Student> notFound = studentService.search("KhongTonTai123");
        assertNotNull(notFound);
        assertTrue(notFound.isEmpty());
    }

    @Test
    @DisplayName("Kiểm tra lấy sinh viên theo ID hợp lệ - getById(id)")
    void testGetById() {
        Student first = studentRepository.findAll().get(0);
        Student found = studentService.getById(first.getId());
        assertNotNull(found);
        assertEquals(first.getStudentCode(), found.getStudentCode());
    }

    @Test
    @DisplayName("Kiểm tra lấy sinh viên theo ID không tồn tại -> ném ngoại lệ")
    void testGetByIdNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.getById(nonExistentId);
        });
        assertTrue(exception.getMessage().contains("Không tìm thấy sinh viên"));
    }

    @Test
    @DisplayName("Kiểm tra thêm mới sinh viên - save(student)")
    void testSaveNewStudent() {
        Student newStudent = new Student("SV003", "Lê Hoàng C", "c@gmail.com", "0923456789", "C2024C");
        Student saved = studentService.save(newStudent);
        assertNotNull(saved.getId());
        assertEquals("SV003", saved.getStudentCode());
        assertEquals("Lê Hoàng C", saved.getFullName());
    }

    @Test
    @DisplayName("Kiểm tra cập nhật sinh viên đã có - save(student)")
    void testUpdateExistingStudent() {
        Student existing = studentRepository.findAll().get(0);
        existing.setFullName("Nguyễn Văn A Đã Sửa");
        existing.setClassName("C2024_NEW");

        Student updated = studentService.save(existing);
        assertEquals(existing.getId(), updated.getId());
        assertEquals("Nguyễn Văn A Đã Sửa", updated.getFullName());
        assertEquals("C2024_NEW", updated.getClassName());
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

    @Test
    @DisplayName("Kiểm tra xóa sinh viên với ID không tồn tại -> ném ngoại lệ")
    void testDeleteNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        assertThrows(RuntimeException.class, () -> {
            studentService.delete(nonExistentId);
        });
    }
}
