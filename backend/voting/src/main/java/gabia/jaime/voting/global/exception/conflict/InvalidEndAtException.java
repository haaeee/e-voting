package gabia.jaime.voting.global.exception.conflict;

public class InvalidEndAtException extends ConflictException {

    public InvalidEndAtException() {
        super("종료 시각은 현재 시각 이후로만 설정 가능합니다.");
    }
}
