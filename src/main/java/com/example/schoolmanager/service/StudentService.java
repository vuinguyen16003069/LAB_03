package com.example.schoolmanager.service;

import com.example.schoolmanager.entity.Student;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    List<Student> getAll();

    List<Student> search(String keyword);

    Student getById(@NonNull UUID id);

    Student save(@NonNull Student student);

    void delete(@NonNull UUID id);
}
