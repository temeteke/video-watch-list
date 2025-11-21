package com.example.videowatchlog.application.dto;

import jakarta.validation.constraints.Size;

/**
 * CreateSeriesRequestDTO - シリーズ作成リクエスト
 */
public class CreateSeriesRequestDTO {
    @Size(min = 0, max = 100, message = "シーズン名は100文字以下である必要があります")
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
