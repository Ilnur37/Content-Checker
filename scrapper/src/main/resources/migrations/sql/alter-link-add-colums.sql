ALTER TABLE link
    ADD COLUMN name TEXT NOT NULL default '';
ALTER TABLE link
    ADD COLUMN author TEXT NOT NULL default '';
ALTER TABLE link
    ADD COLUMN last_check_at TIMESTAMP WITH TIME ZONE NOT NULL;
