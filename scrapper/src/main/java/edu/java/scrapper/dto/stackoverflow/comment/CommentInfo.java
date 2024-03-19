package edu.java.scrapper.dto.stackoverflow.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.java.scrapper.dto.stackoverflow.OwnerInfo;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentInfo {
    private OwnerInfo owner;

    @JsonProperty("creation_date")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private OffsetDateTime creationDate;
}
