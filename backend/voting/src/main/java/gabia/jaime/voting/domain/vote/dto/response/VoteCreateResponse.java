package gabia.jaime.voting.domain.vote.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class VoteCreateResponse {

    private final Long id;

    private final String message;

    public static VoteCreateResponse success(final Long id) {
        return new VoteCreateResponse(id, "투표가 정상적으로 반영되었습니다.");
    }

    public static VoteCreateResponse fail() {
        return new VoteCreateResponse(null, "투표가 반영되지 않았습니다.");
    }
}
