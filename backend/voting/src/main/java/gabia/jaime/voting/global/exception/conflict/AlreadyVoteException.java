package gabia.jaime.voting.global.exception.conflict;

public class AlreadyVoteException extends ConflictException {

    public AlreadyVoteException() {
        super("이미 투표를 하신 현안 입니다.");
    }
}
