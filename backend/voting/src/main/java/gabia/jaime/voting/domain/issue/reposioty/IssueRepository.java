package gabia.jaime.voting.domain.issue.reposioty;

import gabia.jaime.voting.domain.issue.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {
}
