package gabia.jaime.voting.domain.issue.entity;

import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.vote.entity.Vote;
import gabia.jaime.voting.domain.vote.entity.VoteType;
import gabia.jaime.voting.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static gabia.jaime.voting.domain.issue.entity.IssueStatus.CLOSE;
import static gabia.jaime.voting.domain.issue.entity.IssueStatus.OPEN;
import static gabia.jaime.voting.domain.vote.entity.VoteType.*;

@Entity
@Table(name = "issue")
@Getter @ToString(callSuper = true)
public class Issue extends BaseEntity {

    private static final int VALID_VOTE_COUNT = 10;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
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

    @Column(name = "yes_count", nullable = false)
    private int yesCount;

    @Column(name = "no_count", nullable = false)
    private int noCount;

    @Column(name = "give_up_count", nullable = false)
    private int giveUpCount;

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY)
    private Set<Vote> votes = new LinkedHashSet<>();

    protected Issue() {
    }

    @Builder
    private Issue(Long id, Agenda agenda, IssueType issueType, IssueStatus issueStatus, LocalDateTime startAt, LocalDateTime endAt,
                  int yesCount, int noCount, int giveUpCount) {
        this.id = id;
        this.agenda = agenda;
        this.issueType = issueType;
        this.issueStatus = issueStatus;
        this.startAt = startAt;
        this.endAt = endAt;
        this.yesCount = yesCount;
        this.noCount = noCount;
        this.giveUpCount = giveUpCount;
    }

    public static Issue of(Agenda agenda, IssueType issueType, LocalDateTime startAt, LocalDateTime endAt) {
        return Issue.builder()
                .agenda(agenda)
                .issueType(issueType)
                .issueStatus(OPEN)
                .startAt(startAt)
                .endAt(endAt)
                .yesCount(0)
                .noCount(0)
                .giveUpCount(0)
                .build();
    }

    public void close() {
        this.issueStatus = CLOSE;
        this.agenda.changeCompletedStatus();
    }

    public int getAvailableCount() {
        return VALID_VOTE_COUNT - (yesCount + noCount + giveUpCount);
    }

    public int getTotalVoteCount() {
        return yesCount + noCount + giveUpCount;
    }

    public void addVoteCount(final VoteType voteType, int voteCount) {
        if (voteType == YES) {
            this.yesCount += voteCount;
        }
        else if (voteType == NO) {
            this.noCount += voteCount;
        }
        else if (voteType == GIVE_UP) {
            this.giveUpCount += voteCount;
        }
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