package gabia.jaime.voting.domain.member.service;

import gabia.jaime.voting.domain.member.dto.response.MemberJoinResponse;
import gabia.jaime.voting.domain.member.dto.response.MemberLoginResponse;
import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.domain.member.entity.Role;
import gabia.jaime.voting.domain.member.repository.MemberRepository;
import gabia.jaime.voting.global.exception.badrequest.DuplicatedEmailException;
import gabia.jaime.voting.global.exception.notfound.MemberNotFoundException;
import gabia.jaime.voting.global.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static gabia.jaime.voting.fixture.AdminFixture.*;
import static gabia.jaime.voting.fixture.MemberFixture.MEMBER_1;
import static gabia.jaime.voting.fixture.MemberFixture.MEMBER_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService sut;

    @Mock
    MemberRepository memberRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Test
    void 주주_회원이_가입정보를_입력하면_회원_가입_정보가_제공된다() {
        // given
        final Member member_1 = MEMBER_1.createMember(1L);

        given(memberRepository.findByEmail(member_1.getEmail())).willReturn(Optional.empty());
        // TODO: TestCode에서 static 메서드로 member를 생성하여, equalsAndHashCode 에서 nullPointerException 발생
        given(memberRepository.save(any(Member.class))).willReturn(member_1);

        // when
        final MemberJoinResponse joinResponse = sut.join(member_1.getEmail(), member_1.getPassword(), member_1.getNickname(), member_1.getVoteRightCount());

        // then
        assertAll(
                () -> assertThat(joinResponse.getId()).isEqualTo(1L),
                () -> assertThat(joinResponse.getEmail()).isEqualTo("member1@email.com"),
                () -> assertThat(joinResponse.getRole()).isEqualTo(Role.SHAREHOLDER)
        );
    }

    @Test
    void 관리자_회원이_가입정보를_입력하면_회원_가입_정보가_제공된다() {
        // given
        final Member admin_1 = ADMIN1.createAdmin(1L);

        given(memberRepository.findByEmail(admin_1.getEmail())).willReturn(Optional.empty());
        // TODO: TestCode에서 static 메서드로 member를 생성하여, equalsAndHashCode 에서 nullPointerException 발생
        given(memberRepository.save(any(Member.class))).willReturn(admin_1);

        // when
        final MemberJoinResponse joinResponse = sut.joinAdmin(admin_1.getEmail(), admin_1.getPassword(), admin_1.getNickname());

        // then
        assertAll(
                () -> assertThat(joinResponse.getId()).isEqualTo(1L),
                () -> assertThat(joinResponse.getEmail()).isEqualTo("admin1@email.com"),
                () -> assertThat(joinResponse.getRole()).isEqualTo(Role.ADMIN)
        );
    }

    @Test
    void 회원_가입시에_email_중복이_발생하면_예외를_던진다() {
        final Member member_1 = MEMBER_1.createMember(1L);

        given(memberRepository.findByEmail(member_1.getEmail())).willReturn(Optional.of(member_1));

        // when
        final Throwable t = catchThrowable(() -> sut.join(member_1.getEmail(), member_1.getPassword(), member_1.getNickname(), member_1.getVoteRightCount()));

        // then
        assertThat(t)
                .isInstanceOf(DuplicatedEmailException.class);
        then(memberRepository).should().findByEmail(member_1.getEmail());
    }

    @Test
    void 주주_회원_가입시에_의결권을_명시하지_않으면_기본_의결권_갯수인_1개로_제공한다() {
        final Member member_2 = MEMBER_2.createMember(1L);

        given(memberRepository.findByEmail(member_2.getEmail())).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member_2);

        // when
        final MemberJoinResponse joinResponse = sut.join(member_2.getEmail(), member_2.getPassword(), member_2.getNickname(), null);

        // then
        assertThat(joinResponse.getVoteRightCount()).isEqualTo(1);
    }

    @Test
    void 로그인정보를_입력하면_로그인_후_토큰이_발급된다() {
        // given
        Member member_1 = MEMBER_1.createMember();
        String accessToken = "test";

        ReflectionTestUtils.setField(sut, "secretKey", "abcdefghijklmnopqrxtuvwxyz-1234567890");
        ReflectionTestUtils.setField(sut, "expiredTimeMs", 8640000L);

        given(memberRepository.findByEmail(member_1.getEmail())).willReturn(Optional.of(MEMBER_1.createMember(1L)));
        given(passwordEncoder.matches(member_1.getPassword(), MEMBER_1.createMember(1L).getPassword())).willReturn(true);
        given(jwtTokenProvider.generateAccessToken(member_1.getEmail(), "abcdefghijklmnopqrxtuvwxyz-1234567890", 8640000L))
                .willReturn(accessToken);

        // when
        MemberLoginResponse response = sut.login(member_1.getEmail(), member_1.getPassword());

        // then
        assertThat(response.getAccessToken()).isEqualTo("test");
    }

    @Test
    void 로그인시_유저가_존재하지_않으면_에외를_던진다() {
        // given
        Member member_1 = MEMBER_1.createMember();
        given(memberRepository.findByEmail(member_1.getEmail())).willReturn(Optional.empty());

        // when
        final Throwable t = catchThrowable(() -> sut.login(member_1.getEmail(), member_1.getPassword()));

        // then
        assertThat(t)
                .isInstanceOf(MemberNotFoundException.class);
        then(memberRepository).should().findByEmail(member_1.getEmail());
    }

}