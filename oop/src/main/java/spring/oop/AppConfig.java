package spring.oop;

import spring.oop.discount.FixDiscountPolicy;
import spring.oop.member.MemberService;
import spring.oop.member.MemberServiceImpl;
import spring.oop.member.MemoryMemberRepository;
import spring.oop.order.OrderService;
import spring.oop.order.OrderServiceImpl;

public class AppConfig {

    public MemberService memberService(){
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService(){
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
    }

}
