package com.digital.transactions.expenses.health.mysql;

import com.digital.transactions.expenses.health.LoggableHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@LoggableHealthIndicator("mysqlHealthIndicator") // Register as a Spring component with a custom name
@Component
public class MySqlHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public MySqlHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            // Attempt to get a connection to verify database connectivity
            if (connection.isValid(1)) { // Check if the connection is valid within 1 second
                return Health.up().withDetail("database", "MySQL").build();
            } else {
                return Health.down().withDetail("database", "MySQL").withDetail("error", "Invalid connection").build();
            }
        } catch (SQLException e) {
            return Health.down().withDetail("database", "MySQL").withDetail("error", e.getMessage()).build();
        }
    }
}