package gabia.jaime.voting.domain.member.service;

import gabia.jaime.voting.domain.member.dto.response.MemberJoinResponse;
import gabia.jaime.voting.domain.member.dto.response.MemberLoginResponse;
import gabia.jaime.voting.domain.member.entity.Member;
import gabia.jaime.voting.domain.member.repository.MemberRepository;
import gabia.jaime.voting.global.exception.conflict.DuplicatedEmailException;
import gabia.jaime.voting.global.exception.conflict.PasswordInvalidException;
import gabia.jaime.voting.global.exception.notfound.MemberNotFoundException;
import gabia.jaime.voting.global.security.JwtTokenProvider;
import gabia.jaime.voting.global.security.MemberDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private static final Integer DEFAULT_VOTE_RIGHT_COUNT = 1;

    private final String secretKey;
    private final Long expiredTimeMs;
    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;


    public AuthService(@Value("${security.jwt.secret-key}") final String secretKey,
                       @Value("${security.jwt.expired-time-ms}") final Long expiredTimeMs,
                       final JwtTokenProvider jwtTokenProvider,
                       final PasswordEncoder passwordEncoder,
                       final MemberRepository memberRepository) {
        this.secretKey = secretKey;
        this.expiredTimeMs = expiredTimeMs;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }

    public MemberDetails loadMemberByEmail(final String email) {
        Member member = findMember(email);
        return MemberDetails.from(member);
    }

    /**
     * <h2>주주 가입 메서드</h2>
     * @param email
     * @param password
     * @param nickname
     * @param voteRightCount
     */
    @Transactional
    public MemberJoinResponse join(final String email, final String password, final String nickname, Integer voteRightCount) {
        validateEmail(email);
        voteRightCount = checkVoteRightCount(voteRightCount);

        Member member = memberRepository.save(Member.createShareHolder(email, passwordEncoder.encode(password), nickname, voteRightCount));

        return MemberJoinResponse.from(member);
    }

    /**
     * <h2> 관리자 가입 메서드(현재 계획: 관리자 회원 가입 메서드는 제공 X)</h2>
     * @param email
     * @param password
     * @param nickname
     */
    @Transactional
    public MemberJoinResponse joinAdmin(final String email, final String password, final String nickname) {
        validateEmail(email);

        final Member adminMember = memberRepository.save(Member.createAdministrator(email, password, nickname));

        return MemberJoinResponse.from(adminMember);
    }

    /**
     * email 중복 체크 메서드
     * @param email
     */
    private void validateEmail(final String email) {
        memberRepository.findByEmail(email).ifPresent(it -> {
            throw new DuplicatedEmailException();
        });
    }

    /**
     * 주주가 가입할 시에 의결권 갯수가 명시되지 않은 주주는 의결권을 1개 가진다.
     * @param voteRightCount
     */
    private Integer checkVoteRightCount(final Integer voteRightCount) {
        if (Objects.isNull(voteRightCount)) {
            return DEFAULT_VOTE_RIGHT_COUNT;
        }
        return voteRightCount;
    }

    /**
     * <h2>주주, 관리자 로그인 메서드</h2>
     * @param email
     * @param password
     */
    public MemberLoginResponse login(final String email, final String password) {
        Member findMember = findMember(email);

        if (!passwordEncoder.matches(password, findMember.getPassword())) {
            throw new PasswordInvalidException();
        }
        String accessToken = jwtTokenProvider.generateAccessToken(email, secretKey, expiredTimeMs);
        return MemberLoginResponse.of(accessToken);
    }

    private Member findMember(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }
}
