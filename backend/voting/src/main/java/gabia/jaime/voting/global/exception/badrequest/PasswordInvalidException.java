package gabia.jaime.voting.global.exception.badrequest;

public class PasswordInvalidException extends BadRequestException {

    public PasswordInvalidException() {
        super("비밀번호가 올바르지 않습니다.");
    }
}
