package gabia.jaime.voting.domain.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberLoginRequest {

    private String email;

    private String password;

    private MemberLoginRequest() {
    }

    @Builder
    public MemberLoginRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
