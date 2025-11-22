package com.example.videowatchlog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig - Web全体の設定
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * CORS設定を追加
     *
     * 開発環境ではフロントエンド（localhost:3000）からのアクセスを許可する。
     * 本番環境では環境変数や設定ファイルで許可するオリジンを変更できる。
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
