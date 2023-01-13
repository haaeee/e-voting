package gabia.jaime.voting.global.exception.conflict;

public class PasswordInvalidException extends ConflictException {

    public PasswordInvalidException() {
        super("비밀번호가 올바르지 않습니다.");
    }
}
