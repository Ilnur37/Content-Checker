package edu.java.models.dto.response;

import edu.java.models.ReasonOfError;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorResponse {
    private String description;
    private String code;
    private String exceptionName;
    private String exceptionMessage;
    private ReasonOfError reasonOfError;
    private List<String> stacktrace;

    public static ApiErrorResponse toApiErrorResponse(
        Throwable ex,
        String description,
        String code,
        ReasonOfError reasonOfError
    ) {
        return ApiErrorResponse.builder()
            .description(description)
            .code(code)
            .exceptionName(ex.getClass().getName())
            .exceptionMessage(ex.getMessage())
            .reasonOfError(reasonOfError)
            .stacktrace(getStacktrace(ex))
            .build();
    }

    private static List<String> getStacktrace(Throwable ex) {
        return Arrays.stream(ex.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.toList());
    }
}
