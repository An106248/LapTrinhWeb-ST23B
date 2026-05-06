package com.example.schoolmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.schoolmanager.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    /** Tìm tên chứa chuỗi (pattern dạng %g% do service truyền vào) — tránh lỗi CONCAT/LIKE theo từng DB. */
    @Query("SELECT s FROM Student s WHERE LOWER(s.name) LIKE LOWER(:pattern)")
    List<Student> findByNameContainingIgnoreCase(@Param("pattern") String pattern);
}
