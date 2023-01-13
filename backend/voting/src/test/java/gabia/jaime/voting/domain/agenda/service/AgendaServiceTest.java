package gabia.jaime.voting.domain.agenda.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gabia.jaime.voting.domain.agenda.dto.response.AgendaResponse;
import gabia.jaime.voting.domain.agenda.entity.AgendaStatus;
import gabia.jaime.voting.domain.agenda.repository.AgendaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@DisplayName("안건 조회")
@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @InjectMocks
    private AgendaService sut;

    @Mock
    private AgendaRepository agendaRepository;

    @Test
    void 안건_상태값_없이_게시글을_검색하면_전체_안건_페이지를_반환한다() {
        // given
        Pageable pageable = Pageable.ofSize(10);
        given(agendaRepository.findWithoutAgendaStatus(pageable)).willReturn(Page.empty());

        // when
        Page<AgendaResponse> agendas = sut.search(null, pageable);

        // then
        assertThat(agendas).isEmpty();
        then(agendaRepository).should().findWithoutAgendaStatus(pageable);
    }

    @Test
    void 안건_상태값과_함께_게시글을_검색하면_상태값에_해당하는_안건_페이지를_반환한다() {
        // given
        AgendaStatus agendaStatus = AgendaStatus.COMPLETED;
        Pageable pageable = Pageable.ofSize(10);
        given(agendaRepository.findWithAgendaStatus(agendaStatus, pageable)).willReturn(Page.empty());

        // when
        Page<AgendaResponse> agendas = sut.search(agendaStatus, pageable);

        // then
        assertThat(agendas).isEmpty();
        then(agendaRepository).should().findWithAgendaStatus(agendaStatus, pageable);
    }

}