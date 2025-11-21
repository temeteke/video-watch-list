package com.example.videowatchlog.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * UpdateTitleRequestDTO - タイトル更新リクエスト
 */
public class UpdateTitleRequestDTO {
    @NotBlank(message = "タイトル名は必須です")
    @Size(min = 1, max = 200)
    private String name;

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
