package gabia.jaime.voting.global.exception.conflict;

public class InvalidDurationException extends ConflictException {

    public InvalidDurationException() {
        super("종료시각은 시작시각 이후로만 설정 가능합니다.");
    }
}
