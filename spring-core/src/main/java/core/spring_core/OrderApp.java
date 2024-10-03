package core.spring_core;

import core.spring_core.member.Grade;
import core.spring_core.member.Member;
import core.spring_core.member.MemberService;
import core.spring_core.member.MemberServiceImpl;
import core.spring_core.order.Order;
import core.spring_core.order.OrderService;
import core.spring_core.order.OrderServiceImpl;

public class OrderApp {

    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        OrderService orderService = new OrderServiceImpl();

        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);

        System.out.println("order = " + order);  // toString() 실행됨
        System.out.println("order.calculatePrice = " + order.calculatePrice());
    }
}
