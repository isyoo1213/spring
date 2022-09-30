package spring.oop.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.oop.AppConfig;
import spring.oop.member.MemberService;

import static org.assertj.core.api.Assertions.*;

class SingletonTest {

    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer(){
        AppConfig appConfig = new AppConfig();

        //1. 조회 - 호출할 때마다 인스턴스를 생성하는지 확인
        MemberService memberService1 = appConfig.memberService();

        //2. 조회 - 비교인스턴스
        MemberService memberService2 = appConfig.memberService();

        //참조값이 다른 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);
        System.out.println("인스턴스 참조값 동일성 확인 결과 = " + String.valueOf(memberService1==memberService2));

        //memberService1 != memberService2
        assertThat(memberService1).isNotSameAs(memberService2);

    }

    @Test
    @DisplayName("싱글톤 패턴을 사용한 객체 사용")
    void singletonServiceTest(){
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();

        System.out.println("singletonService1 = " + singletonService1);
        System.out.println("singletonService2 = " + singletonService2);
        System.out.println("인스턴스 참조값 동일성 확인 결과 = " + String.valueOf(singletonService1==singletonService2));

        assertThat(singletonService1).isSameAs(singletonService2);
        //sameAs - == (실제 instance 비교)
        //equalTo - equals 메서드 (오버라이딩 가능)

    }

}
