package user.userservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import user.userservice.repository.MemberRepository;
import user.userservice.repository.MemoryMemberRepository;
import user.userservice.service.MemberService;

@Configuration
public class SpringConfig {

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
        //생성자를 통한 DI가 필요할 경우, Bean을 등록하는 함수 자체를 반환
    }

    @Bean
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
        //자료형은 인터페이스로 하되 구현체를 반환
    }

}
