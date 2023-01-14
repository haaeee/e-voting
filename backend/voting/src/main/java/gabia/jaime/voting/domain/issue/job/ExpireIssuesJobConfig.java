//package gabia.jaime.voting.domain.issue.job;
//
//import gabia.jaime.voting.domain.issue.entity.Issue;
//import gabia.jaime.voting.domain.issue.entity.IssueStatus;
//import java.time.LocalDateTime;
//import java.util.Map;
//import javax.persistence.EntityManagerFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.database.JpaCursorItemReader;
//import org.springframework.batch.item.database.JpaItemWriter;
//import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
//import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@RequiredArgsConstructor
//public class ExpireIssuesJobConfig {
//
//    private static final int CHUNK_SIZE = 5;
//
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//    private final EntityManagerFactory entityManagerFactory;
//
//    @Bean
//    public Job expiresIssuesJob() {
//        return jobBuilderFactory.get("expiresIssuesJob")
//                .start(expiresIssuesStep())
//                .build();
//    }
//
//    @Bean
//    public Step expiresIssuesStep() {
//        return stepBuilderFactory.get("expiresIssuesStep")
//                .<Issue, Issue>chunk(CHUNK_SIZE)
//                .reader(expiresIssuesItemReader())
//                .processor(expiresIssuesItemProcessor())
//                .writer(expiresIssuesItemWriter())
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    public JpaCursorItemReader<Issue> expiresIssuesItemReader() {
//        return new JpaCursorItemReaderBuilder<Issue>()
//                .name("expiresIssuesItemReader")
//                .entityManagerFactory(entityManagerFactory)
//                .queryString("select i from Issue i join fetch i.agenda where i.issueStatus = :status and i.endAt <= :endAt")
//                .parameterValues(Map.of("status", IssueStatus.OPEN, "endAt", LocalDateTime.now()))
//                .build();
//    }
//
//    @Bean
//    public ItemProcessor<Issue, Issue> expiresIssuesItemProcessor() {
//        return issue -> {
//            issue.close();
//            return issue;
//        };
//    }
//
//    @Bean
//    public JpaItemWriter<Issue> expiresIssuesItemWriter() {
//        return new JpaItemWriterBuilder<Issue>()
//                .entityManagerFactory(entityManagerFactory)
//                .build();
//    }
//}
