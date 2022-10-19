package spring.oop.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring.oop.discount.DiscountPolicy;
import spring.oop.discount.FixDiscountPolicy;
import spring.oop.discount.RateDiscountPolicy;
import spring.oop.member.Member;
import spring.oop.member.MemberRepository;
import spring.oop.member.MemoryMemberRepository;

@Component
public class OrderServiceImpl implements OrderService {

    //final 선언과 생성자 주입을 통해 무조건적으로 초기화 되도록 설정 --> 필수 & 불변적인 생성자 주입의 성격
    @Autowired private MemberRepository memberRepository;

    /**
     기존의 의존 - DiscountPolicy 인터페이스(추상화)와 new FixDiscountPolicy() 구현체(구체화)에 모두 의존
     */
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    /**
     새로운 의존 - DiscountPolicy 인터페이스에만 의존 but 구현체 생성 해결 필요
      */
    @Autowired private DiscountPolicy discountPolicy;

/* 수정자 주입
    //수정자 주입 - 수정자 메서드를 통한 의존관계 주입 - 필드에 final 생략해야 함
    // ** 필드 - 클래스 내부 + 생성자나 메서드 외부 --> ** 초기화하지 않아도 자동으로 초기값을 가짐
    // ** 생성자 주입은 JAVA 코드가 실행되면서 자동적으로 bean등록 + 의존관계 주입이 동시에 일어남
    // ** 수정자 주입은 명확하게 스프링 컨테이너의 'bean 등록'과 '의존관계 주입'이 분리되어 일어남
    // *** '선택적( by @Autowired(required=false) ), 변경가능(배우 바꾸고 싶을 때 인스턴스 호출)'한 의존관계에 적합
    @Autowired //수정자에 @Autowired를 통해 의존관계 인식 가능
    public void setMemberRepository(MemberRepository memberRepository) {
        System.out.println("수정자 주입 memberRepository = " + memberRepository);
        this.memberRepository = memberRepository;
    }

    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        System.out.println("수정자 주입 discountPolicy = " + discountPolicy);
        this.discountPolicy = discountPolicy;
    }
*/

/* 생성자 주입
    //생성자 1개일 경우에는 @Autowired 생략 가능
    @Autowired //ApplicationContext를 통해 인자로 받는 의존관계들의 인스턴스를 찾아 주입해줌
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        System.out.println("생성자 memberRepository = " + memberRepository);
        System.out.println("생성자 discountPolicy = " + discountPolicy);
        //생성자 주입 시 인자로 넘어오는 인스턴스의 유무 파악
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
*/
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
