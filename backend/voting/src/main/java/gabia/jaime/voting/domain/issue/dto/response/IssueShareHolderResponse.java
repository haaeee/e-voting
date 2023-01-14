package gabia.jaime.voting.domain.issue.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import gabia.jaime.voting.domain.agenda.dto.response.AgendaSimpleResponse;
import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.issue.entity.IssueStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IssueShareHolderResponse implements IssueResponse {

    private final Long id;

    private final IssueStatus issueStatus;

    private final int yesCount;

    private final int noCount;

    private final int giveUpCount;

    @JsonProperty("agenda")
    private final AgendaSimpleResponse agendaSimpleResponse;

    public IssueShareHolderResponse(final Issue issue) {
        this.id = issue.getId();
        this.issueStatus = issue.getIssueStatus();
        this.yesCount = issue.getYesCount();
        this.noCount = issue.getNoCount();
        this.giveUpCount = issue.getGiveUpCount();
        this.agendaSimpleResponse = AgendaSimpleResponse.from(issue.getAgenda());
    }

    public static IssueShareHolderResponse from(final Issue issue) {
        return new IssueShareHolderResponse(issue);
    }
}
