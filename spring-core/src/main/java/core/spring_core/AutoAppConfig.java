package core.spring_core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(  /* 컴포넌트 스캔 : @Component 애노테이션이 붙은 클래스를 찾아서 전부 스프링 빈으로 등록해준다 */
    excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
}
