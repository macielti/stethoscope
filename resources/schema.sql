CREATE TABLE IF NOT EXISTS healthcheck (
    id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    code INT NOT NULL,
    requested_at TIMESTAMP NOT NULL
);