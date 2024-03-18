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
import org.springframework.stereotype.Repository;
import static org.jooq.impl.DSL.notExists;
import static org.jooq.impl.DSL.selectOne;

@Repository
@RequiredArgsConstructor
public class JooqLinkDao implements LinkDao<Link> {

    private final DSLContext dsl;

    private final edu.java.scrapper.domain.jooq.generate.tables.Link linkTable =
        edu.java.scrapper.domain.jooq.generate.tables.Link.LINK;

    private final edu.java.scrapper.domain.jooq.generate.tables.ChatLink chatLinkTable =
        edu.java.scrapper.domain.jooq.generate.tables.ChatLink.CHAT_LINK;

    public List<Link> getAll() {
        return dsl.selectFrom(linkTable)
            .fetchInto(Link.class);
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return dsl.selectFrom(linkTable)
            .where(linkTable.URL.eq(url))
            .fetchOptionalInto(Link.class);
    }

    @Override
    public Optional<Link> findById(long id) {
        return dsl.selectFrom(linkTable)
            .where(linkTable.ID.eq(id))
            .fetchOptionalInto(Link.class);
    }

    @Override
    public List<Link> getByLastCheck(OffsetDateTime dateTime) {
        return dsl.selectFrom(linkTable)
            .where(linkTable.LAST_CHECK_AT.lessThan(dateTime))
            .fetchInto(Link.class);
    }

    @Override
    public int save(Link newLink) {
        return dsl.insertInto(linkTable)
            .set(dsl.newRecord(linkTable, newLink))
            .execute();
    }

    @Override
    public int updateLastCheckAtById(long id, OffsetDateTime dateTime) {
        return dsl.update(linkTable)
            .set(linkTable.LAST_CHECK_AT, dateTime)
            .where(linkTable.ID.eq(id))
            .execute();
    }

    @Override
    public int updateLastUpdateAtById(long id, OffsetDateTime dateTime) {
        return dsl.update(linkTable)
            .set(linkTable.LAST_UPDATE_AT, dateTime)
            .where(linkTable.ID.eq(id))
            .execute();
    }

    @Override
    public int deleteByUrl(String url) {
        return dsl.deleteFrom(linkTable)
            .where(linkTable.URL.eq(url))
            .execute();
    }

    @Override
    public int deleteById(long id) {
        return dsl.deleteFrom(linkTable)
            .where(linkTable.ID.eq(id))
            .execute();
    }

    @Override
    public int deleteUnnecessary() {
        SelectConditionStep<Record1<Integer>> query = selectOne()
            .from(chatLinkTable)
            .where(chatLinkTable.LINK_ID.eq(linkTable.ID));

        return dsl.deleteFrom(linkTable)
            .where(notExists(query))
            .execute();
    }
}
