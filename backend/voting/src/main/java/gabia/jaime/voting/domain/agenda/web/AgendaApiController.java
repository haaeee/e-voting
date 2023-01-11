package gabia.jaime.voting.domain.agenda.web;

import gabia.jaime.voting.domain.agenda.entity.AgendaStatus;
import gabia.jaime.voting.domain.agenda.service.AgendaService;
import gabia.jaime.voting.global.dto.Result;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/agendas")
public class AgendaApiController {

    private final AgendaService agendaService;

    public AgendaApiController(final AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping
    public ResponseEntity search(final @RequestParam(required = false) AgendaStatus agendaStatus, final Pageable pageable) {
        return ResponseEntity.ok(Result.createSuccessResult(agendaService.search(agendaStatus, pageable)));
    }
}
