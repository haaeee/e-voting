package gabia.jaime.voting.domain.agenda.service;

import static gabia.jaime.voting.domain.member.entity.Role.ROLE_ADMIN;

import gabia.jaime.voting.domain.agenda.dto.request.AgendaCreateRequest;
import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.agenda.entity.AgendaStatus;
import gabia.jaime.voting.domain.agenda.repository.AgendaRepository;
import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.issue.reposioty.IssueRepository;
import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.domain.member.entity.Role;
import gabia.jaime.voting.domain.member.repository.MemberRepository;
import gabia.jaime.voting.global.exception.conflict.BeforeIssueException;
import gabia.jaime.voting.global.exception.forbidden.AdminForbiddenException;
import gabia.jaime.voting.global.exception.notfound.MemberNotFoundException;
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

        // Completed는 생성할 수 없다.
        if (agendaCreateRequest.getAgendaStatus() == AgendaStatus.COMPLETED) {
            throw new BeforeIssueException();
        }

        // Pending
        if (agendaCreateRequest.getAgendaStatus() == AgendaStatus.PENDING) {
            final Agenda pendingAgenda =
                    Agenda.of(agendaCreateRequest.getTitle(), agendaCreateRequest.getContent(), agendaCreateRequest.getAgendaStatus(), adminMember);
            return agendaRepository.save(pendingAgenda).getId();
        }

        // Running Agenda 생성
        final Agenda runningAgenda =
                Agenda.of(agendaCreateRequest.getTitle(), agendaCreateRequest.getContent(), agendaCreateRequest.getAgendaStatus(), adminMember);
        final Issue runningIssue =
                Issue.of(runningAgenda, agendaCreateRequest.getIssueType(), agendaCreateRequest.getStartAt(), agendaCreateRequest.getEndAt());

        agendaRepository.save(runningAgenda);
        issueRepository.save(runningIssue);

        return runningAgenda.getId();
    }

    private void validateAdmin(final Role role) {
        if (role != ROLE_ADMIN) {
            throw new AdminForbiddenException();
        }
    }

    private Member findMember(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }
}
