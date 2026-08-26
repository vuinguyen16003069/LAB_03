package com.example.schoolmanager.repository;

import com.example.schoolmanager.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    // Bài tập 3: Tìm kiếm sinh viên theo tên (không phân biệt chữ hoa chữ thường)
    List<Student> findByNameContainingIgnoreCase(String keyword);

    // Tìm kiếm theo giới tính
    List<Student> findByGenderIgnoreCase(String gender);
}
