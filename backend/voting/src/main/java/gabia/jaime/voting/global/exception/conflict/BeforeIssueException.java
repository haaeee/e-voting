package gabia.jaime.voting.global.exception.conflict;

public class BeforeIssueException extends ConflictException {

    public BeforeIssueException() {
        super("이슈로 생성하기전에 완료가 될 수 없습니다.");
    }
}
