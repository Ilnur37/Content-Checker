package edu.java.scrapper.domain.jdbc.dao;

import edu.java.scrapper.domain.jdbc.model.link.Link;
import edu.java.scrapper.domain.jdbc.model.link.LinkRowMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkDao {
    private final JdbcClient jdbcClient;
    private final LinkRowMapper linkRowMapper;

    public List<Link> getAll() {
        String sql = "SELECT * FROM link";
        return jdbcClient.sql(sql)
            .query(linkRowMapper).list();
    }

    public Optional<Link> getByUrl(String url) {
        String sql = "SELECT * FROM link WHERE url = ?";
        return jdbcClient.sql(sql)
            .param(url)
            .query(linkRowMapper).optional();
    }

    public Optional<Link> getById(long id) {
        String sql = "SELECT * FROM link WHERE id = ?";
        return jdbcClient.sql(sql)
            .param(id)
            .query(linkRowMapper).optional();
    }

    public List<Link> getByLustCheck(OffsetDateTime dateTime) {
        String sql = "SELECT * FROM link WHERE link.last_check_at < ?";
        return jdbcClient.sql(sql)
            .param(dateTime)
            .query(linkRowMapper).list();
    }

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

    public void updateLastCheckAtById(long id, OffsetDateTime dateTime) {
        String sql = "UPDATE link SET last_check_at = ? WHERE id = ?";
        jdbcClient.sql(sql)
            .params(dateTime, id)
            .update();
    }

    public void updateLastUpdateAtById(long id, OffsetDateTime dateTime) {
        String sql = "UPDATE link SET last_update_at = ? WHERE id = ?";
        jdbcClient.sql(sql)
            .params(dateTime, id)
            .update();
    }

    public int deleteByUrl(String url) {
        String sql = "DELETE FROM link WHERE url = ?";
        return jdbcClient.sql(sql)
            .param(url)
            .update();
    }

    public void deleteById(long id) {
        String sql = "DELETE FROM link WHERE id = ?";
        jdbcClient.sql(sql)
            .param(id)
            .update();
    }
}
