package edu.java.dto.stackoverflow;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionInfo {
    private List<AnswerInfo> items;
}
