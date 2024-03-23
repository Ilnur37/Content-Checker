package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    Api api,
    Supported supported,

    AccessType databaseAccessType
) {
    public record Api(String gitUrl, String stackoverflowUrl, String botUrl) {
    }

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record Supported(String stackoverflow, String github) {
    }

    public enum AccessType {
        JDBC,
        JOOQ
    }
}
