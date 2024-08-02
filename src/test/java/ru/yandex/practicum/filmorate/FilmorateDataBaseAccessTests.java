package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.dal.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserDbStorage.class, UserRowMapper.class, UserService.class,
        FilmDbStorage.class, FilmRowMapper.class, GenreDbStorage.class, GenreRowMapper.class, GenreService.class,
        MpaDbStorage.class, MpaRowMapper.class, MpaService.class})
public class FilmorateDataBaseAccessTests {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    Film film1;
    Film film2;
    Film film3;

    User user1;
    User user2;
    User user3;


    @BeforeEach
    void setUp() {
        film1 = Film.builder()
                .name("film_1")
                .description("description_1")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(11)
                .mpa(mpaDbStorage.findById(1))
                .build();

        film2 = Film.builder()
                .name("film_2")
                .description("description_2")
                .releaseDate(LocalDate.of(2002, 2, 2))
                .duration(22)
                .mpa(mpaDbStorage.findById(2))
                .build();

        film3 = Film.builder()
                .name("film_3")
                .description("description_3")
                .releaseDate(LocalDate.of(2003, 3, 3))
                .duration(33)
                .mpa(mpaDbStorage.findById(3))
                .build();

        user1 = User.builder()
                .email("us1@mail.ru")
                .login("user1")
                .name("userName1")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();

        user2 = User.builder()
                .email("us2@mail.ru")
                .login("user2")
                .name("userName2")
                .birthday(LocalDate.of(1992, 2, 2))
                .build();

        user3 = User.builder()
                .email("us3@mail.ru")
                .login("user3")
                .name("userName3")
                .birthday(LocalDate.of(1993, 3, 3))
                .build();
    }


    /**
     * добавления и получения всех фильмов
     */
    @Test
    @DirtiesContext
    public void addingAndReceivingAllMovies() {
        addAllFilm();
        assertEquals(3, filmDbStorage.findAll().size(), "Неверное количество фильмов");
    }

    /**
     * добавления и получения всех пользователей
     */
    @Test
    @DirtiesContext
    public void addingAndReceivingAllUsers() {
        addAllUser();
        assertEquals(3, userDbStorage.findAll().size(), "Неверное количество пользователей");
    }

    /**
     * получение всех рейтингов
     */
    @Test
    @DirtiesContext
    public void gettingAllTheRatings() {
        assertEquals(5, mpaDbStorage.findAll().size());
    }

    /**
     * получение всех жанров
     */
    @Test
    @DirtiesContext
    public void gettingAllTheGenres() {
        assertEquals(6, genreDbStorage.findAll().size());
    }

    /**
     * получение фильмов по id тестирование граничещих значений
     */
    @Test
    @DirtiesContext
    public void getFilmById() {
        addAllFilm();
        assertEquals("film_1", filmDbStorage.findById(1).getName(), "Неверный фильм");
        assertEquals("film_2", filmDbStorage.findById(2).getName(), "Неверный фильм");
        assertEquals("film_3", filmDbStorage.findById(3).getName(), "Неверный фильм");
        try {
            filmDbStorage.findById(4);
        } catch (Exception e) {
            assertEquals("Фильм с ID=4 не найден", e.getMessage(), "Получено неверное исключение");
        }
    }

    /**
     * получение пользователей по id тестирование граничещих значений
     */
    @Test
    @DirtiesContext
    public void getUserById() {
        addAllUser();
        assertEquals("userName1", userDbStorage.findById(1).getName(), "Неверный пользователь");
        assertEquals("userName2", userDbStorage.findById(2).getName(), "Неверный пользователь");
        assertEquals("userName3", userDbStorage.findById(3).getName(), "Неверный пользователь");
        try {
            userDbStorage.findById(4);
        } catch (Exception e) {
            assertEquals("Пользователь с ID = 4 не найден", e.getMessage(), "Получено неверное исключение");
        }
    }

    /**
     * получение рейтингов по id тестирование граничещих значений
     */
    @Test
    @DirtiesContext
    public void getMpaById() {
        assertEquals("G", mpaDbStorage.findById(1).getName(), "Получен неверный рейтинг");
        assertEquals("PG-13", mpaDbStorage.findById(3).getName(), "Получен неверный рейтинг");
        assertEquals("NC-17", mpaDbStorage.findById(5).getName(), "Получен неверный рейтинг");
        try {
            mpaDbStorage.findByIdValid(7);
        } catch (Exception e) {
            assertEquals("Рейтинг с ID=7 не найден", e.getMessage(), "Получено неверное исключение");
        }
    }

