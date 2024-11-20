## 임베디드 타입 (복합 값 타입)

- 새로운 값 타입을 직접 정의할 수 있고, 이것을 JPA는 임베디드 타입(embedded type)이라고 한다
  - 주로 기본 값 타입을 모아서 만들어서 복합 값 타입이라고도 함
- 엔티티 타입이 아닌 값 타입에 해당

<br>

- `@Embeddable` : 값 타입을 정의하는 곳에 표시
- `@Embedded` : 값 타입을 사용하는 곳에 표시
  - @Embeddable, @Embedded 둘 중 하나만 넣어도 동작하지만, 둘 다 명시하는 것을 권장한다!
- 기본 생성자 필수

### 임베디드 타입의 장점

- 재사용이 가능하다
- 높은 응집도
- `Period.isWork()` 처럼 해당 값 타입만 사용하는 의미있는 메서드를 만들 수 있다
  - 객체지향적 설계
- 임베디드 타입을 포함한 모든 값 타입은, 값 타입을 소유한 엔티티에 생명주기를 의존

## 임베디드 타입과 테이블 매핑

- 임베디드 타입을 사용하기 전과 후에 매핑하는 테이블은 같다
  - 임베디드 타입은 엔티티의 값일 뿐이며 테이블에는 값의 형태로 매핑된다
- 객체와 테이블을 아주 세밀하게 매핑하는 것이 가능하다
  - 공통으로 사용할 수 있는 것들이 많아지기 때문에 장점이 많다

<br>

- 임베디드 타입을 정의할 때 연관관계를 추가할 수 있다
  - 외래키를 가지고 있으면 된다
- 임베디드 타입의 값이 null이면 매핑한 컬럼 값은 모두 null

## @AttributeOverride, 속성 재정의

- 한 엔티티에서 같은 값 타입을 여러 개 사용하려면 속성 재정의가 필요하다
  - 칼럼명이 중복되어 테이블에 매핑되기 때문
- `@AttributeOverrides`, `@AttributeOverride`를 사용해서 칼럼명 속성을 재정의

```java
@Embedded
@AttributeOverrides({  // 임베디드 타입이 중복되기 때문에(칼럼 중복) 칼럼명 재정의
    @AttributeOverride(name = "city", column = @Column(name = "WORK_CITY")),
    @AttributeOverride(name = "street", column = @Column(name = "WORK_STREET")),
    @AttributeOverride(name = "zipcode", column = @Column(name = "WORK_ZIPCODE"))
})
private Address workAddress;
```

