INSERT INTO link(id, url, created_at, last_update_at, name, author, last_check_at) OVERRIDING SYSTEM VALUE
VALUES (10, 'defaultUrl', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'defaultName', 'defaultAuthor', CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;
