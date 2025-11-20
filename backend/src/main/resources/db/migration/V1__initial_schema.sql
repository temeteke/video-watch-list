-- Create watch_status enum type
CREATE TYPE watch_status AS ENUM ('to_watch', 'watching', 'completed');

-- Create titles table
CREATE TABLE titles (
    id BIGSERIAL PRIMARY KEY,
    title_name VARCHAR(255) NOT NULL,
    description TEXT,
    watch_status watch_status NOT NULL DEFAULT 'to_watch',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_titles_watch_status ON titles(watch_status);
CREATE INDEX idx_titles_created_at ON titles(created_at);

-- Create series table
CREATE TABLE series (
    id BIGSERIAL PRIMARY KEY,
    title_id BIGINT NOT NULL,
    series_number INT NOT NULL,
    series_name VARCHAR(255) NOT NULL,
    description TEXT,
    watch_status watch_status NOT NULL DEFAULT 'to_watch',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_series_title FOREIGN KEY (title_id) REFERENCES titles(id) ON DELETE CASCADE,
    UNIQUE (title_id, series_number)
);

CREATE INDEX idx_series_title_id ON series(title_id);
CREATE INDEX idx_series_watch_status ON series(watch_status);

-- Create episodes table
CREATE TABLE episodes (
    id BIGSERIAL PRIMARY KEY,
    series_id BIGINT NOT NULL,
    episode_number INT NOT NULL,
    episode_name VARCHAR(255) NOT NULL,
    description TEXT,
    watch_status watch_status NOT NULL DEFAULT 'to_watch',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_episodes_series FOREIGN KEY (series_id) REFERENCES series(id) ON DELETE CASCADE,
    UNIQUE (series_id, episode_number)
);

CREATE INDEX idx_episodes_series_id ON episodes(series_id);
CREATE INDEX idx_episodes_watch_status ON episodes(watch_status);

-- Create viewing_records table
CREATE TABLE viewing_records (
    id BIGSERIAL PRIMARY KEY,
    episode_id BIGINT NOT NULL,
    viewed_at TIMESTAMP NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_viewing_records_episode FOREIGN KEY (episode_id) REFERENCES episodes(id) ON DELETE CASCADE
);

CREATE INDEX idx_viewing_records_episode_id ON viewing_records(episode_id);
CREATE INDEX idx_viewing_records_viewed_at ON viewing_records(viewed_at);

-- Create title_info_urls table (for title information URLs)
CREATE TABLE title_info_urls (
    id BIGSERIAL PRIMARY KEY,
    title_id BIGINT NOT NULL,
    url_type VARCHAR(50) NOT NULL,
    url VARCHAR(2048) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_title_info_urls_title FOREIGN KEY (title_id) REFERENCES titles(id) ON DELETE CASCADE
);

CREATE INDEX idx_title_info_urls_title_id ON title_info_urls(title_id);
CREATE INDEX idx_title_info_urls_url_type ON title_info_urls(url_type);

-- Create watch_page_urls table (for watching URLs)
CREATE TABLE watch_page_urls (
    id BIGSERIAL PRIMARY KEY,
    episode_id BIGINT NOT NULL,
    url VARCHAR(2048) NOT NULL,
    platform VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_watch_page_urls_episode FOREIGN KEY (episode_id) REFERENCES episodes(id) ON DELETE CASCADE
);

CREATE INDEX idx_watch_page_urls_episode_id ON watch_page_urls(episode_id);
CREATE INDEX idx_watch_page_urls_platform ON watch_page_urls(platform);
