package gabia.jaime.voting.domain.member.dto.response;

import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.domain.member.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberJoinResponse {

    private final Long id;

    private final String email;

    private final Integer voteRightCount;

    private final Role role;

    @Builder
    private MemberJoinResponse(final Long id, final String email, final Integer voteRightCount, final Role role) {
        this.id = id;
        this.email = email;
        this.voteRightCount = voteRightCount;
        this.role = role;
    }

    public static MemberJoinResponse from(Member member) {
        return MemberJoinResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .voteRightCount(member.getVoteRightCount())
                .role(member.getRole())
                .build();
    }
}
