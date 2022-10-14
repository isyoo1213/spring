package spring.oop.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component//@Component("memberServiceSpec") 이렇게 '소문자'로 시작하는 bean이름을 직접 등록 가능
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    //생성자에 @Autowired를 통해 단순히 @Bean등록만 하는 @Component에서 누락된 의존관계를 형성 by ApplicationContext
    // ** 스프링 컨테이너는 생성자의 인자로 전달받는 MemberRepository '타입'의 bean을 탐색 ( 같은 type일 경우 충돌 - 뒷부분에서 배울예정)
    // ** 참고 - @ComponentScan으로 등록한 bean --> 이름은 앞글자를 소문자로 등록 ex> memberRepository, memoryMemberRepository
    @Autowired //ac.getBean(MemberRepository.class) --> 이 처럼 작동함
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    } //생성자를 통한 주입을 통해 MemberRepository라는 추상화에만 의존가능

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    //테스트 용도 - AppConfig에서 MemberService와 OrderService 생성 시 MemberRepository가 싱글톤으로 유지되는지 확인하기 위함
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
