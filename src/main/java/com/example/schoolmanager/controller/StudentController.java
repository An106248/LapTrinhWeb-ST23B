package com.example.schoolmanager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.schoolmanager.model.Student;
import com.example.schoolmanager.service.StudentService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ================== WEB (Thymeleaf) ==================

    @GetMapping
    public String list(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        List<Student> students = studentService.search(keyword);
        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        return "students/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("student", new Student());
        return "/students/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        return "students/form";
    }

    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("student") Student student,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "students/form";
        }

        Student saved = studentService.save(student);
        redirectAttributes.addFlashAttribute("newStudent", saved);
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        studentService.findById(id).ifPresent(s -> studentService.delete(id));
        return "redirect:/students";
    }

    // ================== REST API ==================

    @GetMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Student>> apiList(
            @RequestParam(required = false) String keyword) {

        return ResponseEntity.ok(studentService.search(keyword));
    }

    @GetMapping(value = "/api/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Student> apiGetById(@PathVariable Integer id) {

        return studentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Student> apiCreate(@Valid @RequestBody Student student) {

        Student saved = studentService.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping(value = "/api/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Student> apiUpdate(
            @PathVariable Integer id,
            @Valid @RequestBody Student student) {

        if (studentService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        student.setId(id);
        return ResponseEntity.ok(studentService.save(student));
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> apiDelete(@PathVariable Integer id) {

        if (studentService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ================== VALIDATION HANDLER ==================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (a, b) -> a));

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Dữ liệu không hợp lệ");
        body.put("errors", errors);

        return ResponseEntity.badRequest().body(body);
    }
}
