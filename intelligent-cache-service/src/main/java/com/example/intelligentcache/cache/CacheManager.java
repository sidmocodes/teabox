package com.example.intelligentcache.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CacheManager {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // Current caching policy.
    private String cachingPolicy;

    // Inject default policy and allowed policies from configuration.
    @Value("${caching.default-policy}")
    private String defaultPolicy;

    @Value("${caching.policies}")
    private String policiesStr;

    private List<String> allowedPolicies;

    @PostConstruct
    public void init() {
        cachingPolicy = defaultPolicy;
        allowedPolicies = Arrays.stream(policiesStr.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        System.out.println("Allowed Policies: " + allowedPolicies);
        System.out.println("Default Policy: " + cachingPolicy);
    }

    // Get a value from the Redis cache.
    // If the key is not found, fetch content from the dummy content service, cache it, and return it.
    public String get(String key) {
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            // Cache miss: fetch content from dummy content service
            RestTemplate restTemplate = new RestTemplate();
            value = restTemplate.getForObject("http://dummy-content-service:5001/api/content?key=" + key, String.class);
            redisTemplate.opsForValue().set(key, value);
            return "Cache Miss: Fetched from content source: " + value;
        }
        return "Cache Hit: " + value;
    }

    // Set a value in the Redis cache.
    public String set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        return "Value cached successfully using policy: " + cachingPolicy;
    }

    // Update caching policy if the provided policy is allowed.
    public void setPolicy(String policy) {
        if (allowedPolicies.contains(policy)) {
            this.cachingPolicy = policy;
            System.out.println("Caching policy updated to: " + policy);
        } else {
            System.out.println("Policy " + policy + " is not allowed. Allowed policies: " + allowedPolicies);
        }
    }

    // Fetch a new caching policy from the AI module.
    public String fetchNewPolicyFromAI() {
        RestTemplate restTemplate = new RestTemplate();
        String policy = restTemplate.getForObject("http://ai-caching-service/api/policy", String.class);
        return policy;
    }
}