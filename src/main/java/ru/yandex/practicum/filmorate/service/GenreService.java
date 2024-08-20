package ru.yandex.practicum.filmorate.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dal.GenreDbStorage;

import java.util.List;
import java.util.Set;

@Log4j2
@Service
public class GenreService {
    GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public List<Genre> getAllGenres() {
        return genreDbStorage.findAll();
    }

    public Set<Genre> findGenresByFilm(Long filmId) {
        log.debug("Find genres by Film Id: {}", filmId);
        return genreDbStorage.findGenresByFilm(filmId);
    }

    public Genre findGenreById(int genreId) {
        log.debug("Find genre by Genre Id: {}", genreId);
        return genreDbStorage.findById(genreId);
    }

    public Genre findGenreByIdValid(int genreId) {
        log.debug("Find genre by Genre Id Valid: {}", genreId);
        return genreDbStorage.findByIdValid(genreId);
    }
}
