package spring.oop.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spring.oop.AppConfig;
import spring.oop.member.Grade;
import spring.oop.member.Member;
import spring.oop.member.MemberService;
import spring.oop.member.MemberServiceImpl;

public class OrderServiceTest {

    MemberService memberService;
    OrderService orderService;

    @BeforeEach
    public void beforeEach(){
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
        orderService = appConfig.orderService();
    }

    @Test
    void createOrder(){
        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(member.getId(), "itemA", 10000);
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);

    }

    @Test
    void fieldInjectionTest(){
        OrderServiceImpl orderService = new OrderServiceImpl();
        //필드 주입의 경우, orderService 인스턴스에 주입된 memoryRepository 같은 의존관계를 직접적으로 설정 불가
        //--> this.memberRepository 가 null이므로 setter를 열어서 주입하면 결국 수정자 주입이 되어버림
        //순수한 orderServiceImpl 인스턴스를 생성해서 테스트 돌리는 것이 불가능
        //1. CoreApplicationTest - SpringBoot를 통한 spring container의 테스트에서는 bean등록 후 바로 사용 가능
        //( 이 경우 순수한 JAVA코드를 통한 Test가 아닌 Boot에서 제공하는 Test )
        //2. @Configuration이 붙은 설정정보 클래스에서의 사용
        //( 이 경우 설정정보 클래스는 spring에서만 사용 --> 이 경우도 필드 주입보다는 parameter로 넘기는 방식으로 교체 가능 )
        //즉, 필드 주입이 된 인스턴스의 자체적이고 순수한 Test에서 제약이 발생한다
//        orderService.createOrder(1L, "itemA", 10000);
    }

}
