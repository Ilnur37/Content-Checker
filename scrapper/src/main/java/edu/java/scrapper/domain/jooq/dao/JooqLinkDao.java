package edu.java.scrapper.domain.jooq.dao;

import edu.java.scrapper.domain.LinkDao;
import edu.java.scrapper.domain.jooq.generate.tables.pojos.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import static edu.java.scrapper.domain.jooq.generate.tables.ChatLink.CHAT_LINK;
import static edu.java.scrapper.domain.jooq.generate.tables.Link.LINK;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.selectOne;

@RequiredArgsConstructor
public class JooqLinkDao implements LinkDao<Link> {

    private final DSLContext dsl;

    public List<Link> getAll() {
        return dsl.selectFrom(LINK)
            .fetchInto(Link.class);
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return dsl.selectFrom(LINK)
            .where(LINK.URL.eq(url))
            .fetchOptionalInto(Link.class);
    }

    @Override
    public Optional<Link> findById(long id) {
        return dsl.selectFrom(LINK)
            .where(LINK.ID.eq(id))
            .fetchOptionalInto(Link.class);
    }

    @Override
    public List<Link> getByLastCheck(OffsetDateTime dateTime) {
        return dsl.selectFrom(LINK)
            .where(LINK.LAST_CHECK_AT.lessThan(dateTime))
            .fetchInto(Link.class);
    }

    @Override
    public int save(Link newLink) {
        return dsl.insertInto(LINK)
            .set(dsl.newRecord(LINK, newLink))
            .execute();
    }

    @Override
    public int updateLastCheckAtById(long id, OffsetDateTime dateTime) {
        return dsl.update(LINK)
            .set(LINK.LAST_CHECK_AT, dateTime)
            .where(LINK.ID.eq(id))
            .execute();
    }

    @Override
    public int updateLastUpdateAtById(long id, OffsetDateTime dateTime) {
        return dsl.update(LINK)
            .set(LINK.LAST_UPDATE_AT, dateTime)
            .where(LINK.ID.eq(id))
            .execute();
    }

    @Override
    public int deleteByUrl(String url) {
        return dsl.deleteFrom(LINK)
            .where(LINK.URL.eq(url))
            .execute();
    }

    @Override
    public int deleteById(long id) {
        return dsl.deleteFrom(LINK)
            .where(LINK.ID.eq(id))
            .execute();
    }

    @Override
    public int deleteUnnecessary() {
        SelectConditionStep<Record1<Integer>> query = selectOne()
            .from(CHAT_LINK)
            .where(CHAT_LINK.LINK_ID.eq(LINK.ID));

        return dsl.deleteFrom(LINK)
            .where(notExists(query))
            .execute();
    }
}
