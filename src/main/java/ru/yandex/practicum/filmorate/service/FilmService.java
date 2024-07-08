package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        log.debug("film={}", film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film newFilm) {
        log.debug("newFilm={}", newFilm);
        return filmStorage.updateFilm(newFilm);
    }

    public void removeFilm(Long id) {
        log.debug("id={}", id);
        filmStorage.removeFilm(id);
    }

    public Collection<Film> getAllFilms() {
        log.debug("films.size={}", filmStorage.getAllFilms().size());
        return filmStorage.getAllFilms();
    }

    public Film getFilmOfId(Long id) {
        log.debug("id={}", id);
        return filmStorage.getFilmOfId(id);
    }

    public void addLike(Long filmId, Long userId) {
        log.debug("usedId={}, filmId={}", userId, filmId);
        userStorage.getUserOfId(userId);
        filmStorage.getFilmOfId(filmId).getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        log.debug("filmId={}, usedId={}", filmId, userId);
        if (filmStorage.getFilmOfId(filmId).getLikes() == null) {
            throw new NullPointerException("список лайков пуст");
        }
        userStorage.getUserOfId(userId);
        if (filmStorage.getFilmOfId(filmId).getLikes().contains(userId)) {

            filmStorage.getFilmOfId(filmId).getLikes().remove(userId);
        }
    }

    public List<Film> getTopList(int count) {
        log.debug("count={}", count);
        if (count <= 0) {
            count = 10;
        }
        List<Film> filmsList = new ArrayList<>(filmStorage.getAllFilms());
        return filmsList.stream()
                .filter(i -> i.getLikes() != null)
                .sorted(new FilmComparator())
                .limit(count)
                .toList();
    }
}