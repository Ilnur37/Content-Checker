package edu.java.scrapper.controller;

import edu.java.models.dto.request.AddLinkRequest;
import edu.java.models.dto.request.RemoveLinkRequest;
import edu.java.models.dto.response.LinkResponse;
import edu.java.models.dto.response.ListLinksResponse;
import edu.java.scrapper.service.jdbc.JdbcChatService;
import edu.java.scrapper.service.jdbc.JdbcLinkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("scrapper-api")
@RequiredArgsConstructor
public class ScrapperController implements ScrapperApi {
    private final JdbcChatService chatService;
    private final JdbcLinkService linkService;
    private final String defaultLink = "aa";
    private final int defaultId = 1;

    @Override
    public ResponseEntity<Void> registerChat(@Min(1) @PathVariable long id) {
        chatService.register(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteChat(@Min(1) @PathVariable long id) {
        chatService.unregister(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ListLinksResponse> getAllLinks(@Min(1) @RequestHeader("Tg-Chat-Id") long tgChatId) {
        List<LinkResponse> linkResponses = linkService.getAll(tgChatId)
            .stream()
            .map(link -> new LinkResponse(link.getId(), link.getUrl()))
            .toList();
        ListLinksResponse links = new ListLinksResponse(linkResponses, linkResponses.size());
        return new ResponseEntity<>(links, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LinkResponse> addLink(
        @Min(1) @RequestHeader("Tg-Chat-Id") long tgChatId,
        @Valid @RequestBody AddLinkRequest addLinkRequest
    ) {
        //linkService.add(tgChatId, addLinkRequest.link());
        // объект LinkResponse с заполненными данными
        LinkResponse linkResponse = new LinkResponse(defaultId, defaultLink);
        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LinkResponse> removeLink(
        @Min(1) @RequestHeader("Tg-Chat-Id") long tgChatId,
        @Valid @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        // Логика удаления ссылки для указанного чата
        LinkResponse linkResponse =
            new LinkResponse(defaultId, defaultLink); // объект LinkResponse с заполненными данными
        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }
}
