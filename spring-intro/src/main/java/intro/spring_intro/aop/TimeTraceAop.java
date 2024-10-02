package intro.spring_intro.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/*
- 스프링 빈으로 등록해주어야 함!
- @Component 로 빈을 등록해줘도 되지만,
- 이런 경우들은 spring config에서 스프링 빈으로 등록해서 쓰는 걸 더 선호함
    (service, repository 같은 평범한, 정형화되어 만들 수 있는 경우는 컴포넌트 스캔으로 많이 하지만
    aop 같은 것들은 정형화되지 않은, 특별한 경우이기 때문에 config에서 등록하여 인지할 수 있게 하는 걸 더 선호)
 */
//@Component
@Aspect  /* AOP */
public class TimeTraceAop {

    /*
    https://www.inflearn.com/community/questions/48156/aop-timetraceaop-%EB%A5%BC-component-%EB%A1%9C-%EC%84%A0%EC%96%B8-vs-springconfig%EC%97%90-bean%EC%9C%BC%EB%A1%9C-%EB%93%B1%EB%A1%9D
    - 기존 TimeTraceAop의 AOP 대상을 지정하는 @Around 코드를 보면, SpringConfig의 timeTraceAop() 메서드도 AOP로 처리하게 된다
    - 이게 바로 자기 자신인 TimeTraceAop를 생성하는 코드이기 때문에 순환참조 문제가 발생한다
    - 반면에 컴포넌트 스캔을 사용할 때는 AOP의 대상이 되는 이런 코드 자체가 없기 때문에 문제가 발생하지 않는다
    - 따라서 AOP 설정 클래스를 빈으로 직접 등록할 때, 다음과 같이 AOP 대상에서 SpringConfig를 빼주면 문제가 해결된다!
     */
    /*
    - @Around()는 어디에 AOP를 적용할지 타겟팅하는 것!
     */
    @Around("execution(* intro.spring_intro..*(..)) && !target(intro.spring_intro.SpringConfig)")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());  // 어떤 메서드를 콜했는지 해당 메서드의 이름!

        try {
            return joinPoint.proceed();  // joinPoint.proceed() -> 다음 메소드로 진행!
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }
}
