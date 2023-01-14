package gabia.jaime.voting.domain.issue.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IssueExceptionResponse implements IssueResponse {

    private final Long id;

    private final String message;
}
