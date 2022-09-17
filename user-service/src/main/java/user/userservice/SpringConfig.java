package user.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import user.userservice.repository.MemberRepository;
import user.userservice.repository.MemoryMemberRepository;
import user.userservice.service.MemberService;

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;

    @Autowired //생성자 1개인 경우 생략 가능 SpringConfig 또한 bean등록되므로 DI 가능
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository);
        //생성자를 통한 DI가 필요할 경우, Bean을 등록하는 함수 자체를 반환
        //SpringDataJpa를 사용할 경우, Repository주입 또한 SpringConfig에 주입된 것과 같은 memberRepository를 바로 넘겨서 사용
    }

//    @Bean
//    public MemberRepository memberRepository(){
////        return new MemoryMemberRepository();
//        //자료형은 인터페이스로 하되 구현체를 반환
//    } // SpringDataJpa에서 따로 repository 구현 및 빈 등록 불필요

}
