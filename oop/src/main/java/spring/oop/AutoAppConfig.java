package spring.oop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import spring.oop.member.MemberRepository;
import spring.oop.member.MemoryMemberRepository;

//Filter로 AppConfig 및 Test를 위해 정의한 설정 정보들이 스프링 컨테이너에 자동 등록되는 것을 방지
//Configuration 어노테이션 또한 @Component 어노테이션을 포함하고 있음
@Configuration
@ComponentScan(
        //탐색할 패키지 위치를 정하지 않을 경우 - @ConponentScan 어노테이션이 붙은 클래스가 위치한 패키지와 하위를 모두 탐색
        //즉, spring.oop 하위의 모든 패키지를 탐색 --> ** 설정정보 클래스를 최상위 패키지에 위치하고 따로 설정하지 않는 방식이 관례
//        basePackages = "spring.oop.member",
        //원하는 패키지에서 컴포넌트스캔 가능 -> ** 'AutoAppConfig 자신을 포함한' 위치 내의 component만 스캔
        //즉, @Configuration이 붙어 설정 정보를 구성해야하는 '자기 자신'을 제외한 Configuration을 제외
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {

    @Bean(name = "memoryMemberRepository")
    MemberRepository memberRepository(){
        return new MemoryMemberRepository();
        //이미 @Component 어노테이션이 붙은 MemoryMemberRepository 클래스가 존재
        //->> bean 이름은 default로 소문자로 시작하는 memoryMemberRepository로 bean 등록됨
        //Test의 scan 패키지의 AutoAppConfigTest(memberService bean 찾는 테스트)를 실행해보면 아래와 같은 로그
        //Overriding bean definition for bean 'memoryMemberRepository' with a different definition:
        // replacing (**아래의 bean을 with 다음의 bean으로 replacing함)
        // [Generic bean: class [spring.oop.member.MemoryMemberRepository]; ... factoryMethodName=null;  defined in file [C: ... spring\oop\member\MemoryMemberRepository.class]]
        // with (** 아래의 bean으로 replacing) --> *** 수동 bean이 우선권을 가지며 + 자동 bean을 overriding
        // ... factoryBeanName=autoAppConfig; factoryMethodName=memberRepository; defined in spring.oop.AutoAppConfig]

        // + Spring의 default - Overriding 'True'
        //vs SpringBoot의 default - Overriding 'False' : 자동 bean과 수동 bean 충돌 시 '오류 발생'
        // + SpringBoot에서 Overriding False를 True로 변경하려면 --> application.properties에서 설정
        // ex) spring.main.allow-bean-definition-overriding=true
    }

}
