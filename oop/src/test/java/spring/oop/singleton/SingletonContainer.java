package spring.oop.singleton;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.oop.AppConfig;
import spring.oop.member.MemberService;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonContainer {

    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer(){
//        AppConfig appConfig = new AppConfig();

        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        //1. 조회 - 호출할 때마다 인스턴스를 생성하는지 확인
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);

        //2. 조회 - 비교인스턴스
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        //참조값이 같은 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);
        System.out.println("인스턴스 참조값 동일성 확인 결과 = " + String.valueOf(memberService1==memberService2));

        //memberService1 == memberService2
        assertThat(memberService1).isSameAs(memberService2);
    }

}
