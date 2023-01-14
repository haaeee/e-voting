package gabia.jaime.voting.domain.issue.job;

import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.issue.entity.IssueStatus;
import java.time.LocalDateTime;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ExpireIssuesTaskletJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ExpireIssuesTasklet expireIssuesTasklet;

    @Bean
    public Job expiresIssuesJob() {
        return jobBuilderFactory.get("expiresIssuesJob")
                .start(expiresIssuesStep())
                .build();
    }

    @Bean
    public Step expiresIssuesStep() {
        return stepBuilderFactory.get("expiresIssuesStep")
                .tasklet(expireIssuesTasklet)
                .build();
    }
}
