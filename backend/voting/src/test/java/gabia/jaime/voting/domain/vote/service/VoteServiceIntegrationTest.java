package gabia.jaime.voting.domain.vote.service;

import static gabia.jaime.voting.domain.agenda.entity.AgendaStatus.COMPLETED;
import static gabia.jaime.voting.domain.issue.entity.IssueStatus.CLOSE;
import static gabia.jaime.voting.domain.vote.entity.VoteType.NO;
import static gabia.jaime.voting.domain.vote.service.VoteServiceConcurrentTest.MEMBER_DETAILS_ENUM.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.issue.reposioty.IssueRepository;
import gabia.jaime.voting.domain.member.repository.MemberRepository;
import gabia.jaime.voting.domain.vote.dto.request.VoteCreateRequest;
import gabia.jaime.voting.domain.vote.dto.response.VoteCreateResponse;
import gabia.jaime.voting.domain.vote.repository.VoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("종료 시간 이후에 투표")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest
public class VoteServiceIntegrationTest {

    @Autowired
    VoteService sut;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    IssueRepository issueRepository;

    @Test
    void 투표__종료_시간_이후에_투표를_하면_투표_상태_값이_CLOSE_로_변하고_투표_반영되지_않음을_리턴_한다() {
        // given
        Long issueId = 3L;
        final VoteCreateRequest voteCreateRequest = new VoteCreateRequest(NO);

        // when
        VoteCreateResponse response = sut.vote(MEMBER_DETAILS1.getMemberDetails(), voteCreateRequest, issueId);

        // then

        Issue issue = issueRepository.findById(issueId).get();
        assertAll(
                () -> assertThat(response.getId()).isNull(),
                () -> assertThat(response.getMessage()).isEqualTo("투표가 반영되지 않았습니다."),
                () -> assertThat(issue.getIssueStatus()).isEqualTo(CLOSE),
                () -> assertThat(issue.getAgenda().getAgendaStatus()).isEqualTo(COMPLETED)
        );
    }
}
