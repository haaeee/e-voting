package gabia.jaime.voting.domain.vote.service;

import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.vote.dto.request.VoteCreateRequest;
import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.issue.entity.IssueStatus;
import gabia.jaime.voting.domain.issue.entity.IssueType;
import gabia.jaime.voting.domain.issue.reposioty.IssueRepository;
import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.domain.member.repository.MemberRepository;
import gabia.jaime.voting.domain.vote.dto.response.VoteCreateResponse;
import gabia.jaime.voting.domain.vote.entity.Vote;
import gabia.jaime.voting.domain.vote.entity.VoteType;
import gabia.jaime.voting.domain.vote.repository.VoteRepository;
import gabia.jaime.voting.global.exception.forbidden.AdminCanNotVoteException;
import gabia.jaime.voting.global.exception.conflict.ClosedIssueException;
import gabia.jaime.voting.global.exception.notfound.IssueNotFoundException;
import gabia.jaime.voting.global.security.MemberDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static gabia.jaime.voting.domain.agenda.entity.AgendaStatus.COMPLETED;
import static gabia.jaime.voting.domain.agenda.entity.AgendaStatus.RUNNING;
import static gabia.jaime.voting.domain.issue.entity.IssueStatus.CLOSE;
import static gabia.jaime.voting.domain.issue.entity.IssueStatus.OPEN;
import static gabia.jaime.voting.domain.issue.entity.IssueType.LIMITED;
import static gabia.jaime.voting.domain.member.entity.Role.ROLE_ADMIN;
import static gabia.jaime.voting.domain.member.entity.Role.ROLE_SHAREHOLDER;
import static gabia.jaime.voting.domain.vote.entity.VoteType.NO;
import static gabia.jaime.voting.domain.vote.entity.VoteType.YES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import static org.mockito.BDDMockito.*;

