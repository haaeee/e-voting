package gabia.jaime.voting.global.exception.unauthorized;



import gabia.jaime.voting.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends CustomException {

    public UnAuthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
