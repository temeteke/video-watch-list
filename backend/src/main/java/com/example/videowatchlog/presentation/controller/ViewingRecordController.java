package com.example.videowatchlog.presentation.controller;

import com.example.videowatchlog.application.dto.ViewingRecordRequestDTO;
import com.example.videowatchlog.application.usecase.CompleteEpisodeUseCase;
import com.example.videowatchlog.application.usecase.AddViewingRecordUseCase;
import com.example.videowatchlog.application.usecase.DeleteViewingRecordUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Viewing Records", description = "視聴履歴管理 API")
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
    @Operation(
        summary = "エピソードの視聴を完了",
        description = "未視聴のエピソードを視聴済みにマークし、最初の視聴記録を作成します。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "視聴完了成功"),
        @ApiResponse(responseCode = "404", description = "エピソードが見つかりません"),
        @ApiResponse(responseCode = "400", description = "既に視聴済みまたはリクエストが不正")
    })
    public ResponseEntity<Void> completeEpisode(
            @Parameter(description = "エピソードID", required = true, example = "1")
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
    @Operation(
        summary = "視聴記録を追加",
        description = "既に視聴済みのエピソードに新しい視聴記録を追加します。同じエピソードを複数回視聴した場合などに使用します。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "視聴記録追加成功"),
        @ApiResponse(responseCode = "404", description = "エピソードが見つかりません"),
        @ApiResponse(responseCode = "400", description = "未視聴状態またはリクエストが不正")
    })
    public ResponseEntity<Void> addViewingRecord(
            @Parameter(description = "エピソードID", required = true, example = "1")
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
    @Operation(
        summary = "視聴記録を削除",
        description = "指定された視聴記録を削除します。すべての視聴記録が削除された場合、エピソードは未視聴状態に戻ります。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "視聴記録削除成功"),
        @ApiResponse(responseCode = "404", description = "視聴記録が見つかりません")
    })
    public ResponseEntity<Void> deleteViewingRecord(
            @Parameter(description = "視聴記録ID", required = true, example = "1")
            @PathVariable("id") Long recordId) {
        deleteViewingRecordUseCase.execute(recordId);
        return ResponseEntity.ok().build();
    }
}