@DisplayName("투표 생성")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @InjectMocks
    private VoteService sut;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private VoteRepository voteRepository;

    @Test
    void 이슈_현안이_존재하지_않으면_예외를_던진다() {
        // given
        Long issueId = 0L;
        VoteCreateRequest voteCreateRequest = new VoteCreateRequest(YES);

        MemberDetails shareHolderDetails = createShareHolderDetails();
        Member shareHolder = createShareHolder(1L, 1);
        given(issueRepository.findWithAgendaByIdSelectForUpdate(issueId)).willReturn(Optional.empty());

        // when
        Throwable t = catchThrowable(() -> sut.vote(shareHolderDetails, voteCreateRequest, issueId));

        // then
        assertThat(t).isInstanceOf(IssueNotFoundException.class);
        then(issueRepository).should().findWithAgendaByIdSelectForUpdate(issueId);
    }

    @Test
    void 관리자는_투표할_시에_예외를_던진다() {
        // given
        Long issueId = 1L;
        MemberDetails adminDetails = createAdminDetails();
        final VoteCreateRequest voteCreateRequest = new VoteCreateRequest(NO);
        final Issue issue = createIssue(1L, LIMITED, OPEN, LocalDateTime.now().minusDays(2L), LocalDateTime.now().plusDays(3L), 3, 1, 2);
        given(memberRepository.findByEmail(adminDetails.getEmail())).willReturn(Optional.of(createAdmin(1L)));
        given(issueRepository.findWithAgendaByIdSelectForUpdate(issueId)).willReturn(Optional.of(issue));

        // when
        Throwable t = catchThrowable(() -> sut.vote(adminDetails, voteCreateRequest, issueId));

        // then
        assertThat(t).isInstanceOf(AdminCanNotVoteException.class);
        then(issueRepository).should().findWithAgendaByIdSelectForUpdate(issueId);
        then(memberRepository).should().findByEmail(adminDetails.getEmail());
    }

    @Test
    void 종료된_현안은_투표를_할_수_없다() {
        // given
        Long issueId = 1L;
        final VoteCreateRequest voteCreateRequest = new VoteCreateRequest(NO);
        MemberDetails shareHolderDetails = createShareHolderDetails();
        final Issue issue = createIssue(1L, LIMITED, CLOSE, LocalDateTime.now().minusDays(2L), LocalDateTime.now().minusDays(1L), 3, 1, 2);
        given(memberRepository.findByEmail(shareHolderDetails.getEmail())).willReturn(Optional.of(createShareHolder(1L, 3)));
        given(issueRepository.findWithAgendaByIdSelectForUpdate(issueId)).willReturn(Optional.of(issue));

        // when
        Throwable t = catchThrowable(() -> sut.vote(shareHolderDetails, voteCreateRequest, issueId));

        // then
        assertThat(t).isInstanceOf(ClosedIssueException.class);
        then(issueRepository).should().findWithAgendaByIdSelectForUpdate(issueId);
        then(memberRepository).should().findByEmail(shareHolderDetails.getEmail());
    }

    @Test
    void 현안의_명시된_종료시간_이후에_투표를_할_수_없다() {
        // given
        Long issueId = 1L;
        final VoteCreateRequest voteCreateRequest = new VoteCreateRequest(NO);
        MemberDetails shareHolderDetails = createShareHolderDetails();
        final Issue issue = createIssue(1L, LIMITED, OPEN, LocalDateTime.now().minusDays(2L), LocalDateTime.now().minusDays(1L), 3, 1, 2);
        given(memberRepository.findByEmail(shareHolderDetails.getEmail())).willReturn(Optional.of(createShareHolder(1L, 3)));
        given(issueRepository.findWithAgendaByIdSelectForUpdate(issueId)).willReturn(Optional.of(issue));

        // when
        VoteCreateResponse response = sut.vote(shareHolderDetails, voteCreateRequest, issueId);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNull(),
                () -> assertThat(response.getMessage()).isEqualTo("투표가 반영되지 않았습니다."),
                () -> assertThat(issue.getIssueStatus()).isEqualTo(CLOSE),
                () -> assertThat(issue.getAgenda().getAgendaStatus()).isEqualTo(COMPLETED)
        );
        then(issueRepository).should().findWithAgendaByIdSelectForUpdate(issueId);
        then(memberRepository).should().findByEmail(shareHolderDetails.getEmail());
    }

    @Test
    void 선착순투표__투표_가능한_수가_주주의_의결권보다_크면_전체_의결권을_사용한후에_생성된_투표의_ID가_반환된다() {
        // given
        Long issueId = 1L;
        final VoteCreateRequest voteCreateRequest = new VoteCreateRequest(NO);
        MemberDetails shareHolderDetails = createShareHolderDetails();
        final Member shareHolder = createShareHolder(1L, 3);
        final Issue issue = createIssue(1L, LIMITED, OPEN, LocalDateTime.now().minusDays(2L), LocalDateTime.now().plusDays(1L), 3, 1, 2);
        given(memberRepository.findByEmail(shareHolderDetails.getEmail())).willReturn(Optional.of(shareHolder));
        given(issueRepository.findWithAgendaByIdSelectForUpdate(issueId)).willReturn(Optional.of(issue));
        given(voteRepository.save(any(Vote.class))).willReturn(createVote(1L, NO, 3, issue, shareHolder));

        // when
        VoteCreateResponse response = sut.vote(shareHolderDetails, voteCreateRequest, issueId);

        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(issue.getNoCount()).isEqualTo(1 + 3);
        then(issueRepository).should().findWithAgendaByIdSelectForUpdate(issueId);
        then(memberRepository).should().findByEmail(shareHolderDetails.getEmail());
        then(voteRepository).should().save(any(Vote.class));
    }


    /**
     * 투표 관련 테스트 생성 메서드
     */
    private Vote createVote(final VoteType voteType, final int voteCount, final Issue issue, final Member member) {
        return Vote.builder().voteType(voteType).voteCount(voteCount).issue(issue).member(member).build();
    }
    private Vote createVote(final Long id, final VoteType voteType, final int voteCount, final Issue issue, final Member member) {
        return Vote.builder().id(id).voteType(voteType).voteCount(voteCount).issue(issue).member(member).build();
    }

    /**
     * 이슈 관련 테스트 생성 메서드
     */
    private Issue createIssue(final Long id, final IssueType issueType, final IssueStatus issueStatus,
                              final LocalDateTime startAt, final LocalDateTime endAt,
                              final int yesCount, final int noCount, final int giveUpCount) {
        return Issue.builder()
                .id(id)
                .agenda(createAgenda(1L))
                .issueType(issueType)
                .issueStatus(issueStatus)
                .startAt(startAt)
                .endAt(endAt)
                .yesCount(yesCount)
                .noCount(noCount)
                .giveUpCount(giveUpCount)
                .build();
    }

    /**
     * 안관 관련 테스트 생성 메서드
     */
    private Agenda createAgenda(final Long id) {
        return Agenda.builder().id(id).title("클라우드 개발팀 인턴 점심").content("점심은 반작으로?").agendaStatus(RUNNING).member(createAdmin(1L)).build();
    }


    /**
     * 유저 관련 테스트 생성 메서드
     */
    private Member createShareHolder(final Long id, final int voteRightCount){
        return Member.builder().id(id).email("shareHolder" + id + "@email.com").password("password").role(ROLE_SHAREHOLDER)
                .voteRightCount(voteRightCount).build();
    }

    private Member createAdmin(final Long id){
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
