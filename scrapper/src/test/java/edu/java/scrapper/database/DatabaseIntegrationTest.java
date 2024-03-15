package edu.java.scrapper.database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class DatabaseIntegrationTest extends IntegrationTest {
    @Autowired
    JdbcClient jdbcClient;

    @Test
    void createAllTables() {
        String sql = "SELECT table_name FROM scrapper.information_schema.tables";
        var result = jdbcClient.sql(sql)
            .query()
            .singleColumn();
        assertAll(
            () -> Assertions.assertTrue(result.contains("chat")),
            () -> Assertions.assertTrue(result.contains("link")),
            () -> Assertions.assertTrue(result.contains("chat_link"))
        );
    }

    @Test
    void createColumnsInChatTable() {
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
        var columns = jdbcClient.sql(sql)
            .param(new String("chat"))
            .query()
            .singleColumn();

        assertAll(
            () -> Assertions.assertEquals(3, columns.size()),
            () -> Assertions.assertTrue(columns.contains("id")),
            () -> Assertions.assertTrue(columns.contains("tg_chat_id")),
            () -> Assertions.assertTrue(columns.contains("created_at"))
        );
    }

    @Test
    void createColumnsInLinkTable() {
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
        var columns = jdbcClient.sql(sql)
            .param(new String("link"))
            .query()
            .singleColumn();

        assertAll(
            () -> Assertions.assertEquals(7, columns.size()),
            () -> Assertions.assertTrue(columns.contains("id")),
            () -> Assertions.assertTrue(columns.contains("url")),
            () -> Assertions.assertTrue(columns.contains("created_at")),
            () -> Assertions.assertTrue(columns.contains("last_update_at")),
            () -> Assertions.assertTrue(columns.contains("name")),
            () -> Assertions.assertTrue(columns.contains("author")),
            () -> Assertions.assertTrue(columns.contains("last_check_at"))
        );
    }

    @Test
    void createColumnsInChatLinkTable() {
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
        var columns = jdbcClient.sql(sql)
            .param(new String("chat_link"))
            .query()
            .singleColumn();

        assertAll(
            () -> Assertions.assertEquals(2, columns.size()),
            () -> Assertions.assertTrue(columns.contains("chat_id")),
            () -> Assertions.assertTrue(columns.contains("link_id"))
        );
    }
}
