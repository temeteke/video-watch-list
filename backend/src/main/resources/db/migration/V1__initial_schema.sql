-- Create watch_status enum type
CREATE TYPE watch_status AS ENUM ('UNWATCHED', 'WATCHING', 'COMPLETED');

-- Create titles table
CREATE TABLE titles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_titles_created_at ON titles(created_at);
CREATE INDEX idx_titles_name ON titles(LOWER(name));

-- Create title_info_urls table
CREATE TABLE title_info_urls (
    id BIGSERIAL PRIMARY KEY,
    title_id BIGINT NOT NULL,
    url VARCHAR(2000) NOT NULL,
    CONSTRAINT fk_title_info_urls_title FOREIGN KEY (title_id) REFERENCES titles(id) ON DELETE CASCADE
);

CREATE INDEX idx_title_info_urls_title_id ON title_info_urls(title_id);

-- Create series table
CREATE TABLE series (
    id BIGSERIAL PRIMARY KEY,
    title_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_series_title FOREIGN KEY (title_id) REFERENCES titles(id) ON DELETE CASCADE
);

CREATE INDEX idx_series_title_id ON series(title_id);

-- Create episodes table
CREATE TABLE episodes (
    id BIGSERIAL PRIMARY KEY,
    series_id BIGINT NOT NULL,
    episode_info VARCHAR(500) NOT NULL,
    watch_status watch_status NOT NULL DEFAULT 'UNWATCHED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_episodes_series FOREIGN KEY (series_id) REFERENCES series(id) ON DELETE CASCADE
);

CREATE INDEX idx_episodes_series_id ON episodes(series_id);
CREATE INDEX idx_episodes_watch_status ON episodes(watch_status);

-- Create watch_page_urls table
CREATE TABLE watch_page_urls (
    id BIGSERIAL PRIMARY KEY,
    episode_id BIGINT NOT NULL,
    url VARCHAR(2000) NOT NULL,
    CONSTRAINT fk_watch_page_urls_episode FOREIGN KEY (episode_id) REFERENCES episodes(id) ON DELETE CASCADE
);

CREATE INDEX idx_watch_page_urls_episode_id ON watch_page_urls(episode_id);

-- Create viewing_records table
CREATE TABLE viewing_records (
    id BIGSERIAL PRIMARY KEY,
    episode_id BIGINT NOT NULL,
    watched_at TIMESTAMP NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_viewing_records_episode FOREIGN KEY (episode_id) REFERENCES episodes(id) ON DELETE CASCADE
);

CREATE INDEX idx_viewing_records_episode_id ON viewing_records(episode_id);
CREATE INDEX idx_viewing_records_watched_at ON viewing_records(watched_at);
