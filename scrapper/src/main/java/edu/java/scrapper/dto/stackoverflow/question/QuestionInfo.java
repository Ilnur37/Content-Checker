package edu.java.scrapper.dto.stackoverflow.question;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.scrapper.dto.stackoverflow.OwnerInfo;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionInfo {
    private OwnerInfo owner;

    @JsonProperty("answer_count")
    private int answerCount;

    @JsonProperty("last_activity_date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private OffsetDateTime lastActivityDate;

    @JsonProperty("last_edit_date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private OffsetDateTime lastEditDate;

    @JsonProperty("question_id")
    private long questionId;

    private String title;
}
