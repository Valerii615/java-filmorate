package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        log.info("Начало обрадотки эндпоинта добавления фильма в память приложения /films metod: Post");
        film.setId(getNextId());
        film.setLikes(new HashSet<>());
        log.info("Объекту film.name = \"{}\" присвоен id = {}", film.getName(), film.getId());
        films.put(film.getId(), film);
        log.info("Фильм id = {} добавлен в память приложения", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        log.info("Начало обрадотки эндпоинта обновления данных фильма /films metod: Put");
        if (newFilm.getId() == null) {
            log.warn("Id должен быть указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Фильм id = {} обновлен {}", oldFilm.getId(), oldFilm);
            return oldFilm;
        }
        log.warn("Фильм с id = {} не найден", newFilm.getId());
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    @Override
    public void removeFilm(Long id) {
        getFilmOfId(id);
        films.remove(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film getFilmOfId(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("фильма с id=" + id + " не найдено");
        }
        return films.get(id);
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
