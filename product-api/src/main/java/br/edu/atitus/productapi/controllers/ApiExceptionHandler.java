package br.edu.atitus.productapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> status(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getReason());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validation(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(exception.getBindingResult().getFieldErrors().stream()
                .findFirst().map(error -> error.getDefaultMessage()).orElse("Dados inválidos"));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> unreadableRequest(HttpMessageNotReadableException ignored) {
        return ResponseEntity.badRequest().body("Corpo da requisição inválido. Verifique os dados enviados.");
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> missingParameter(MissingServletRequestParameterException ignored) {
        return ResponseEntity.badRequest().body("Parâmetro obrigatório ausente.");
    }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> uploadTooLarge(MaxUploadSizeExceededException ignored) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("A imagem deve ter no máximo 5 MB");
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> invalidParameter(MethodArgumentTypeMismatchException ignored) {
        return ResponseEntity.badRequest().body("Parâmetro inválido");
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> unexpected(Exception ignored) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível concluir a solicitação");
    }
}
