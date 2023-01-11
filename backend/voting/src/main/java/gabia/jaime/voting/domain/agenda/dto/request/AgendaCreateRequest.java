package gabia.jaime.voting.domain.agenda.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import gabia.jaime.voting.domain.agenda.entity.AgendaStatus;
import gabia.jaime.voting.domain.issue.entity.IssueType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AgendaCreateRequest {

    private String title;

    private String content;

    @JsonProperty("agendaStatus")
    private AgendaStatus agendaStatus;
    @JsonProperty("issueType")
    private IssueType issueType;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Builder
    public AgendaCreateRequest(final String title, final String content, final AgendaStatus agendaStatus, final IssueType issueType,
                               final LocalDateTime startAt, final LocalDateTime endAt) {
        this.title = title;
        this.content = content;
        this.agendaStatus = agendaStatus;
        this.issueType = issueType;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
