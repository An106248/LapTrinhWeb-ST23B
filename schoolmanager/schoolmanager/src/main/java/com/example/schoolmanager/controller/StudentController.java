package com.example.schoolmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.schoolmanager.model.Student;
import com.example.schoolmanager.service.StudentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Danh sách + tìm kiếm
    @GetMapping
    public String list(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        List<Student> students = studentService.search(keyword);

        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);

        return "students/list";
    }

    // Form thêm
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("student", new Student());
        return "students/form";
    }

    // Form sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "students/form";
    }

    // Lưu
    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("student") Student student,
            BindingResult result) {

        if (result.hasErrors()) {
            return "students/form";
        }

        studentService.save(student);
        return "redirect:/students";
    }

    // Xóa
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        studentService.delete(id);
        return "redirect:/students";
    }
}
