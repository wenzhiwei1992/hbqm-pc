package com.smartpc.chiyun.utils;

import com.smartpc.chiyun.encryption.Encryption;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.net.URL;

public class EhcacheUtil {

    private static final String path = "/ehcache.xml";

    public static final String THREAD_ORG_ID = "threadOrgId";

    private URL url;

    private CacheManager manager;

    private static EhcacheUtil ehCache;

    private EhcacheUtil(String path) {
        url = getClass().getResource(path);
        manager = CacheManager.create(url);
    }

    public static EhcacheUtil getInstance() {
        if (ehCache == null) {
            ehCache = new EhcacheUtil(path);
        }
        return ehCache;
    }

    public CacheManager getCacheManager() {
        return manager;
    }

    public void put(String cacheName, String key, Object value) {
        Cache cache = manager.getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
    }

    public Object get(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();
    }

    public Cache get(String cacheName) {
        return manager.getCache(cacheName);
    }

    public void remove(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        cache.remove(key);
    }


    /**
     * 创建缓存区间
     */
    public void createCache(String cacheName) {
        Cache memoryOnlyCache = manager.getCache(cacheName);
        //建立一个缓存实例
        if (memoryOnlyCache == null) {
            //项目名称   缓存最大数量 是否缓存成文件 设定缓存是否过期  对象存活时间   无访问多长时间缓存失效
            memoryOnlyCache = new Cache(cacheName, 5000, false, false, 7100, 7100);
            //在内存管理器中添加缓存实例
            manager.addCache(memoryOnlyCache);
        }
    }


    /**
     * 创建缓存区间
     */
    public void createCacheByTime(String cacheName,Long timeToLiveSeconds) {
        Cache memoryOnlyCache = manager.getCache(cacheName);
        //建立一个缓存实例
        if (memoryOnlyCache == null) {
            //项目名称   缓存最大数量 是否缓存成文件 设定缓存是否过期  对象存活时间   无访问多长时间缓存失效
            memoryOnlyCache = new Cache(cacheName, 5000, false, false, timeToLiveSeconds, timeToLiveSeconds);
            //在内存管理器中添加缓存实例
            manager.addCache(memoryOnlyCache);
        }
    }

}
