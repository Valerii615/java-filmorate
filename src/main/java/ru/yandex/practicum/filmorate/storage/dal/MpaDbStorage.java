package ru.yandex.practicum.filmorate.storage.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage extends BaseDbStorage<Mpa> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM rating";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating WHERE rating_id = ?";

    public MpaDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Mpa> rowMapper) {
        super(jdbcTemplate, rowMapper);
    }

    public List<Mpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Mpa findById(long mpaId) {
        Optional<Mpa> mpa = findOne(FIND_BY_ID_QUERY, mpaId);
        return mpa.orElseThrow(() -> new NotFoundException("Рейтинг с ID=" + mpaId + " не найден"));
    }

    public Mpa findByIdValid(long mpaId) {
        Optional<Mpa> mpa = findOne(FIND_BY_ID_QUERY, mpaId);
        return mpa.orElseThrow(() -> new ValidationException("Рейтинг с ID=" + mpaId + " не найден"));
    }

}
