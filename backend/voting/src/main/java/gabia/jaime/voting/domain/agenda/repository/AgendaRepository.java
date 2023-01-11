package gabia.jaime.voting.domain.agenda.repository;

import gabia.jaime.voting.domain.agenda.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
}
