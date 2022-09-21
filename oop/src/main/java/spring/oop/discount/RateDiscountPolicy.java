package spring.oop.discount;

import spring.oop.member.Grade;
import spring.oop.member.Member;

public class RateDiscountPolicy implements DiscountPolicy {

    private int discountRate = 10;

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP){
            return price * discountRate / 100;
        } else {
            return 0;
        }
    }

}
