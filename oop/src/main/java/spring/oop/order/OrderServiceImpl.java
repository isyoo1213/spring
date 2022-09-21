package spring.oop.order;

import spring.oop.discount.DiscountPolicy;
import spring.oop.discount.FixDiscountPolicy;
import spring.oop.member.Member;
import spring.oop.member.MemberRepository;
import spring.oop.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {

        Member findMember = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(findMember, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

}
