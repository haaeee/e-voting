package gabia.jaime.voting.domain.vote.repository;

import gabia.jaime.voting.domain.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
