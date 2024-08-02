package ru.yandex.practicum.filmorate.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class MpaRowMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("rating_id"));
            mpa.setName(rs.getString("rating_code"));
            return mpa;
        } catch (Exception e) {
            log.warn("Ошибка маппинга Mpa: {}", e.getMessage());
        }
        return null;
    }
}
