package ru.yandex.practicum.modelValidator;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FilmValidatorTests {
    static InMemoryFilmStorage filmStorage;
    static Film film;
    static Validator validator;

    /**
     * инициализация filmController
     */
    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        filmStorage = new InMemoryFilmStorage();
    }

    /**
     * проверка граничных значений: поле "name"
     */
    @Test
    public void checkingTheBoundaryValuesOfTheNameField() {
        film = Film.builder()
                .name(null)
                .description("описание")
                .releaseDate(LocalDate.of(2024, 6, 22))
                .duration(30)
                .build();

        List<ConstraintViolation> violationList = new ArrayList<>(validator.validate(film));
        assertEquals(1, violationList.size(), "неверное количество исключений");
        assertEquals("не должно быть пустым", violationList.getFirst().getMessage(), "получено неверное исключение");

        film.setName("   ");
        List<ConstraintViolation> violationList1 = new ArrayList<>(validator.validate(film));
        assertEquals(1, violationList1.size(), "неверное количество исключений");
        assertEquals("не должно быть пустым", violationList1.getFirst().getMessage(), "получено неверное исключение");

        film.setName("Имя");
        List<ConstraintViolation> violationList2 = new ArrayList<>(validator.validate(film));
        assertEquals(0, violationList2.size(), "неверное количество исключений");
    }

    /**
     * проверка граничных значений: поле "description"
     */
    @Test
    public void checkingTheBoundaryValuesOfTheDescriptionField() {
        film = Film.builder()
                .name("Имя")
                .description(null)
                .releaseDate(LocalDate.of(2024, 6, 22))
                .duration(30)
                .build();

        List<ConstraintViolation> violationList = new ArrayList<>(validator.validate(film));
        assertEquals(1, violationList.size(), "неверное количество исключений");
        assertEquals("не должно быть пустым", violationList.getFirst().getMessage(), "получено неверное исключение");

        film.setDescription("Очень длинное описание  " +
                "11111111111111111111111111111111111111111111111111111111111" +
                "11111111111111111111111111111111111111111111111111111111111" +
                "11111111111111111111111111111111111111111111111111111111111");
        List<ConstraintViolation> violationList1 = new ArrayList<>(validator.validate(film));
        assertEquals("размер должен находиться в диапазоне от 0 до 200", violationList1.getFirst().getMessage(), "получено неверное исключение");

        film.setDescription("обычное описание");
        List<ConstraintViolation> violationList2 = new ArrayList<>(validator.validate(film));
        assertEquals(0, violationList2.size(), "неверное количество исключений");
    }

    /**
     * проверка граничных значений: поле "releaseDate"
     */
    @Test
    public void checkingTheBoundaryValuesOfTheReleaseDateField() {
        film = Film.builder()
                .name("Имя")
                .description("Описание")
                .releaseDate(null)
                .duration(30)
                .build();

        List<ConstraintViolation> violationList = new ArrayList<>(validator.validate(film));
        assertEquals(0, violationList.size(), "неверное количество исключений");

        film.setReleaseDate(LocalDate.of(2024, 6, 22));
        List<ConstraintViolation> violationList1 = new ArrayList<>(validator.validate(film));
        assertEquals(0, violationList1.size(), "неверное количество исключений");

        film.setReleaseDate(LocalDate.of(1, 1, 1));
        List<ConstraintViolation> violationList3 = new ArrayList<>(validator.validate(film));
        assertEquals("Date must not be before 1895-12-28", violationList3.getFirst().getMessage(), "получено неверное исключение");
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
                .duration(0)
                .build();

        List<ConstraintViolation> violationList = new ArrayList<>(validator.validate(film));
        assertEquals(0, violationList.size(), "неверное количество исключений");

        film.setDuration(-30);
        List<ConstraintViolation> violationList2 = new ArrayList<>(validator.validate(film));
        assertEquals("должно быть больше или равно 0", violationList2.getFirst().getMessage(), "получено неверное исключение");

        film.setDuration(30);
        List<ConstraintViolation> violationList3 = new ArrayList<>(validator.validate(film));
        assertEquals(0, violationList3.size(), "неверное количество исключений");
    }
}