package gabia.jaime.voting.domain.agenda.repository;

import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.agenda.entity.AgendaStatus;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AgendaRepository extends JpaRepository<Agenda, Long>, AgendaRepositoryCustom {

    @Modifying
    @Transactional
    @Query("update Agenda a set a.agendaStatus = :status where a.id in :ids")
    void updateStatusByIds(List<Long> ids, AgendaStatus status);
}
