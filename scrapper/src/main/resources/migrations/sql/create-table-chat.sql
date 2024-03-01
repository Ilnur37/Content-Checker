CREATE TABLE chat
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY,
    tg_chat_id BIGINT                   NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    PRIMARY KEY (id)
);
