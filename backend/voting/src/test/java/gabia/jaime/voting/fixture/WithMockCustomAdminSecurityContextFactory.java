package gabia.jaime.voting.fixture;

import gabia.jaime.voting.global.security.MemberDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

import static gabia.jaime.voting.domain.member.entity.Role.ROLE_ADMIN;

public class WithMockCustomAdminSecurityContextFactory implements WithSecurityContextFactory<WithMockAdmin> {

    @Override
    public SecurityContext createSecurityContext(final WithMockAdmin annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(ROLE_ADMIN.name()));
        MemberDetails memberDetails = MemberDetails.builder()
                .email("admin@email.com")
                .role(ROLE_ADMIN)
                .build();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                memberDetails, null, grantedAuthorities);
        context.setAuthentication(authenticationToken);
        return context;
    }
}
