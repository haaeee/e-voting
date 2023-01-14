package gabia.jaime.voting.domain.issue.service;

import static gabia.jaime.voting.domain.member.entity.Role.ROLE_ADMIN;

import gabia.jaime.voting.domain.issue.dto.response.IssueAdminResponse;
import gabia.jaime.voting.domain.issue.dto.response.IssueShareHolderResponse;
import gabia.jaime.voting.domain.issue.dto.response.IssueResponse;
import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.issue.reposioty.IssueRepository;
import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.domain.member.repository.MemberRepository;
import gabia.jaime.voting.global.exception.notfound.IssueNotFoundException;
import gabia.jaime.voting.global.exception.notfound.MemberNotFoundException;
import gabia.jaime.voting.global.security.MemberDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class IssueService {

    private final IssueRepository issueRepository;
    private final MemberRepository memberRepository;

    public IssueService(final IssueRepository issueRepository, final MemberRepository memberRepository) {
        this.issueRepository = issueRepository;
        this.memberRepository = memberRepository;
    }

    public IssueResponse search(final MemberDetails memberDetails, final Long issueId) {
        final Member member = findMember(memberDetails.getEmail());

        //  ADMIN RESPONSE
        if (member.getRole() == ROLE_ADMIN) {
            Issue issue = issueRepository.findWithAgendaAndVotesById(issueId).orElseThrow(IssueNotFoundException::new);
            return IssueAdminResponse.from(issue);
        }

        // SHAREHOLDER RESPONSE
        final Issue issue = issueRepository.findWithAgendaById(issueId).orElseThrow(IssueNotFoundException::new);
        return IssueShareHolderResponse.from(issue);
    }

    private Member findMember(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

}
