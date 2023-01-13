package gabia.jaime.voting.domain.vote.service;

import gabia.jaime.voting.domain.agenda.entity.AgendaStatus;
import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.issue.entity.IssueStatus;
import gabia.jaime.voting.domain.issue.reposioty.IssueRepository;
import gabia.jaime.voting.domain.member.repository.MemberRepository;
import gabia.jaime.voting.domain.vote.dto.request.VoteCreateRequest;
import gabia.jaime.voting.domain.vote.repository.VoteRepository;
import gabia.jaime.voting.global.security.MemberDetails;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.LongStream;

import static gabia.jaime.voting.domain.issue.entity.IssueStatus.*;
import static gabia.jaime.voting.domain.member.entity.Role.ROLE_SHAREHOLDER;
import static gabia.jaime.voting.domain.vote.entity.VoteType.YES;
import static gabia.jaime.voting.domain.vote.service.VoteServiceConcurrentTest.MEMBER_DETAILS_ENUM.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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
public class VoteServiceConcurrentTest {

    @Autowired
    VoteService sut;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    IssueRepository issueRepository;

    @Test
    void 샘플_데이터가_생성되는지_확인() {
        issueRepository.findAll()
                .forEach(System.out::println);
    }

    @Test
    void 제한_없는_현안에_동시에_투표를_하여도_동시성을_보장한다() throws InterruptedException {
        // given
        final Long issueId = 2L;
        final int VOTING_PEOPLE = 5;

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(VOTING_PEOPLE);
        CountDownLatch countDownLatch = new CountDownLatch(VOTING_PEOPLE);

        List<MemberDetails> memberDetailsList = Arrays.stream(MEMBER_DETAILS_ENUM.values())
                .map(MEMBER_DETAILS_ENUM::getMemberDetails)
                .collect(Collectors.toList());

        for (MemberDetails memberDetails : memberDetailsList) {
            executorService.execute(() -> {
                MEMBER_VOTE member_vote = new MEMBER_VOTE(memberDetails, new VoteCreateRequest(YES));
                member_vote.concurrent_vote(sut, issueId);
                countDownLatch.countDown();
            });
        }

        // then
        countDownLatch.await();
        final Issue issue = issueRepository.findById(issueId).get();
        assertThat(issue.getTotalVoteCount()).isEqualTo(11);
    }

    @Test
    void 선착순_현안에_동시에_투표를_하여도_동시성을_보장한다() throws InterruptedException {
        // given
        final Long issueId = 1L;
        final int VOTING_PEOPLE = 5;

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(VOTING_PEOPLE);
        CountDownLatch countDownLatch = new CountDownLatch(VOTING_PEOPLE);

        List<MemberDetails> memberDetailsList = Arrays.stream(MEMBER_DETAILS_ENUM.values())
                .map(MEMBER_DETAILS_ENUM::getMemberDetails)
                .collect(Collectors.toList());

        for (MemberDetails memberDetails : memberDetailsList) {
            executorService.execute(() -> {
                MEMBER_VOTE member_vote = new MEMBER_VOTE(memberDetails, new VoteCreateRequest(YES));
                member_vote.concurrent_vote(sut, issueId);
                countDownLatch.countDown();
            });
        }

        // then
        countDownLatch.await();
        final Issue issue = issueRepository.findById(issueId).get();
        assertAll(
                () -> assertThat(issue.getTotalVoteCount()).isEqualTo(10),
                () -> assertThat(issue.getIssueStatus()).isEqualTo(CLOSE),
                () -> assertThat(issue.getAgenda().getAgendaStatus()).isEqualTo(AgendaStatus.COMPLETED)
        );
    }

    enum MEMBER_DETAILS_ENUM {
        MEMBER_DETAILS1(MemberDetails.builder().email("member1@email.com").role(ROLE_SHAREHOLDER).build()),
        MEMBER_DETAILS2(MemberDetails.builder().email("member2@email.com").role(ROLE_SHAREHOLDER).build()),
        MEMBER_DETAILS3(MemberDetails.builder().email("member3@email.com").role(ROLE_SHAREHOLDER).build()),
        MEMBER_DETAILS4(MemberDetails.builder().email("member4@email.com").role(ROLE_SHAREHOLDER).build()),
        MEMBER_DETAILS5(MemberDetails.builder().email("member5@email.com").role(ROLE_SHAREHOLDER).build());

        private final MemberDetails memberDetails;

        MEMBER_DETAILS_ENUM(final MemberDetails memberDetails) {
            this.memberDetails = memberDetails;
        }

        public MemberDetails getMemberDetails() {
            return memberDetails;
        }
    }

    static class MEMBER_VOTE {

        private final MemberDetails memberDetails;
        private final VoteCreateRequest voteCreateRequest;

        public MEMBER_VOTE(final MemberDetails memberDetails,
                           final VoteCreateRequest voteCreateRequest) {
            this.memberDetails = memberDetails;
            this.voteCreateRequest = voteCreateRequest;
        }

        public void concurrent_vote(VoteService voteService, Long issueId) {
            voteService.vote(memberDetails, voteCreateRequest, issueId);
        }
    }

}
