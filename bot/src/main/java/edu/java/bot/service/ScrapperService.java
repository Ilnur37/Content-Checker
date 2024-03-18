package edu.java.bot.service;

import edu.java.bot.client.ScrapperClient;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperService {
    private final ScrapperClient scrapperClient;

    public void registerChat(long id) {
        scrapperClient.registerChat(id);
    }

    public void deleteChat(long id) {
        scrapperClient.deleteChat(id);
    }

    public ListLinksResponse getAllLinks(long id) {
        return scrapperClient.getAllLinks(id);
    }

    public LinkResponse addLink(long id, String link) {
        return scrapperClient.addLink(id, link);
    }

    public LinkResponse removeLink(long id, String link) {
        return scrapperClient.removeLink(id, link);
    }
}
