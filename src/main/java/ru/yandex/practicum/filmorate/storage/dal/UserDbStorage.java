package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
public class UserDbStorage extends BaseDbStorage<User> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
    private static final String INSERT_FRIEND_QUERY = "INSERT INTO friends_list(user_id, friend_id, confirmed) VALUES (?, ?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM friends_list WHERE user_id = ? AND friend_id = ?";
    private static final String FIND_FRIENDS_LIST_QUERY = "SELECT friend_id FROM friends_list WHERE user_id = ?";
    private static final String UPDATE_FRIEND_LIST_QUERY = "UPDATE friends_list SET confirmed = ? WHERE user_id = ? AND friend_id = ?";

    public UserDbStorage(JdbcTemplate jdbcTemplate, RowMapper<User> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    public List<User> findAll() {
        List<User> userList = findMany(FIND_ALL_QUERY);
        for (User user : userList) {
            user.setFriends(findFriendsListId(user.getId()));
        }
        return userList;
    }

    public User findById(long userId) {
        Optional<User> optionalUser = findOne(FIND_BY_ID_QUERY, userId);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("Пользователь с ID = " + userId + " не найден"));
        user.setFriends(findFriendsListId(user.getId()));
        return user;
    }

    public User addUser(User user) {
        long id = insert(INSERT_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        user.setId(id);
        return user;
    }

    public User updateUser(User user) {
        findById(user.getId());
        update(UPDATE_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    public void addInFriend(long userId, long friendId) {
        User user  = findById(userId);
        User friend = findById(friendId);
        boolean confirmedFriends = friend.getFriends().contains(user.getId()) && user.getFriends().contains(friend.getId());
        insertData(INSERT_FRIEND_QUERY, userId, friendId, confirmedFriends);
    }

    public void removeFromFriend(long userId, long friendId) {
        findById(userId);
        findById(friendId);
        insertData(DELETE_FRIEND_QUERY, userId, friendId);
        insertData(UPDATE_FRIEND_LIST_QUERY, false, friendId, userId);
    }

    public Set<User> findFriends(long userId) {
        findById(userId);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(FIND_FRIENDS_LIST_QUERY, userId);
        Set<User> friends = new HashSet<>();
        while (sqlRowSet.next()) {
            friends.add(findById(sqlRowSet.getLong("friend_id")));
        }
        return friends;
    }

    public Set<Long> findFriendsListId(long userId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(FIND_FRIENDS_LIST_QUERY, userId);
        Set<Long> friendsId = new HashSet<>();
        while (sqlRowSet.next()) {
            friendsId.add(sqlRowSet.getLong("friend_id"));
        }
        return friendsId;
    }

    public Set<User> findCommonFriends(long userId, long otherId) {
        Set<Long> userFriends = findFriendsListId(userId);
        Set<Long> otherFriends = findFriendsListId(otherId);
        Set<User> commonFriends = new HashSet<>();
        for (Long userFriend : userFriends) {
            for (Long otherFriend : otherFriends) {
                if (Objects.equals(userFriend, otherFriend)) {
                    commonFriends.add(findById(userFriend));
                }
            }
        }
        return commonFriends;
    }
}
