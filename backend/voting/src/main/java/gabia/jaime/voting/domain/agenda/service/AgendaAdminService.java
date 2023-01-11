package gabia.jaime.voting.domain.agenda.service;

import static gabia.jaime.voting.domain.member.entity.Role.ROLE_ADMIN;

import gabia.jaime.voting.domain.agenda.dto.request.AgendaCreateRequest;
import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.agenda.entity.AgendaStatus;
import gabia.jaime.voting.domain.agenda.repository.AgendaRepository;
import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.issue.entity.IssueType;
import gabia.jaime.voting.domain.issue.reposioty.IssueRepository;
import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.domain.member.entity.Role;
import gabia.jaime.voting.domain.member.repository.MemberRepository;
import gabia.jaime.voting.global.exception.badrequest.BadRequestException;
import gabia.jaime.voting.global.exception.notfound.MemberNotFoundException;
import gabia.jaime.voting.global.exception.unauthorized.UnAuthorizedException;
import gabia.jaime.voting.global.security.MemberDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AgendaAdminService {

    private final AgendaRepository agendaRepository;
    private final MemberRepository memberRepository;
    private final IssueRepository issueRepository;

    public AgendaAdminService(final AgendaRepository agendaRepository, final MemberRepository memberRepository, final IssueRepository issueRepository) {
        this.agendaRepository = agendaRepository;
        this.memberRepository = memberRepository;
        this.issueRepository = issueRepository;
    }

    @Transactional
    public Long save(final MemberDetails memberDetails, final AgendaCreateRequest agendaCreateRequest) {
        validateAdmin(memberDetails.getRole());

        final Member adminMember = findMember(memberDetails.getEmail());

        if (agendaCreateRequest.getAgendaStatus() == AgendaStatus.COMPLETED) {
            // TODO
            throw new BadRequestException("올바르지 않은 요청입니다.");
        }
        if (agendaCreateRequest.getAgendaStatus() == AgendaStatus.PENDING) {
            final Agenda pendingAgenda = Agenda.createPendingAgenda(agendaCreateRequest.getTitle(), agendaCreateRequest.getContent(), adminMember);
            return agendaRepository.save(pendingAgenda).getId();
        }

        // Running Agenda 생성
        final IssueType issueType = agendaCreateRequest.getIssueType();
        final Agenda runningAgenda;
        final Issue runningIssue;
        if (issueType == IssueType.LIMITED) {
            runningAgenda = Agenda.createRunningAgenda(agendaCreateRequest.getTitle(), agendaCreateRequest.getContent(), adminMember);
            runningIssue = Issue.createLimitedIssue(runningAgenda, agendaCreateRequest.getStartAt(), agendaCreateRequest.getEndAt());
        } else {
            runningAgenda = Agenda.createRunningAgenda(agendaCreateRequest.getTitle(), agendaCreateRequest.getContent(), adminMember);
            runningIssue = Issue.createNoLimitedIssue(runningAgenda, agendaCreateRequest.getStartAt(), agendaCreateRequest.getEndAt());
        }

        agendaRepository.save(runningAgenda);
        issueRepository.save(runningIssue);

        return runningAgenda.getId();
    }

    private void validateAdmin(final Role role) {
        if (role != ROLE_ADMIN) {
            throw new UnAuthorizedException("관리자만 안건을 생성할 수 있습니다.");
        }
    }

    private Member findMember(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }
}
