package gabia.jaime.voting.domain.agenda.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.agenda.entity.AgendaStatus;
import lombok.Builder;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

@Getter
@JsonInclude(Include.NON_NULL)
public class AgendaResponse {

    private final Long id;

    private final String title;

    private final String content;

    private final AgendaStatus agendaStatus;

    private final Long issueId;

    private final Long adminId;

    @Builder
    private AgendaResponse(final Long id, final String title, final String content,
                          final AgendaStatus agendaStatus, final Long issueId, final Long adminId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.agendaStatus = agendaStatus;
        this.issueId = issueId;
        this.adminId = adminId;
    }

    public static AgendaResponse from(Agenda agenda) {
        return AgendaResponse.builder()
                .id(agenda.getId())
                .title(agenda.getTitle())
                .content(agenda.getContent())
                .agendaStatus(agenda.getAgendaStatus())
                .issueId(agenda.getIssue() == null ? null : agenda.getId())
                .adminId(agenda.getMember().getId())
                .build();
    }
}
