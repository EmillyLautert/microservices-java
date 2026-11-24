package br.edu.atitus.authapi.dtos;

import br.edu.atitus.authapi.entities.UserEntity;
import br.edu.atitus.authapi.entities.UserType;

public record UserResponseDto(Long id, String name, String email, String phone, UserType type) {
    public static UserResponseDto fromEntity(UserEntity user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getType());
    }
}
