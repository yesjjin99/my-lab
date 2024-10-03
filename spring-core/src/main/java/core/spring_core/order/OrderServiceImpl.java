package core.spring_core.order;

import core.spring_core.discount.DiscountPolicy;
import core.spring_core.discount.FixDiscountPolicy;
import core.spring_core.member.Member;
import core.spring_core.member.MemberRepository;
import core.spring_core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);  // SRP 원칙이 잘 지켜짐 : 할인에 대한 책임은 discountPolicy 에 넘겼기 때문

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
