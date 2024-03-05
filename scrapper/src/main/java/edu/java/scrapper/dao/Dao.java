package edu.java.scrapper.dao;

import java.util.List;

public interface Dao<T> {
    List<T> getAll();

    int save(T t);

    int delete(T t);
}
