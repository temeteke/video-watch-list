package com.example.videowatchlog.presentation.controller;

import com.example.videowatchlog.application.dto.CreateTitleRequestDTO;
import com.example.videowatchlog.application.dto.TitleSummaryDTO;
import com.example.videowatchlog.application.dto.UpdateTitleRequestDTO;
import com.example.videowatchlog.application.usecase.CreateTitleUseCase;
import com.example.videowatchlog.application.usecase.DeleteTitleUseCase;
import com.example.videowatchlog.application.usecase.GetAllTitlesUseCase;
import com.example.videowatchlog.application.usecase.GetTitleDetailUseCase;
import com.example.videowatchlog.application.usecase.UpdateTitleUseCase;
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

    public TitleController(
            CreateTitleUseCase createTitleUseCase,
            GetAllTitlesUseCase getAllTitlesUseCase,
            GetTitleDetailUseCase getTitleDetailUseCase,
            UpdateTitleUseCase updateTitleUseCase,
            DeleteTitleUseCase deleteTitleUseCase) {
        this.createTitleUseCase = createTitleUseCase;
        this.getAllTitlesUseCase = getAllTitlesUseCase;
        this.getTitleDetailUseCase = getTitleDetailUseCase;
        this.updateTitleUseCase = updateTitleUseCase;
        this.deleteTitleUseCase = deleteTitleUseCase;
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

    @GetMapping("/{id}")
    public ResponseEntity<TitleSummaryDTO> getTitleDetail(@PathVariable Long id) {
        TitleSummaryDTO result = getTitleDetailUseCase.execute(id);
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
