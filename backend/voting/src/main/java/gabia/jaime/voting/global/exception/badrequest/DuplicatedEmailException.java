package gabia.jaime.voting.global.exception.badrequest;

public class DuplicatedEmailException extends BadRequestException {

    public DuplicatedEmailException() {
        super("이메일은 중복 될 수 없습니다.");
    }
}
