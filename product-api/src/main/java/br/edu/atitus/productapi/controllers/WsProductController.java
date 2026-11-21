package br.edu.atitus.productapi.controllers;

import br.edu.atitus.productapi.dtos.ProductRequest;
import br.edu.atitus.productapi.dtos.ProductResponse;
import br.edu.atitus.productapi.services.ProductService;
import br.edu.atitus.productapi.servicesimpl.ImageStorageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ws/products")
public class WsProductController {
    private final ProductService service;
    private final ImageStorageService imageStorage;
    public WsProductController(ProductService service, ImageStorageService imageStorage) {
        this.service = service; this.imageStorage = imageStorage;
    }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductRequest request) { return service.create(request); }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) { return service.update(id, request); }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductResponse uploadImage(@PathVariable Long id, @RequestPart("file") MultipartFile file) {
        return service.updateImage(id, imageStorage.store(file));
    }
}
