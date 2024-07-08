package ru.yandex.practicum.filmorate.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Log4j2
@Service
public class UserService {

    UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User newUser) {
        return userStorage.updateUser(newUser);
    }

    public void removeUser(Long id) {
        userStorage.removeUser(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserOfId(Long id) {
        return userStorage.getUserOfId(id);
    }

    public void addInFriends(Long id, Long friendId) {
        log.debug("(add) id={}, friendId={}", id, friendId);
        User user = userStorage.getUserOfId(id);
        User userFr = userStorage.getUserOfId(friendId);
        user.getFriends().add(friendId);
        userFr.getFriends().add(id);
    }

    public void removeFromFriends(Long id, Long friendId) {
        log.debug("(remove) id={}, friendId={}", id, friendId);
        User user = userStorage.getUserOfId(id);
        User userFr = userStorage.getUserOfId(friendId);
        user.getFriends().remove(friendId);
        userFr.getFriends().remove(id);
    }

    public Collection<User> getFriendsList(Long id) {
        log.debug("id={}", id);
        Collection<User> friendsList = new ArrayList<>();
        User user = userStorage.getUserOfId(id);
        if (user.getFriends() == null) {
            throw new NullPointerException("список друзей пуст");
        }
        for (Long idFriend : user.getFriends()) {
            friendsList.add(userStorage.getUserOfId(idFriend));
        }
        return friendsList;
    }

    public Collection<User> getSharedListOfFriends(Long id, Long otherId) {
        log.debug("id={}, otherId={}", id, otherId);
        Collection<User> sharedFriendsList = new ArrayList<>();
        for (Long idFriends : userStorage.getUserOfId(id).getFriends()) {
            if (userStorage.getUserOfId(otherId).getFriends().contains(idFriends)) {
                sharedFriendsList.add(userStorage.getUserOfId(idFriends));
            }
        }
        return sharedFriendsList;
    }
}