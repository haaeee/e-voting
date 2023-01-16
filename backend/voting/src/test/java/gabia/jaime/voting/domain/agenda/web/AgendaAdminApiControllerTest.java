package gabia.jaime.voting.domain.agenda.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import gabia.jaime.voting.domain.agenda.dto.request.AgendaCreateRequest;
import gabia.jaime.voting.fixture.ControllerTest;
import gabia.jaime.voting.fixture.WithMockAdmin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static gabia.jaime.voting.domain.agenda.entity.AgendaStatus.PENDING;
import static gabia.jaime.voting.domain.agenda.entity.AgendaStatus.RUNNING;
import static gabia.jaime.voting.domain.issue.entity.IssueType.LIMITED;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
class AgendaAdminApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockAdmin
    void 관리자는_PENDING_안건을_생성할_수_있다() throws Exception {
        // given
        final String authorizationHeader = "Bearer Token";
        final AgendaCreateRequest agendaCreateRequest = AgendaCreateRequest.builder()
                .title("title")
                .content("content")
                .agendaStatus(PENDING)
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/agendas")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(agendaCreateRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("agenda-create-without-Issue",
                        requestFields(
                                fieldWithPath("title").description("안건의 주제"),
                                fieldWithPath("content").description("안건의 세부내용"),
                                fieldWithPath("agendaStatus").description("안건의 상태"),
                                fieldWithPath("startAt").description("현안의 투표 시작시각").optional(),
                                fieldWithPath("endAt").description("현안의 투표 종료시각").optional(),
                                fieldWithPath("issueType").description("현안의 투표 방식").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 여부"),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.id").description("생성된 안건 ID"),
                                fieldWithPath("data.title").description("생성된 안건 TITLE"),
                                fieldWithPath("data.content").description("생성된 안건 CONTENT"),
                                fieldWithPath("data.agendaStatus").description("생성된 안건 상태"),
                                fieldWithPath("data.adminId").description("안건을 생성한 관리자 ID")
                        )));
    }

    @Test
    @WithMockAdmin
    void 관리자는_RUNNGING_안건을_생성할_때_현안_ISSUE도_생성할_수_있다() throws Exception {
        // given
        final String authorizationHeader = "Bearer Token";
        final AgendaCreateRequest agendaCreateRequest = AgendaCreateRequest.builder()
                .title("title")
                .content("content")
                .agendaStatus(RUNNING)
                .issueType(LIMITED)
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now().plusDays(2L))
                .build();

        // when & then
        mockMvc.perform(post("/api/v1/agendas")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(agendaCreateRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("agenda-create-with-issue",
                        requestFields(
                                fieldWithPath("title").description("안건의 주제"),
                                fieldWithPath("content").description("안건의 세부내용"),
                                fieldWithPath("agendaStatus").description("안건의 상태"),
                                fieldWithPath("startAt").description("현안의 투표 시작시각"),
                                fieldWithPath("endAt").description("현안의 투표 종료시각"),
                                fieldWithPath("issueType").description("현안의 투표 방식")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 여부"),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.id").description("생성된 안건 ID"),
                                fieldWithPath("data.title").description("생성된 안건 TITLE"),
                                fieldWithPath("data.content").description("생성된 안건 CONTENT"),
                                fieldWithPath("data.agendaStatus").description("생성된 안건 상태"),
                                fieldWithPath("data.issueId").description("생성된 현안 ID"),
                                fieldWithPath("data.adminId").description("안건을 생성한 관리자 ID")
                        )));
    }
}