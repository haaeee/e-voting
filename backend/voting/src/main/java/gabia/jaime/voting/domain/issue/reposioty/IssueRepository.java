package gabia.jaime.voting.domain.issue.reposioty;

import gabia.jaime.voting.domain.issue.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Issue i join fetch i.agenda where i.id = :id")
    Optional<Issue> findWithAgendaByIdSelectForUpdate(Long id);

}
