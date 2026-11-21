package br.edu.atitus.authapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SigninRequestDto(@NotBlank @Email String email, @NotBlank String password) { }
