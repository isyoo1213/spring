package spring.oop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

//Filter로 AppConfig 및 Test를 위해 정의한 설정 정보들이 스프링 컨테이너에 자동 등록되는 것을 방지
//Configuration 어노테이션 또한 @Component 어노테이션을 포함하고 있음
@Configuration
@ComponentScan(
        //탐색할 패키지 위치를 정하지 않을 경우 - @ConponentScan 어노테이션이 붙은 클래스가 위치한 패키지와 하위를 모두 탐색
        //즉, spring.oop 하위의 모든 패키지를 탐색 --> ** 설정정보 클래스를 최상위 패키지에 위치하고 따로 설정하지 않는 방식이 관례
        basePackages = "spring.oop.member", //원하는 패키지에서 컴포넌트스캔 가능 -> AutoAppConfig 자신을 포함한 위치 내의 component만 스캔
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {
}
