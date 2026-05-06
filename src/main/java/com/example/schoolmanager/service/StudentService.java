package com.example.schoolmanager.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.schoolmanager.model.Student;
import com.example.schoolmanager.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Student save(Student student) {
        return repository.save(student);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Optional<Student> findById(int id) {
        return repository.findById(id);
    }

    /** Dùng cho controller Thymeleaf (cần Student hoặc null). */
    public Student getStudentById(int id) {
        return findById(id).orElse(null);
    }

    public List<Student> search(String keyword) {
        if (keyword == null || (keyword = keyword.trim()).isEmpty()) {
            return repository.findAll();
        }
        if (keyword.matches("\\d+")) {
            try {
                int id = Integer.parseInt(keyword);
                return findById(id).map(List::of).orElse(Collections.emptyList());
            } catch (NumberFormatException e) {
                return repository.findAll();
            }
        }
        String pattern = "%" + keyword + "%";
        return repository.findByNameContainingIgnoreCase(pattern);
    }
}
