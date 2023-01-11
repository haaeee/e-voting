package gabia.jaime.voting.global.exception.forbidden;


import gabia.jaime.voting.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends CustomException {

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
