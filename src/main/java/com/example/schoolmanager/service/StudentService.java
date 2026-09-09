package com.example.schoolmanager.service;

import com.example.schoolmanager.entity.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    List<Student> getAll();

    List<Student> search(String keyword);

    Student getById(UUID id);

    Student save(Student student);

    void delete(UUID id);
}
