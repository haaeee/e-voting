package gabia.jaime.voting.global.exception.forbidden;


public class AdminCanNotVoteException extends ForbiddenException {

    public AdminCanNotVoteException() {
        super("관리자는 투표하실 수 없습니다.");
    }
}
