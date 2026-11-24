package br.edu.atitus.productapi.controllers;

import br.edu.atitus.productapi.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final ProductService productService;

    public CategoryController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<String> getCategories() {
        return productService.findCategories();
    }
}
