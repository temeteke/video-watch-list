package com.example.videowatchlog.presentation.controller;

import com.example.videowatchlog.application.dto.CreateSeriesRequestDTO;
import com.example.videowatchlog.application.dto.SeriesDetailDTO;
import com.example.videowatchlog.application.dto.UpdateSeriesRequestDTO;
import com.example.videowatchlog.application.usecase.CreateSeriesUseCase;
import com.example.videowatchlog.application.usecase.DeleteSeriesUseCase;
import com.example.videowatchlog.application.usecase.GetSeriesDetailUseCase;
import com.example.videowatchlog.application.usecase.UpdateSeriesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private final GetSeriesDetailUseCase getSeriesDetailUseCase;
    private final UpdateSeriesUseCase updateSeriesUseCase;
    private final DeleteSeriesUseCase deleteSeriesUseCase;

    public SeriesController(
            CreateSeriesUseCase createSeriesUseCase,
            GetSeriesDetailUseCase getSeriesDetailUseCase,
            UpdateSeriesUseCase updateSeriesUseCase,
            DeleteSeriesUseCase deleteSeriesUseCase) {
        this.createSeriesUseCase = createSeriesUseCase;
        this.getSeriesDetailUseCase = getSeriesDetailUseCase;
        this.updateSeriesUseCase = updateSeriesUseCase;
        this.deleteSeriesUseCase = deleteSeriesUseCase;
    }

    @GetMapping("/{seriesId}")
    @Operation(
        summary = "シリーズ詳細を取得",
        description = "指定されたIDのシリーズ詳細を取得します。"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "取得成功",
            content = @Content(schema = @Schema(implementation = SeriesDetailDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "シリーズが見つかりません")
    })
    public ResponseEntity<SeriesDetailDTO> getSeriesDetail(
            @Parameter(description = "タイトルID", required = true, example = "1")
            @PathVariable Long titleId,
            @Parameter(description = "シリーズID", required = true, example = "1")
            @PathVariable Long seriesId) {
        SeriesDetailDTO detail = getSeriesDetailUseCase.execute(seriesId);
        return ResponseEntity.ok(detail);
    }

    @PostMapping
    @Operation(
        summary = "新規シリーズを作成",
        description = "タイトル配下に新しいシリーズを作成し、デフォルトエピソードを自動生成します。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "シリーズ作成成功",
            content = @Content(schema = @Schema(implementation = SeriesDetailDTO.class))),
        @ApiResponse(responseCode = "404", description = "タイトルが見つかりません"),
        @ApiResponse(responseCode = "400", description = "リクエストが不正")
    })
    public ResponseEntity<SeriesDetailDTO> createSeries(
            @Parameter(description = "タイトルID", required = true, example = "1")
            @PathVariable Long titleId,
            @Valid @RequestBody CreateSeriesRequestDTO request) {
        Long seriesId = createSeriesUseCase.execute(titleId, request);
        SeriesDetailDTO created = getSeriesDetailUseCase.execute(seriesId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{seriesId}")
    @Operation(
        summary = "シリーズを更新",
        description = "既存のシリーズ情報を更新します。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "更新成功",
            content = @Content(schema = @Schema(implementation = SeriesDetailDTO.class))),
        @ApiResponse(responseCode = "404", description = "シリーズが見つかりません")
    })
    public ResponseEntity<SeriesDetailDTO> updateSeries(
            @Parameter(description = "タイトルID", required = true, example = "1")
            @PathVariable Long titleId,
            @Parameter(description = "シリーズID", required = true, example = "1")
            @PathVariable Long seriesId,
            @Valid @RequestBody UpdateSeriesRequestDTO request) {
        updateSeriesUseCase.execute(seriesId, request.getName());
        SeriesDetailDTO updated = getSeriesDetailUseCase.execute(seriesId);
        return ResponseEntity.ok(updated);
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
