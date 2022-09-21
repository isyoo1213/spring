package spring.oop.discount;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.oop.member.Grade;
import spring.oop.member.Member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class RateDiscountPolicyTest {

    RateDiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인이 적용되어야 한다.")
    void vip_o(){
        //given
        Member member = new Member(1L, "memberA", Grade.VIP);

        //when
        int discountAmount = rateDiscountPolicy.discount(member, 10000);

        //then
        assertThat(discountAmount).isEqualTo(1000);
    }

    @Test
    @DisplayName("VIP가 아닌 경우 할인 적용이 안돼야 한다.")
    void vip_x(){
        //given
        Member member = new Member(1L, "memberA", Grade.BASIC);

        //when
        int discountAmount = rateDiscountPolicy.discount(member, 10000);

        //then
        assertThat(discountAmount).isEqualTo(0);
    }

}