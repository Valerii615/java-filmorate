package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("начало обработки эндпоинта получения всех фильмов");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmOfId(@PathVariable Long id) {
        log.info("начало обработки эндпоинта получения фильма по id={}", id);
        return filmService.getFilmOfId(id);
    }

    @GetMapping("/popular")
    public List<Film> getTopList(@RequestParam int count) {
        log.info("начало обработки эндпоинта получения Top списка фильмов");
        return filmService.getTopList(count);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("начало обработки эндпоинта добавления фильма");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.info("начало обработки эндпоинта обнавления фильма");
        return filmService.updateFilm(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("начало обработки эндпоинта добавления лайка к фильму");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("начало обработки эндпоинта удаления лайка с фильма");
        filmService.removeLike(id, userId);
    }
}