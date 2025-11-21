package com.example.videowatchlog.domain.model;

import java.util.Objects;

/**
 * WatchPageUrl value object (immutable).
 * Represents a URL to a page where an episode can be watched (e.g., Netflix, Amazon Prime).
 *
 * Key business rules:
 * - URL must be 10-2000 characters
 * - URL must start with http:// or https://
 * - Platform is optional
 * - Value object is immutable
 */
public class WatchPageUrl {
    private final String url;
    private final String platform;

    /**
     * Constructor for WatchPageUrl value object.
     *
     * @param url URL string (required, 10-2000 characters, must start with http:// or https://)
     * @param platform Platform name (optional, e.g., "Netflix", "Amazon Prime")
     * @throws IllegalArgumentException if validation fails
     */
    public WatchPageUrl(String url, String platform) {
        validateUrl(url);
        this.url = url;
        this.platform = platform != null ? platform : "";
    }

    /**
     * Constructor for WatchPageUrl with just URL.
     *
     * @param url URL string (required)
     * @throws IllegalArgumentException if validation fails
     */
    public WatchPageUrl(String url) {
        this(url, null);
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

    public String getPlatform() {
        return platform;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WatchPageUrl that = (WatchPageUrl) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return "WatchPageUrl{" +
                "url='" + url + '\'' +
                ", platform='" + platform + '\'' +
                '}';
    }
}
