package gabia.jaime.voting.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "Class : {}, Status : {}, Message : {}";

    @ExceptionHandler(CustomException.class)
    public ResponseEntity customExceptionHandler(CustomException e) {
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getStatus(), e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(e.getErrorResult());
    }

}
