package com.example.schoolmanager.service.impl;

import com.example.schoolmanager.entity.Student;
import com.example.schoolmanager.repository.StudentRepository;
import com.example.schoolmanager.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@SuppressWarnings("null")
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return studentRepository.findAll();
        }
        String value = keyword.trim();
        return studentRepository.findByStudentCodeContainingIgnoreCaseOrFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContainingIgnoreCase(
                value, value, value, value);
    }

    @Override
    @Transactional(readOnly = true)
    public Student getById(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với id: " + id));
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void delete(UUID id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy sinh viên với id: " + id);
        }
        studentRepository.deleteById(id);
    }
}
