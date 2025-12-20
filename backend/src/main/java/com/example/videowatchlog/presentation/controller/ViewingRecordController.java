package com.example.videowatchlog.presentation.controller;

import com.example.videowatchlog.application.usecase.DeleteViewingRecordUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ViewingRecordController - 視聴履歴削除 REST API
 *
 * Handles deletion of viewing records.
 */
@RestController
@RequestMapping("/viewing-records")
@Tag(name = "Viewing Records", description = "視聴履歴管理 API")
public class ViewingRecordController {
    private final DeleteViewingRecordUseCase deleteViewingRecordUseCase;

    public ViewingRecordController(
            DeleteViewingRecordUseCase deleteViewingRecordUseCase) {
        this.deleteViewingRecordUseCase = deleteViewingRecordUseCase;
    }

    /**
     * Delete a viewing record.
     * If all viewing records are deleted, episode reverts to UNWATCHED.
     *
     * @param recordId ID of the viewing record to delete
     * @return 204 No Content if successful
     * @throws IllegalArgumentException if record or episode not found
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "視聴記録を削除",
        description = "指定された視聴記録を削除します。すべての視聴記録が削除された場合、エピソードは未視聴状態に戻ります。"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "視聴記録削除成功"),
        @ApiResponse(responseCode = "404", description = "視聴記録が見つかりません")
    })
    public ResponseEntity<Void> deleteViewingRecord(
            @Parameter(description = "視聴記録ID", required = true, example = "1")
            @PathVariable("id") Long recordId) {
        deleteViewingRecordUseCase.execute(recordId);
        return ResponseEntity.noContent().build();
    }
}
