package com.lab5.controller;

import com.lab5.dao.CategoryDao;
import com.lab5.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/categories")
public class CategoryRestController {

    @Autowired
    CategoryDao dao;

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(dao.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Category> get(@PathVariable("id") String id) {
        if (!dao.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dao.findById(id).get());
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        if (dao.existsById(category.getId())) {
            return ResponseEntity.badRequest().build();
        }
        dao.save(category);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable("id") String id, @RequestBody Category category) {
        if (!dao.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        dao.save(category);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        if (!dao.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        dao.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
