package br.edu.atitus.authapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequestDto(@NotBlank @Size(max = 255) String name,
                               @NotBlank @Email @Size(max = 255) String email,
                               @NotBlank @Size(min = 8, max = 72, message = "A senha deve ter entre {min} e {max} caracteres.") String password,
                               @Size(max = 20) String phone) { }
