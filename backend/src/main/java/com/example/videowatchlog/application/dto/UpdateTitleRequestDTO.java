package com.example.videowatchlog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * UpdateTitleRequestDTO - タイトル更新リクエスト
 */
@Schema(description = "タイトル更新リクエスト")
public class UpdateTitleRequestDTO {
    @Schema(description = "タイトル名", example = "進撃の巨人", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 200)
    @NotBlank(message = "タイトル名は必須です")
    @Size(min = 1, max = 200)
    private String name;

    @Schema(description = "作品情報URL一覧", example = "[\"https://example.com\"]")
    private List<String> titleInfoUrls;

    public UpdateTitleRequestDTO() {}

    public UpdateTitleRequestDTO(String name, List<String> titleInfoUrls) {
        this.name = name;
        this.titleInfoUrls = titleInfoUrls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTitleInfoUrls() {
        return titleInfoUrls;
    }

    public void setTitleInfoUrls(List<String> titleInfoUrls) {
        this.titleInfoUrls = titleInfoUrls;
    }
}
