package gabia.jaime.voting.global.exception.notfound;


public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException() {
        super("회원을 찾을 수 없습니다.");
    }
}
