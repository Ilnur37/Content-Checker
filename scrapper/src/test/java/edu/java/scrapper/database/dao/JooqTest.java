package edu.java.scrapper.database.dao;

import edu.java.scrapper.database.IntegrationTest;
import edu.java.scrapper.domain.jooq.dao.ChatDaoJooq;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Rollback
@Transactional
public class JooqTest extends IntegrationTest {
    @Autowired
    ChatDaoJooq chatDaoJooq;
    @Autowired
    private DSLContext dsl;
    @Autowired
    private JdbcClient jdbcClient;

    @Test
    void getByTgChatId() {
        //Добавление чата с заданным tgChatId
        System.out.println(1);
    }
}
