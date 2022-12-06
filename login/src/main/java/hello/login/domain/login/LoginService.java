package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return null이면 로그인 실패
     */
    public Member login(String loginId, String password){
/* //Optional 사용하기
        Optional<Member> findMemberOptional =  memberRepository.findByLoginId(loginId);
        Member member = findMemberOptional.get(); //get() - Null일 경우 예외 반환 (@NotNull)
        if(member.getPassword().equals(password)){
            return member;
        } else {
            return null;
        }
*/
        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
        return findMemberOptional.filter(member -> member.getPassword().equals(password))
                .orElse(null); //orElse()와 orElseGet() 추가적으로 공부하기
    }

}
