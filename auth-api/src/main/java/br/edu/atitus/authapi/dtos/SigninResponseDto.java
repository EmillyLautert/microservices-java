package br.edu.atitus.authapi.dtos;

public record SigninResponseDto(UserResponseDto user, String token) { }
