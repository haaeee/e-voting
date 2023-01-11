package gabia.jaime.voting.global.exception.badrequest;


import gabia.jaime.voting.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
