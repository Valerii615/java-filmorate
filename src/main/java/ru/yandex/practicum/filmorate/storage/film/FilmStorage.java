package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film newFilm);

    void removeFilm(Long id);

    Collection<Film> getAllFilms();

    Film getFilmOfId(Long id);

}