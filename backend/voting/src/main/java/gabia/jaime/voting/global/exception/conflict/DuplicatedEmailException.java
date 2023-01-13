package gabia.jaime.voting.global.exception.conflict;

public class DuplicatedEmailException extends ConflictException {

    public DuplicatedEmailException() {
        super("이메일은 중복 될 수 없습니다.");
    }
}
