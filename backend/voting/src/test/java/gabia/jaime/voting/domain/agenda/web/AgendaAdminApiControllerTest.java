package gabia.jaime.voting.domain.agenda.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import gabia.jaime.voting.domain.agenda.dto.request.AgendaCreateRequest;
import gabia.jaime.voting.domain.agenda.service.AgendaAdminService;
import gabia.jaime.voting.fixture.WithMockAdmin;
import gabia.jaime.voting.global.security.MemberDetails;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static gabia.jaime.voting.domain.agenda.entity.AgendaStatus.PENDING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(value = AgendaAdminApiControllerTest.class)
class AgendaAdminApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AgendaAdminService agendaAdminService;

    @Disabled
    @WithMockAdmin
    @Test
    void 관리자는_안건을_생성할_수_있다() throws Exception {
        // given
        final String authorizationHeader = "Bearer Token";
        final AgendaCreateRequest agendaCreateRequest = AgendaCreateRequest.builder()
                .title("title")
                .content("content")
                .agendaStatus(PENDING)
                .build();
        given(agendaAdminService.save(any(MemberDetails.class), any(AgendaCreateRequest.class)))
                .willReturn(1L);

        // when
        final ResultActions resultActions = mockMvc.perform(
                post("/api/v1/agendas")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(agendaCreateRequest))
        );

        // then
//        resultActions.andExpect(status().isCreated())
//                .andDo(print())
//                .andDo(
//                        document("agendas-create")
//                );
    }
}