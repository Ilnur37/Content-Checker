package edu.java.scrapper.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class ListLinksResponse {
    private List<LinkResponse> links;
    private int size;
}
