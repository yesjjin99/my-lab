# 스프링의 핵심

- 스프링은 자바 언어 기반의 프레임워크
- 자바 언어의 가장 큰 특징 → 객체 지향 언어
- 스프링은 객체 지향 언어가 가진 강력한 특징을 살려내는 프레임워크
- 스프링은 좋은 객체 지향 애플리케이션을 개발할 수 있게 도와주는 프레임워크

> 💡 스프링의 진짜 핵심은 결국 제대로 된 객체 지향 프로그래밍을 할 수 있게 도와주는 도구라는 것!

<br>

# 객체 지향 원리 적용

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

## 관심사 분리

### SRP 단일 책임 원칙
**한 클래스는 하나의 책임만 가져야 한다.**

- 클라이언트 객체는 직접 구현 객체를 생성하고, 연결하고, 실행하는 다양한 책임을 가지고 있음 
- SRP 단일 책임 원칙을 따르면서 관심사를 분리함
- 구현 객체를 생성하고 연결하는 책임은 AppConfig가 담당
- 클라이언트 객체는 실행하는 책임만 담당

### DIP 의존관계 역전 원칙
**프로그래머는 “추상화에 의존해야지, 구체화에 의존하면 안된다.” 의존성 주입은 이 원칙을 따르는 방법 중 하나다.**

- 새로운 할인 정책을 개발하고, 적용하려고 하니 클라이언트 코드도 함께 변경해야 했다. 
  - 왜냐하면 기존 클라이언트 코드( `OrderServiceImpl` )는 DIP를 지키며 `DiscountPolicy` 추상화 인터페이스에 의존하는 것 같았지만
  - `FixDiscountPolicy` 구체화 구현 클래스에도 함께 의존했다.
- 클라이언트 코드가 `DiscountPolicy` 추상화 인터페이스에만 의존하도록 코드를 변경했다.
  - 하지만 클라이언트 코드는 인터페이스만으로는 아무것도 실행할 수 없다.
- AppConfig가 `FixDiscountPolicy` 객체 인스턴스를 클라이언트 코드 대신 생성해서 클라이언트 코드에 의 존관계를 주입했다. 
- 이렇게 해서 DIP 원칙을 따르면서 문제도 해결했다.
  
### OCP 개방 폐쇄 원칙
**소프트웨어 요소는 확장에는 열려 있으나 변경에는 닫혀 있어야 한다**

- 다형성 사용하고 클라이언트가 DIP를 지킴
- 애플리케이션을 사용 영역과 구성 영역으로 나눔
- AppConfig가 의존관계를 `FixDiscountPolicy` `RateDiscountPolicy` 로 변경해서 클라이언트 코드에 주입하므로 클라이언트 코드는 변경하지 않아도 됨
- **소프트웨어 요소를 새롭게 확장해도 사용 영역의 변경은 닫혀 있다!**
