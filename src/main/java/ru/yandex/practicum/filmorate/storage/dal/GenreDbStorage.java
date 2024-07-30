package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genre";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE genre_id = ?";
    private static final String FIND_GENRE_BY_FILM_QUERY = "SELECT gl.genre_id, genre.name FROM genre LEFT JOIN genre_list gl ON genre.genre_id = gl.genre_id WHERE film_id = ?";

    public GenreDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Genre> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Genre findById(int id) {
        Optional<Genre> genre = findOne(FIND_BY_ID_QUERY, id);
        return genre.orElseThrow(() -> new NotFoundException("Жанр с ID=" + id + " не найден"));
    }

    public Genre findByIdValid(int id) {
        Optional<Genre> genre = findOne(FIND_BY_ID_QUERY, id);
        return genre.orElseThrow(() -> new ValidationException("Жанр с ID=" + id + " не найден"));
    }

    public Set<Genre> findGenresByFilm(Long filmId) {
        List<Genre> genreIdList = findMany(FIND_GENRE_BY_FILM_QUERY, filmId);
        return new HashSet<>(genreIdList);
    }

}
