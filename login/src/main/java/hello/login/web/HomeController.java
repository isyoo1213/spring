package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

    //Login처리까지 되는 새로운 Home Controller
    @GetMapping("/")
    public String homeLogin(
            @CookieValue(name = "memberId", required = false) Long memberId, //스프링이 Type Converting해줌
            Model model
    ){
    //Cookie값 꺼내 쓰는 방법은 다양 - HttpServletRequest에서 꺼내서 사용하는 방법도 가능
    //required = false를 통해 쿠키를 가지지 않은 비로그인 사용자도 접근 가능하도록 허용

        if(memberId == null){
            return "home";
        }

        //쿠키 존재시 로그인한 사용자 정보 확인
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null){//사용자가 존재하지 않을 수도, 쿠키가 만료됐을 수도 있는 상황
            return "home";
        }

        //View에서 로그인한 사용자 정보를 활용하기 위해서 Model에 저장
        model.addAttribute("member", loginMember);

        return "loginHome";
    }
}