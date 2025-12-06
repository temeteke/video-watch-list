package com.example.videowatchlog.integration;

import com.example.videowatchlog.domain.model.Episode;
import com.example.videowatchlog.domain.model.Series;
import com.example.videowatchlog.domain.model.Title;
import com.example.videowatchlog.domain.model.ViewingRecord;
import com.example.videowatchlog.domain.model.WatchStatus;
import com.example.videowatchlog.domain.service.TitleIdService;
import com.example.videowatchlog.domain.service.SeriesIdService;
import com.example.videowatchlog.domain.service.EpisodeIdService;
import com.example.videowatchlog.domain.service.ViewingRecordIdService;
import com.example.videowatchlog.application.dto.ViewingRecordRequestDTO;
import com.example.videowatchlog.infrastructure.persistence.EpisodeRepositoryImpl;
import com.example.videowatchlog.infrastructure.persistence.SeriesRepositoryImpl;
import com.example.videowatchlog.infrastructure.persistence.TitleRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ViewingRecordController.
 * Tests the full flow from REST API to database persistence.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("ViewingRecordControllerIntegration")
class ViewingRecordControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TitleIdService titleIdService;

    @Autowired
    private SeriesIdService seriesIdService;

    @Autowired
    private EpisodeIdService episodeIdService;

    @Autowired
    private ViewingRecordIdService viewingRecordIdService;

    @Autowired
    private TitleRepositoryImpl titleRepository;

    @Autowired
    private SeriesRepositoryImpl seriesRepository;

    @Autowired
    private EpisodeRepositoryImpl episodeRepository;

    private Title testTitle;
    private Series testSeries;
    private Episode testEpisode;

    @BeforeEach
    void setUp() {
        // Create test data: Title → Series → Episode
        Long titleId = titleIdService.generateId();
        testTitle = Title.create(titleId, "Test Drama");
        testTitle = titleRepository.save(testTitle);

        Long seriesId = seriesIdService.generateId();
        testSeries = Series.create(seriesId, testTitle.getId(), "Season 1");
        testSeries = seriesRepository.save(testSeries);

        Long episodeId = episodeIdService.generateId();
        testEpisode = Episode.create(episodeId, testSeries.getId(), "Episode 1");
        testEpisode = episodeRepository.save(testEpisode);
    }

    @Test
    @DisplayName("should complete episode and create first viewing record")
    void testCompleteEpisode() throws Exception {
        // Arrange
        Long episodeId = testEpisode.getId();
        LocalDateTime watchedAt = LocalDateTime.now().minusHours(1);

        ViewingRecordRequestDTO request = new ViewingRecordRequestDTO(watchedAt, 4, "Great episode!");

        // Act & Assert
        mockMvc.perform(post("/api/v1/episodes/{id}/complete", episodeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Verify episode status changed
        Episode updatedEpisode = episodeRepository.findById(episodeId).orElseThrow();
        assertEquals(WatchStatus.WATCHED, updatedEpisode.getWatchStatus());
        assertEquals(1, updatedEpisode.getViewingRecords().size());
    }

    @Test
    @DisplayName("should add viewing record to watched episode")
    void testAddViewingRecord() throws Exception {
        // Arrange: First complete the episode
        testEpisode.markAsWatched();
        testEpisode = episodeRepository.save(testEpisode);

        Long episodeId = testEpisode.getId();
        LocalDateTime watchedAt = LocalDateTime.now().minusHours(2);

        ViewingRecordRequestDTO request = new ViewingRecordRequestDTO(watchedAt, 5, "Excellent!");

        // Act & Assert
        mockMvc.perform(post("/api/v1/episodes/{id}/viewing-records", episodeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Verify viewing record was added
        Episode updatedEpisode = episodeRepository.findById(episodeId).orElseThrow();
        assertEquals(1, updatedEpisode.getViewingRecords().size());
    }

    @Test
    @DisplayName("should delete viewing record")
    void testDeleteViewingRecord() throws Exception {
        // Arrange: Create episode with viewing record
        testEpisode.markAsWatched();
        Long recordId = viewingRecordIdService.generateId();
        ViewingRecord record = ViewingRecord.create(recordId, testEpisode.getId(), LocalDateTime.now().minusHours(1), 4, "Good");
        testEpisode.addViewingRecord(record);
        testEpisode = episodeRepository.save(testEpisode);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/viewing-records/{id}", recordId))
                .andExpect(status().isOk());

        // Verify viewing record was deleted and episode reverted to UNWATCHED
        Episode updatedEpisode = episodeRepository.findById(testEpisode.getId()).orElseThrow();
        assertTrue(updatedEpisode.getViewingRecords().isEmpty(), "Viewing records should be empty");
        assertEquals(WatchStatus.UNWATCHED, updatedEpisode.getWatchStatus(), "Episode should be reverted to UNWATCHED");
    }

    @Test
    @DisplayName("should return 404 when completing non-existent episode")
    void testCompleteNonExistentEpisode() throws Exception {
        // Arrange
        Long invalidId = 999999L;
        LocalDateTime watchedAt = LocalDateTime.now().minusHours(1);

        ViewingRecordRequestDTO request = new ViewingRecordRequestDTO(watchedAt, 4, "Test");

        // Act & Assert
        mockMvc.perform(post("/api/v1/episodes/{id}/complete", invalidId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
