package edu.java.bot.model.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "supported")
public class SupportedDomain {
    private static List<String> domains;

    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        SupportedDomain.domains = domains;
    }

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
