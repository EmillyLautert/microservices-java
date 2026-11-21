package br.edu.atitus.productapi.controllers;

import br.edu.atitus.productapi.dtos.DrawnBookResponse;
import br.edu.atitus.productapi.services.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/internal/kits")
public class InternalKitController {
    private final ProductService service;
    public InternalKitController(ProductService service) { this.service = service; }

    @GetMapping("/{kitId}/draw")
    public List<DrawnBookResponse> draw(@PathVariable Long kitId, @RequestParam int quantity) {
        return service.findKitBooks(kitId, quantity).stream()
                .map(book -> new DrawnBookResponse(book.getTitle(), book.getAuthor(), book.getImage())).toList();
    }
}
