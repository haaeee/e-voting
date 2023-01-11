package gabia.jaime.voting.domain.agenda.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.agenda.entity.AgendaStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static gabia.jaime.voting.domain.agenda.entity.QAgenda.*;
import static gabia.jaime.voting.domain.issue.entity.QIssue.*;

public class AgendaRepositoryCustomImpl implements AgendaRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public AgendaRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Agenda> findWithoutAgendaStatus(final Pageable pageable) {
        final List<Agenda> contents = jpaQueryFactory.selectFrom(agenda)
                .leftJoin(agenda.issue, issue).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        final JPAQuery<Long> countQuery = jpaQueryFactory.select(agenda.count()).from(agenda);

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Agenda> findWithAgendaStatus(final AgendaStatus agendaStatus, final Pageable pageable) {
        final List<Agenda> contents = jpaQueryFactory.selectFrom(agenda)
                .leftJoin(agenda.issue, issue).fetchJoin()
                .where(agenda.agendaStatus.eq(agendaStatus))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        final JPAQuery<Long> countQuery = jpaQueryFactory.select(agenda.count()).from(agenda).where(agenda.agendaStatus.eq(agendaStatus));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }
}
