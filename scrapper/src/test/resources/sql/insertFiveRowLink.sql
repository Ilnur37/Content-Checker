INSERT INTO link(id, url, created_at, last_update_at, name, author, last_check_at) OVERRIDING SYSTEM VALUE
VALUES (10, 'defaultUrl1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'defaultName', 'defaultAuthor', CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;
INSERT INTO link(id, url, created_at, last_update_at, name, author, last_check_at) OVERRIDING SYSTEM VALUE
VALUES (20, 'defaultUrl2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'defaultName', 'defaultAuthor',
        CURRENT_TIMESTAMP + INTERVAL '20' SECOND)
ON CONFLICT DO NOTHING;
INSERT INTO link(id, url, created_at, last_update_at, name, author, last_check_at) OVERRIDING SYSTEM VALUE
VALUES (30, 'defaultUrl3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'defaultName', 'defaultAuthor',
        CURRENT_TIMESTAMP + INTERVAL '40' SECOND)
ON CONFLICT DO NOTHING;
INSERT INTO link(id, url, created_at, last_update_at, name, author, last_check_at) OVERRIDING SYSTEM VALUE
VALUES (40, 'defaultUrl4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'defaultName', 'defaultAuthor',
        CURRENT_TIMESTAMP + INTERVAL '60' SECOND)
ON CONFLICT DO NOTHING;
INSERT INTO link(id, url, created_at, last_update_at, name, author, last_check_at) OVERRIDING SYSTEM VALUE
VALUES (50, 'defaultUrl5', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'defaultName', 'defaultAuthor',
        CURRENT_TIMESTAMP + INTERVAL '80' SECOND)
ON CONFLICT DO NOTHING;
