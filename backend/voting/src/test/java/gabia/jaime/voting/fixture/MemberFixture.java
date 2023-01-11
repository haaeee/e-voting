package gabia.jaime.voting.fixture;

import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.domain.member.entity.Member.MemberBuilder;
import gabia.jaime.voting.domain.member.entity.Role;

public enum MemberFixture {

    MEMBER_1("member1@email.com", "1234", "memeber1", Role.SHAREHOLDER, 5),
    MEMBER_2("member2@email.com", "1234", "memeber2", Role.SHAREHOLDER, 1),
    MEMBER_3("member3@email.com", "1234", "memeber3", Role.SHAREHOLDER, 7);

    private final String email;
    private final String password;
    private final String nickname;
    private final Integer voteRightCount;
    private final Role role;

    MemberFixture(String email, String password, String nickname, Role role, Integer voteRightCount) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.voteRightCount = voteRightCount;
        this.role = role;
    }

    public Member createMember() {
        return createMember(null);
    }

    public Member createMember(Long id) {
        return createMemberBuilder(id).build();
    }

    public MemberBuilder createMemberBuilder(Long id) {
        return Member.builder()
                .id(id)
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .role(this.role)
                .voteRightCount(this.voteRightCount);
    }

}
