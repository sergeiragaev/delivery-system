package ru.skillbox.authservice.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class UserDto {

    @NotBlank
    private String name;

    @Size(min = 5, max = 100, message = "Password must be between 5 and 100 characters")
    private String password;
}
