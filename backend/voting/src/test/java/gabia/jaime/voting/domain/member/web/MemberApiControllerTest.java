package gabia.jaime.voting.domain.member.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import gabia.jaime.voting.domain.member.dto.request.MemberJoinRequest;
import gabia.jaime.voting.domain.member.dto.request.MemberLoginRequest;
import gabia.jaime.voting.domain.member.service.AuthService;
import gabia.jaime.voting.fixture.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ControllerTest
class MemberApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AuthService authService;

    @Test
    void 주주_회원가입() throws Exception {
        // given
        MemberJoinRequest joinRequest = MemberJoinRequest.builder()
                .email("TEST@email.com")
                .password("TEST_PASSWORD")
                .nickname("TEST_NICKNAME")
                .voteRightCount(3)
                .build();

        String json = mapper.writeValueAsString(joinRequest);

        // when & then
        mvc.perform(RestDocumentationRequestBuilders.post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("shareholder-join",
                        requestFields(
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("password").description("사용자 비밀번호"),
                                fieldWithPath("nickname").description("사용자 닉네임"),
                                fieldWithPath("voteRightCount").description("주주 의결권 수").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 여부"),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.id").description("생성된 사용자 ID"),
                                fieldWithPath("data.email").description("생성된 사용자 EMAIl"),
                                fieldWithPath("data.voteRightCount").description("생성된 사용자 의결권 수"),
                                fieldWithPath("data.role").description("생성된 사용자 권한")
                        )));
    }
    @Test
    void 사용자_로그인_테스트() throws Exception {
        // given
        MemberJoinRequest joinRequest = MemberJoinRequest.builder()
                .email("TEST@email.com")
                .password("TEST_PASSWORD")
                .nickname("TEST_NICKNAME")
                .voteRightCount(3)
                .build();

        MemberLoginRequest memberLoginRequest = MemberLoginRequest.builder()
                .email("TEST@email.com")
                .password("TEST_PASSWORD")
                .build();


        authService.join(joinRequest.getEmail(), joinRequest.getPassword(), joinRequest.getNickname(), joinRequest.getVoteRightCount());

        String json = mapper.writeValueAsString(memberLoginRequest);


        // when
        mvc.perform(RestDocumentationRequestBuilders.post("/api/v1/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-login",
                        requestFields(
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("password").description("사용자 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("상태 코드"),
                                subsectionWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                fieldWithPath("data.accessToken").description("생성된 사용자 AccessToken")
                        )));
    }
}