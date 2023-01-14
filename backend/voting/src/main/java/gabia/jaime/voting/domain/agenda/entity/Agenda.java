package gabia.jaime.voting.domain.agenda.entity;

import gabia.jaime.voting.domain.issue.entity.Issue;
import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.global.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "agenda")
@Getter @ToString(callSuper = true)
public class Agenda extends BaseEntity {

    private static final int MAXIMUM_CONTENT_LENGTH = 1000;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, length = MAXIMUM_CONTENT_LENGTH)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "agenda_status", nullable = false)
    private AgendaStatus agendaStatus;

    @OneToOne(mappedBy = "agenda", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Issue issue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    protected Agenda() {
    }

    @Builder
    private Agenda(Long id, String title, String content, AgendaStatus agendaStatus, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.agendaStatus = agendaStatus;
        this.member = member;
    }

    public static Agenda of(String title, String content, AgendaStatus agendaStatus, Member member) {
        return Agenda.builder()
                .title(title)
                .content(content)
                .agendaStatus(agendaStatus)
                .member(member)
                .build();
    }

    public void changeRunningStatus(Issue issue) {
        this.issue = issue;
        this.agendaStatus = AgendaStatus.RUNNING;
    }

    public void changeCompletedStatus() {
        this.agendaStatus = AgendaStatus.COMPLETED;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agenda agenda = (Agenda) o;
        return getId().equals(agenda.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
