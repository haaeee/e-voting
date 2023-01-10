package gabia.jaime.voting.domain.issue.entity;

import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.vote.entity.Vote;
import gabia.jaime.voting.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "issue")
@Getter @ToString(callSuper = true)
public class Issue extends BaseEntity {

    private static final int VALID_VOTE_COUNT = 10;
    private static final int NO_LIMITED_ISSUE_COUNT_NUMBER_FLAG = -1;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agenda_id", nullable = false)
    private Agenda agenda;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_type", nullable = false)
    private IssueType issueType;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_status", nullable = false)
    private IssueStatus issueStatus;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    /**
     * <p>선착순 의결권 : 10</p>
     * <p>제한 없는 투표 : -1</p>
     */
    @Column(name = "vote_count", nullable = false)
    private int voteCount;

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY)
    private Set<Vote> votes = new LinkedHashSet<>();

    protected Issue() {
    }

    @Builder(access = AccessLevel.PRIVATE)
    private Issue(Long id, Agenda agenda, IssueType issueType, IssueStatus issueStatus, LocalDateTime startAt, LocalDateTime endAt, int voteCount) {
        this.id = id;
        this.agenda = agenda;
        this.issueType = issueType;
        this.issueStatus = issueStatus;
        this.startAt = startAt;
        this.endAt = endAt;
        this.voteCount = voteCount;
    }

    public static Issue createLimitedIssue(Agenda agenda, LocalDateTime startAt, LocalDateTime endAt) {
        return Issue.builder()
                .agenda(agenda)
                .issueType(IssueType.LIMITED)
                .issueStatus(IssueStatus.OPEN)
                .startAt(startAt)
                .endAt(endAt)
                .voteCount(VALID_VOTE_COUNT)
                .build();
    }

    public static Issue createNoLimitedIssue(Agenda agenda, LocalDateTime startAt, LocalDateTime endAt) {
        return Issue.builder()
                .agenda(agenda)
                .issueType(IssueType.NO_LIMITED)
                .issueStatus(IssueStatus.OPEN)
                .startAt(startAt)
                .endAt(endAt)
                .voteCount(NO_LIMITED_ISSUE_COUNT_NUMBER_FLAG)
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
        final Issue issue = (Issue) o;
        return getId().equals(issue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}