package edu.java.scrapper.database.service;

import edu.java.scrapper.service.jdbc.JdbcChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JdbcChatServiceTest {
    @Autowired
    private JdbcChatService jdbcChatService;

    @Test
    @Transactional
    @Rollback
    void register() {

    }
}
