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

    public void registerChat(int id) {
        scrapperClient.registerChat(id);
    }

    public void deleteChat(int id) {
        scrapperClient.deleteChat(id);
    }

    public ListLinksResponse getAllLinks(int id) {
        return scrapperClient.getAllLinks(id);
    }

    public LinkResponse addLink(int id, String link) {
        return scrapperClient.addLink(id, link);
    }

    public LinkResponse removeLink(int id, String link) {
        return scrapperClient.removeLink(id, link);
    }
}
