package com.example.schoolmanager.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "student_code")
    private String studentCode;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "class_name")
    private String className;

    public Student() {
    }

    public Student(String studentCode, String fullName, String email, String phone, String className) {
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.className = className;
    }

    public Student(UUID id, String studentCode, String fullName, String email, String phone, String className) {
        this.id = id;
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.className = className;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return fullName;
    }

    public void setName(String name) {
        this.fullName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", studentCode='" + studentCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
