package gabia.jaime.voting.global.exception.conflict;


import gabia.jaime.voting.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ConflictException extends CustomException {

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
