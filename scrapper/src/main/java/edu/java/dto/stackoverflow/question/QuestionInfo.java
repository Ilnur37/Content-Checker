package edu.java.dto.stackoverflow.question;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.dto.stackoverflow.OwnerInfo;
import edu.java.dto.stackoverflow.answer.AnswerInfo;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionInfo {
    @JsonIgnore
    private List<AnswerInfo> answers;

    private OwnerInfo owner;

    @JsonProperty("answer_count")
    private int answerCount;

    @JsonProperty("last_activity_date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private OffsetDateTime lastActivityDate;

    @JsonProperty("creation_date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private OffsetDateTime creationDate;

    @JsonProperty("question_id")
    private long questionId;
}
