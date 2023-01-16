package gabia.jaime.voting.domain.agenda.web;

import gabia.jaime.voting.domain.agenda.dto.request.AgendaCreateRequest;
import gabia.jaime.voting.domain.agenda.dto.request.AgendaToIssueRequest;
import gabia.jaime.voting.domain.agenda.dto.response.AgendaResponse;
import gabia.jaime.voting.domain.agenda.service.AgendaAdminService;
import gabia.jaime.voting.global.dto.Result;
import gabia.jaime.voting.global.security.MemberDetails;
import gabia.jaime.voting.global.util.ClassUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class AgendaAdminApiController {

    private final AgendaAdminService agendaAdminService;

    public AgendaAdminApiController(final AgendaAdminService agendaAdminService) {
        this.agendaAdminService = agendaAdminService;
    }

    @PostMapping("/api/v1/agendas")
    public ResponseEntity<Result> create(@RequestBody AgendaCreateRequest agendaCreateRequest,
                                       Authentication authentication) {
        final MemberDetails memberDetails = ClassUtils.getSafeCastInstance(authentication.getPrincipal(),
                MemberDetails.class);
        final AgendaResponse response = agendaAdminService.save(memberDetails, agendaCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Result.createSuccessResult(response));
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

    @DeleteMapping("/api/v1/agendas/{id}")
    public void delete(Authentication authentication, @PathVariable("id") Long agendaId) {
        final MemberDetails memberDetails = ClassUtils.getSafeCastInstance(authentication.getPrincipal(),
                MemberDetails.class);
        agendaAdminService.delete(memberDetails, agendaId);
    }

}
