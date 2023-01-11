package gabia.jaime.voting.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberJoinRequest {

    private String email;

    private String password;

    private String nickname;

    private Integer voteRightCount;

    private MemberJoinRequest() {
    }

    @Builder
    public MemberJoinRequest(final String email, final String password, final String nickname, final Integer voteRightCount) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.voteRightCount = voteRightCount;
    }
}
