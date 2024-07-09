package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    @NotBlank(message = "must not be blank")
    @Email
    private String email;
    @NotBlank(message = "must not be blank")
    @NotContainSpace
    private String login;
    private String name;
    @Past(message = "must be a past date")
    private LocalDate birthday;
    private Set<Long> friends;
}