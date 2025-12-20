package com.example.videowatchlog.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * CreateEpisodeRequestDTO - エピソード作成リクエスト
 */
@Schema(description = "エピソード作成リクエスト")
public class CreateEpisodeRequestDTO {
    @Schema(description = "エピソード情報", example = "第1話 巨人襲撃", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 200)
    @NotBlank(message = "エピソード情報は必須です")
    @Size(min = 1, max = 200, message = "エピソード情報は1～200文字である必要があります")
    private String episodeInfo;

    @Schema(description = "視聴ページURL一覧", example = "[\"https://example.com/episode/1\"]")
    private List<String> watchPageUrls;

    public CreateEpisodeRequestDTO() {}

    public CreateEpisodeRequestDTO(String episodeInfo, List<String> watchPageUrls) {
        this.episodeInfo = episodeInfo;
        this.watchPageUrls = watchPageUrls;
    }

    public String getEpisodeInfo() {
        return episodeInfo;
    }

    public void setEpisodeInfo(String episodeInfo) {
        this.episodeInfo = episodeInfo;
    }

    public List<String> getWatchPageUrls() {
        return watchPageUrls;
    }

    public void setWatchPageUrls(List<String> watchPageUrls) {
        this.watchPageUrls = watchPageUrls;
    }
}
