package gabia.jaime.voting.global.security;

import gabia.jaime.voting.domain.member.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String TOKEN_TYPE = "Bearer";

    private final String secretKey;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(final String secretKey, final AuthService authService, final JwtTokenProvider jwtTokenProvider) {
        this.secretKey = secretKey;
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(header) || !header.startsWith((TOKEN_TYPE))) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = header.split(" ")[1].trim();

            String username = jwtTokenProvider.getUsername(token, secretKey);
            MemberDetails memberDetails = authService.loadMemberByEmail(username);

            if (!jwtTokenProvider.validate(token, username, secretKey)) {
                filterChain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    memberDetails, null,
                    memberDetails.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (RuntimeException e) {
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

}