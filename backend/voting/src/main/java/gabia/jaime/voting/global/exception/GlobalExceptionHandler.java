package gabia.jaime.voting.global.exception;

import gabia.jaime.voting.global.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "Class : {}, Status : {}, Message : {}";

    @ExceptionHandler(CustomException.class)
    public ResponseEntity customExceptionHandler(CustomException e) {
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getStatus(), e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(e.getErrorResult());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity runtimeExceptionHandler(RuntimeException e) {
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), 500, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.createErrorResult(e.toString()));
    }
}
