package gabia.jaime.voting.domain.agenda.dto.request;

import gabia.jaime.voting.domain.issue.entity.IssueType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgendaToIssueRequest {

    private IssueType issueType;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Builder
    public AgendaToIssueRequest(final IssueType issueType, final LocalDateTime startAt, final LocalDateTime endAt) {
        this.issueType = issueType;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
