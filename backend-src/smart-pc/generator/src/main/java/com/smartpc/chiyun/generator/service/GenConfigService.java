package com.smartpc.chiyun.generator.service;

import com.smartpc.chiyun.generator.domain.GenConfig;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@CacheConfig(cacheNames = "genConfig")
public interface GenConfigService {

    /**
     * find
     * @return
     */
    @Cacheable(key = "'1'")
    GenConfig find();

    /**
     * update
     * @param genConfig
     * @return
     */
    @CacheEvict(allEntries = true)
    GenConfig update(GenConfig genConfig);
}
