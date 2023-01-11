package gabia.jaime.voting.domain.member.entity;

import gabia.jaime.voting.domain.vote.entity.Vote;
import gabia.jaime.voting.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static gabia.jaime.voting.domain.member.entity.Role.*;

@Entity
@Table(name = "member", indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "nickname", unique = true),
        @Index(columnList = "createdAt")
})
@Getter @ToString(callSuper = true)
public class Member extends BaseEntity {

    public static final int ADMIN_RIGHT_COUNT = 0;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "vote_right_count", nullable = false)
    private Integer voteRightCount;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private Set<Vote> votes = new LinkedHashSet<>();

    protected Member() {
    }

    @Builder
    private Member(Long id, String email, String password, String nickname, Integer voteRightCount, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.voteRightCount = voteRightCount;
        this.role = role;
    }

    public static Member createShareHolder(String email, String password, String nickname, Integer voteRightCount) {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .voteRightCount(voteRightCount)
                .role(ROLE_SHAREHOLDER)
                .build();
    }

    public static Member createAdministrator(String email, String password, String nickname) {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .voteRightCount(ADMIN_RIGHT_COUNT)
                .role(ROLE_ADMIN)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Member member = (Member) o;
        return getId().equals(member.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}