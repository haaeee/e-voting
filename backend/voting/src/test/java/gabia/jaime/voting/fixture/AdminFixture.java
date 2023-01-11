package gabia.jaime.voting.fixture;

import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.domain.member.entity.Member.MemberBuilder;
import gabia.jaime.voting.domain.member.entity.Role;

public enum AdminFixture {

    ADMIN1("admin1@email.com", "{noop}1234", "amdin1", Role.ADMIN),
    ADMIN2("admin2@email.com", "{noop}1234", "admin2", Role.ADMIN);

    private final String email;
    private final String password;
    private final String nickname;
    private final Role role;

    AdminFixture(String email, String password, String nickname, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public Member createAdmin() {
        return createAdmin(null);
    }

    public Member createAdmin(Long id) {
        return createMemberBuilder(id).build();
    }

    public MemberBuilder createMemberBuilder(Long id) {
        return Member.builder()
                .id(id)
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .role(this.role);
    }

}
