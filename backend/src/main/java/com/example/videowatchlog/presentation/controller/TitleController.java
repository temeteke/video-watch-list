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
 * TitleController - タイトル REST API
 */
@RestController
@RequestMapping("/titles")
@Tag(name = "Titles", description = "タイトル(作品)管理 API")
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
    @Operation(
        summary = "新規タイトルを作成",
        description = "新しいタイトルを作成し、デフォルトシリーズとデフォルトエピソードを自動生成します。"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "タイトル作成成功",
            content = @Content(schema = @Schema(implementation = TitleSummaryDTO.class))
        ),
        @ApiResponse(
            responseCode = "409",
            description = "タイトル名が既に存在(重複)",
            content = @Content(schema = @Schema(type = "object"))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "リクエストが不正",
            content = @Content(schema = @Schema(type = "object"))
        )
    })
    public ResponseEntity<TitleSummaryDTO> createTitle(@Valid @RequestBody CreateTitleRequestDTO request) {
        TitleSummaryDTO result = createTitleUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    @Operation(
        summary = "タイトル一覧を取得",
        description = "登録されているタイトルを一覧で取得します。queryまたはwatchStatusパラメータでフィルタ可能です。"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "取得成功",
            content = @Content(schema = @Schema(implementation = TitleSummaryDTO.class))
        )
    })
    public ResponseEntity<List<TitleSummaryDTO>> getTitles(
            @Parameter(description = "検索キーワード（タイトル名の部分一致）", example = "進撃")
            @RequestParam(required = false) String query,
            @Parameter(description = "視聴ステータスフィルタ", schema = @Schema(allowableValues = {"UNWATCHED", "WATCHED"}))
            @RequestParam(required = false) WatchStatus watchStatus) {
        // 両方のパラメータが null の場合: 全件取得
        if (query == null && watchStatus == null) {
            List<TitleSummaryDTO> result = getAllTitlesUseCase.execute();
            return ResponseEntity.ok(result);
        }

        // いずれかのパラメータがある場合: 検索実行
        List<TitleSummaryDTO> result = searchTitlesUseCase.execute(query, watchStatus);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "タイトル詳細を取得",
        description = "指定されたIDのタイトル詳細を取得します。シリーズとエピソード情報も含まれます。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "取得成功"),
        @ApiResponse(responseCode = "404", description = "タイトルが見つかりません")
    })
    public ResponseEntity<TitleDetailDTO> getTitleDetail(
            @Parameter(description = "タイトルID", required = true, example = "1")
            @PathVariable Long id) {
        TitleDetailDTO result = getTitleDetailUseCase.execute(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "タイトルを更新",
        description = "既存のタイトル情報を更新します。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "更新成功",
            content = @Content(schema = @Schema(implementation = TitleDetailDTO.class))),
        @ApiResponse(responseCode = "404", description = "タイトルが見つかりません"),
        @ApiResponse(responseCode = "409", description = "タイトル名が既に存在(重複)")
    })
    public ResponseEntity<TitleDetailDTO> updateTitle(
            @Parameter(description = "タイトルID", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateTitleRequestDTO request) {
        updateTitleUseCase.execute(id, request);
        TitleDetailDTO updated = getTitleDetailUseCase.execute(id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "タイトルを削除",
        description = "指定されたタイトルとそれに関連するすべてのデータ（シリーズ、エピソード、視聴記録）を削除します。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "削除成功"),
        @ApiResponse(responseCode = "404", description = "タイトルが見つかりません")
    })
    public ResponseEntity<Void> deleteTitle(
            @Parameter(description = "タイトルID", required = true, example = "1")
            @PathVariable Long id) {
        deleteTitleUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
