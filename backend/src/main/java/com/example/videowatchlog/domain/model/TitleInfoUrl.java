package com.example.videowatchlog.domain.model;

import java.util.Objects;

/**
 * TitleInfoUrl value object (immutable).
 * Represents a URL to a title information page (e.g., Wikipedia, IMDb).
 *
 * Key business rules:
 * - URL must be 10-2000 characters
 * - URL must start with http:// or https://
 * - Value object is immutable
 */
public class TitleInfoUrl {
    private final String url;

    /**
     * Constructor for TitleInfoUrl value object.
     *
     * @param url URL string (required, 10-2000 characters, must start with http:// or https://)
     * @throws IllegalArgumentException if validation fails
     */
    public TitleInfoUrl(String url) {
        validateUrl(url);
        this.url = url;
    }

    /**
     * Validates URL according to business rules.
     *
     * @param url URL to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateUrl(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL must not be null or empty");
        }
        if (url.length() < 10) {
            throw new IllegalArgumentException("URL must be at least 10 characters");
        }
        if (url.length() > 2000) {
            throw new IllegalArgumentException("URL must not exceed 2000 characters");
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new IllegalArgumentException("URL must start with http:// or https://");
        }
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TitleInfoUrl that = (TitleInfoUrl) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return "TitleInfoUrl{" +
                "url='" + url + '\'' +
                '}';
    }
}
