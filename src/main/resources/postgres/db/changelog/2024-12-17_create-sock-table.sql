DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'sock_sequence') THEN
        CREATE SEQUENCE sock_sequence START WITH 1 INCREMENT BY 1;
    END IF;
END $$;

CREATE TABLE IF NOT EXISTS sock (
    id BIGINT PRIMARY KEY NOT NULL DEFAULT nextval('sock_sequence'),
    hex_color VARCHAR(7) NOT NULL,
    cotton_percentage SMALLINT NOT NULL CHECK (cotton_percentage >= 0 AND cotton_percentage <= 100),
    count INTEGER NOT NULL CHECK (count >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);