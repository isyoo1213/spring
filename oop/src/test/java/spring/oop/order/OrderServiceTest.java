package spring.oop.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import spring.oop.member.Grade;
import spring.oop.member.Member;
import spring.oop.member.MemberService;
import spring.oop.member.MemberServiceImpl;

public class OrderServiceTest {

    MemberService memberService = new MemberServiceImpl();
    OrderService orderService = new OrderServiceImpl();

    @Test
    void createOrder(){
        Long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(member.getId(), "itemA", 10000);
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);

    }

}
