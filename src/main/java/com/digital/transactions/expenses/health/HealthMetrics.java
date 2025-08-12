package com.digital.transactions.expenses.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HealthMetrics {

    private static final Logger logger = LoggerFactory.getLogger(HealthMetrics.class);

    private final ApplicationContext applicationContext;

    public HealthMetrics(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Scheduled(fixedRate = 60000) // Runs every 1 minute (60000 milliseconds)
    public void logHealthIndicators() {
        Map<String, Object> healthIndicators = applicationContext.getBeansWithAnnotation(LoggableHealthIndicator.class);

        healthIndicators.forEach((beanName, bean) -> {
            if (bean instanceof HealthIndicator) {
                HealthIndicator indicator = (HealthIndicator) bean;
                String indicatorName = bean.getClass().getSimpleName();
                LoggableHealthIndicator annotation = bean.getClass().getAnnotation(LoggableHealthIndicator.class);
                if (annotation != null && !annotation.value().isEmpty()) {
                    indicatorName = annotation.value(); // Use the annotation's value if provided
                }

                try {
                    var health = indicator.health();
                    logger.info("Health Indicator: {}, Status: {}, Details: {}", indicatorName, health.getStatus(), health.getDetails());
                } catch (Exception e) {
                    logger.error("Error executing health indicator: " + indicatorName, e);
                }
            }
        });
    }
}