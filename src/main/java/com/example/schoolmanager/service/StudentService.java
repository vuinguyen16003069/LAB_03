package com.example.schoolmanager.service;

import com.example.schoolmanager.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    List<Student> getAllStudents();

    Optional<Student> getStudentById(Integer id);

    Student saveStudent(Student student);

    Student updateStudent(Integer id, Student student);

    void deleteStudent(Integer id);

    List<Student> searchStudents(String keyword);
}
