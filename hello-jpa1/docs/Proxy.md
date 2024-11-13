## 프록시란?

- 실제 클래스를 상속받아서 만들어지는 가짜 객체
- 실제 클래스와 겉 모양은 같지만, 안은 비어있다

- 프록시 객체는 실제 객체의 참조(target)를 보관한다
- 프록시 객체를 호출하면 프록시 객체는 실제 객체의 메서드를 호출한다

```java
findMember = em.find(Member.class, member.getId());  // 실제 엔티티

refMember = em.getReference(Member.class, member.getId());  // 프록시 객체
```

> getReference() 호출시점에는 쿼리가 나가지 않지만, 엔티티의 값이 실제 사용되는 시점에 쿼리가 나간다
> <br>=> em.find() 와 달리 실제 엔티티 객체가 아닌 가짜, 속칭 프록시 엔티티 객체를 반환해준다

<br>

## 프록시 객체의 초기화

1. getReference() 를 통해 프록시 객체를 가져온다
   - 처음에는 프록시 안에 target 값이 없기 때문에 getName() 같은 메서드가 호출되면 JPA가 영속성 컨텍스트에 진짜 멤버 객체를 가져오도록 초기화를 요청한다!
2. Member 의 속성인 name 을 실제 사용하는 시점에 내부적으로 영속성 컨텍스트에게 초기화를 요청한다
    - 레퍼런스 객체가 내부에 실제 엔티티 타겟에 대한 값을 가진다
3. 이미 초기화가 되었으니, 이후부터는 초기화 없이 프록시(레퍼런스 객체)가 target 에게 요청을 전달한다

<br>

## 프록시의 특징

> 프록시 객체는 처음 사용할 때 한 번만 초기화
- 이후에는 초기화를 하지 않고 내부에 등록된 target 을 통해 접근하게 된다

> 프록시 객체가 초기화되면 프록시 객체를 통해서 실제 엔티티에 접근 가능
- 교체되는 게 아니라 프록시는 유지되고, 내부의 target 에만 실제 엔티티를 생성해서 값을 딱 채우는 것

```java
Member member = new Member();
member.setName("hello");
em.persist(member);

em.flush();
em.clear();

Member refMember = em.getReference(Member.class, member.getId());
System.out.println("before: refMember = " + refMember.getClass());
System.out.println("refMember.getName() = " + refMember.getName());
System.out.println("after: refMember = " + refMember.getClass());
```
```java
before: refMember = class hellojpa.Member$HibernateProxy$uPL1nDw0
Hibernate: 
    select
        m1_0.MEMBER_ID,
        m1_0.createdBy,
        m1_0.createdDate,
        m1_0.lastModifiedBy,
        m1_0.lastModifiedDate,
        l1_0.name,
        m1_0.USERNAME,
        t1_0.TEAM_ID,
        t1_0.createdBy,
        t1_0.createdDate,
        t1_0.lastModifiedBy,
        t1_0.lastModifiedDate,
        t1_0.name 
    from
        Member m1_0
    left join
        Team t1_0 
            on t1_0.TEAM_ID=m1_0.TEAM_ID 
    where
        m1_0.MEMBER_ID=?
refMember.getName() = hello
after: refMember = class hellojpa.Member$HibernateProxy$uPL1nDw0
```

<br>

> 프록시 객체는 원본 엔티티를 상속받음, 따라서 타입 체크시 주의해야 함 (== 비교 실패, 대신 instance of 사용)
- 즉, 프록시 객체의 타입 체크를 하고싶다면 instance of 를 사용해야 한다!

```java
Member member1 = new Member();
member1.setName("hello1");
em.persist(member1);

Member member2 = new Member();
member2.setName("hello2");
em.persist(member2);

em.flush();
em.clear();

Member m1 = em.find(Member.class, member1.getId());
Member m2 = em.getReference(Member.class, member2.getId());

System.out.println("m1 == m2: " + (m1.getClass() == m2.getClass()));  // false
System.out.println("m1 == Member: " + (m1 instanceof Member));  // true
System.out.println("m2 == Member: " + (m2 instanceof Member));  // true
```

<br>

