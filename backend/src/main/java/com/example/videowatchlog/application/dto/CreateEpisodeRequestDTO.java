package com.example.videowatchlog.application.dto;

import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * CreateEpisodeRequestDTO - エピソード作成リクエスト
 */
public class CreateEpisodeRequestDTO {
    @Size(min = 0, max = 200, message = "エピソード情報は200文字以下である必要があります")
    private String episodeInfo;

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
