package gabia.jaime.voting.fixture;

import java.util.List;

import gabia.jaime.voting.global.security.MemberDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import static gabia.jaime.voting.domain.member.entity.Role.ROLE_SHAREHOLDER;

public class WithMockCustomShareHolderSecurityContextFactory implements WithSecurityContextFactory<WithMockShareHolder> {

    public SecurityContext createSecurityContext(final WithMockShareHolder annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(ROLE_SHAREHOLDER.name()));
        MemberDetails memberDetails = MemberDetails.builder()
                .email("member1@email.com")
                .role(ROLE_SHAREHOLDER)
                .build();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                memberDetails, null, grantedAuthorities);
        context.setAuthentication(authenticationToken);
        return context;
    }
}
