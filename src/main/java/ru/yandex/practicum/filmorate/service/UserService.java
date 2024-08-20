package ru.yandex.practicum.filmorate.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.UserDbStorage;

import java.util.Collection;

@Log4j2
@Service
public class UserService {

    private final UserDbStorage userDbStorage;

    @Autowired
    public UserService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public User addUser(User user) {
        log.debug("Adding user: {}", user);
        return userDbStorage.addUser(user);
    }

    public User updateUser(User newUser) {
        log.debug("Updating user: {}", newUser);
        return userDbStorage.updateUser(newUser);
    }

    public Collection<User> getAllUsers() {
        return userDbStorage.findAll();
    }

    public User getUserOfId(Long id) {
        log.debug("Finding user by id: {}", id);
        return userDbStorage.findById(id);
    }

    public void addInFriends(Long id, Long friendId) {
        log.debug("(add) id={}, friendId={}", id, friendId);
        userDbStorage.addInFriend(id, friendId);
    }

    public void removeFromFriends(Long id, Long friendId) {
        log.debug("(remove) id={}, friendId={}", id, friendId);
        userDbStorage.removeFromFriend(id, friendId);
    }

    public Collection<User> getFriendsList(Long id) {
        log.debug("id={}", id);
        return userDbStorage.findFriends(id);
    }

    public Collection<User> getSharedListOfFriends(Long id, Long otherId) {
        log.debug("id={}, otherId={}", id, otherId);
        return userDbStorage.findCommonFriends(id, otherId);
    }
}