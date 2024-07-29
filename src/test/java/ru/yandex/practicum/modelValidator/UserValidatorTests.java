package ru.yandex.practicum.modelValidator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserValidatorTests {

    static InMemoryUserStorage userStorage;
    static Validator validator;


    /**
     * инициализация userController
     */
    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        userStorage = new InMemoryUserStorage();
    }

    /**
     * проверка граничных значений: поле "email"
     */
    @Test
    public void checkingTheBoundaryValuesOfTheEmailField() {
        User user = User.builder()
                .login("login")
                .name("Имя")
                .birthday(LocalDate.of(1999, 1, 1))
                .build();

        List<ConstraintViolation> violationList = new ArrayList<>(validator.validate(user));
        assertEquals(1, violationList.size(), "неверное количество исключений");
        assertEquals("must not be blank", violationList.getFirst().getMessage(), "получено неверное исключение");

        user.setEmail("sdb233");
        List<ConstraintViolation> violationList2 = new ArrayList<>(validator.validate(user));
        assertEquals("must be a well-formed email address", violationList2.getFirst().getMessage(), "получено неверное исключение");

        user.setEmail("eee@yandex.ru");
        List<ConstraintViolation> violationList3 = new ArrayList<>(validator.validate(user));
        assertEquals(0, violationList3.size(), "неверное количество исключений");
    }

    /**
     * проверка граничных значений: поле "login"
     */
    @Test
    public void checkingTheBoundaryValuesOfTheLoginField() {
        User user = User.builder()
                .email("eee@yandex.ru")
                .name("Имя")
                .birthday(LocalDate.of(1999, 1, 1))
                .build();

        List<ConstraintViolation> violationList = new ArrayList<>(validator.validate(user));
        assertEquals(1, violationList.size(), "неверное количество исключений");
        assertEquals("must not be blank", violationList.getFirst().getMessage(), "получено неверное исключение");

        user.setLogin("login");
        List<ConstraintViolation> violationList2 = new ArrayList<>(validator.validate(user));
        assertEquals(0, violationList2.size(), "неверное количество исключений");

        user.setLogin("l o g i n");
        List<ConstraintViolation> violationList3 = new ArrayList<>(validator.validate(user));
        assertEquals("the value must not contain spaces", violationList3.getFirst().getMessage(), "получено неверное исключение");


    }

    /**
     * проверка граничных значений: поле "name"
     */
    @Test
    public void checkingTheBoundaryValuesOfTheNameField() {
        User user = User.builder()
                .email("eee.@yandex.ru")
                .login("login")
                .birthday(LocalDate.of(1999, 1, 1))
                .build();

        userStorage.validator(user);
        assertEquals(user.getLogin(), user.getName(), "При name == null, ему должно присваиваться значение login");
    }

    /**
     * проверка граничных значений: поле "birthday"
     */
    @Test
    public void checkingTheBoundaryValuesOfTheBirthdayField() {
        User user = User.builder()
                .email("eee@yandex.ru")
                .login("login")
                .name("name")
                .build();

        List<ConstraintViolation> violationList = new ArrayList<>(validator.validate(user));
        assertEquals(0, violationList.size(), "неверное количество исключений");

        user.setBirthday(LocalDate.of(1999, 1, 1));
        List<ConstraintViolation> violationList2 = new ArrayList<>(validator.validate(user));
        assertEquals(0, violationList2.size(), "неверное количество исключений");

        user.setBirthday(LocalDate.of(9999, 9, 9));
        List<ConstraintViolation> violationList3 = new ArrayList<>(validator.validate(user));
        assertEquals("must be a past date", violationList3.getFirst().getMessage(), "получено неверное исключение");
    }
}