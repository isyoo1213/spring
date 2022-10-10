package spring.oop.order;

import spring.oop.discount.DiscountPolicy;
import spring.oop.discount.FixDiscountPolicy;
import spring.oop.discount.RateDiscountPolicy;
import spring.oop.member.Member;
import spring.oop.member.MemberRepository;
import spring.oop.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;

    /**
     기존의 의존 - DiscountPolicy 인터페이스(추상화)와 new FixDiscountPolicy() 구현체(구체화)에 모두 의존
     */
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    /**
     새로운 의존 - DiscountPolicy 인터페이스에만 의존 but 구현체 생성 해결 필요
      */
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {

        Member findMember = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(findMember, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //테스트 용도 - AppConfig에서 MemberService와 OrderService 생성 시 MemberRepository가 싱글톤으로 유지되는지 확인하기 위함
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
    
}
