package com.example.videowatchlog.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for TitleInfoUrl value object.
 * TitleInfoUrl represents a URL to a title information page (e.g., Wikipedia, IMDb).
 */
class TitleInfoUrlTest {

    @Test
    void shouldCreateTitleInfoUrlWithValidUrl() {
        // Given
        String url = "https://en.wikipedia.org/wiki/Breaking_Bad";

        // When
        TitleInfoUrl titleInfoUrl = new TitleInfoUrl(url);

        // Then
        assertNotNull(titleInfoUrl);
        assertEquals(url, titleInfoUrl.getUrl());
    }

    @Test
    void shouldRejectNullUrl() {
        // Given
        String url = null;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new TitleInfoUrl(url);
        });
    }

    @Test
    void shouldRejectEmptyUrl() {
        // Given
        String url = "";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new TitleInfoUrl(url);
        });
    }

    @Test
    void shouldRejectUrlTooShort() {
        // Given
        String url = "http://a"; // Less than 10 characters

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new TitleInfoUrl(url);
        });
    }

    @Test
    void shouldRejectUrlExceedingMaxLength() {
        // Given
        String url = "https://example.com/" + "a".repeat(2000); // Exceeds 2000 characters

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new TitleInfoUrl(url);
        });
    }

    @Test
    void shouldAcceptUrlAtMinLength() {
        // Given
        String url = "http://a.b"; // Exactly 10 characters

        // When
        TitleInfoUrl titleInfoUrl = new TitleInfoUrl(url);

        // Then
        assertNotNull(titleInfoUrl);
        assertEquals(10, titleInfoUrl.getUrl().length());
    }

    @Test
    void shouldAcceptUrlAtMaxLength() {
        // Given
        String url = "https://example.com/" + "a".repeat(1981); // Exactly 2000 characters

        // When
        TitleInfoUrl titleInfoUrl = new TitleInfoUrl(url);

        // Then
        assertNotNull(titleInfoUrl);
        assertEquals(2000, titleInfoUrl.getUrl().length());
    }

    @Test
    void shouldRejectNonHttpUrl() {
        // Given
        String url = "ftp://example.com/file";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            new TitleInfoUrl(url);
        });
    }

    @Test
    void shouldAcceptHttpUrl() {
        // Given
        String url = "http://example.com";

        // When
        TitleInfoUrl titleInfoUrl = new TitleInfoUrl(url);

        // Then
        assertNotNull(titleInfoUrl);
        assertEquals(url, titleInfoUrl.getUrl());
    }

    @Test
    void shouldAcceptHttpsUrl() {
        // Given
        String url = "https://example.com";

        // When
        TitleInfoUrl titleInfoUrl = new TitleInfoUrl(url);

        // Then
        assertNotNull(titleInfoUrl);
        assertEquals(url, titleInfoUrl.getUrl());
    }

    @Test
    void shouldBeEqualForSameUrl() {
        // Given
        String url = "https://en.wikipedia.org/wiki/Breaking_Bad";
        TitleInfoUrl url1 = new TitleInfoUrl(url);
        TitleInfoUrl url2 = new TitleInfoUrl(url);

        // When & Then
        assertEquals(url1, url2);
        assertEquals(url1.hashCode(), url2.hashCode());
    }

    @Test
    void shouldNotBeEqualForDifferentUrls() {
        // Given
        TitleInfoUrl url1 = new TitleInfoUrl("https://en.wikipedia.org/wiki/Breaking_Bad");
        TitleInfoUrl url2 = new TitleInfoUrl("https://www.imdb.com/title/tt0903747/");

        // When & Then
        assertNotEquals(url1, url2);
    }
}
