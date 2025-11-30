package com.example.videowatchlog.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for WatchPageUrl value object.
 * WatchPageUrl represents a URL to a page where an episode can be watched (e.g., Netflix, Amazon Prime).
 */
class WatchPageUrlTest {

    @Test
    void shouldCreateWatchPageUrlWithValidUrl() {
        // Given
        String url = "https://www.netflix.com/watch/12345";

        // When
        WatchPageUrl watchPageUrl = new WatchPageUrl(url);

        // Then
        assertNotNull(watchPageUrl);
        assertEquals(url, watchPageUrl.getUrl());
        assertEquals("", watchPageUrl.getPlatform());
    }

    @Test
    void shouldCreateWatchPageUrlWithPlatform() {
        // Given
        String url = "https://www.netflix.com/watch/12345";
        String platform = "Netflix";

        // When
        WatchPageUrl watchPageUrl = new WatchPageUrl(url, platform);

        // Then
        assertNotNull(watchPageUrl);
        assertEquals(url, watchPageUrl.getUrl());
        assertEquals(platform, watchPageUrl.getPlatform());
    }

    @Test
    void shouldRejectNullUrl() {
        // Given
        String url = null;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new WatchPageUrl(url);
        });
    }

    @Test
    void shouldRejectEmptyUrl() {
        // Given
        String url = "";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new WatchPageUrl(url);
        });
    }

    @Test
    void shouldRejectUrlTooShort() {
        // Given
        String url = "http://a"; // Less than 10 characters

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new WatchPageUrl(url);
        });
    }

    @Test
    void shouldRejectUrlExceedingMaxLength() {
        // Given
        String url = "https://www.netflix.com/" + "a".repeat(2000); // Exceeds 2000 characters

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new WatchPageUrl(url);
        });
    }

    @Test
    void shouldAcceptUrlAtMinLength() {
        // Given
        String url = "http://a.b"; // Exactly 10 characters

        // When
        WatchPageUrl watchPageUrl = new WatchPageUrl(url);

        // Then
        assertNotNull(watchPageUrl);
        assertEquals(10, watchPageUrl.getUrl().length());
    }

    @Test
    void shouldAcceptUrlAtMaxLength() {
        // Given
        String url = "https://www.netflix.com/" + "a".repeat(1976); // Exactly 2000 characters (24 + 1976)

        // When
        WatchPageUrl watchPageUrl = new WatchPageUrl(url);

        // Then
        assertNotNull(watchPageUrl);
        assertEquals(2000, watchPageUrl.getUrl().length());
    }

    @Test
    void shouldRejectNonHttpUrl() {
        // Given
        String url = "ftp://www.example.com/file";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new WatchPageUrl(url);
        });
    }

    @Test
    void shouldAcceptHttpUrl() {
        // Given
        String url = "http://www.netflix.com";

        // When
        WatchPageUrl watchPageUrl = new WatchPageUrl(url);

        // Then
        assertNotNull(watchPageUrl);
        assertEquals(url, watchPageUrl.getUrl());
    }

    @Test
    void shouldAcceptHttpsUrl() {
        // Given
        String url = "https://www.netflix.com";

        // When
        WatchPageUrl watchPageUrl = new WatchPageUrl(url);

        // Then
        assertNotNull(watchPageUrl);
        assertEquals(url, watchPageUrl.getUrl());
    }

    @Test
    void shouldBeEqualForSameUrl() {
        // Given
        String url = "https://www.netflix.com/watch/12345";
        WatchPageUrl url1 = new WatchPageUrl(url, "Netflix");
        WatchPageUrl url2 = new WatchPageUrl(url, "Netflix Alternative");

        // When & Then: Equality is based on URL only, not platform
        assertEquals(url1, url2);
        assertEquals(url1.hashCode(), url2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentUrls() {
        // Given
        WatchPageUrl url1 = new WatchPageUrl("https://www.netflix.com/watch/12345", "Netflix");
        WatchPageUrl url2 = new WatchPageUrl("https://www.amazon.com/watch/12345", "Amazon Prime");

        // When & Then
        assertNotEquals(url1, url2);
    }

    @Test
    void shouldHandleNullPlatform() {
        // Given
        String url = "https://www.netflix.com/watch/12345";

        // When
        WatchPageUrl watchPageUrl = new WatchPageUrl(url, null);

        // Then
        assertEquals("", watchPageUrl.getPlatform());
    }
}
