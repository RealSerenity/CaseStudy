package com.rserenity.categoryservice.rest;

import com.rserenity.categoryservice.business.dto.CategoryDto;
import com.rserenity.categoryservice.business.services.CategoryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:8060")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServices categoryServices;

    @GetMapping("/getAllCategories")
    public List<CategoryDto> getAllCategories() {
        return categoryServices.getAll();
    }

    @PostMapping("/createCategory")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto dto){
        return ResponseEntity.ok(categoryServices.create(dto));
    }

    @DeleteMapping("/deleteCategory")
    public ResponseEntity<Map<String, Boolean>> deleteCategory(@RequestParam Long categoryId){
        return ResponseEntity.ok(categoryServices.delete(categoryId));
    }

    @PutMapping("/updateCategory")
    public ResponseEntity<CategoryDto> updateCategory(@RequestParam Long categoryId, @RequestBody CategoryDto dto){
        return ResponseEntity.ok(categoryServices.updateById(categoryId, dto));
    }

    @GetMapping("/getCategoryById")
    public ResponseEntity<CategoryDto> getCategoryById(@RequestParam Long categoryId) {
        return ResponseEntity.ok(categoryServices.getById(categoryId));
    }

    @GetMapping("/getCategoryCodeById")
    public String getCategoryCodeById(@RequestParam(value = "categoryId") Long categoryId) {
        return categoryServices.getById(categoryId).getCategoryCode();
    }
}