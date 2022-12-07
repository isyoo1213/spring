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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(
            @Validated
            @ModelAttribute("loginForm") LoginForm form,
            BindingResult bindingResult,
            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        //id, password의 오류 등 특정 필드의 문제가 아님 - MemberRepository에서 DB까지 접근해서 데이터를 확인해봐야 판단 가능
        // *** 생각해볼 것 - Login에서는 Service로 구성한 이유 - MemberRepository를 사용해 DB까지 접근하므로?
        // -> Login 자체의 도메인을 활용할 Repository를 구성하는 것보다 기존의 다른 Domain을 활용하므로 Service 계층으로 확장한 개념?
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리 TODO - Cookie를 만들어서 저장

        //Cookie 생성
        // + *** Cookie에 시간 정보를 주지 않으면 세션 쿠키(Session과 상관 X)가 됨 -> 브라우저 종료시 종료
        // cf) 영속쿠키
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));

        //생성한 Cookie를 Response에 실어 보내는 작업 - HttpServletResponse 사용
        response.addCookie(idCookie);

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        //Age가 0인 Cookie를 같은 이름으로 바꿔 만료시키는 방식
        expireCookie(response, "memberId");

        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null); // Ctrl + Alt + P로 파라미터 변수화
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
