package edu.java.bot.model.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SupportedDomain {
    private static final List<String> SUPPORTED_DOMAIN = new ArrayList<>();

    public SupportedDomain() {
        SUPPORTED_DOMAIN.add("stackoverflow.com");
        SUPPORTED_DOMAIN.add("github.com");
    }

    public boolean isValid(String link) {
        URI uri = null;
        try {
            uri = new URI(link);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String host = uri.getHost();
        for (String domain : SUPPORTED_DOMAIN) {
            if (domain.equals(host)) {
                return true;
            }
        }
        return false;
    }
}