    /**
     * получение жанров по id тестирование граничещих значений
     */
    @Test
    @DirtiesContext
    public void getGenreById() {
        assertEquals("Комедия", genreDbStorage.findById(1).getName(), "Получен неверный жанр");
        assertEquals("Триллер", genreDbStorage.findById(4).getName(), "Получен неверный жанр");
        assertEquals("Боевик", genreDbStorage.findById(6).getName(), "Получен неверный жанр");
        try {
            genreDbStorage.findById(7);
        } catch (Exception e) {
            assertEquals("Жанр с ID=7 не найден", e.getMessage(), "Получено неверное исключение");
        }
    }

    /**
     * добавление и удаление лайка фильму
     */
    @Test
    @DirtiesContext
    public void addAndRemoveLike() {
        addAllFilm();
        addAllUser();
        filmDbStorage.addLikeToFilm(1,1);
        assertEquals(1, filmDbStorage.getLikes(1).size(), "Неверное количество лайков");
        filmDbStorage.removeLikeFromFilm(1,1);
        assertEquals(0, filmDbStorage.getLikes(1).size(), "Неверное количество лайков");
    }

    /**
     * добавление у даление друзей
     */
    @Test
    @DirtiesContext
    public void addAndRemoveFriend() {
        addAllUser();
        userDbStorage.addInFriend(1,2);
        assertEquals(1, userDbStorage.findFriendsListId(1).size(), "Неверное количество друзей");
        userDbStorage.removeFromFriend(1,2);
        assertEquals(0, userDbStorage.findFriendsListId(1).size(), "Неверное количество друзей");
    }

    /**
     * добавление пустого email и login в users
     */
    @Test
    @DirtiesContext
    public void addNullEmailAndLoginInUsers() {
        User user = User.builder()
                .email(null)
                .login("user0")
                .name("userName0")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        try {
            userDbStorage.addUser(user);
        } catch (Exception e) {
            assertEquals("PreparedStatementCallback; Значение NULL не разрешено для поля \"EMAIL\"\n" +
                    "NULL not allowed for column \"EMAIL\"; SQL statement:\n" +
                    "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?) [23502-224]",
                    e.getMessage(), "Получено неверное исключение");
        }
        user.setEmail("us0@mail.ru");
        user.setLogin(null);
        try {
            userDbStorage.addUser(user);
        } catch (Exception e) {
            assertEquals("PreparedStatementCallback; Значение NULL не разрешено для поля \"LOGIN\"\n" +
                    "NULL not allowed for column \"LOGIN\"; SQL statement:\n" +
                    "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?) [23502-224]",
                    e.getMessage(), "Получено неверное исключение");
        }
    }

    /**
     * добавление пустого name и description > 200
     */
    @Test
    @DirtiesContext
    public void addNullNameAndLongLengthDescription() {
        Film film = Film.builder()
                .name(null)
                .description("description_0")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(11)
                .mpa(mpaDbStorage.findById(1))
                .build();
        try {
            filmDbStorage.add(film);
        } catch (Exception e) {
            assertEquals("PreparedStatementCallback; Значение NULL не разрешено для поля \"NAME\"\n" +
                    "NULL not allowed for column \"NAME\"; SQL statement:\n" +
                    "INSERT INTO films(name, description, releaseDate, duration, rating) VALUES (?, ?, ?, ?, ?) [23502-224]",
                    e.getMessage(), "Получено неверное исключение");
        }
        film.setName("film0");
        film.setDescription("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                "11111111111");
        try {
            filmDbStorage.add(film);
        } catch (Exception e) {
            assertEquals("PreparedStatementCallback; Значение слишком длинное для поля \"DESCRIPTION CHARACTER VARYING(200)\": \"'1111111111111111111111111111111111111111111111111111111111111111111111111111111... (201)\"\n" +
                            "Value too long for column \"DESCRIPTION CHARACTER VARYING(200)\": \"'1111111111111111111111111111111111111111111111111111111111111111111111111111111... (201)\"; SQL statement:\n" +
                            "INSERT INTO films(name, description, releaseDate, duration, rating) VALUES (?, ?, ?, ?, ?) [22001-224]",
                    e.getMessage(), "Получено неверное исключение");
        }
    }


    public void addAllFilm() {
        filmDbStorage.add(film1);
        filmDbStorage.add(film2);
        filmDbStorage.add(film3);
    }

    public void addAllUser() {
        userDbStorage.addUser(user1);
        userDbStorage.addUser(user2);
        userDbStorage.addUser(user3);
    }
}
