package spring.oop.scan;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.oop.AutoAppConfig;
import spring.oop.member.MemberRepository;
import spring.oop.member.MemberService;
import spring.oop.member.MemoryMemberRepository;
import spring.oop.order.OrderService;
import spring.oop.order.OrderServiceImpl;

import static org.assertj.core.api.Assertions.*;

class AutoAppConfigTest {

    @Test
    void basicScan(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

        MemberService memberService = ac.getBean(MemberService.class);
        System.out.println("memberService = " + memberService.getClass());
        System.out.println("memberService = " + memberService);

        OrderService orderService = ac.getBean(OrderService.class);
        System.out.println("orderService = " + orderService.getClass());
        System.out.println("orderService = " + orderService);

        MemoryMemberRepository memoryMemberRepository = ac.getBean(MemoryMemberRepository.class);
        System.out.println("memoryMemberRepository.getClass() = " + memoryMemberRepository.getClass());
        System.out.println("memoryMemberRepository = " + memoryMemberRepository);

        //필드 주입 테스트
        //orderServiceImpl 인스턴스의 자료형을 인터페이스가 아닌 구체클래스 사용
        //--> memberRepository 인스턴스가 의존관계 주입됐는지 확인하기 위해 구체클래스에 정의된 메서드 사용하기 위함
        OrderServiceImpl orderServiceImpl = ac.getBean(OrderServiceImpl.class);
        MemberRepository memberRepository = orderServiceImpl.getMemberRepository();
        System.out.println("필드주입 memberRepository = " + memberRepository);

        assertThat(memberService).isInstanceOf(MemberService.class);
    }
}
