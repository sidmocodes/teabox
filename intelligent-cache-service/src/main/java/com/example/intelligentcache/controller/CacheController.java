package com.example.intelligentcache.controller;

import com.example.intelligentcache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    @Autowired
    private CacheService cacheService;

    // Retrieve a value from the cache.
    @GetMapping("/get")
    public String getFromCache(@RequestParam String key) {
        return cacheService.getFromCache(key);
    }

    // Store a value in the cache.
    @PostMapping("/set")
    public String setCache(@RequestParam String key, @RequestParam String value) {
        return cacheService.setCache(key, value);
    }

    // Update caching policy dynamically using AI module recommendation.
    @PostMapping("/updatePolicy")
    public String updateCachingPolicy() {
        return cacheService.updateCachingPolicy();
    }
}