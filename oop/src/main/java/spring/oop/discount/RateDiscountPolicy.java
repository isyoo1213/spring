package spring.oop.discount;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import spring.oop.annotation.MainDiscountPolicy;
import spring.oop.member.Grade;
import spring.oop.member.Member;

@Component
//@Primary
//@Qualifier("mainDiscountPolicy") // @Qualifier의 단점 - 지정한 문자열이 컴파일 단계에서 검토되지 않음
@MainDiscountPolicy //커스텀 어노테이션을 활용할 경우, 어노테이션 자체에 대한 컴파일 검토가 들어감
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
