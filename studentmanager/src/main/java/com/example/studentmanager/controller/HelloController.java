package com.example.studentmanager.controller;

import com.example.studentmanager.model.Student;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HelloController {
    // hello springframework boot api
    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot API!";
    }
    // bai 2.1
    @GetMapping("/greet")
    public String greet(@RequestParam String name) {
        return "Xin chào " + name;
    }
    // bai 2.2
    @GetMapping("/students/search")
    public String search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page) {
        return "keyword=" + keyword + ", page=" + page;
    }
    // bai 3
    @GetMapping("/students/{id}")
    public String getStudentById(@PathVariable int id) {
        return "Sinh viên có ID = " + id;
    }
    // bai 4
    @GetMapping("/student")
    public Student getStudent() {
        return new Student(1, "Hoang Truong An", 20);
    }
    // bai 5
    @GetMapping("/students")
    public List<Student> getStudents() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "A", 20));
        list.add(new Student(2, "B", 21));
        list.add(new Student(3, "C", 22));
        return list;
    }
}
