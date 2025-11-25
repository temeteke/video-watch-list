package com.example.videowatchlog.presentation.controller;

import com.example.videowatchlog.application.dto.CreateTitleRequestDTO;
import com.example.videowatchlog.application.dto.TitleDetailDTO;
import com.example.videowatchlog.application.dto.TitleSummaryDTO;
import com.example.videowatchlog.application.dto.UpdateTitleRequestDTO;
import com.example.videowatchlog.application.usecase.CreateTitleUseCase;
import com.example.videowatchlog.application.usecase.DeleteTitleUseCase;
import com.example.videowatchlog.application.usecase.GetAllTitlesUseCase;
import com.example.videowatchlog.application.usecase.GetTitleDetailUseCase;
import com.example.videowatchlog.application.usecase.UpdateTitleUseCase;
import com.example.videowatchlog.application.usecase.SearchTitlesUseCase;
import com.example.videowatchlog.domain.model.WatchStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * TitleController - タイトル REST API
 */
@RestController
@RequestMapping("/titles")
public class TitleController {
    private final CreateTitleUseCase createTitleUseCase;
    private final GetAllTitlesUseCase getAllTitlesUseCase;
    private final GetTitleDetailUseCase getTitleDetailUseCase;
    private final UpdateTitleUseCase updateTitleUseCase;
    private final DeleteTitleUseCase deleteTitleUseCase;
    private final SearchTitlesUseCase searchTitlesUseCase;

    public TitleController(
            CreateTitleUseCase createTitleUseCase,
            GetAllTitlesUseCase getAllTitlesUseCase,
            GetTitleDetailUseCase getTitleDetailUseCase,
            UpdateTitleUseCase updateTitleUseCase,
            DeleteTitleUseCase deleteTitleUseCase,
            SearchTitlesUseCase searchTitlesUseCase) {
        this.createTitleUseCase = createTitleUseCase;
        this.getAllTitlesUseCase = getAllTitlesUseCase;
        this.getTitleDetailUseCase = getTitleDetailUseCase;
        this.updateTitleUseCase = updateTitleUseCase;
        this.deleteTitleUseCase = deleteTitleUseCase;
        this.searchTitlesUseCase = searchTitlesUseCase;
    }

    @PostMapping
    public ResponseEntity<TitleSummaryDTO> createTitle(@Valid @RequestBody CreateTitleRequestDTO request) {
        TitleSummaryDTO result = createTitleUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    public ResponseEntity<List<TitleSummaryDTO>> getAllTitles() {
        List<TitleSummaryDTO> result = getAllTitlesUseCase.execute();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TitleSummaryDTO>> searchTitles(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) WatchStatus watchStatus) {
        List<TitleSummaryDTO> result = searchTitlesUseCase.execute(query, watchStatus);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TitleDetailDTO> getTitleDetail(@PathVariable Long id) {
        TitleDetailDTO result = getTitleDetailUseCase.execute(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTitle(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTitleRequestDTO request) {
        updateTitleUseCase.execute(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTitle(@PathVariable Long id) {
        deleteTitleUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
