package edu.java.bot.service;

import edu.java.bot.client.ScrapperClient;
import edu.java.bot.dto.response.scrapper.LinkResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperService {
    private final ScrapperClient scrapperClient;

    public ResponseEntity<Void> registerChat(int id) {
        return scrapperClient.registerChat(id);
    }

    public ResponseEntity<Void> deleteChat(int id) {
        return scrapperClient.deleteChat(id);
    }

    public ResponseEntity<List<LinkResponse>> getAllLinks(int id) {
        return scrapperClient.getAllLinks(id);
    }

    public ResponseEntity<LinkResponse> addLink(int id, String link) {
        return scrapperClient.addLink(id, link);
    }

    public ResponseEntity<LinkResponse> removeLink(int id, String link) {
        return scrapperClient.removeLink(id, link);
    }
}
