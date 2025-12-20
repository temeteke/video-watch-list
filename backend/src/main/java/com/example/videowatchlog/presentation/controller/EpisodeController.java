package com.example.videowatchlog.presentation.controller;

import com.example.videowatchlog.application.dto.CreateEpisodeRequestDTO;
import com.example.videowatchlog.application.dto.EpisodeDetailDTO;
import com.example.videowatchlog.application.dto.ViewingRecordDetailDTO;
import com.example.videowatchlog.application.dto.ViewingRecordRequestDTO;
import com.example.videowatchlog.application.usecase.AddViewingRecordUseCase;
import com.example.videowatchlog.application.usecase.CompleteEpisodeUseCase;
import com.example.videowatchlog.application.usecase.CreateEpisodeUseCase;
import com.example.videowatchlog.application.usecase.DeleteEpisodeUseCase;
import com.example.videowatchlog.application.usecase.GetEpisodeDetailUseCase;
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

/**
 * EpisodeController - エピソード REST API
 */
@RestController
@RequestMapping("/episodes")
@Tag(name = "Episodes", description = "エピソード管理 API")
public class EpisodeController {
    private final GetEpisodeDetailUseCase getEpisodeDetailUseCase;
    private final CreateEpisodeUseCase createEpisodeUseCase;
    private final UpdateEpisodeUseCase updateEpisodeUseCase;
    private final DeleteEpisodeUseCase deleteEpisodeUseCase;
    private final CompleteEpisodeUseCase completeEpisodeUseCase;
    private final AddViewingRecordUseCase addViewingRecordUseCase;

    public EpisodeController(
            GetEpisodeDetailUseCase getEpisodeDetailUseCase,
            CreateEpisodeUseCase createEpisodeUseCase,
            UpdateEpisodeUseCase updateEpisodeUseCase,
            DeleteEpisodeUseCase deleteEpisodeUseCase,
            CompleteEpisodeUseCase completeEpisodeUseCase,
            AddViewingRecordUseCase addViewingRecordUseCase) {
        this.getEpisodeDetailUseCase = getEpisodeDetailUseCase;
        this.createEpisodeUseCase = createEpisodeUseCase;
        this.updateEpisodeUseCase = updateEpisodeUseCase;
        this.deleteEpisodeUseCase = deleteEpisodeUseCase;
        this.completeEpisodeUseCase = completeEpisodeUseCase;
        this.addViewingRecordUseCase = addViewingRecordUseCase;
    }

    @GetMapping("/{episodeId}")
    @Operation(
        summary = "エピソード詳細を取得",
        description = "指定されたIDのエピソード詳細を取得します。シリーズ・タイトル情報および視聴記録も含まれます。"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "取得成功",
            content = @Content(schema = @Schema(implementation = EpisodeDetailDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "エピソードが見つかりません")
    })
    public ResponseEntity<EpisodeDetailDTO> getEpisodeDetail(
            @Parameter(description = "エピソードID", required = true, example = "1")
            @PathVariable Long episodeId) {
        EpisodeDetailDTO detail = getEpisodeDetailUseCase.execute(episodeId);
        return ResponseEntity.ok(detail);
    }

    @PostMapping
    @Operation(
        summary = "新規エピソードを作成",
        description = "シリーズ配下に新しいエピソードを作成します。"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", description = "エピソード作成成功",
            content = @Content(schema = @Schema(implementation = EpisodeDetailDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "シリーズが見つかりません"),
        @ApiResponse(responseCode = "400", description = "リクエストが不正")
    })
    public ResponseEntity<EpisodeDetailDTO> createEpisode(
            @Parameter(description = "シリーズID", required = true, example = "1")
            @RequestParam Long seriesId,
            @Valid @RequestBody CreateEpisodeRequestDTO request) {
        Long episodeId = createEpisodeUseCase.execute(seriesId, request);
        EpisodeDetailDTO created = getEpisodeDetailUseCase.execute(episodeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{episodeId}")
    @Operation(
        summary = "エピソードを更新",
        description = "既存のエピソード情報を更新します。"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "更新成功",
            content = @Content(schema = @Schema(implementation = EpisodeDetailDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "エピソードが見つかりません")
    })
    public ResponseEntity<EpisodeDetailDTO> updateEpisode(
            @Parameter(description = "エピソードID", required = true, example = "1")
            @PathVariable Long episodeId,
            @Valid @RequestBody CreateEpisodeRequestDTO request) {
        updateEpisodeUseCase.execute(episodeId, request.getEpisodeInfo(), request.getWatchPageUrls());
        EpisodeDetailDTO updated = getEpisodeDetailUseCase.execute(episodeId);
        return ResponseEntity.ok(updated);
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
            @Parameter(description = "エピソードID", required = true, example = "1")
            @PathVariable Long episodeId) {
        deleteEpisodeUseCase.execute(episodeId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{episodeId}/complete")
    @Operation(
        summary = "エピソードの視聴を完了",
        description = "未視聴のエピソードを視聴済みにマークし、最初の視聴記録を作成します。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "視聴完了成功",
            content = @Content(schema = @Schema(implementation = ViewingRecordDetailDTO.class))),
        @ApiResponse(responseCode = "404", description = "エピソードが見つかりません"),
        @ApiResponse(responseCode = "400", description = "既に視聴済みまたはリクエストが不正")
    })
    public ResponseEntity<ViewingRecordDetailDTO> completeEpisode(
            @Parameter(description = "エピソードID", required = true, example = "1")
            @PathVariable Long episodeId,
            @Valid @RequestBody ViewingRecordRequestDTO request) {
        ViewingRecordDetailDTO record = completeEpisodeUseCase.execute(
                episodeId,
                request.getWatchedAt(),
                request.getRating(),
                request.getComment());
        return ResponseEntity.ok(record);
    }

    @PostMapping("/{episodeId}/viewing-records")
    @Operation(
        summary = "視聴記録を追加",
        description = "既に視聴済みのエピソードに新しい視聴記録を追加します。同じエピソードを複数回視聴した場合などに使用します。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "視聴記録追加成功",
            content = @Content(schema = @Schema(implementation = ViewingRecordDetailDTO.class))),
        @ApiResponse(responseCode = "404", description = "エピソードが見つかりません"),
        @ApiResponse(responseCode = "400", description = "未視聴状態またはリクエストが不正")
    })
    public ResponseEntity<ViewingRecordDetailDTO> addViewingRecord(
            @Parameter(description = "エピソードID", required = true, example = "1")
            @PathVariable Long episodeId,
            @Valid @RequestBody ViewingRecordRequestDTO request) {
        ViewingRecordDetailDTO record = addViewingRecordUseCase.execute(
                episodeId,
                request.getWatchedAt(),
                request.getRating(),
                request.getComment());
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

}
