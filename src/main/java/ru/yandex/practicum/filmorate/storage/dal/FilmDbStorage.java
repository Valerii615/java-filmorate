package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.*;

@Repository
public class FilmDbStorage extends BaseDbStorage<Film> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, releaseDate, duration, rating) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, rating = ? WHERE film_id = ?";
    private static final String INSERT_GENRE_QUERY = "INSERT INTO genre_list(film_id, genre_id) VALUES (?, ?)";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO likes_list(film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes_list WHERE film_id = ? AND user_id = ?";
    private static final String FIND_TOP_FILMS_QUERY = "SELECT f.FILM_ID FROM likes_list ll LEFT JOIN films f on F.film_id = ll.film_id group by ll.film_id ORDER BY COUNT(user_id)DESC LIMIT ?";
    private static final String FIND_LIKES_FROM_FILM_QUERY = "SELECT USER_ID FROM LIKES_LIST WHERE FILM_ID = ?";

    MpaService mpaService;
    GenreService genreService;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Film> rowMapper, MpaService mpaService, GenreService genreService) {
        super(jdbcTemplate, rowMapper);
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    public List<Film> findAll() {
        List<Film> films = findMany(FIND_ALL_QUERY);
        List<Film> newFilms = new ArrayList<>();
        for (Film film : films) {
            if (film.getMpa().getId() != 0) {
                film.setMpa(mpaService.getMpaOfId(film.getMpa().getId()));
            }
            film.setGenres(genreService.findGenresByFilm(film.getId()));
            film.setLikes(getLikes(film.getId()));
            newFilms.add(film);
        }
        return newFilms;
    }

    public List<Film> findTopFilms(int count) {
        List<Film> films = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(FIND_TOP_FILMS_QUERY, count);
        while (sqlRowSet.next()) {
            films.add(findById(sqlRowSet.getLong("film_id")));
        }
        return films;
    }

    public Film findById(long filmId) {
        Optional<Film> optionalFilm = findOne(FIND_BY_ID_QUERY, filmId);
        Film film = optionalFilm.orElseThrow(() -> new NotFoundException("Фильм с ID=" + filmId + " не найден"));
        film.setMpa(mpaService.getMpaOfId(film.getMpa().getId()));
        film.setGenres(genreService.findGenresByFilm(film.getId()));
        film.setLikes(getLikes(film.getId()));
        return film;
    }

    public Film add(Film film) {
        checkGenre(film.getGenres());
        checkMpa(film.getMpa());
        long id = insert(INSERT_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        film.setId(id);
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                insertGenreInFilm(film.getId(), genre.getId());
            }
        }
        return film;
    }

    private void insertGenreInFilm(long filmId, int genreId) {
        insertData(INSERT_GENRE_QUERY, filmId, genreId);
    }

    public Film update(Film film) {
        checkGenre(film.getGenres());
        checkMpa(film.getMpa());
        findById(film.getId());
        update(UPDATE_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                insertGenreInFilm(film.getId(), genre.getId());
            }
        }
        return film;
    }

    public void addLikeToFilm(long filmId, long userId) {
        insertData(INSERT_LIKE_QUERY, filmId, userId);
    }

    public void removeLikeFromFilm(long filmId, long userId) {
        insertData(DELETE_LIKE_QUERY, filmId, userId);
    }

    public Set<Long> getLikes(long filmId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(FIND_LIKES_FROM_FILM_QUERY, filmId);
        Set<Long> likes = new HashSet<>();
        while (sqlRowSet.next()) {
            likes.add(sqlRowSet.getLong("user_id"));
        }
        return likes;
    }

    public void checkMpa(Mpa mpa) {
        if (mpa != null) {
            mpaService.getMpaOfIdValid(mpa.getId());
        }
    }

    public void checkGenre(Set<Genre> genreSet) {
        if (genreSet != null && !genreSet.isEmpty()) {
            for (Genre genre : genreSet) {
                if (genre != null) {
                    genreService.findGenreByIdValid(genre.getId());
                }
            }
        }
    }
}