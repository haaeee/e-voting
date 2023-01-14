//package gabia.jaime.voting.domain.issue.job;
//
//import static gabia.jaime.voting.domain.issue.entity.IssueStatus.OPEN;
//
//import gabia.jaime.voting.domain.issue.entity.Issue;
//import gabia.jaime.voting.domain.issue.reposioty.IssueRepository;
//import java.time.LocalDateTime;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.StepContribution;
//import org.springframework.batch.core.scope.context.ChunkContext;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class ExpireIssuesTasklet implements Tasklet {
//
//    private final IssueRepository issueRepository;
//
//    @Override
//    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
//        final List<Issue> issues = issueRepository.findWithAgendaByIssueStatusAndEndAtIsLessThanEqual(OPEN,
//                LocalDateTime.now());
//
//        issues.forEach(Issue::close);
//
//        return RepeatStatus.FINISHED;
//    }
//}
