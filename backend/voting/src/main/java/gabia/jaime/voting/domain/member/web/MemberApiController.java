package gabia.jaime.voting.domain.member.web;

import gabia.jaime.voting.domain.member.dto.request.MemberJoinRequest;
import gabia.jaime.voting.domain.member.dto.request.MemberLoginRequest;
import gabia.jaime.voting.domain.member.dto.response.MemberJoinResponse;
import gabia.jaime.voting.domain.member.dto.response.MemberLoginResponse;
import gabia.jaime.voting.domain.member.service.AuthService;
import gabia.jaime.voting.global.dto.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberApiController {

    private final AuthService authService;

    public MemberApiController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody MemberJoinRequest request) {
        MemberJoinResponse response = authService.join(request.getEmail(), request.getPassword(), request.getNickname(), request.getVoteRightCount());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Result.createSuccessResult(response));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody MemberLoginRequest request) {
        MemberLoginResponse response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.OK)
                .body(Result.createSuccessResult(response));
    }

}