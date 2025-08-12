package com.digital.transactions.expenses.health;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // Can be applied to classes
@Retention(RetentionPolicy.RUNTIME) // Available at runtime
@Component // Makes it a Spring component
public @interface LoggableHealthIndicator {
    String value() default ""; // Optional name for the health indicator
}