> 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 em.getReference()를 호출해도 실제 엔티티 반환
- 처음에 엔티티로 조회하면 이후에도 엔티티를 반환하고, 반대로 처음에 프록시로 조회하면 이후에도 프록시를 반환
  - 같은 트랜잭션 레벨, 같은 영속성 컨텍스트 안에서 가져온 것이며 PK 가 같으면 -> JPA는 항상 동일한 객체를 반환해주어야 한다 (Repeatable Read)
- 즉, 중요한 것은 최초에 무엇으로 조회를 했는지!
  - 처음 em.find() 로 조회하면 1차 캐시에 엔티티가 올라가 이후 getReference() 를 해도 동일한 엔티티가 나오게 된다
  - 처음 em.getReference() 를 조회하면 이후에 em.find(), em.getReference() 를 해도 같은 프록시 객체 인스턴스가 나오게 된다
  - JPA가 같은 트랜잭션 레벨, 같은 영속성 컨텍스트 내에서 동일한 객체를 조회할 때 항상 타입 비교 == 의 true 를 보장한다 (일관성)

```java
Member member = new Member();
member.setName("hello1");
em.persist(member);

em.flush();
em.clear();

Member findMember = em.find(Member.class, member.getId());
System.out.println("findMember = " + findMember.getClass());

Member refMember = em.getReference(Member.class, findMember.getId());
System.out.println("refMember = " + refMember.getClass());

System.out.println("findMember == refMember: " + (findMember == refMember));
```
```java
findMember = class hellojpa.Member
refMember = class hellojpa.Member
findMember == refMember: true
```
<br>

```java
Member member = new Member();
member.setName("hello1");
em.persist(member);

em.flush();
em.clear();

Member refMember1 = em.getReference(Member.class, member.getId());
System.out.println("refMember1 = " + refMember1.getClass());

Member refMember2 = em.getReference(Member.class, refMember1.getId());
System.out.println("refMember2 = " + refMember2.getClass());

System.out.println("refMember1 == refMember2: " + (refMember1 == refMember2));
```
```java
refMember1 = class hellojpa.Member$HibernateProxy$fkYCxjn4
refMember2 = class hellojpa.Member$HibernateProxy$fkYCxjn4
refMember1 == refMember2: true
```
<br>

```java
Member member = new Member();
member.setName("hello1");
em.persist(member);

em.flush();
em.clear();

Member refMember = em.getReference(Member.class, member.getId());
System.out.println("refMember = " + refMember.getClass());

Member findMember = em.getReference(Member.class, refMember.getId());
System.out.println("findMember = " + findMember.getClass());

System.out.println("refMember == findMember: " + (refMember == findMember));
```
```java
refMember = class hellojpa.Member$HibernateProxy$bonVUHC9
findMember = class hellojpa.Member$HibernateProxy$bonVUHC9
refMember == findMember: true
```
<br>

> 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태일 때, 프록시를 초기화하면 문제 발생
- `org.hibernate.LazyInitializationException` 예외

```java
    Member member = new Member();
    member.setName("hello");
    em.persist(member);
    
    em.flush();
    em.clear();
    
    Member refMember = em.getReference(Member.class, member.getId());
    System.out.println("refMember = " + refMember.getClass());
    
    em.clear();  // 영속성 컨텍스트 비우기
    
    refMember.getName();  // 프록시 초기화 시도
    
    tx.commit();  // 트랜잭션 커밋
} catch (Exception e){
    tx.rollback();
    e.printStackTrace();
}
```
```java
org.hibernate.LazyInitializationException: could not initialize proxy [hellojpa.Member#1] - no Session
	at org.hibernate.proxy.AbstractLazyInitializer.initialize(AbstractLazyInitializer.java:165)
	at org.hibernate.proxy.AbstractLazyInitializer.getImplementation(AbstractLazyInitializer.java:314)
	at org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor.intercept(ByteBuddyInterceptor.java:44)
	at org.hibernate.proxy.ProxyConfiguration$InterceptorDispatcher.intercept(ProxyConfiguration.java:102)
	at hellojpa.Member$HibernateProxy$fMvjdfcg.getName(Unknown Source)
	at hellojpa.JpaMain.main(JpaMain.java:95)
```

