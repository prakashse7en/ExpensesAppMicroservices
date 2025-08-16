package com.digital.transactions.expenses.event.consumer;

import com.digital.transactions.expenses.service.UserProfileService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaConsumerServiceImpl {
    private static final String TOPIC = "my-topic";

    @Autowired
    private CacheManager cacheManager;

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerServiceImpl.class);

    @Autowired
    UserProfileService userProfileService;



    @KafkaListener(topics = TOPIC, groupId = "kafka-demo-group1")
    public void consume(ConsumerRecord<String, byte[]> record) {
        String key = record.key();
        String userId = new String(record.value());
        logger.debug("Consumed message: Key = {}, Value = {}", key, userId);
        userProfileService.evictUserProfileCache(UUID.fromString(userId));
    }



}
