package spring.oop;

import spring.oop.discount.DiscountPolicy;
import spring.oop.discount.FixDiscountPolicy;
import spring.oop.discount.RateDiscountPolicy;
import spring.oop.member.MemberRepository;
import spring.oop.member.MemberService;
import spring.oop.member.MemberServiceImpl;
import spring.oop.member.MemoryMemberRepository;
import spring.oop.order.OrderService;
import spring.oop.order.OrderServiceImpl;

public class AppConfig {
//역할과 구현 정보고 담기도록 리팩토링

    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    //설계에 대한 그림을 담은 AppConfig에서 구현체만 변경 가능
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }

    public OrderService orderService(){
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

}
