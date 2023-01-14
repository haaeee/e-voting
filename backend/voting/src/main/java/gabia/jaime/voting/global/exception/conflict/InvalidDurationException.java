package gabia.jaime.voting.global.exception.conflict;

public class InvalidDurationException extends ConflictException {

    public InvalidDurationException() {
        super("종료 시간은 시작 시간 이후로만 설정 가능합니다.");
    }
}
