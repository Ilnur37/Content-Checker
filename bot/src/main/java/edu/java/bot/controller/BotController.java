package edu.java.bot.controller;

import edu.java.bot.request.LinkUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bot-api")
public class BotController {
    @PostMapping("/updates")
    public ResponseEntity<?> sendUpdate(@Valid @RequestBody LinkUpdateRequest linkUpdateRequest) {

        //работа сервиса

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
