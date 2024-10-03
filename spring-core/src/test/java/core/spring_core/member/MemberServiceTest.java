package core.spring_core.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MemberServiceTest {

    private final MemberService memberService = new MemberServiceImpl();

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