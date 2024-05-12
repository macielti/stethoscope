CREATE TABLE IF NOT EXISTS time_window (
    time_window__id uuid NOT NULL,
    time_window__started_at TIMESTAMP NOT NULL,
    time_window__ended_at TIMESTAMP,
    time_window__status VARCHAR(20) NOT NULL,
    PRIMARY KEY (time_window__id));