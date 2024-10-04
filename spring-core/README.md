## OCP와 DIP 위반
```java
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
```

**DIP 위반**
- 구체화가 아닌 추상화에 의존해야 한다!
- 그러나 주문서비스 클라이언트(OrderServiceImpl)에서 추상(인터페이스) 뿐만 아니라 구체 클래스에도 의존하고 있다

**OCP 위반**
- 변경하지 않고 확장할 수 있어야 한다!
- 그러나 현재 코드는 기능을 확장해서 변경하면, 클라이언트 코드에도 영향을 준다