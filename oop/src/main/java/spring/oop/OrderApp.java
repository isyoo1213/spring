package spring.oop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.oop.member.Grade;
import spring.oop.member.Member;
import spring.oop.member.MemberService;
import spring.oop.order.Order;
import spring.oop.order.OrderService;

public class OrderApp {

    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        OrderService orderService = applicationContext.getBean("orderService", OrderService.class);

//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
//        OrderService orderService = appConfig.orderService();

//        기존의 DIP 미준수
//        MemberService memberService = new MemberServiceImpl(null);
//        OrderService orderService = new OrderServiceImpl(null, null);

        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);

        memberService.join(member);

        Order order = orderService.createOrder(member.getId(), "itemA", 20000);
        System.out.println("order = " + order);
        //출력 시 Overriding된 toString()의 보기 편한 포맷으로 출력
        System.out.println("order.calcuatePrice = " + order.calculatePrice());
    }

}
