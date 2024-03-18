package edu.java.scrapper.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkDao<T> {

    List<T> getAll();

    Optional<T> findByUrl(String url);

    Optional<T> findById(long id);

    List<T> getByLastCheck(OffsetDateTime dateTime);

    int save(T newLink);

    int updateLastCheckAtById(long id, OffsetDateTime dateTime);

    int updateLastUpdateAtById(long id, OffsetDateTime dateTime);

    int deleteByUrl(String url);

    int deleteById(long id);

    int deleteUnnecessary();
}
