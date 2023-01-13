package gabia.jaime.voting.domain.vote.repository;

import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.domain.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("select v from Vote v where v.issue = :issue and v.member = :member")
    Optional<Vote> findByIssueAndMember(Issue issue, Member member);
}
