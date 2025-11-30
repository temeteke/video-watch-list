package com.example.videowatchlog.presentation.controller;

import com.example.videowatchlog.application.dto.ViewingRecordRequestDTO;
import com.example.videowatchlog.application.usecase.CompleteEpisodeUseCase;
import com.example.videowatchlog.application.usecase.AddViewingRecordUseCase;
import com.example.videowatchlog.application.usecase.DeleteViewingRecordUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

/**
 * ViewingRecordController - 視聴履歴 REST API
 *
 * Handles operations for viewing records (complete episode, add record, delete record).
 */
@RestController
@RequestMapping("/api/v1")
public class ViewingRecordController {
    private final CompleteEpisodeUseCase completeEpisodeUseCase;
    private final AddViewingRecordUseCase addViewingRecordUseCase;
    private final DeleteViewingRecordUseCase deleteViewingRecordUseCase;

    public ViewingRecordController(
            CompleteEpisodeUseCase completeEpisodeUseCase,
            AddViewingRecordUseCase addViewingRecordUseCase,
            DeleteViewingRecordUseCase deleteViewingRecordUseCase) {
        this.completeEpisodeUseCase = completeEpisodeUseCase;
        this.addViewingRecordUseCase = addViewingRecordUseCase;
        this.deleteViewingRecordUseCase = deleteViewingRecordUseCase;
    }

    /**
     * Complete watching an episode.
     * Marks episode as WATCHED and creates first viewing record.
     *
     * @param episodeId ID of the episode to complete
     * @param request Request body with watched date, rating, and comment
     * @return 200 OK if successful
     * @throws IllegalArgumentException if episode not found
     * @throws IllegalStateException if episode already watched
     */
    @PostMapping("/episodes/{id}/complete")
    public ResponseEntity<Void> completeEpisode(
            @PathVariable("id") Long episodeId,
            @Valid @RequestBody ViewingRecordRequestDTO request) {
        completeEpisodeUseCase.execute(
                episodeId,
                request.getWatchedAt(),
                request.getRating(),
                request.getComment());
        return ResponseEntity.ok().build();
    }

    /**
     * Add a viewing record to an already watched episode.
     *
     * @param episodeId ID of the episode
     * @param request Request body with watched date, rating, and comment
     * @return 200 OK if successful
     * @throws IllegalArgumentException if episode not found
     * @throws IllegalStateException if episode not watched
     */
    @PostMapping("/episodes/{id}/viewing-records")
    public ResponseEntity<Void> addViewingRecord(
            @PathVariable("id") Long episodeId,
            @Valid @RequestBody ViewingRecordRequestDTO request) {
        addViewingRecordUseCase.execute(
                episodeId,
                request.getWatchedAt(),
                request.getRating(),
                request.getComment());
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a viewing record.
     * If all viewing records are deleted, episode reverts to UNWATCHED.
     *
     * @param recordId ID of the viewing record to delete
     * @return 200 OK if successful
     * @throws IllegalArgumentException if record or episode not found
     */
    @DeleteMapping("/viewing-records/{id}")
    public ResponseEntity<Void> deleteViewingRecord(
            @PathVariable("id") Long recordId) {
        deleteViewingRecordUseCase.execute(recordId);
        return ResponseEntity.ok().build();
    }
}
