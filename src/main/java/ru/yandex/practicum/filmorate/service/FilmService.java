package ru.yandex.practicum.filmorate.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dal.FilmDbStorage;

import java.util.Collection;
import java.util.List;

@Log4j2
@Service
public class FilmService {

    private final FilmDbStorage filmDbStorage;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    public Film addFilm(Film film) {
        log.debug("film={}", film);
        return filmDbStorage.add(film);
    }

    public Film updateFilm(Film newFilm) {
        log.debug("newFilm={}", newFilm);
        return filmDbStorage.update(newFilm);
    }

    public Collection<Film> getAllFilms() {
        return filmDbStorage.findAll();
    }

    public Film getFilmOfId(Long id) {
        log.debug("getFilmOfId={}", id);
        return filmDbStorage.findById(id);
    }

    public void addLike(Long filmId, Long userId) {
        log.debug("addLike={}", filmId);
        filmDbStorage.addLikeToFilm(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        log.debug("removeLike={}", filmId);
        filmDbStorage.removeLikeFromFilm(filmId, userId);
    }

    public List<Film> getTopList(int count) {
        log.debug("getTopList={}", count);
        return filmDbStorage.findTopFilms(count);
    }
}