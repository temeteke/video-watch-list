package com.example.videowatchlog.presentation.controller;

import com.example.videowatchlog.application.dto.CreateEpisodeRequestDTO;
import com.example.videowatchlog.application.readmodel.EpisodeReadModel;
import com.example.videowatchlog.application.readmodel.service.EpisodeReadService;
import com.example.videowatchlog.application.usecase.CreateEpisodeUseCase;
import com.example.videowatchlog.application.usecase.DeleteEpisodeUseCase;
import com.example.videowatchlog.application.usecase.UpdateEpisodeUseCase;
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
import java.util.List;

/**
 * EpisodeController - エピソード REST API
 */
@RestController
@RequestMapping("/series/{seriesId}/episodes")
@Tag(name = "Episodes", description = "エピソード管理 API")
public class EpisodeController {
    private final EpisodeReadService episodeReadService;
    private final CreateEpisodeUseCase createEpisodeUseCase;
    private final UpdateEpisodeUseCase updateEpisodeUseCase;
    private final DeleteEpisodeUseCase deleteEpisodeUseCase;

    public EpisodeController(
            EpisodeReadService episodeReadService,
            CreateEpisodeUseCase createEpisodeUseCase,
            UpdateEpisodeUseCase updateEpisodeUseCase,
            DeleteEpisodeUseCase deleteEpisodeUseCase) {
        this.episodeReadService = episodeReadService;
        this.createEpisodeUseCase = createEpisodeUseCase;
        this.updateEpisodeUseCase = updateEpisodeUseCase;
        this.deleteEpisodeUseCase = deleteEpisodeUseCase;
    }

    @GetMapping("/{episodeId}")
    @Operation(
        summary = "エピソード詳細を取得",
        description = "指定されたIDのエピソード詳細を取得します。視聴記録とWatch URLも含まれます。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "取得成功"),
        @ApiResponse(responseCode = "404", description = "エピソードが見つかりません")
    })
    public ResponseEntity<EpisodeReadModel> getEpisodeDetail(
            @Parameter(description = "シリーズID", required = true, example = "1")
            @PathVariable Long seriesId,
            @Parameter(description = "エピソードID", required = true, example = "1")
            @PathVariable Long episodeId) {
        return episodeReadService.getEpisodeDetail(seriesId, episodeId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
        summary = "新規エピソードを作成",
        description = "シリーズ配下に新しいエピソードを作成します。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "エピソード作成成功"),
        @ApiResponse(responseCode = "404", description = "シリーズが見つかりません"),
        @ApiResponse(responseCode = "400", description = "リクエストが不正")
    })
    public ResponseEntity<Void> createEpisode(
            @Parameter(description = "シリーズID", required = true, example = "1")
            @PathVariable Long seriesId,
            @Valid @RequestBody CreateEpisodeRequestDTO request) {
        createEpisodeUseCase.execute(seriesId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{episodeId}")
    @Operation(
        summary = "エピソードを更新",
        description = "既存のエピソード情報を更新します。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "更新成功"),
        @ApiResponse(responseCode = "404", description = "エピソードが見つかりません")
    })
    public ResponseEntity<Void> updateEpisode(
            @Parameter(description = "シリーズID", required = true, example = "1")
            @PathVariable Long seriesId,
            @Parameter(description = "エピソードID", required = true, example = "1")
            @PathVariable Long episodeId,
            @RequestBody CreateEpisodeRequestDTO request) {
        updateEpisodeUseCase.execute(episodeId, request.getEpisodeInfo(), request.getWatchPageUrls());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{episodeId}")
    @Operation(
        summary = "エピソードを削除",
        description = "指定されたエピソードとそれに関連するすべてのデータ（視聴記録）を削除します。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "削除成功"),
        @ApiResponse(responseCode = "404", description = "エピソードが見つかりません")
    })
    public ResponseEntity<Void> deleteEpisode(
            @Parameter(description = "シリーズID", required = true, example = "1")
            @PathVariable Long seriesId,
            @Parameter(description = "エピソードID", required = true, example = "1")
            @PathVariable Long episodeId) {
        deleteEpisodeUseCase.execute(episodeId);
        return ResponseEntity.noContent().build();
    }
}
