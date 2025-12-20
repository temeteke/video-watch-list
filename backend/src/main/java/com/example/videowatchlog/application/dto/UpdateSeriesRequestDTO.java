package com.example.videowatchlog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * UpdateSeriesRequestDTO - シリーズ更新リクエスト
 */
@Schema(description = "シリーズ更新リクエスト")
public class UpdateSeriesRequestDTO {
    @Schema(description = "シリーズ名", example = "Season 2", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    @NotBlank(message = "シーズン名は必須です")
    @Size(min = 1, max = 100, message = "シーズン名は1～100文字である必要があります")
    private String name;

    public UpdateSeriesRequestDTO() {}

    public UpdateSeriesRequestDTO(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
