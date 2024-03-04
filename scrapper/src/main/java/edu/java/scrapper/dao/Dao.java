package edu.java.scrapper.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> getById(long id);

    List<T> getAll();

    int save(T t);

    int delete(T t);
}
