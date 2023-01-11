package gabia.jaime.voting.global.config;

import gabia.jaime.voting.domain.member.service.AuthService;
import gabia.jaime.voting.global.exception.CustomAuthenticationEntryPoint;
import gabia.jaime.voting.global.security.JwtTokenFilter;
import gabia.jaime.voting.global.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final AuthService authService;
    private final String secretKey;
    private final JwtTokenProvider jwtTokenProvider;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(final AuthService authService,
                          @Value("${security.jwt.secret-key}") final String secretKey,
                          final JwtTokenProvider jwtTokenProvider,
                          final CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.authService = authService;
        this.secretKey = secretKey;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .mvcMatchers(
                                "/api/*/members/join",
                                "/api/*/members/login").permitAll()
                        .antMatchers("/api/**/agendas", "/api/**/votes", "api/**/issues/").authenticated()
                )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtTokenFilter(secretKey, authService, jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .build();
    }
}
