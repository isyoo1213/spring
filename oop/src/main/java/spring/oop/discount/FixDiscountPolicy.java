package spring.oop.discount;

import spring.oop.member.Grade;
import spring.oop.member.Member;

public class FixDiscountPolicy implements DiscountPolicy {

    private int discountAmount = 1000;

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP){
            //enum type은 등호연산 가능
            return discountAmount;
        } else {
            return 0;
        }
    }

}
