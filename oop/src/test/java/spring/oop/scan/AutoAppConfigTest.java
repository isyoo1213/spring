package spring.oop.scan;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.oop.AutoAppConfig;
import spring.oop.member.MemberService;
import spring.oop.order.OrderService;

import static org.assertj.core.api.Assertions.*;

class AutoAppConfigTest {

    @Test
    void basicScan(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

        MemberService memberService = ac.getBean(MemberService.class);
        System.out.println("memberService = " + memberService.getClass());

        OrderService orderService = ac.getBean(OrderService.class);
        System.out.println("orderService = " + orderService.getClass());

        assertThat(memberService).isInstanceOf(MemberService.class);
    }
}
