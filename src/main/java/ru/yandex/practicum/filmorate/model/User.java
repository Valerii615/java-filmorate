package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @NotContainSpace
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}