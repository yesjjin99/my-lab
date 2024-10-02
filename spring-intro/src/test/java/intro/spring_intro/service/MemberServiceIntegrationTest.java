package intro.spring_intro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import intro.spring_intro.domain.Member;
import intro.spring_intro.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/*
- @SpringBootTest : 스프링 컨테이너와 테스트를 함께 실행한다. (스프링을 띄워서 테스트를 실행한다)
- @Transactional : 테스트 케이스에 이 애노테이션이 있으면, 테스트 시작 전에 트랜잭션을 시작하고, 테스트 완료 후(테스트 각각)에 항상 롤백한다.
                   - 이렇게 하면 DB에 데이터가 남지 않으므로 다음 테스트에 영향을 주지 않는다.
                   - 테스트 케이스에 @Transactional 이 붙었을 때만 롤백하도록 동작한다
 */
@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    /* 테스트는 가장 끝단이기 때문에 테스트 코드 작성 시에는 가장 편한 방법을 사용한다^^ */
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void join() throws Exception {
        // given
        Member member = new Member();
        member.setName("hello");

        // when
        Long saveId = memberService.join(member);

        // then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(findMember.getId()).isEqualTo(saveId);
        assertThat(findMember.getName()).isEqualTo("hello");
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("hello");

        Member member2 = new Member();
        member2.setName("hello");

        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
            () -> memberService.join(member2));

        // then
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다!");
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}