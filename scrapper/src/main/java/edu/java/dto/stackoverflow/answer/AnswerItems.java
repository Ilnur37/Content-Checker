package edu.java.dto.stackoverflow.answer;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class AnswerItems {
    @JsonProperty("items")
    private List<AnswerInfo> answerInfo;
}
