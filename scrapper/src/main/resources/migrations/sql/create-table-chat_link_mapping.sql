CREATE TABLE chat_link
(
    id      BIGINT GENERATED ALWAYS AS IDENTITY,
    chat_id BIGINT NOT NULL,
    link_id BIGINT NOT NULL,
    FOREIGN KEY (chat_id) REFERENCES chat (id) ON DELETE CASCADE,
    FOREIGN KEY (link_id) REFERENCES link (id),
    PRIMARY KEY (id)
);
