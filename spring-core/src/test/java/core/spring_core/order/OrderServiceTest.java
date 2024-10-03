package core.spring_core.order;



import static org.assertj.core.api.Assertions.*;

import core.spring_core.member.Grade;
import core.spring_core.member.Member;
import core.spring_core.member.MemberService;
import core.spring_core.member.MemberServiceImpl;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    MemberService memberService = new MemberServiceImpl();
    OrderService orderService = new OrderServiceImpl();

    @Test
    public void createOrder() throws Exception {
        // given
        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        // when
        Order order = orderService.createOrder(memberId, "itemA", 10000);

        // then
        assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }

}