package core.spring_core.member;

import static org.assertj.core.api.Assertions.assertThat;

import core.spring_core.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberServiceTest {

    MemberService memberService;

    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }

    @Test
    public void join() throws Exception {
        // given
        Member member = new Member(1L, "memberA", Grade.VIP);

        // when
        memberService.join(member);

        // then
        Member findMember = memberService.findMember(member.getId());
        assertThat(member).isEqualTo(findMember);
    }

    @Test
    void findMember() {
    }
}