package spring.oop.autowired;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.oop.AutoAppConfig;
import spring.oop.discount.DiscountPolicy;
import spring.oop.member.Grade;
import spring.oop.member.Member;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class AllBeanTest {

    @Test
    void findAllBean(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);
        // ** Context 생성시(컨테이너 생성 시), 등록하는 설정정보 클래스는 bean으로 자동 등록된다

        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "userA", Grade.VIP);
        int fixDiscountPrice = discountService.callDiscount(member, 10000, "fixDiscountPolicy");

        assertThat(discountService).isInstanceOf(DiscountService.class);
        System.out.println("discountService = " + discountService.getClass());

        assertThat(fixDiscountPrice).isEqualTo(1000);

        int rateDiscountPrice = discountService.callDiscount(member, 20000, "rateDiscountPolicy");
        assertThat(rateDiscountPrice).isEqualTo(2000);
    }

    static class DiscountService{
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policies;

//        @Autowired //@Autowired 어노테이션을 사용하지 않더라도, DiscountService.class가 설정정보로 지정되어 bean등록 되므로
        //1개의 생성자인 경우 @Autowired 생략 가능
        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            System.out.println("policyMap = " + policyMap);
            System.out.println("policies = " + policies);
        }

        //** 포인트는, 같은 추상화 타입의 bean들을 '2개 이상' 의존관계로 주입하면서 '다형성'을 구축
        // '동적으로 의존관계 주입할 bean을 선택해야 할 경우 유용하게 사용 가능'
        public int callDiscount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = policyMap.get(discountCode);
            System.out.println("discountPolicy = " + discountPolicy.getClass());
            return discountPolicy.discount(member, price);
        }
    }
}
