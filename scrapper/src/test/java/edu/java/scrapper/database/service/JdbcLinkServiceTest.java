package edu.java.scrapper.database.service;

import edu.java.scrapper.dao.ChatDao;
import edu.java.scrapper.dao.ChatLinkDao;
import edu.java.scrapper.dao.LinkDao;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Rollback
@Transactional
public class JdbcLinkServiceTest {
    @Autowired
    JdbcLinkService linkService;
    @Autowired
    private ChatDao chatDao;
    @Autowired
    private LinkDao linkDao;
    @Autowired
    private ChatLinkDao chatLinkDao;


}
