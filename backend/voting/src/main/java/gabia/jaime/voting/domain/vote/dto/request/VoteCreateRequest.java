package gabia.jaime.voting.domain.vote.dto.request;

import gabia.jaime.voting.domain.vote.entity.VoteType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VoteCreateRequest {

    private VoteType voteType;

    public VoteCreateRequest(final VoteType voteType) {
        this.voteType = voteType;
    }

}
