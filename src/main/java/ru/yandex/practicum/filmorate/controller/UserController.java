package ru.yandex.practicum.filmorate.controller;

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

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserOfId(@PathVariable Long id) {
        return userService.getUserOfId(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsList(@PathVariable Long id) {
        return userService.getFriendsList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getSharedListOfFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getSharedListOfFriends(id, otherId);
    }

    @PostMapping
    public User addingUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updatingUser(@RequestBody User newUser) {
        return userService.updateUser(newUser);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addInFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addInFriends(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFromFriends(id, friendId);
    }
}