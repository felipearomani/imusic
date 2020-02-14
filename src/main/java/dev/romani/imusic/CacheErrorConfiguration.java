package dev.romani.imusic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheErrorConfiguration extends CachingConfigurerSupport {

    @Override
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }

    @Slf4j
    public static class CustomCacheErrorHandler implements CacheErrorHandler {

        @Override
        public void handleCacheGetError(RuntimeException e, Cache cache, Object o) {
            log.info(e.getMessage(), e);
        }

        @Override
        public void handleCachePutError(RuntimeException e, Cache cache, Object o, Object o1) {
            log.info(e.getMessage(), e);
        }

        @Override
        public void handleCacheEvictError(RuntimeException e, Cache cache, Object o) {
            log.info(e.getMessage(), e);
        }

        @Override
        public void handleCacheClearError(RuntimeException e, Cache cache) {
            log.info(e.getMessage(), e);
        }
    }
}