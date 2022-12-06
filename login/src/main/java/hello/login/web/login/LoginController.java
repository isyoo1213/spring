package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        //id, password의 오류 등 특정 필드의 문제가 아님 - MemberRepository에서 DB까지 접근해서 데이터를 확인해봐야 판단 가능
        // *** 생각해볼 것 - Login에서는 Service로 구성한 이유 - MemberRepository를 사용해 DB까지 접근하므로?
        // -> Login 자체의 도메인을 활용할 Repository를 구성하는 것보다 기존의 다른 Domain을 활용하므로 Service 계층으로 확장한 개념?
        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리 TODO

        return "redirect:/";
    }
}
