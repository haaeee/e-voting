package gabia.jaime.voting.global.exception.conflict;

public class ClosedIssueException extends ConflictException {

    public ClosedIssueException() {
        super("이미 종료된 현안 입니다.");
    }
}
