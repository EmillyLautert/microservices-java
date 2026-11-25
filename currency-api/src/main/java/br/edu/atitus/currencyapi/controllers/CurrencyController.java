package br.edu.atitus.currencyapi.controllers;

import br.edu.atitus.currencyapi.dtos.CurrencyResponse;
import br.edu.atitus.currencyapi.services.CurrencyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Locale;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {
    private final CurrencyService service;
    public CurrencyController(CurrencyService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<CurrencyResponse> getCurrency(@RequestParam String source, @RequestParam String target) {
        String normalizedSource = normalizeCurrency(source, "source");
        String normalizedTarget = normalizeCurrency(target, "target");
        return ResponseEntity.ok(service.findBySourceCurrencyAndTargetCurrency(normalizedSource, normalizedTarget));
    }

    private String normalizeCurrency(String currency, String field) {
        if (currency == null || !currency.trim().matches("(?i)[a-z]{3}"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O parâmetro " + field + " deve conter um código de moeda com 3 letras");
        return currency.trim().toUpperCase(Locale.ROOT);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> responseStatus(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getReason());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> notFound(EntityNotFoundException ignored) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cotação não encontrada para as moedas informadas");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> missingParameter(MissingServletRequestParameterException ignored) {
        return ResponseEntity.badRequest().body("Informe as moedas de origem e destino.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> invalidParameter(MethodArgumentTypeMismatchException ignored) {
        return ResponseEntity.badRequest().body("Parâmetro inválido.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> unexpected(Exception ignored) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível consultar a cotação");
    }
}
