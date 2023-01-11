package gabia.jaime.voting.domain.member.dto.response;

import lombok.Getter;

@Getter
public class MemberLoginResponse {

    private final String accessToken;

    private MemberLoginResponse(final String accessToken) {
        this.accessToken = accessToken;
    }

    public static MemberLoginResponse of(final String accessToken) {
        return new MemberLoginResponse(accessToken);
    }
}
