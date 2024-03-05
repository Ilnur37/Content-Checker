package edu.java.scrapper.dao;

import edu.java.scrapper.model.link.Link;
import edu.java.scrapper.model.link.LinkRowMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkDao implements Dao<Link> {
    public final JdbcClient jdbcClient;
    private final LinkRowMapper linkRowMapper;

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

    @Override
    public List<Link> getAll() {
        String sql = "SELECT * FROM link";
        return jdbcClient.sql(sql)
            .query(linkRowMapper).list();
    }

    @Override
    public int save(Link link) {
        String sql = "INSERT INTO link(url, created_at, last_update_at) VALUES (?, ?, ?)";
        return jdbcClient.sql(sql)
            .params(link.getUrl(), link.getCreatedAt(), link.getLastUpdateAt())
            .update();
    }

    @Override
    public int delete(Link link) {
        String sql = "DELETE FROM link WHERE url = ?";
        return jdbcClient.sql(sql)
            .param(link.getUrl())
            .update();
    }
}
