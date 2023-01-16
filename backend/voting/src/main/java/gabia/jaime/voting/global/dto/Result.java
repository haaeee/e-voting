package gabia.jaime.voting.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    private Code code;
    private String message;
    private T data;

    @Builder
    public Result(Code code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result createErrorResult(String message) {
        return Result.builder()
                .code(Code.ERROR)
                .message(message)
                .data(null)
                .build();
    }

    public static <T> Result createSuccessResult(T data) {
        return Result.builder()
                .code(Code.SUCCESS)
                .data(data)
                .build();
    }

}