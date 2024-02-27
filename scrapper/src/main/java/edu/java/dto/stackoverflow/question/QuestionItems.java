package edu.java.dto.stackoverflow.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class QuestionItems {
    @JsonProperty("items")
    private List<QuestionInfo> questionInfo;
}
