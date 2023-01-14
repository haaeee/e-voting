package gabia.jaime.voting.domain.issue.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.vote.dto.response.VoteResponse;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class IssueAdminResponse extends IssueShareHolderResponse implements IssueResponse {

    @JsonProperty("votes")
    private final Set<VoteResponse> votes;

    public IssueAdminResponse(final Issue issue) {
        super(issue);
        this.votes = issue.getVotes().stream()
                .map(VoteResponse::from)
                .collect(Collectors.toUnmodifiableSet());
    }

    public static IssueAdminResponse from(final Issue issue) {
        return new IssueAdminResponse(issue);
    }

}
