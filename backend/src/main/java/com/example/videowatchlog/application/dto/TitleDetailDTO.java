package com.example.videowatchlog.application.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TitleDetailDTO - タイトル詳細情報（階層構造を含む）
 *
 * タイトル詳細ページで使用される完全な情報
 */
public class TitleDetailDTO {
    private Long id;
    private String name;
    private List<SeriesDetailDTO> series;
    private List<String> titleInfoUrls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TitleDetailDTO(Long id, String name, List<SeriesDetailDTO> series,
                         List<String> titleInfoUrls, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.series = series;
        this.titleInfoUrls = titleInfoUrls;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<SeriesDetailDTO> getSeries() {
        return series;
    }

    public List<String> getTitleInfoUrls() {
        return titleInfoUrls;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * SeriesDetailDTO - シリーズ詳細情報
     */
    public static class SeriesDetailDTO {
        private Long id;
        private Long titleId;
        private String name;
        private List<EpisodeDetailDTO> episodes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public SeriesDetailDTO(Long id, Long titleId, String name, List<EpisodeDetailDTO> episodes,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.titleId = titleId;
            this.name = name;
            this.episodes = episodes;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        // Getters
        public Long getId() {
            return id;
        }

        public Long getTitleId() {
            return titleId;
        }

        public String getName() {
            return name;
        }

        public List<EpisodeDetailDTO> getEpisodes() {
            return episodes;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }

    /**
     * EpisodeDetailDTO - エピソード詳細情報
     */
    public static class EpisodeDetailDTO {
        private Long id;
        private Long seriesId;
        private String episodeInfo;
        private String watchStatus;
        private List<String> watchPageUrls;
        private List<ViewingRecordDTO> viewingRecords;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public EpisodeDetailDTO(Long id, Long seriesId, String episodeInfo, String watchStatus,
                               List<String> watchPageUrls, List<ViewingRecordDTO> viewingRecords,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.seriesId = seriesId;
            this.episodeInfo = episodeInfo;
            this.watchStatus = watchStatus;
            this.watchPageUrls = watchPageUrls;
            this.viewingRecords = viewingRecords;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        // Getters
        public Long getId() {
            return id;
        }

        public Long getSeriesId() {
            return seriesId;
        }

        public String getEpisodeInfo() {
            return episodeInfo;
        }

        public String getWatchStatus() {
            return watchStatus;
        }

        public List<String> getWatchPageUrls() {
            return watchPageUrls;
        }

        public List<ViewingRecordDTO> getViewingRecords() {
            return viewingRecords;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getUpdatedAt() {
            return updatedAt;
        }
    }

    /**
     * ViewingRecordDTO - 視聴記録情報
     */
    public static class ViewingRecordDTO {
        private Long id;
        private Long episodeId;
        private LocalDateTime watchedAt;
        private Integer rating;
        private String comment;
        private LocalDateTime recordedAt;

        public ViewingRecordDTO(Long id, Long episodeId, LocalDateTime watchedAt,
                               Integer rating, String comment, LocalDateTime recordedAt) {
            this.id = id;
            this.episodeId = episodeId;
            this.watchedAt = watchedAt;
            this.rating = rating;
            this.comment = comment;
            this.recordedAt = recordedAt;
        }

        // Getters
        public Long getId() {
            return id;
        }

        public Long getEpisodeId() {
            return episodeId;
        }

        public LocalDateTime getWatchedAt() {
            return watchedAt;
        }

        public Integer getRating() {
            return rating;
        }

        public String getComment() {
            return comment;
        }

        public LocalDateTime getRecordedAt() {
            return recordedAt;
        }
    }
}
