package gabia.jaime.voting.domain.agenda.service;


import gabia.jaime.voting.domain.agenda.repository.AgendaRepository;
import gabia.jaime.voting.domain.issue.reposioty.IssueRepository;
import gabia.jaime.voting.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("현안 생성 및 (Pending -> Issue) 상태 변경")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class AgendaAdminServiceTest {

    @InjectMocks
    private AgendaAdminService sut;

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private IssueRepository issueRepository;


}