package com.example.videowatchlog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * CreateSeriesRequestDTO - シリーズ作成リクエスト
 */
@Schema(description = "シリーズ作成リクエスト")
public class CreateSeriesRequestDTO {
    @Schema(description = "シリーズ名", example = "Season 1", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 100)
    @NotBlank(message = "シーズン名は必須です")
    @Size(min = 1, max = 100, message = "シーズン名は1～100文字である必要があります")
    private String name;

    public CreateSeriesRequestDTO() {}

    public CreateSeriesRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
