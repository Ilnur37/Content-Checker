package edu.java.scrapper.dto.request.bot;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LinkUpdateRequest {
    private int id;
    private String url;
    private String description;
    private List<Integer> tgChatIds;
}
