package com.oktech.boasaude.controller;

import com.oktech.boasaude.entity.Category;
import com.oktech.boasaude.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @PostMapping
    public Category salvar(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @GetMapping
    public List<Category> listar() {
        return categoryRepository.findAll();
    }
}

