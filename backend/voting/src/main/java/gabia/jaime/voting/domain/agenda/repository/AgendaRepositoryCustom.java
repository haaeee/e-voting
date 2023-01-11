package gabia.jaime.voting.domain.agenda.repository;

import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.agenda.entity.AgendaStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AgendaRepositoryCustom {

    Page<Agenda> findWithoutAgendaStatus(Pageable pageable);

    Page<Agenda> findWithAgendaStatus(AgendaStatus agendaStatus, Pageable pageable);
}
