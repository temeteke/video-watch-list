package com.example.videowatchlog.presentation.controller;

import com.example.videowatchlog.application.dto.CreateEpisodeRequestDTO;
import com.example.videowatchlog.application.usecase.CreateEpisodeUseCase;
import com.example.videowatchlog.application.usecase.DeleteEpisodeUseCase;
import com.example.videowatchlog.application.usecase.UpdateEpisodeUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * EpisodeController - エピソード REST API
 */
@RestController
@RequestMapping("/series/{seriesId}/episodes")
public class EpisodeController {
    private final CreateEpisodeUseCase createEpisodeUseCase;
    private final UpdateEpisodeUseCase updateEpisodeUseCase;
    private final DeleteEpisodeUseCase deleteEpisodeUseCase;

    public EpisodeController(
            CreateEpisodeUseCase createEpisodeUseCase,
            UpdateEpisodeUseCase updateEpisodeUseCase,
            DeleteEpisodeUseCase deleteEpisodeUseCase) {
        this.createEpisodeUseCase = createEpisodeUseCase;
        this.updateEpisodeUseCase = updateEpisodeUseCase;
        this.deleteEpisodeUseCase = deleteEpisodeUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> createEpisode(
            @PathVariable Long seriesId,
            @Valid @RequestBody CreateEpisodeRequestDTO request) {
        createEpisodeUseCase.execute(seriesId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{episodeId}")
    public ResponseEntity<Void> updateEpisode(
            @PathVariable Long seriesId,
            @PathVariable Long episodeId,
            @RequestBody CreateEpisodeRequestDTO request) {
        updateEpisodeUseCase.execute(episodeId, request.getEpisodeInfo(), request.getWatchPageUrls());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{episodeId}")
    public ResponseEntity<Void> deleteEpisode(
            @PathVariable Long seriesId,
            @PathVariable Long episodeId) {
        deleteEpisodeUseCase.execute(episodeId);
        return ResponseEntity.noContent().build();
    }
}
