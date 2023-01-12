package gabia.jaime.voting.domain.vote.service;

import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.issue.entity.IssueStatus;
import gabia.jaime.voting.domain.issue.entity.IssueType;
import gabia.jaime.voting.domain.issue.reposioty.IssueRepository;
import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.domain.member.repository.MemberRepository;
import gabia.jaime.voting.domain.vote.dto.request.VoteCreateRequest;
import gabia.jaime.voting.domain.vote.entity.Vote;
import gabia.jaime.voting.domain.vote.entity.VoteType;
import gabia.jaime.voting.domain.vote.repository.VoteRepository;
import gabia.jaime.voting.global.security.MemberDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

import static gabia.jaime.voting.domain.agenda.entity.AgendaStatus.RUNNING;
import static gabia.jaime.voting.domain.member.entity.Role.ROLE_ADMIN;
import static gabia.jaime.voting.domain.member.entity.Role.ROLE_SHAREHOLDER;
import static gabia.jaime.voting.domain.vote.entity.VoteType.YES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DisplayNameGenerator.*;

/**
 * <h2> member:  투표권 </h2>
 * <ul>
 *     <li>member1: 2</li>
 *     <li>member2: 2</li>
 *     <li>member3: 2</li>
 *     <li>member4: 2</li>
 *     <li>member5: 3</li>
 * </ul>
 * <h2> issue: 현안</h2>
 * <ul>
 *      <li>issue1 : 열려 있는 현안 + 기간이 남음 + 선착순</li>
 *      <li>issue2 :열려 있는 현안 + 기간이 남음 + 제한 없음</li>
 *      <li>issue3 :열려 있는 현안 + 기간이 지남 투표하려고하면 CLOSE로 issue_status 변경 및 agenda completed로</li>
 * </ul>
 */
@DisplayName("동시성 테스트 및 투표에 사용한 의결권 갯수 검증 테스트")
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
    void test() {
        final Issue issue = issueRepository.findById(1L).get();
        issueRepository.delete(issue);
    }

    @Test
    void 샘플_데이터가_생성되는지_확인() {
        issueRepository.findAll()
                .forEach(System.out::println);
    }

    @Test
    void 제한_없는_현안에_동시에_투표를_하여도_동시성을_보장한다() {
        // given
        Long issueId = 2L;

        // when
        LongStream.rangeClosed(1, 5).parallel()
                .forEach((id) -> sut.vote(createShareHolderDetails(id), new VoteCreateRequest(YES), issueId));

        // then
        final Issue issue = issueRepository.findById(issueId).get();
        assertThat(issue.getTotalVoteCount()).isEqualTo(11);
    }

    @Test
    void 선착순_현안에_동시에_투표를_하여도_동시성을_보장한다() throws InterruptedException {
        // given
        Long issueId = 1L;

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(5);
        List<MemberDetails> memberDetails = List.of(createShareHolderDetails(1L), createShareHolderDetails(2L), createShareHolderDetails(3L),
                createShareHolderDetails(4L), createShareHolderDetails(5L));

        CountDownLatchT countDownLatchT = new CountDownLatchT();
        for (MemberDetails memberDetail : memberDetails) {
            executorService.execute(() -> {
                countDownLatchT.vote(sut, memberDetail, new VoteCreateRequest(YES), issueId);
                countDownLatch.countDown();
            });
        }

        // then
        countDownLatch.await();
//        final Issue issue = issueRepository.findById(issueId).get();
//        assertThat(issue.getTotalVoteCount()).isEqualTo(10);
    }


    /**
     * 투표 관련 테스트 생성 메서드
     */
    private Vote createVote(VoteType voteType, int voteCount, Issue issue, Member member) {
        return Vote.builder().voteType(voteType).voteCount(voteCount).issue(issue).member(member).build();
    }
    private Vote createVote(Long id, VoteType voteType, int voteCount, Issue issue, Member member) {
        return Vote.builder().id(id).voteType(voteType).voteCount(voteCount).issue(issue).member(member).build();
    }

    /**
     * 이슈 관련 테스트 생성 메서드
     */
    private Issue createIssue(Long id, IssueType issueType, IssueStatus issueStatus, LocalDateTime startAt, LocalDateTime endAt,
                              int yesCount, int noCount, int giveUpCount) {
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
        return Member.builder().id(id).email("member" + id + "@email.com").password("password").role(ROLE_SHAREHOLDER)
                .voteRightCount(voteRightCount).build();
    }

    private Member createAdmin(final Long id){
        return Member.builder().id(id).email("admin" + id + "@email.com").password("password").role(ROLE_ADMIN)
                .voteRightCount(0).build();
    }

    private MemberDetails createShareHolderDetails(final Long id) {
        return MemberDetails.builder().email("member" + id + "@email.com").role(ROLE_SHAREHOLDER).build();
    }

    private MemberDetails createAdminDetails() {
        return MemberDetails.builder().email("admin@email.com").role(ROLE_ADMIN).build();
    }

    public static class CountDownLatchT {

        int count = 0;

        public void vote(final VoteService voteService, final MemberDetails memberDetails, final VoteCreateRequest voteCreateRequest, final Long issueId) {
            voteService.vote(memberDetails, voteCreateRequest, issueId);
        }
    }

}
