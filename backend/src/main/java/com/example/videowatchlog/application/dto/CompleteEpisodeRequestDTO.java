package com.example.videowatchlog.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * CompleteEpisodeRequestDTO - エピソード視聴完了リクエスト
 */
public class CompleteEpisodeRequestDTO {
    @NotNull(message = "視聴完了日時は必須です")
    @PastOrPresent(message = "視聴完了日時は過去である必要があります")
    private LocalDateTime watchedAt;

    @NotNull(message = "評価は必須です")
    @Min(value = 1, message = "評価は1以上である必要があります")
    @Max(value = 5, message = "評価は5以下である必要があります")
    private Integer rating;

    @Size(min = 0, max = 2000, message = "感想は2000文字以下である必要があります")
    private String comment;

    public CompleteEpisodeRequestDTO() {}

    public CompleteEpisodeRequestDTO(LocalDateTime watchedAt, Integer rating, String comment) {
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
