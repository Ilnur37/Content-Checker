package edu.java.bot.domain;

import java.util.regex.Pattern;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Setter
@Component
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "supported")
public class SupportedDomain {
    private static final String HTTPS = "https://";
    private String stackoverflow;
    private String github;

    public boolean isValid(String link) {
        if (link.contains(github)) {
            return isValidGitHub(link, github);
        }
        if (link.contains(stackoverflow)) {
            return isValidStackOverFlow(link, stackoverflow);
        }
        return false;
    }

    private boolean isValidGitHub(String link, String regex) {
        String currRegex = HTTPS + regex + "/[^/]+/[^/]+";
        return Pattern.matches(currRegex, link);
    }

    private boolean isValidStackOverFlow(String link, String regex) {
        String currRegex = HTTPS + regex + "/[^/]+/[^/]+/[^/]+";
        return Pattern.matches(currRegex, link);
    }

}
