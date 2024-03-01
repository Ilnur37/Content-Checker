package edu.java.bot.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "supported")
public class SupportedDomain {
    private List<String> domains;

    public boolean isValid(String link) {
        URI uri = null;
        try {
            uri = new URI(link);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String host = uri.getHost();
        return domains.contains(host);
    }
}
