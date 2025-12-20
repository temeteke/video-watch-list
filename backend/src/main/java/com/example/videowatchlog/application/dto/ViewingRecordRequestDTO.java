package com.example.videowatchlog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * ViewingRecordRequestDTO - 視聴記録リクエスト
 */
@Schema(description = "視聴記録リクエスト")
public class ViewingRecordRequestDTO {
    @Schema(description = "視聴日時", example = "2025-01-15T20:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "視聴日時は必須です")
    @PastOrPresent(message = "視聴日時は過去である必要があります")
    private LocalDateTime watchedAt;

    @Schema(description = "評価（1～5）", example = "5", requiredMode = Schema.RequiredMode.REQUIRED, minimum = "1", maximum = "5")
    @NotNull(message = "評価は必須です")
    @Min(value = 1, message = "評価は1以上である必要があります")
    @Max(value = 5, message = "評価は5以下である必要があります")
    private Integer rating;

    @Schema(description = "視聴感想・コメント", example = "素晴らしいエピソード！", maxLength = 2000)
    @Size(min = 0, max = 2000, message = "感想は2000文字以下である必要があります")
    private String comment;

    public ViewingRecordRequestDTO() {}

    public ViewingRecordRequestDTO(LocalDateTime watchedAt, Integer rating, String comment) {
        this.watchedAt = watchedAt;
        this.rating = rating;
        this.comment = comment;
    }

    public LocalDateTime getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(LocalDateTime watchedAt) {
        this.watchedAt = watchedAt;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
