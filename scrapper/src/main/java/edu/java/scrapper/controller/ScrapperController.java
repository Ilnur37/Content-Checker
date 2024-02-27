package edu.java.scrapper.controller;

import edu.java.scrapper.dto.request.AddLinkRequest;
import edu.java.scrapper.dto.request.RemoveLinkRequest;
import edu.java.scrapper.dto.response.LinkResponse;
import edu.java.scrapper.dto.response.ListLinksResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("scrapper-api")
public class ScrapperController implements ScrapperApi {
    @Override
    public ResponseEntity<Void> registerChat(@Min(1) @PathVariable long id) {
        // Логика регистрации чата
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteChat(@Min(1) @PathVariable long id) {
        // Логика удаления чата
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ListLinksResponse> getAllLinks(@Min(1) @RequestHeader("Tg-Chat-Id") long tgChatId) {
        // Логика получения всех ссылок для указанного чата
        ListLinksResponse links = new ListLinksResponse();
        // Заполнение списка ссылок
        return new ResponseEntity<>(links, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LinkResponse> addLink(
        @Min(1) @RequestHeader("Tg-Chat-Id") long tgChatId,
        @Valid @RequestBody AddLinkRequest addLinkRequest
    ) {
        // Логика добавления ссылки для указанного чата
        LinkResponse linkResponse = new LinkResponse(); // объект LinkResponse с заполненными данными
        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<LinkResponse> removeLink(
        @Min(1) @RequestHeader("Tg-Chat-Id") long tgChatId,
        @Valid @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        // Логика удаления ссылки для указанного чата
        LinkResponse linkResponse = new LinkResponse(); // объект LinkResponse с заполненными данными
        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }
}
