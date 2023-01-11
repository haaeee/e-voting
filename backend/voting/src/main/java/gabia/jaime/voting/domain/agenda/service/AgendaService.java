package gabia.jaime.voting.domain.agenda.service;

import gabia.jaime.voting.domain.agenda.dto.response.AgendaResponse;
import gabia.jaime.voting.domain.agenda.entity.AgendaStatus;
import gabia.jaime.voting.domain.agenda.repository.AgendaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class AgendaService {

    private final AgendaRepository agendaRepository;

    public AgendaService(final AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    public Page<AgendaResponse> search(AgendaStatus agendaStatus, Pageable pageable) {

        if (agendaStatus == null) {
            return agendaRepository.findWithoutAgendaStatus(pageable).map(AgendaResponse::from);
        }

        return agendaRepository.findWithAgendaStatus(agendaStatus, pageable).map(AgendaResponse::from);
    }
}
