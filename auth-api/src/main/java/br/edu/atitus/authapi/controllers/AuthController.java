package br.edu.atitus.authapi.controllers;

import br.edu.atitus.authapi.components.JwtUtil;
import br.edu.atitus.authapi.dtos.SigninRequestDto;
import br.edu.atitus.authapi.dtos.SigninResponseDto;
import br.edu.atitus.authapi.dtos.SignupRequestDto;
import br.edu.atitus.authapi.dtos.UserResponseDto;
import br.edu.atitus.authapi.entities.UserEntity;
import br.edu.atitus.authapi.entities.UserType;
import br.edu.atitus.authapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationConfiguration authConfig;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, AuthenticationConfiguration authConfig, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authConfig = authConfig;
        this.jwtUtil = jwtUtil;
    }

    private UserEntity convertDTO2Entity(SignupRequestDto dto) {
        UserEntity user = new UserEntity();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        return user;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody SignupRequestDto dto) throws Exception {
        UserEntity user = convertDTO2Entity(dto);
        user.setType(UserType.Common);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDto.fromEntity(userService.save(user)));
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponseDto> signin(@Valid @RequestBody SigninRequestDto signin) throws Exception {
        AuthenticationManager manager = authConfig.getAuthenticationManager();
        String email = signin.email().trim().toLowerCase();
        manager.authenticate(new UsernamePasswordAuthenticationToken(email, signin.password()));
        UserEntity user = (UserEntity) userService.loadUserByUsername(email);
        return ResponseEntity.ok(new SigninResponseDto(UserResponseDto.fromEntity(user), jwtUtil.generateToken(user)));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> statusException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getReason());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> authenticationException(AuthenticationException ignored) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream().findFirst()
                .map(error -> error.getDefaultMessage()).orElse("Dados inválidos");
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> conflict(DataIntegrityViolationException ignored) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe usuário com este e-mail");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> unexpected(Exception ignored) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível concluir a solicitação");
    }
}
