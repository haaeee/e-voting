package gabia.jaime.voting.domain.agenda.web;

import gabia.jaime.voting.domain.agenda.dto.request.AgendaCreateRequest;
import gabia.jaime.voting.domain.agenda.dto.request.AgendaToIssueRequest;
import gabia.jaime.voting.domain.agenda.dto.response.AgendaResponse;
import gabia.jaime.voting.domain.agenda.service.AgendaAdminService;
import gabia.jaime.voting.global.dto.Result;
import gabia.jaime.voting.global.security.MemberDetails;
import gabia.jaime.voting.global.util.ClassUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class AgendaAdminApiController {

    private final AgendaAdminService agendaAdminService;

    public AgendaAdminApiController(final AgendaAdminService agendaAdminService) {
        this.agendaAdminService = agendaAdminService;
    }

    @PostMapping("/api/v1/agendas")
    public ResponseEntity<Void> create(@RequestBody AgendaCreateRequest agendaCreateRequest,
                                       Authentication authentication) {
        final MemberDetails memberDetails = ClassUtils.getSafeCastInstance(authentication.getPrincipal(),
                MemberDetails.class);
        final Long savedId = agendaAdminService.save(memberDetails, agendaCreateRequest);
        return ResponseEntity.created(URI.create("/api/v1/agendas/" + savedId))
                .build();
    }

    // TODO: PATCH VS POST (issue) 생성도 하기에
    @PatchMapping("/api/v1/agendas/{id}")
    public ResponseEntity<Result> issue(@RequestBody AgendaToIssueRequest agendaToIssueRequest,
                                        Authentication authentication, @PathVariable("id") Long agendaId) {
        final MemberDetails memberDetails = ClassUtils.getSafeCastInstance(authentication.getPrincipal(),
                MemberDetails.class);
        AgendaResponse response = agendaAdminService.issue(memberDetails, agendaToIssueRequest, agendaId);
        return ResponseEntity.ok(Result.createSuccessResult(response));
    }

}
