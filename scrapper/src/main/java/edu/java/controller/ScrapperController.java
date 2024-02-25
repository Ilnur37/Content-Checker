package edu.java.controller;

import edu.java.dto.request.AddLinkRequest;
import edu.java.dto.request.RemoveLinkRequest;
import edu.java.dto.response.LinkResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("scrapper-api")
public class ScrapperController {
    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> registerChat(@Min(1) @PathVariable int id) {
        // Логика регистрации чата
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(@Min(1) @PathVariable int id) {
        // Логика удаления чата
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/links")
    public ResponseEntity<List<LinkResponse>> getAllLinks(@Min(1) @RequestHeader("Tg-Chat-Id") int tgChatId) {
        // Логика получения всех ссылок для указанного чата
        List<LinkResponse> links = new ArrayList<>();
        // Заполнение списка ссылок
        return new ResponseEntity<>(links, HttpStatus.OK);
    }

    @PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(
        @Min(1) @RequestHeader("Tg-Chat-Id") int tgChatId,
        @Valid @RequestBody AddLinkRequest addLinkRequest
    ) {
        // Логика добавления ссылки для указанного чата
        LinkResponse linkResponse = new LinkResponse(); // объект LinkResponse с заполненными данными
        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> removeLink(
        @Min(1) @RequestHeader("Tg-Chat-Id") int tgChatId,
        @Valid @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        // Логика удаления ссылки для указанного чата
        LinkResponse linkResponse = new LinkResponse(); // объект LinkResponse с заполненными данными
        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }
}
