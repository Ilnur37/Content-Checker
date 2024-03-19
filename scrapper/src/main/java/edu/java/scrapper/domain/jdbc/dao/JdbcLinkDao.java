package edu.java.scrapper.domain.jdbc.dao;

import edu.java.scrapper.domain.LinkDao;
import edu.java.scrapper.domain.jdbc.model.link.Link;
import edu.java.scrapper.domain.jdbc.model.link.LinkRowMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JdbcLinkDao implements LinkDao<Link> {

    private final JdbcClient jdbcClient;
    private final LinkRowMapper linkRowMapper;

    @Override
    public List<Link> getAll() {
        String sql = "SELECT * FROM link";
        return jdbcClient.sql(sql)
            .query(linkRowMapper).list();
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        String sql = "SELECT * FROM link WHERE url = ?";
        return jdbcClient.sql(sql)
            .param(url)
            .query(linkRowMapper).optional();
    }

    @Override
    public Optional<Link> findById(long id) {
        String sql = "SELECT * FROM link WHERE id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(linkRowMapper).optional();
    }

    @Override
    public List<Link> getByLastCheck(OffsetDateTime dateTime) {
        String sql = "SELECT * FROM link WHERE link.last_check_at < ?";
        return jdbcClient.sql(sql)
            .param(dateTime)
            .query(linkRowMapper).list();
    }

    @Override
    public int save(Link link) {
        String sql =
            "INSERT INTO link(url, created_at, last_update_at, author, name, last_check_at) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcClient.sql(sql)
            .params(
                link.getUrl(),
                link.getCreatedAt(),
                link.getLastUpdateAt(),
                link.getAuthor(),
                link.getName(),
                link.getLastCheckAt()
            )
            .update();
    }

    @Override
    public int updateLastCheckAtById(long id, OffsetDateTime dateTime) {
        String sql = "UPDATE link SET last_check_at = ? WHERE id = ?";
        return jdbcClient.sql(sql)
            .params(dateTime, id)
            .update();
    }

    @Override
    public int updateLastUpdateAtById(long id, OffsetDateTime dateTime) {
        String sql = "UPDATE link SET last_update_at = ? WHERE id = ?";
        return jdbcClient.sql(sql)
            .params(dateTime, id)
            .update();
    }

    @Override
    public int deleteByUrl(String url) {
        String sql = "DELETE FROM link WHERE url = ?";
        return jdbcClient.sql(sql)
            .param(url)
            .update();
    }

    @Override
    public int deleteById(long id) {
        String sql = "DELETE FROM link WHERE id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .update();
    }

    @Override
    public int deleteUnnecessary() {
        String sql = "DELETE FROM link WHERE NOT EXISTS ("
            + "    SELECT 1"
            + "    FROM chat_link"
            + "    WHERE link_id = link.id"
            + ")";
        return jdbcClient.sql(sql)
            .update();
    }
}
