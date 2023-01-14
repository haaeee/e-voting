package gabia.jaime.voting.global.exception.conflict;

public class NotPendingAgendaException extends ConflictException {

    public NotPendingAgendaException() {
        super("보류 상태의 안건이 아닙니다.");
    }
}
