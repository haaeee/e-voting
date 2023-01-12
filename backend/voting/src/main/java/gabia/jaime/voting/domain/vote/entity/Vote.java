package gabia.jaime.voting.domain.vote.entity;

import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "vote")
public class Vote {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type", nullable = false)
    private VoteType voteType;

    @Column(name = "vote_count", nullable = false)
    private int voteCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    protected Vote() {
    }

    @Builder
    private Vote(Long id, VoteType voteType, int voteCount, Issue issue, Member member) {
        this.id = id;
        this.voteType = voteType;
        this.voteCount = voteCount;
        this.issue = issue;
        this.member = member;
    }

    public static Vote createYes(Issue issue, Member member, int voteCount) {
        return Vote.builder()
                .voteType(VoteType.YES)
                .voteCount(voteCount)
                .issue(issue)
                .member(member)
                .build();
    }

    public static Vote createNo(Issue issue, Member member, int voteCount) {
        return Vote.builder()
                .voteType(VoteType.NO)
                .voteCount(voteCount)
                .issue(issue)
                .member(member)
                .build();
    }

    public static Vote createGiveUp(Issue issue, Member member, int voteCount) {
        return Vote.builder()
                .voteType(VoteType.GIVE_UP)
                .voteCount(voteCount)
                .issue(issue)
                .member(member)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return getId().equals(vote.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}