package com.example.videowatchlog.config;

import com.example.videowatchlog.domain.repository.TitleRepository;
import com.example.videowatchlog.domain.service.TitleDuplicationCheckService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DomainServiceConfig - Domain層のサービスをBean登録する
 *
 * Architecture Decision:
 * Domain層のサービスはSpringフレームワークに依存すべきではありません。
 * そのため、@Service アノテーションではなく、このConfiguration クラスで
 * Bean登録することで、DDD原則（Domain層の純粋性）を保ちます。
 *
 * 依存関係：
 * - DomainServiceConfig（@Configuration）は Infrastructure層の性質
 * - Beanを登録する際に必要なRepositoryインターフェースはDomain層
 * - 依存方向は正しく保たれています（Config → Domain Interface）
 */
@Configuration
public class DomainServiceConfig {

    /**
     * TitleDuplicationCheckService をBean登録
     *
     * @param titleRepository Domain層のRepositoryインターフェース
     * @return TitleDuplicationCheckService
     */
    @Bean
    public TitleDuplicationCheckService titleDuplicationCheckService(TitleRepository titleRepository) {
        return new TitleDuplicationCheckService(titleRepository);
    }
}
