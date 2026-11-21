package br.edu.atitus.authapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequestDto(@NotBlank String name, @NotBlank @Email String email,
                               @NotBlank @Size(min = 8, max = 72) String password) { }
