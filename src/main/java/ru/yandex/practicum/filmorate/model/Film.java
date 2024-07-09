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
public class Film {
    private Long id;
    @NotBlank(message = "must not be blank")
    private String name;
    @NotBlank(message = "must not be blank")
    @Size(max = 200)
    private String description;
    @MinimumDate
    private LocalDate releaseDate;
    @PositiveOrZero(message = "must be greater than or equal to 0")
    private int duration;
    private Set<Long> likes;
}