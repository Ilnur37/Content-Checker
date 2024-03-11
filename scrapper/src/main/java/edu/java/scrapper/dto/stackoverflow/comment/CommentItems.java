package edu.java.scrapper.dto.stackoverflow.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class CommentItems {
    @JsonProperty("items")
    private List<CommentInfo> commentInfo;
}
