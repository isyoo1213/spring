package spring.oop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

//Filter로 AppConfig 및 Test를 위해 정의한 설정 정보들이 스프링 컨테이너에 자동 등록되는 것을 방지
//Configuration 어노테이션 또한 @Component 어노테이션을 포함하고 있음
@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
}
