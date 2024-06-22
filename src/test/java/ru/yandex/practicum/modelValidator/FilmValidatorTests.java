package ru.yandex.practicum.modelValidator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidatorTests {
    static FilmController filmController;
    static Film film;

    /**
     * инициализация filmController
     */
    @BeforeAll
    static void preparation() {
        filmController = new FilmController();
    }

    /**
     * проверка граничных значений: поле "name"
     */
    @Test
    public void checkingTheBoundaryValuesOfTheNameField() {
        film = Film.builder()
                .description("описание")
                .releaseDate(LocalDate.of(2024, 6, 22))
                .duration(30)
                .build();

        try {
            filmController.validator(film);
        } catch (Exception e) {
            assertEquals("Название не может быть пустым", e.getMessage(), "Получено неверное исключение");
        }
        try {
            film.setName("        ");
            filmController.validator(film);
        } catch (Exception e) {
            assertEquals("Название не может быть пустым", e.getMessage(), "Получено неверное исключение");
        }
        try {
            film.setName("Имя");
            filmController.validator(film);
        } catch (Exception e) {
            fail("Исключение не должно было появиться");
        }
    }

    /**
     * проверка граничных значений: поле "description"
     */
    @Test
    public void checkingTheBoundaryValuesOfTheDescriptionField() {
        film = Film.builder()
                .name("Имя")
                .releaseDate(LocalDate.of(2024, 6, 22))
                .duration(30)
                .build();

        try {
            filmController.validator(film);
        } catch (Exception e) {
            fail("Исключение не должно было появиться");
        }
        try {
            film.setDescription("Описание");
            filmController.validator(film);
        } catch (Exception e) {
            fail("Исключение не должно было появиться");
        }
        try {
            film.setDescription("Очень длинное описание  " +
                    "11111111111111111111111111111111111111111111111111111111111" +
                    "11111111111111111111111111111111111111111111111111111111111" +
                    "11111111111111111111111111111111111111111111111111111111111");
            filmController.validator(film);
        } catch (Exception e) {
            assertEquals("Длина описания не должна превышать — 200 символов", e.getMessage(), "Получено неверное исключение");
        }
    }

    /**
     * проверка граничных значений: поле "releaseDate"
     */
    @Test
    public void checkingTheBoundaryValuesOfTheReleaseDateField() {
        film = Film.builder()
                .name("Имя")
                .description("Описание")
                .duration(30)
                .build();

        try {
            filmController.validator(film);
        } catch (Exception e) {
            fail("Исключение не должно было появиться");
        }
        try {
            film.setReleaseDate(LocalDate.of(2024, 6, 22));
            filmController.validator(film);
        } catch (Exception e) {
            fail("Исключение не должно было появиться");
        }
        try {
            film.setReleaseDate(LocalDate.of(1, 1, 1));
            filmController.validator(film);
        } catch (Exception e) {
            assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года", e.getMessage(), "Получено неверное исключение");
        }
    }

    /**
     * проверка граничных значений: поле "duration"
     */
    @Test
    public void checkingTheBoundaryValuesOfTheDurationField() {
        film = Film.builder()
                .name("Имя")
                .description("Описание")
                .releaseDate(LocalDate.of(2024, 6, 22))
                .build();
        try {
            filmController.validator(film);
        } catch (Exception e) {
            assertEquals("Продолжительность фильма должна быть положительным числом", e.getMessage(), "Получено неверное исключение");
        }
        try {
            film.setDuration(-30);
            filmController.validator(film);
        } catch (Exception e) {
            assertEquals("Продолжительность фильма должна быть положительным числом", e.getMessage(), "Получено неверное исключение");
        }
        try {
            film.setDuration(30);
            filmController.validator(film);
        } catch (Exception e) {
            fail("Исключение не должно было появиться");
        }
    }
}