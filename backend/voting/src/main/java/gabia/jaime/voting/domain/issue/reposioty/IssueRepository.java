package gabia.jaime.voting.domain.issue.reposioty;

import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.issue.entity.IssueStatus;
import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from Issue i join fetch i.agenda where i.id = :id")
    Optional<Issue> findWithAgendaByIdSelectForUpdate(Long id);

    @Query("select i from Issue i join fetch i.agenda where i.id = :id")
    Optional<Issue> findWithAgendaAndVotesById(Long id);

    @Query("select i from Issue i join fetch i.agenda where i.id = :id")
    Optional<Issue> findWithAgendaById(Long id);

    @Query("select i from Issue i join fetch i.agenda where i.issueStatus = :status and i.endAt <= :endAt")
    List<Issue> findWithAgendaByIssueStatusAndEndAtIsLessThanEqual(IssueStatus status, LocalDateTime endAt);

    @Modifying
    @Transactional
    @Query("update Issue i set i.issueStatus = :status where i.id in :ids")
    void updateStatusByIds(List<Long> ids, IssueStatus status);
}
