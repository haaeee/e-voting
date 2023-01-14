package gabia.jaime.voting.global.exception.conflict;

public class DuplicatedNicknameException extends ConflictException {

    public DuplicatedNicknameException() {
        super("닉네임은 중복 될 수 없습니다.");
    }
}
