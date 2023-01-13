package gabia.jaime.voting.domain.issue.web;

import gabia.jaime.voting.domain.vote.dto.request.VoteCreateRequest;
import gabia.jaime.voting.domain.vote.service.VoteService;
import gabia.jaime.voting.global.security.MemberDetails;
import gabia.jaime.voting.global.util.ClassUtils;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/issues")
public class IssueApiController {

    private final VoteService voteService;

    public IssueApiController(final VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/{issueId}")
    public ResponseEntity<Void> vote(@RequestBody final VoteCreateRequest voteCreateRequest, final Authentication authentication,
                                     @PathVariable final Long issueId) {
        final MemberDetails memberDetails = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), MemberDetails.class);
        final Long savedId = voteService.vote(memberDetails, voteCreateRequest, issueId);
        return ResponseEntity.created(URI.create("/api/v1/issues/" + savedId))
                .build();
    }
}
