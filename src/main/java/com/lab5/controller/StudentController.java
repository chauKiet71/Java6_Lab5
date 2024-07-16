package com.lab5.controller;

import com.lab5.dao.StudentDao;
import com.lab5.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin({"*"})
@RestController
public class StudentController {

    @Autowired
    StudentDao dao;

    @GetMapping("/rest/student")
    public List<Student> getStudents() {
        return dao.findAll();
    }

    @GetMapping("/rest/student/{email}")
    public Student getStudent(@PathVariable String email) {
        return dao.findById(email).get();
    }

    @PostMapping("/rest/student")
    public Student addStudent(@RequestBody Student student) {
        return dao.save(student);
    }

    @PutMapping("/rest/student/{email}")
    public Student updateStudent(@PathVariable String email, @RequestBody Student student) {
        dao.save(student);
        return student;
    }

    @DeleteMapping("/rest/student/{email}")
    public void deleteStudent(@PathVariable String email) {
        dao.deleteById(email);
    }
}
