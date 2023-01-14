package gabia.jaime.voting.domain.issue.web;

import gabia.jaime.voting.domain.issue.dto.response.IssueResponse;
import gabia.jaime.voting.domain.issue.service.IssueService;
import gabia.jaime.voting.domain.vote.dto.request.VoteCreateRequest;
import gabia.jaime.voting.domain.vote.dto.response.VoteCreateResponse;
import gabia.jaime.voting.domain.vote.service.VoteService;
import gabia.jaime.voting.global.dto.Result;
import gabia.jaime.voting.global.security.MemberDetails;
import gabia.jaime.voting.global.util.ClassUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/issues")
public class IssueApiController {

    private final VoteService voteService;

    private final IssueService issueService;

    public IssueApiController(final VoteService voteService, final IssueService issueService) {
        this.voteService = voteService;
        this.issueService = issueService;
    }

    @PostMapping("/{issueId}")
    public ResponseEntity<Result> vote(@RequestBody final VoteCreateRequest voteCreateRequest, final Authentication authentication,
                                     @PathVariable final Long issueId) {
        final MemberDetails memberDetails = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), MemberDetails.class);
        final VoteCreateResponse response = voteService.vote(memberDetails, voteCreateRequest, issueId);

        // TODO: 현재 response 에서 id null 로 성공, 실패를 판별...(dirty)
        if (response.getId() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Result.createSuccessResult(response));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Result.createSuccessResult(response));
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<Result> search(final Authentication authentication, @PathVariable final Long issueId) {
        final MemberDetails memberDetails = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), MemberDetails.class);
        final IssueResponse response =  issueService.search(memberDetails, issueId);
        return ResponseEntity.ok(Result.createSuccessResult(response));
    }
}
