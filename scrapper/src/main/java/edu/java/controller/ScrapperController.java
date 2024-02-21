package edu.java.controller;

import edu.java.response.LinkResponse;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("scrapper-api")
public class ScrapperController {
    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<?> registerChat(@PathVariable int id) {
        // Логика регистрации чата
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable int id) {
        // Логика удаления чата
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/links")
    public ResponseEntity<List<LinkResponse>> getAllLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId) {
        // Логика получения всех ссылок для указанного чата
        List<LinkResponse> links = new ArrayList<>();
        // Заполнение списка ссылок
        return new ResponseEntity<>(links, HttpStatus.OK);
    }

    /*@PostMapping("/links")
    public ResponseEntity<LinkResponse> addLink(@RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody AddLinkRequest addLinkRequest) {
        // Логика добавления ссылки для указанного чата
        LinkResponse linkResponse = new LinkResponse(); // Ваш объект LinkResponse с заполненными данными
        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }

    @DeleteMapping("/links")
    public ResponseEntity<LinkResponse> removeLink(@RequestHeader("Tg-Chat-Id") Long tgChatId, @RequestBody RemoveLinkRequest removeLinkRequest) {
        // Логика удаления ссылки для указанного чата
        LinkResponse linkResponse = new LinkResponse(); // Ваш объект LinkResponse с заполненными данными
        return new ResponseEntity<>(linkResponse, HttpStatus.OK);
    }*/
}
