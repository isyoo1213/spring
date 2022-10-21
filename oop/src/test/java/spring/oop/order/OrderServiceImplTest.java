package spring.oop.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import spring.oop.discount.FixDiscountPolicy;
import spring.oop.member.Grade;
import spring.oop.member.Member;
import spring.oop.member.MemoryMemberRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    @Test
    void createOrder(){
//        OrderServiceImpl orderService = new OrderServiceImpl(); //수정자 주입을 위한 테스트
        //순수한 JAVA를 통한 단위 테스트에서는 오류가 발생할 수밖에 없음.
        //수정자 주입 또한 set수정자를 통한 this.의존관계의 주입이 Spring을 통해 일어나므로
        //bean등록 되어야할 인스턴스를 임의로 new할 경우에 의존관계 주입이 일어나지 않은 인스턴스만 생성됨
        // vs 생성자 주입 --> 생성자 주입은 JAVA자체에 의해 생성자가 호출되어 의존관계 주입까지 이루어져버림
        // ** 즉, 'bean등록'과 '의존관계주입'의 단계가 분리된 수정자 주입은 순수한 단위테스트에 단점을 가지게 됨

        MemoryMemberRepository memberRepository = new MemoryMemberRepository();
        memberRepository.save(new Member(1L, "memberA", Grade.VIP));

        OrderServiceImpl orderService = new OrderServiceImpl(memberRepository, new FixDiscountPolicy());
        //생성자 주입의 장점
        //1. 순수한 단위테스트 가능
        //2. 필드(의존관계 변수)에 final 설정을 통해 의존관계 주입의 누락을 차단
        //** final은 즉시 초기화하거나 생성자를 통한 초기화가 강제됨
        Order order = orderService.createOrder(1L, "itemA", 10000);
        assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }

}