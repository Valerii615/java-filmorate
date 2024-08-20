package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("начало обработки эндпоинта получения всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserOfId(@PathVariable Long id) {
        log.info("начало обработки эндпоинта получения пользователя по id={}", id);
        return userService.getUserOfId(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsList(@PathVariable Long id) {
        log.info("начало обработки эндпоинта получения списка друзей id={}", id);
        return userService.getFriendsList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getSharedListOfFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("начало обработки эндпоинта получения списка общих друзей id={}, otherId={}", id, otherId);
        return userService.getSharedListOfFriends(id, otherId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("начало обработки эндпоинта добавления пользователя");
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        log.info("начало обработки эндпоинта обнавления пользователя");
        return userService.updateUser(newUser);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addInFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("начало обработки эндпоинта добавления пользователя в друзья id={}, friendId={}", id, friendId);
        userService.addInFriends(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("начало обработки эндпоинта удаления пользователя из друзей id={}, friendId={}", id, friendId);
        userService.removeFromFriends(id, friendId);
    }
}