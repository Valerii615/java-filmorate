package ru.yandex.practicum.filmorate.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dal.MpaDbStorage;

import java.util.Collection;

@Log4j2
@Service
public class MpaService {
    MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Collection<Mpa> getAllMpa() {
        return mpaDbStorage.findAll();
    }


    public Mpa getMpaOfId(int id) {
        log.debug("detMpaOfId: id={}", id);
        return mpaDbStorage.findById(id);
    }

    public Mpa getMpaOfIdValid(int id) {
        log.debug("detMpaOfIdValid: id={}", id);
        return mpaDbStorage.findByIdValid(id);
    }

}
