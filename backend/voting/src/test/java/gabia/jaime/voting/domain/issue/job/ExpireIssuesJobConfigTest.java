package gabia.jaime.voting.domain.issue.job;

import static gabia.jaime.voting.domain.agenda.entity.AgendaStatus.RUNNING;
import static gabia.jaime.voting.domain.issue.entity.IssueType.LIMITED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gabia.jaime.voting.config.TestBatchConfig;
import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.agenda.repository.AgendaRepository;
import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.issue.reposioty.IssueRepository;
import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.domain.member.repository.MemberRepository;
import gabia.jaime.voting.global.config.JpaConfig;
import gabia.jaime.voting.global.config.P6spyConfig;
import gabia.jaime.voting.global.exception.notfound.MemberNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBatchTest
@SpringBootTest
@ContextConfiguration(classes = {ExpireIssuesTaskletJobConfig.class, ExpireIssuesTasklet.class, TestBatchConfig.class, JpaConfig.class, P6spyConfig.class})
class ExpireIssuesJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Test
    void test_expiresIssuesJob() throws Exception {
        // given
        Member admin = memberRepository.findById(6L).orElseThrow(MemberNotFoundException::new);
        final LocalDateTime now = LocalDateTime.now();
        List<Agenda> agendas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Agenda agenda = Agenda.of("title", "content", RUNNING, admin);
            Issue issue = Issue.of(agenda, LIMITED, now.minusDays(10L), now.minusDays(1L));
            agenda.setIssue(issue);
            agendaRepository.save(agenda);
        }

        issueRepository.findAll().forEach(System.out::println);


        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        JobInstance jobInstance = jobExecution.getJobInstance();

        // then
        assertAll(
                () -> assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED),
                () -> assertThat(jobInstance.getJobName()).isEqualTo("expiresIssuesJob")
        );
    }

}