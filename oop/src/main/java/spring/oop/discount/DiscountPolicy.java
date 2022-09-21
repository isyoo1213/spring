package spring.oop.discount;

import spring.oop.member.Member;

public interface DiscountPolicy {

    /**
     *
     * @param member
     * @param price
     * @return 할인 금액 ( 실질적으로 할인 된 금액 )
     */
    int discount(Member member, int price);

}
