package core.spring_core;

import core.spring_core.discount.DiscountPolicy;
import core.spring_core.discount.FixDiscountPolicy;
import core.spring_core.discount.RateDiscountPolicy;
import core.spring_core.member.MemberRepository;
import core.spring_core.member.MemberService;
import core.spring_core.member.MemberServiceImpl;
import core.spring_core.member.MemoryMemberRepository;
import core.spring_core.order.OrderService;
import core.spring_core.order.OrderServiceImpl;

/**
 * 애플리케이션의 전체 동작 방식을 설정(config)하기 위해
 * 구현 객체를 생성하고, 생성한 객체 인스턴스의 참조를 셍성자를 통해서 주입(연결)하는 책임을 가지는 별도의 설정 클래스
 */
public class AppConfig {
    /* 생성자 주입 (DI) */

    public MemberService memberService() {  // 역할
        return new MemberServiceImpl(memberRepository());  // 구현
    }

    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
