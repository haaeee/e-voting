package gabia.jaime.voting.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import gabia.jaime.voting.global.dto.Result;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(mapper.writeValueAsString(Result.createErrorResult("인증과정에서 오류가 발생하였습니다.")));
    }
}
