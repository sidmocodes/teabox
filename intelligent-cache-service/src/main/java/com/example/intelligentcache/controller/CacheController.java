package com.example.intelligentcache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.intelligentcache.service.CacheService;

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