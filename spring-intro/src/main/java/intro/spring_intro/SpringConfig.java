package intro.spring_intro;

import intro.spring_intro.aop.TimeTraceAop;
import intro.spring_intro.repository.JdbcMemberRepository;
import intro.spring_intro.repository.JdbcTemplateMemberRepository;
import intro.spring_intro.repository.JpaMemberRepository;
import intro.spring_intro.repository.MemberRepository;
import intro.spring_intro.repository.MemoryMemberRepository;
import intro.spring_intro.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/* 자바 코드로 직접 스프링 빈 등록하기 */
/*
- 실무에서는 주로 정형화된 컨트롤러, 서비스, 리포지토리 같은 코드는 컴포넌트 스캔을 사용한다.
- 정형화 되지 않거나, 상황에 따라 구현 클래스를 변경해야 하면 설정을 통해 스프링 빈으로 등록
 */
@Configuration
public class SpringConfig {

    /* 스프링 데이터 JPA가 `SpringDataJpaMemberRepository` 를 스프링 빈으로 자동 등록해준다 */
    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

    // https://www.inflearn.com/community/questions/48156/aop-timetraceaop-%EB%A5%BC-component-%EB%A1%9C-%EC%84%A0%EC%96%B8-vs-springconfig%EC%97%90-bean%EC%9C%BC%EB%A1%9C-%EB%93%B1%EB%A1%9D
    @Bean
    public TimeTraceAop timeTraceAop() {
        return new TimeTraceAop();
    }

    /*
    private final DataSource dataSource;

    // 원래는 @PersistenceContext 를 붙여서 받아오지만, 생성자로 DI 해줘도 가능하다
    private EntityManager em;

    public SpringConfig(DataSource dataSource, EntityManager em) {
        this.dataSource = dataSource;
        this.em = em;
    }

    @Bean
    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource);
//        return new JdbcTemplateMemberRepository(dataSource);
//        return new JpaMemberRepository(em);

    }
    */
}
