package com.example.intelligentcache.service;

import com.example.intelligentcache.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private CacheManager cacheManager;

    public String getFromCache(String key) {
        return cacheManager.get(key);
    }

    public String setCache(String key, String value) {
        return cacheManager.set(key, value);
    }

    // Call the AI module to fetch a new caching policy and update it.
    public String updateCachingPolicy() {
        String newPolicy = cacheManager.fetchNewPolicyFromAI();
        cacheManager.setPolicy(newPolicy);
        return "Caching policy updated to: " + newPolicy;
    }
}