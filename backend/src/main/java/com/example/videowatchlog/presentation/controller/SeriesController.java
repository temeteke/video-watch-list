package com.example.videowatchlog.presentation.controller;

import com.example.videowatchlog.application.dto.CreateSeriesRequestDTO;
import com.example.videowatchlog.application.usecase.CreateSeriesUseCase;
import com.example.videowatchlog.application.usecase.DeleteSeriesUseCase;
import com.example.videowatchlog.application.usecase.UpdateSeriesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * SeriesController - シリーズ REST API
 */
@RestController
@RequestMapping("/titles/{titleId}/series")
@Tag(name = "Series", description = "シリーズ管理 API")
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
    @Operation(
        summary = "新規シリーズを作成",
        description = "タイトル配下に新しいシリーズを作成し、デフォルトエピソードを自動生成します。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "シリーズ作成成功"),
        @ApiResponse(responseCode = "404", description = "タイトルが見つかりません"),
        @ApiResponse(responseCode = "400", description = "リクエストが不正")
    })
    public ResponseEntity<Void> createSeries(
            @Parameter(description = "タイトルID", required = true, example = "1")
            @PathVariable Long titleId,
            @Valid @RequestBody CreateSeriesRequestDTO request) {
        createSeriesUseCase.execute(titleId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{seriesId}")
    @Operation(
        summary = "シリーズを更新",
        description = "既存のシリーズ情報を更新します。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "更新成功"),
        @ApiResponse(responseCode = "404", description = "シリーズが見つかりません")
    })
    public ResponseEntity<Void> updateSeries(
            @Parameter(description = "タイトルID", required = true, example = "1")
            @PathVariable Long titleId,
            @Parameter(description = "シリーズID", required = true, example = "1")
            @PathVariable Long seriesId,
            @RequestBody String name) {
        updateSeriesUseCase.execute(seriesId, name);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{seriesId}")
    @Operation(
        summary = "シリーズを削除",
        description = "指定されたシリーズとそれに関連するすべてのデータ（エピソード、視聴記録）を削除します。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "削除成功"),
        @ApiResponse(responseCode = "404", description = "シリーズが見つかりません")
    })
    public ResponseEntity<Void> deleteSeries(
            @Parameter(description = "タイトルID", required = true, example = "1")
            @PathVariable Long titleId,
            @Parameter(description = "シリーズID", required = true, example = "1")
            @PathVariable Long seriesId) {
        deleteSeriesUseCase.execute(seriesId);
        return ResponseEntity.noContent().build();
    }
}
