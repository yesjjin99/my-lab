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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 애플리케이션의 전체 동작 방식을 설정(config)하기 위해 (애플리케이션의 구성, 설정 정보)
 * 구현 객체를 생성하고, 생성한 객체 인스턴스의 참조를 셍성자를 통해서 주입(연결)하는 책임을 가지는 별도의 설정 클래스
 */
@Configuration
public class AppConfig {
    /* 생성자 주입 (DI) */

    @Bean
    public MemberService memberService() {  // 역할
        return new MemberServiceImpl(memberRepository());  // 구현
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
