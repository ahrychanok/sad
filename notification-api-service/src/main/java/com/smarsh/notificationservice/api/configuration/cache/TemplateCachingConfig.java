package com.smarsh.notificationservice.api.configuration.cache;

import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * Caching configuration for validation service.
 *
 * @author Dzmitry_Sulauka
 */
@Configuration
@EnableCaching
public class TemplateCachingConfig {

    @Value("${notification.service.templates.cacheTTL}")
    private Integer cacheTtl;

    @Bean
    @Primary
    public CacheManager templateCacheManager() {
        return new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(name, CacheBuilder.newBuilder()
                        .expireAfterWrite(cacheTtl, TimeUnit.SECONDS)
                        .build()
                        .asMap(), false);
            }
        };
    }
}
