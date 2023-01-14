package gabia.jaime.voting.domain.issue.service;

import static gabia.jaime.voting.domain.agenda.entity.AgendaStatus.RUNNING;
import static gabia.jaime.voting.domain.issue.entity.IssueStatus.OPEN;
import static gabia.jaime.voting.domain.issue.entity.IssueType.LIMITED;
import static gabia.jaime.voting.domain.member.entity.Role.ROLE_ADMIN;
import static gabia.jaime.voting.domain.member.entity.Role.ROLE_SHAREHOLDER;
import static gabia.jaime.voting.domain.vote.entity.VoteType.YES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.issue.dto.response.IssueResponse;
import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.issue.entity.IssueStatus;
import gabia.jaime.voting.domain.issue.reposioty.IssueRepository;
import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.domain.member.repository.MemberRepository;
import gabia.jaime.voting.domain.vote.entity.Vote;
import gabia.jaime.voting.domain.vote.entity.VoteType;
import gabia.jaime.voting.global.security.MemberDetails;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("현안 조회")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @InjectMocks
    private IssueService sut;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private MemberRepository memberRepository;


    @Test
    void 관리자가_현안을_조회_하면_투표의_결과_및_투표자_정보까지_나온다() {
        // given
        Long issueId = 1L;
        MemberDetails memberDetails = createAdminDetails();
        Agenda agenda = createAgenda(1L);
        Issue issue = createIssue(1L, agenda, OPEN, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        given(memberRepository.findByEmail("admin@email.com")).willReturn(Optional.of(createAdmin(1L)));
        given(issueRepository.findWithAgendaAndVotesById(issueId)).willReturn(
                Optional.of(issue));

        // when
        IssueResponse issueResponse = sut.search(memberDetails, issueId);

        // then
        assertThat(issueResponse)
                .hasFieldOrPropertyWithValue("yesCount", issue.getYesCount())
                .hasFieldOrPropertyWithValue("noCount", issue.getYesCount())
                .hasFieldOrPropertyWithValue("giveUpCount", issue.getGiveUpCount())
                .hasFieldOrProperty("agendaSimpleResponse")
                .hasFieldOrProperty("votes");
//                .hasFieldOrPropertyWithValue("agendaSimpleResponse", AgendaSimpleResponse.from(issue.getAgenda()))
//                .hasFieldOrPropertyWithValue("votes", issue.getVotes().stream()
//                        .map(VoteResponse::from)
//                        .collect(Collectors.toUnmodifiableSet()));

        then(memberRepository).should().findByEmail("admin@email.com");
        then(issueRepository).should().findWithAgendaAndVotesById(issueId);
    }

    @Test
    void 주주가_현안을_조회_하면_투표의_결과가_나온다() {
        // given
        Long issueId = 1L;
        MemberDetails memberDetails = createShareHolderDetails();
        Agenda agenda = createAgenda(1L);
        Issue issue = createIssue(1L, agenda, OPEN, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1));
        given(memberRepository.findByEmail("shareHolder@email.com")).willReturn(Optional.of(createShareHolder(1L)));
        given(issueRepository.findWithAgendaById(issueId)).willReturn(
                Optional.of(issue));

        // when
        IssueResponse issueResponse = sut.search(memberDetails, issueId);

        // then
        assertThat(issueResponse)
                .hasFieldOrPropertyWithValue("yesCount", issue.getYesCount())
                .hasFieldOrPropertyWithValue("noCount", issue.getYesCount())
                .hasFieldOrPropertyWithValue("giveUpCount", issue.getGiveUpCount())
                .hasFieldOrProperty("agendaSimpleResponse");
//                .hasFieldOrPropertyWithValue("agendaSimpleResponse", AgendaSimpleResponse.from(issue.getAgenda()))
//                .hasFieldOrPropertyWithValue("votes", issue.getVotes().stream()
//                        .map(VoteResponse::from)
//                        .collect(Collectors.toUnmodifiableSet()));

        then(memberRepository).should().findByEmail("shareHolder@email.com");
        then(issueRepository).should().findWithAgendaById(issueId);
    }


    /**
     * 투표 관련 테스트 생성 메서드
     */
    private Vote createVote(final VoteType voteType, final int voteCount, final Issue issue, final Member member) {
        return Vote.builder().voteType(voteType).voteCount(voteCount).issue(issue).member(member).build();
    }

    private Vote createVote(final Long id, final VoteType voteType, final int voteCount, final Issue issue,
                            final Member member) {
        return Vote.builder().id(id).voteType(voteType).voteCount(voteCount).issue(issue).member(member).build();
    }

    /**
     * 이슈 관련 테스트 생성 메서드
     */
    private Issue createIssue(final Long id, final Agenda agenda, final IssueStatus issueStatus,
                              final LocalDateTime startAt, final LocalDateTime endAt) {
        final Issue issue = Issue.builder()
                .id(id)
                .agenda(agenda)
                .issueType(LIMITED)
                .issueStatus(issueStatus)
                .startAt(startAt)
                .endAt(endAt)
                .build();

        issue.addVote(createVote(1L, YES, 3, issue, createShareHolder(2L)));
        issue.addVote(createVote(2L, YES, 3, issue, createShareHolder(3L)));
        return issue;
    }

    /**
     * 안관 관련 테스트 생성 메서드
     */
    private Agenda createAgenda(final Long id) {
        return Agenda.builder().id(id).title("클라우드 개발팀 인턴 점심").content("점심은 반작으로?").agendaStatus(RUNNING)
                .member(createAdmin(1L)).build();
    }


    private Member createShareHolder(final Long id) {
        return Member.builder().id(id).email("shareHolder" + id + "@email.com").password("password")
                .role(ROLE_SHAREHOLDER)
                .voteRightCount(3).build();
    }

    private Member createAdmin(final Long id) {
        return Member.builder().id(id).email("admin" + id + "@email.com").password("password").role(ROLE_ADMIN)
                .voteRightCount(0).build();
    }

    private MemberDetails createShareHolderDetails() {
        return MemberDetails.builder().email("shareHolder@email.com").role(ROLE_SHAREHOLDER).build();
    }

    private MemberDetails createAdminDetails() {
        return MemberDetails.builder().email("admin@email.com").role(ROLE_ADMIN).build();
    }
}