package gabia.jaime.voting.domain.issue.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import gabia.jaime.voting.domain.agenda.dto.request.AgendaCreateRequest;
import gabia.jaime.voting.domain.agenda.service.AgendaAdminService;
import gabia.jaime.voting.domain.issue.service.IssueService;
import gabia.jaime.voting.domain.vote.dto.request.VoteCreateRequest;
import gabia.jaime.voting.domain.vote.service.VoteService;
import gabia.jaime.voting.fixture.ControllerTest;
import gabia.jaime.voting.fixture.WithMockAdmin;
import gabia.jaime.voting.fixture.WithMockShareHolder;
import gabia.jaime.voting.global.security.MemberDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static gabia.jaime.voting.domain.agenda.entity.AgendaStatus.*;
import static gabia.jaime.voting.domain.issue.entity.IssueType.*;
import static gabia.jaime.voting.domain.member.entity.Role.ROLE_ADMIN;
import static gabia.jaime.voting.domain.vote.entity.VoteType.YES;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest
class IssueApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockShareHolder
    void 주주는_투표를_할_수_있다() throws Exception {
        // given
        final String authorizationHeader = "Bearer Token";
        final VoteCreateRequest voteCreateRequest = new VoteCreateRequest(YES);

        // when & then
        mvc.perform(post("/api/v1/issues/{issueId}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(voteCreateRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("vote-create",
                        requestFields(
                                fieldWithPath("voteType").description("투표 YES/NO/GIVE_UP")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 여부"),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.id").description("생성된 투표 ID"),
                                fieldWithPath("data.message").description("투표 성공 여부 Message")
                        )));
    }

    @Test
    @WithMockShareHolder
    void 주주는_현안을_조회할_수_있다() throws Exception {
        // given
        final String authorizationHeader = "Bearer Token";
        // when & then
        mvc.perform(get("/api/v1/issues/{issueId}", 1L)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("vote-search-shareholder",
                        responseFields(
                                fieldWithPath("code").description("성공 여부"),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.id").description("투표 ID"),
                                fieldWithPath("data.issueStatus").description("투표 진행 여부"),
                                fieldWithPath("data.yesCount").description("투표 찬성 갯수"),
                                fieldWithPath("data.noCount").description("투표 반대 갯수"),
                                subsectionWithPath("data.agenda").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.agenda.agendaId").description("안건 ID"),
                                fieldWithPath("data.agenda.title").description("안건 TITLE"),
                                fieldWithPath("data.agenda.content").description("안건 CONTENT"),
                                fieldWithPath("data.agenda.agendaStatus").description("안건 상태"),
                                fieldWithPath("data.agenda.adminId").description("안건을 생성한 관리자 ID")
                        )));
    }

    @Test
    @WithMockAdmin
    void 관리자는_현안을_조회할_수_있다() throws Exception {
        // given
        final String authorizationHeader = "Bearer Token";

        FieldDescriptor[] vote = new FieldDescriptor[]{
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("투표 ID"),
                fieldWithPath("voteType").type(JsonFieldType.STRING).description("투표 찬성/반대/기권"),
                fieldWithPath("voteCount").type(JsonFieldType.NUMBER).description("투표에 행사한 의결권 수"),
                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("투표 의결권 사용자 ID"),
                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("투표 생성시간")
        };


        // when & then
        mvc.perform(get("/api/v1/issues/{issueId}", 4L)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("vote-search-admin",
                        responseFields(
                                fieldWithPath("code").description("성공 여부"),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.id").description("투표 ID"),
                                fieldWithPath("data.issueStatus").description("투표 진행 여부"),
                                fieldWithPath("data.yesCount").description("투표 찬성 갯수"),
                                fieldWithPath("data.noCount").description("투표 반대 갯수"),
                                subsectionWithPath("data.agenda").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.agenda.agendaId").description("안건 ID"),
                                fieldWithPath("data.agenda.title").description("안건 TITLE"),
                                fieldWithPath("data.agenda.content").description("안건 CONTENT"),
                                fieldWithPath("data.agenda.agendaStatus").description("안건 상태"),
                                fieldWithPath("data.agenda.adminId").description("안건을 생성한 관리자 ID"),
                                subsectionWithPath("data.votes").type(JsonFieldType.ARRAY).description("투표 현황")
                        ).andWithPrefix("data.votes.[]", vote)));
    }
}