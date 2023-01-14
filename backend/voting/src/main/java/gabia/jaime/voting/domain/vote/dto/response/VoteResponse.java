package gabia.jaime.voting.domain.vote.dto.response;

import gabia.jaime.voting.domain.vote.entity.Vote;
import gabia.jaime.voting.domain.vote.entity.VoteType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VoteResponse {

    private final Long id;

    private final VoteType voteType;

    private final int voteCount;

    private final Long memberId;

    private final LocalDateTime createdAt;

    @Builder
    private VoteResponse(final Long id, final VoteType voteType, final int voteCount, final Long memberId,
                         final LocalDateTime createdAt) {
        this.id = id;
        this.voteType = voteType;
        this.voteCount = voteCount;
        this.memberId = memberId;
        this.createdAt = createdAt;
    }

    public static VoteResponse from(Vote vote) {
        return VoteResponse.builder()
                .id(vote.getId())
                .voteType(vote.getVoteType())
                .voteCount(vote.getVoteCount())
                .memberId(vote.getMember().getId())
                .createdAt(vote.getCreatedAt())
                .build();
    }
}
