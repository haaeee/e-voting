package gabia.jaime.voting.domain.agenda.service;

import static gabia.jaime.voting.domain.agenda.entity.AgendaStatus.PENDING;
import static gabia.jaime.voting.domain.member.entity.Role.ROLE_ADMIN;

import gabia.jaime.voting.domain.agenda.dto.request.AgendaCreateRequest;
import gabia.jaime.voting.domain.agenda.dto.request.AgendaToIssueRequest;
import gabia.jaime.voting.domain.agenda.dto.response.AgendaResponse;
import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.agenda.entity.AgendaStatus;
import gabia.jaime.voting.domain.agenda.repository.AgendaRepository;
import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.issue.reposioty.IssueRepository;
import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.domain.member.entity.Role;
import gabia.jaime.voting.domain.member.repository.MemberRepository;
import gabia.jaime.voting.global.exception.conflict.BeforeIssueException;
import gabia.jaime.voting.global.exception.conflict.InvalidDurationException;
import gabia.jaime.voting.global.exception.conflict.InvalidEndAtException;
import gabia.jaime.voting.global.exception.conflict.NotPendingAgendaException;
import gabia.jaime.voting.global.exception.forbidden.AdminForbiddenException;
import gabia.jaime.voting.global.exception.forbidden.ForbiddenException;
import gabia.jaime.voting.global.exception.notfound.AgendaNotFoundException;
import gabia.jaime.voting.global.exception.notfound.MemberNotFoundException;
import gabia.jaime.voting.global.security.MemberDetails;
import java.time.LocalDateTime;
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
    public AgendaResponse save(final MemberDetails memberDetails, final AgendaCreateRequest agendaCreateRequest) {
        validateAdmin(memberDetails.getRole());

        final Member adminMember = findMember(memberDetails.getEmail());

        // Completed 는 생성할 수 없다.
        if (agendaCreateRequest.getAgendaStatus() == AgendaStatus.COMPLETED) {
            throw new BeforeIssueException();
        }

        // Pending
        if (agendaCreateRequest.getAgendaStatus() == AgendaStatus.PENDING) {
            final Agenda pendingAgenda =
                    Agenda.of(agendaCreateRequest.getTitle(), agendaCreateRequest.getContent(), agendaCreateRequest.getAgendaStatus(), adminMember);
            return AgendaResponse.from(agendaRepository.save(pendingAgenda));
        }

        // Running Agenda 생성
        validateTime(agendaCreateRequest.getStartAt(),  agendaCreateRequest.getEndAt());

        final Agenda runningAgenda =
                Agenda.of(agendaCreateRequest.getTitle(), agendaCreateRequest.getContent(), agendaCreateRequest.getAgendaStatus(), adminMember);
        final Issue runningIssue =
                Issue.of(runningAgenda, agendaCreateRequest.getIssueType(), agendaCreateRequest.getStartAt(), agendaCreateRequest.getEndAt());

        final Agenda agenda = agendaRepository.save(runningAgenda);
        agenda.setIssue(runningIssue);

        return AgendaResponse.from(agenda);
    }

    // Only Pending => ISSUE 화
    @Transactional
    public AgendaResponse issue(final MemberDetails memberDetails, final AgendaToIssueRequest request, final Long agendaId) {
        validateAdmin(memberDetails.getRole());
        validateTime(request.getStartAt(), request.getEndAt());

        Agenda agenda = findAgenda(agendaId);
        validateAgenda(agenda);

        // issue 생성
        Issue issue = Issue.of(agenda, request.getIssueType(), request.getStartAt(), request.getEndAt());
        issueRepository.save(issue);
        agenda.changeRunningStatus(issue);

        return AgendaResponse.from(agenda);
    }

    @Transactional
    public void delete(final MemberDetails memberDetails, final Long agendaId) {
        final Agenda agenda = findAgenda(agendaId);
        final Member member = findMember(memberDetails.getEmail());
        if (!agenda.getMember().getId().equals(member.getId())) {
            throw new ForbiddenException("생성하신 안건이 아닙니다.");
        }
        agendaRepository.delete(agenda);
    }

    private void validateAdmin(final Role role) {
        if (role != ROLE_ADMIN) {
            throw new AdminForbiddenException();
        }
    }

    private void validateAgenda(final Agenda agenda) {
        if (agenda.getAgendaStatus() != PENDING) {
            throw new NotPendingAgendaException();
        }
    }

    private void validateTime(final LocalDateTime startAt, final LocalDateTime endAt) {
        if (!endAt.isAfter(LocalDateTime.now())) {
            throw new InvalidEndAtException();
        }
        if (startAt.isEqual(endAt) || startAt.isAfter(endAt)) {
            throw new InvalidDurationException();
        }
    }

    private Member findMember(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Agenda findAgenda(final Long id) {
        return agendaRepository.findById(id)
                .orElseThrow(AgendaNotFoundException::new);
    }

}
