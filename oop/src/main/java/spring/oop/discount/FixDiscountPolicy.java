package spring.oop.discount;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import spring.oop.member.Grade;
import spring.oop.member.Member;

@Component// 이미 RateDiscountPolicy가 @Component를 통해 bean등록된 상황
// + AutoAppConfig에서 DiscountPolicy를 생성자 주입을 통해 받고있으므로, 어느 bean을 주입받아야할 지 오류발생
//@Qualifier("fixDiscountPolicy")
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
