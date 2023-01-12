package gabia.jaime.voting.global.exception.forbidden;


public class AdminForbiddenException extends ForbiddenException {

    public AdminForbiddenException() {
        super("관리자만 안건을 생성할 수 있습니다.");
    }
}
