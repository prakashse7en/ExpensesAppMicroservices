package com.digital.transactions.expenses.event.consumer;

import com.digital.transactions.expenses.service.UserProfileService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaConsumerServiceImpl {
    private static final String TOPIC = "my-topic";

    @Autowired
    private CacheManager cacheManager;

    private Logger logger = LoggerFactory.getLogger(KafkaConsumerServiceImpl.class);

    @Autowired
    UserProfileService userProfileService;



    @KafkaListener(topics = TOPIC, groupId = "kafka-demo-group1")
    public void consume(ConsumerRecord<String, byte[]> record) {
        String key = record.key();
        String userId = new String(record.value());
        System.out.println("Consumed message: Key = " + key + ", Value = " + (userId));

        userProfileService.evictUserProfileCache(UUID.fromString(userId));
        /*//cacheManager.getCache("userprofileCache").evict(value);
        Cache cache = cacheManager.getCache("userprofileCache");
        if (cache != null) {

            if(cache.evictIfPresent("userprofileCache::ec95ae18-b0df-4e35-b08b-246139cfeb46")){
                System.out.println("cache is present");
            }
            cache.evict("userprofileCache::ec95ae18-b0df-4e35-b08b-246139cfeb46");  // Manually evict the cache entry
            System.out.println("Evicted user cache for userId: " + userId);
        }*/
    }



}
