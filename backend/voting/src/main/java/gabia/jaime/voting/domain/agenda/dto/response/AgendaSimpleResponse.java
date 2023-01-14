package gabia.jaime.voting.domain.agenda.dto.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonInclude;
import gabia.jaime.voting.domain.agenda.entity.Agenda;
import gabia.jaime.voting.domain.agenda.entity.AgendaStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(Include.NON_NULL)
public class AgendaSimpleResponse {

    private final Long agendaId;

    private final String title;

    private final String content;

    private final AgendaStatus agendaStatus;

    private final Long adminId;

    @Builder
    private AgendaSimpleResponse(final Long agendaId, final String title, final String content,
                                 final AgendaStatus agendaStatus, final Long issueId, final Long adminId) {
        this.agendaId = agendaId;
        this.title = title;
        this.content = content;
        this.agendaStatus = agendaStatus;
        this.adminId = adminId;
    }

    public static AgendaSimpleResponse from(Agenda agenda) {
        return AgendaSimpleResponse.builder()
                .agendaId(agenda.getId())
                .title(agenda.getTitle())
                .content(agenda.getContent())
                .agendaStatus(agenda.getAgendaStatus())
                .adminId(agenda.getMember().getId())
                .build();
    }
}
