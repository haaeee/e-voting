package gabia.jaime.voting.domain.agenda.web;

import gabia.jaime.voting.fixture.ControllerTest;
import gabia.jaime.voting.fixture.WithMockAdmin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
class AgendaApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockAdmin
    void 사용자는_모든_안건에_대해서_조회가_가능하다() throws Exception {
        // given
        final String authorizationHeader = "Bearer Token";

        // when & then
        mockMvc.perform(get("/api/v1/agendas")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("agenda-search-without-agendaStatus",
                        responseFields(
                                fieldWithPath("code").description("성공 여부"),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.content").description("조회된 안건"),
                                fieldWithPath("data.pageable").description("Pageable")
                        )));
    }

    @Test
    @WithMockAdmin
    void 사용자는_안건에_대해_상태값을_명시하고_조회가_가능하다() throws Exception {
        // given
        final String authorizationHeader = "Bearer Token";

        // when & then
        mockMvc.perform(get("/api/v1/agendas")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("agendaStatus", "PENDING"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("agenda-search-with-agendastatus",
                        responseFields(
                                fieldWithPath("code").description("성공 여부"),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.content").description("조회된 안건"),
                                fieldWithPath("data.pageable").description("Pageable")
                        )));
    }
}