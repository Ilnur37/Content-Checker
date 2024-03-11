CREATE TABLE chat_link
(
    chat_id BIGINT NOT NULL,
    link_id BIGINT NOT NULL,
    FOREIGN KEY (chat_id) REFERENCES chat (id),
    FOREIGN KEY (link_id) REFERENCES link (id),
    PRIMARY KEY (chat_id, link_id)
);
