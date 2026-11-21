package br.edu.atitus.productapi.controllers;

import br.edu.atitus.productapi.dtos.ProductResponse;
import br.edu.atitus.productapi.entities.ProductType;
import br.edu.atitus.productapi.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }

    @GetMapping
    public Page<ProductResponse> getAll(@RequestParam(required = false) String category,
                                        @RequestParam(required = false) ProductType type,
                                        @RequestParam(required = false) String search,
                                        @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        return service.findAll(category, type, search, pageable);
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id) { return service.findById(id); }
}
