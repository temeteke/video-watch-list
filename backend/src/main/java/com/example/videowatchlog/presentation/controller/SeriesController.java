package com.example.videowatchlog.presentation.controller;

import com.example.videowatchlog.application.dto.CreateSeriesRequestDTO;
import com.example.videowatchlog.application.usecase.CreateSeriesUseCase;
import com.example.videowatchlog.application.usecase.DeleteSeriesUseCase;
import com.example.videowatchlog.application.usecase.UpdateSeriesUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * SeriesController - シリーズ REST API
 */
@RestController
@RequestMapping("/api/v1/titles/{titleId}/series")
public class SeriesController {
    private final CreateSeriesUseCase createSeriesUseCase;
    private final UpdateSeriesUseCase updateSeriesUseCase;
    private final DeleteSeriesUseCase deleteSeriesUseCase;

    public SeriesController(
            CreateSeriesUseCase createSeriesUseCase,
            UpdateSeriesUseCase updateSeriesUseCase,
            DeleteSeriesUseCase deleteSeriesUseCase) {
        this.createSeriesUseCase = createSeriesUseCase;
        this.updateSeriesUseCase = updateSeriesUseCase;
        this.deleteSeriesUseCase = deleteSeriesUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> createSeries(
            @PathVariable Long titleId,
            @Valid @RequestBody CreateSeriesRequestDTO request) {
        createSeriesUseCase.execute(titleId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{seriesId}")
    public ResponseEntity<Void> updateSeries(
            @PathVariable Long titleId,
            @PathVariable Long seriesId,
            @RequestBody String name) {
        updateSeriesUseCase.execute(seriesId, name);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{seriesId}")
    public ResponseEntity<Void> deleteSeries(
            @PathVariable Long titleId,
            @PathVariable Long seriesId) {
        deleteSeriesUseCase.execute(seriesId);
        return ResponseEntity.noContent().build();
    }
}
