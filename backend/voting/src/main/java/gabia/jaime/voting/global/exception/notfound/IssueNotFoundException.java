package gabia.jaime.voting.global.exception.notfound;


public class IssueNotFoundException extends NotFoundException {

    public IssueNotFoundException() {
        super("현안을 찾을 수 없습니다.");
    }
}
