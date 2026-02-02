package com.example.schoolmanager.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.schoolmanager.model.Student;
import com.example.schoolmanager.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Student save(Student student) {
        return repository.save(student);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Student getStudentById(int id) {
        return repository.findById(id).orElse(null);
    }


    public List<Student> search(String keyword) {
        if (keyword == null || (keyword = keyword.trim()).isEmpty()) {
            return repository.findAll();
        }

        // Tìm theo ID nếu nhập toàn số
        if (keyword.matches("\\d+")) {
            int id = Integer.parseInt(keyword);
            Student s = repository.findById(id).orElse(null);
            return (s != null) ? List.of(s) : Collections.emptyList();
        }

        String pattern = "%" + keyword + "%";
        return repository.findByNameContainingIgnoreCase(pattern);
    }
}
