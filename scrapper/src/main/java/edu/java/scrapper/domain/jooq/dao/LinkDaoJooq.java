package edu.java.scrapper.domain.jooq.dao;

import edu.java.scrapper.domain.jooq.generate.tables.pojos.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkDaoJooq {
    private final DSLContext dsl;
    private final edu.java.scrapper.domain.jooq.generate.tables.Link linkTable =
        edu.java.scrapper.domain.jooq.generate.tables.Link.LINK;

    public List<Link> getAll() {
        return dsl.selectFrom(linkTable)
            .fetchInto(Link.class);
    }

    public Optional<Link> getByUrl(String url) {
        return dsl.selectFrom(linkTable)
            .where(linkTable.URL.eq(url))
            .fetchOptionalInto(Link.class);
    }

    public Optional<Link> getById(long id) {
        return dsl.selectFrom(linkTable)
            .where(linkTable.ID.eq(id))
            .fetchOptionalInto(Link.class);
    }

    public List<Link> getByLustUpdate(OffsetDateTime dateTime) {
        return dsl.selectFrom(linkTable)
            .where(linkTable.LAST_UPDATE_AT.lessThan(dateTime))
            .fetchInto(Link.class);
    }

    public int save(Link newLink) {
        return dsl.insertInto(linkTable)
            .set(dsl.newRecord(linkTable, newLink))
            .execute();
    }

    public int updateLastUpdateAtById(long id, OffsetDateTime dateTime) {
        return dsl.update(linkTable)
            .set(linkTable.LAST_UPDATE_AT, dateTime)
            .where(linkTable.ID.eq(id))
            .execute();
    }

    public int deleteByUrl(String url) {
        return dsl.deleteFrom(linkTable)
            .where(linkTable.URL.eq(url))
            .execute();
    }

    public int deleteById(long id) {
        return dsl.deleteFrom(linkTable)
            .where(linkTable.ID.eq(id))
            .execute();
    }
}
