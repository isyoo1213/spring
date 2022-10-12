package spring.oop.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.oop.AppConfig;
import spring.oop.member.MemberRepository;
import spring.oop.member.MemberService;
import spring.oop.member.MemberServiceImpl;
import spring.oop.order.OrderService;
import spring.oop.order.OrderServiceImpl;

import static org.assertj.core.api.Assertions.*;

class ConfigurationSingletonTest {

    @Test
    void configurationTest(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService =  ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();
        System.out.println("memberService --> memberRepository = " + memberRepository1);
        System.out.println("orderService --> memberRepository = " + memberRepository2);
        System.out.println("memberRepository = " + memberRepository);

        assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
    }

    @Test
    void configurationDeep(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);
        //AppConfig.class로 조회했음에도 CGLIB가 AppConfig 클래스를 상속받고 있으므로 조회 가능
        // * 부모타입을 조회하면 자식타입도 끌려나옴
        // + AppConfig 자체는 인스턴스를 생성하지 않고, CGLIB의 인스턴스가 이름도 가로챔

        System.out.println("bean = " + bean.getClass());
        //실제 출력 내용 - bean = class spring.oop.AppConfig$$EnhancerBySpringCGLIB$$62536d5f
        //AppConfig를 상속받은 CGLIB라는 바이트코드 조작 라이브러리를 통해 스프링컨테이너가 생성한 클래스의 인스턴스
    }

}
