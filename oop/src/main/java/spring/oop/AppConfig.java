package spring.oop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.oop.discount.DiscountPolicy;
import spring.oop.discount.FixDiscountPolicy;
import spring.oop.discount.RateDiscountPolicy;
import spring.oop.member.MemberRepository;
import spring.oop.member.MemberService;
import spring.oop.member.MemberServiceImpl;
import spring.oop.member.MemoryMemberRepository;
import spring.oop.order.OrderService;
import spring.oop.order.OrderServiceImpl;

//Configuration 어노테이션을 제거할 경우, 순수 JAVA를 통해 싱글톤이 아닌 방식으로 모든 인스턴스 생성해버림
@Configuration
public class AppConfig {
//역할과 구현 정보고 담기도록 리팩토링

    //@Bean을 통한 경우 - repository를 사용하겠다는 의존관계가 명확하게 코드화
    //@Component 방식 - 단지 Bean등록만을 표현하므로 의존관계 설정을 위한 작업 필요 --> @Autowired 사용
    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    //설계에 대한 그림을 담은 AppConfig에서 구현체만 변경 가능
    @Bean
    public DiscountPolicy discountPolicy() {
        System.out.println("call AppConfig.discountPolicy");
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

    @Bean
    public MemberService memberService(){
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService(){
//필드주입 Test를 위해 임시 주석처리 후 null 리턴
        return new OrderServiceImpl(memberRepository(), discountPolicy());
//        return null;
    }

}
