package gabia.jaime.voting.global.exception.forbidden;


public class AdminForbiddenException extends ForbiddenException {

    public AdminForbiddenException() {
        super("관리자만 안건 생성 / 현안으로 변경할 수 있습니다.");
    }
}
