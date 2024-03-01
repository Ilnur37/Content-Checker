CREATE TABLE link
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY,
    url            TEXT                     NOT NULL,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL,
    last_update_at TIMESTAMP WITH TIME ZONE NOT NULL,

    PRIMARY KEY (id)
);
