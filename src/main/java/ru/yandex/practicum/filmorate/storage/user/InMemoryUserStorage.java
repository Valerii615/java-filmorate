package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        log.info("Начало обрадотки эндпоинта добавления пользователя в память приложения /users metod: Post");
        validator(user);
        user.setId(getNextId());
        log.info("Объекту user.name = \"" + user.getName() + "\" присвоен id = " + user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь id = " + user.getId() + " добавлен в память приложения");
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        log.info("Начало обрадотки эндпоинта обновления данных пользователя /users metod: Put");
        if (newUser.getId() == null) {
            log.error("Id должен быть указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            validator(newUser);
            User oldUser = users.get(newUser.getId());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Пользователь id = " + newUser.getId() + " обновлен " + oldUser);
            return oldUser;
        }
        log.error("Пользователь с id = " + newUser.getId() + " не найден");
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    @Override
    public void removeUser(Long id) {
        getUserOfId(id);
        users.remove(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserOfId(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("пользователя с id=" + id + " не найдено");
        }
        return users.get(id);
    }

    public void validator(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains(String.valueOf('@'))) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(String.valueOf(' '))) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday() != null) {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Дата рождения не может быть в будущем");
                throw new ValidationException("Дата рождения не может быть в будущем");
            }
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Так как имя не было указано, ему присвоено значение поля login = " + user.getLogin());
        }
        log.info("Валидация прошла успешно");

    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
