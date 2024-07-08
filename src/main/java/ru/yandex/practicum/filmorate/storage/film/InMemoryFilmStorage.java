package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();


    @Override
    public Film addFilm(Film film) {
        log.info("Начало обрадотки эндпоинта добавления фильма в память приложения /films metod: Post");
        validator(film);
        film.setId(getNextId());
        log.info("Объекту film.name = \"" + film.getName() + "\" присвоен id = " + film.getId());
        films.put(film.getId(), film);
        log.info("Фильм id = " + film.getId() + " добавлен в память приложения");
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        log.info("Начало обрадотки эндпоинта обновления данных фильма /films metod: Put");
        if (newFilm.getId() == null) {
            log.error("Id должен быть указан");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            validator(newFilm);
            Film oldFilm = films.get(newFilm.getId());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Фильм id = " + oldFilm.getId() + " обновлен " + oldFilm);
            return oldFilm;
        }
        log.error("Фильм с id = " + newFilm.getId() + " не найден");
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


    private void validator(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() != null) {
            if (film.getDescription().length() > 200) {
                log.error("Длина описания не должна превышать — 200 символов, текущая длинна: " + film.getDescription().length());
                throw new ValidationException("Длина описания не должна превышать — 200 символов, текущая длинна: " + film.getDescription().length());
            }
        }
        if (film.getReleaseDate() != null) {
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.error("Дата релиза должна быть не раньше 28 декабря 1895 года");
                throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
            }
        }
        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        log.info("Валидация прошла успешно");
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
