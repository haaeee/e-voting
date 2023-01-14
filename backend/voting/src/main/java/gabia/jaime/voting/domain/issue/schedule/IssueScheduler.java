package gabia.jaime.voting.domain.issue.schedule;

import static gabia.jaime.voting.domain.agenda.entity.AgendaStatus.COMPLETED;
import static gabia.jaime.voting.domain.issue.entity.IssueStatus.*;
import static gabia.jaime.voting.domain.issue.entity.IssueStatus.OPEN;

import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.agenda.repository.AgendaRepository;
import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.issue.reposioty.IssueRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IssueScheduler {

    private final IssueRepository issueRepository;
    private final AgendaRepository agendaRepository;

    @Scheduled(cron = "0 0/30 * * * *")
    public void expiresIssue() {
        LocalDateTime now = LocalDateTime.now();
        log.info("스케줄러 시작 시간 = {}", now);

        List<Issue> expiredIssues = issueRepository.findWithAgendaByIssueStatusAndEndAtIsLessThanEqual(
                OPEN, now);

        if (expiredIssues.isEmpty()) {
            return;
        }

        expiredIssues.forEach(Issue::close);

        List<Long> issueIds = expiredIssues.stream()
                .map(Issue::getId)
                .collect(Collectors.toList());

        List<Long> agendaIds = expiredIssues.stream()
                .map(Issue::getAgenda)
                .map(Agenda::getId)
                .collect(Collectors.toList());

        issueRepository.updateStatusByIds(issueIds, CLOSE);
        agendaRepository.updateStatusByIds(agendaIds, COMPLETED);

        log.info("현안 종료, issueIds : {}", issueIds);
        log.info("현안 종료, agendaIds : {}", agendaIds);
    }
}
