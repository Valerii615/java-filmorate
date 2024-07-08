package ru.yandex.practicum.modelValidator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

public class UserValidatorTests {

    static InMemoryUserStorage userStorage;
    User user;

    /**
     * инициализация userController
     */
    @BeforeAll
    static void preparation() {
        userStorage = new InMemoryUserStorage();
    }

    /**
     * проверка граничных значений: поле "email"
     */
    @Test
    public void checkingTheBoundaryValuesOfTheEmailField() {
        user = User.builder()
                .login("login")
                .name("Имя")
                .birthday(LocalDate.of(1999, 1, 1))
                .build();

        try {
            userStorage.validator(user);
        } catch (Exception e) {
            assertEquals("Электронная почта не может быть пустой и должна содержать символ @", e.getMessage(), "Получено неверное исключение");
        }
        try {
            user.setEmail("       ");
            userStorage.validator(user);
        } catch (Exception e) {
            assertEquals("Электронная почта не может быть пустой и должна содержать символ @", e.getMessage(), "Получено неверное исключение");
        }
        try {
            user.setEmail("eee.yandex.ru");
            userStorage.validator(user);
        } catch (Exception e) {
            assertEquals("Электронная почта не может быть пустой и должна содержать символ @", e.getMessage(), "Получено неверное исключение");
        }
        try {
            user.setEmail("eee@yandex.ru");
            userStorage.validator(user);
        } catch (Exception e) {
            fail("Исключение не должно было появиться");
        }
    }

    /**
     * проверка граничных значений: поле "login"
     */
    @Test
    public void checkingTheBoundaryValuesOfTheLoginField() {
        user = User.builder()
                .email("eee@yandex.ru")
                .name("Имя")
                .birthday(LocalDate.of(1999, 1, 1))
                .build();

        try {
            userStorage.validator(user);
        } catch (Exception e) {
            assertEquals("Логин не может быть пустым и содержать пробелы", e.getMessage(), "Получено неверное исключение");
        }
        try {
            user.setLogin("       ");
            userStorage.validator(user);
        } catch (Exception e) {
            assertEquals("Логин не может быть пустым и содержать пробелы", e.getMessage(), "Получено неверное исключение");
        }
        try {
            user.setLogin("l o g i n");
            userStorage.validator(user);
        } catch (Exception e) {
            assertEquals("Логин не может быть пустым и содержать пробелы", e.getMessage(), "Получено неверное исключение");
        }
        try {
            user.setLogin("login");
        } catch (Exception e) {
            fail("Исключение не должно было появиться");
        }
    }

    /**
     * проверка граничных значений: поле "name"
     */
    @Test
    public void checkingTheBoundaryValuesOfTheNameField() {
        user = User.builder()
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
        user = User.builder()
                .email("eee@yandex.ru")
                .login("login")
                .name("name")
                .build();

        try {
            userStorage.validator(user);
        } catch (Exception e) {
            fail("Исключение не должно было появиться");
        }
        try {
            user.setBirthday(LocalDate.of(1999, 1, 1));
            userStorage.validator(user);
        } catch (Exception e) {
            fail("Исключение не должно было появиться");
        }
        try {
            user.setBirthday(LocalDate.of(9999, 9, 9));
            userStorage.validator(user);
        } catch (Exception e) {
            assertEquals("Дата рождения не может быть в будущем", e.getMessage(), "Получено неверное исключение");
        }
    }
}