## 컴포넌트 스캔

- 컴포넌트 스캔을 사용하려면 먼저 `@ComponentScan` 을 설정 정보에 붙여주면 된다
  - 기존에 직접 스프링 빈 등록과 의존관계를 주입하던 AppConfig 와 다르게 @Bean 으로 클래스를 등록하지 않아도 된다

> 컴포넌트 스캔은 `@Component` 애노테이션이 붙은 클래스를 모두 스캔해서 스프링 빈으로 등록한다


```java
@Component
 public class MemberServiceImpl implements MemberService {
     private final MemberRepository memberRepository;
}
```

<br>

### Autowired

- 기존에는 @Bean 으로 등록한 스프링 빈들을 사용해 직접 의존관계를 명시해주었지만, 컴포넌트 스캔의 경우 이런 설정 정보 자체가 없다
- 따라서 @Autowired 애노테이션을 통해 의존관계를 자동으로 주입시켜준다
  - 생성자에 @Autowired 를 지정하면 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입한다

```java
@Component
 public class MemberServiceImpl implements MemberService {
     private final MemberRepository memberRepository;
     
     @Autowired
     public MemberServiceImpl(MemberRepository memberRepository) {
         this.memberRepository = memberRepository;
     }
}
```

<br>

### 컴포넌트 스캔 기본 대상

- 컴포넌트 스캔의 시작 위치를 지정할 수 있다
  - 권장하는 방법은 패키지 위치를 지정하지 않고, 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것
  - 스프링 부트도 이 방법을 기본으로 제공한다
    - @SpringBootApplication을 프로젝트의 시작 루트 위치에 두는 것이 관례 (@ComponentScan이 붙어있음)


- @Component 뿐만 아니라 다음의 내용도 추가로 대상에 포함된다 (@Component 를 포함하고 있음)
  - `@Component` : 컴포넌트 스캔에서 사용
  - `@Controller` : 스프링 MVC 컨트롤러에서 사용
  - `@Service` : 스프링 비즈니스 로직에서 사용
  - `@Repository` : 스프링 데이터 접근 계층에서 사용 
  - `@Configuration` : 스프링 설정 정보에서 사용

<br>

### 필터

- includeFilters : 컴포넌트 스캔 대상을 추가로 지정한다. 
- excludeFilters : 컴포넌트 스캔에서 제외할 대상을 지정한다.

```java
@Configuration
@ComponentScan(
    includeFilters = @Filter(type = FilterType.ANNOTATION, classes =
        MyIncludeComponent.class),
    excludeFilters = @Filter(type = FilterType.ANNOTATION, classes =
        MyExcludeComponent.class)
)
static class ComponentFilterAppConfig {
}
```

<br>

- (참고) 커스텀 애노테이션
    ```java
    package hello.core.scan.filter;

    import java.lang.annotation.*;
    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface MyIncludeComponent {
    }
    ```

<br>

### 중복 등록과 충돌

컴포넌트 스캔에서 같은 빈 이름을 등록하면 어떻게 될까?

1. 자동 빈 등록 vs 자동 빈 등록
- 컴포넌트 스캔에 의해 자동으로 스프링 빈이 등록되는데, 그 이름이 같은 경우 스프링은 오류를 발생시킨다.
   - `ConflictingBeanDefinitionException` 예외 발생

2. 수동 빈 등록 vs 자동 빈 등록
- 이 경우 수동 빈 등록이 우선권을 가진다 (수동 빈이 자동 빈을 오버라이딩 해버린다)
- 다만, 최근 스프링 부트에서는 수동 빈 등록과 자동 빈 등록이 충돌나면 오류가 발생하도록 기본 값을 바꾸었다
