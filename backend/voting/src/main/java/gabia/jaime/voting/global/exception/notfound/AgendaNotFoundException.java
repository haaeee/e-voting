package gabia.jaime.voting.global.exception.notfound;


public class AgendaNotFoundException extends NotFoundException {

    public AgendaNotFoundException() {
        super("안건을 찾을 수 없습니다.");
    }
}
