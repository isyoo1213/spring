package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    //@PostMapping("/login")
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

    //SessionManager를 통해 Session을 생성 + 회원 데이터 보관
    //@PostMapping("/login")
    public String loginV2(
            @Validated
            @ModelAttribute("loginForm") LoginForm form,
            BindingResult bindingResult,
            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리 TODO - Cookie를 만들어서 저장

/*
        //Cookie 생성
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));

        //생성한 Cookie를 Response에 실어 보내는 작업 - HttpServletResponse 사용
        response.addCookie(idCookie);
*/
        /**
         * SessionManager를 통해 Session을 생성 + 회원 데이터 보관
         */
        //1. createSession() -> SessionId 생성 + 값을 Session에 저장 + Cookie 생성 + Response에 담기
        sessionManager.createSession(loginMember, response);

        return "redirect:/";
    }

    //Spring이 제공하는 HttpSession 사용하기
    @PostMapping("/login")
    public String loginV3(
            @Validated
            @ModelAttribute("loginForm") LoginForm form,
            BindingResult bindingResult,
            HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리 TODO - Cookie를 만들어서 저장

        //Session이 있으면 해당 Session을 반환하고, 없으면 신규 Session을 생성해서 반환
        HttpSession session = request.getSession();
        //getSession()의 Default argument는 true -> 세션을 '생성'하는 것 - 생략가능
        //cf) false - 세션이 있으면 기존 세션을 반환하고, 없을 경우 생성하지 않고 'Null'을 반환
        // 의문점 - getSession()의 반환값은 sessionStore? SessionManager의 인스턴스화..?

        //Session에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        //session.setAttribute(SessionConst.LOGIN_MEMBER, "loginMember");
        // *** 하나의 session에 여러 값을 보관할 수 있다 - 같은 key로 다른 value를 저장 가능..?
        // -> 덮어쓰기 되거나, 후입선출되는 자료구조일 가능성 높음 -> 서버 오류 생김

        return "redirect:/";
    }

    //@PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expireCookie(response, "memberId");

        return "redirect:/";
    }

    //SessionManager를 통한 Cookie Expire
    //@PostMapping("/logout")
    public String logoutV2(
            //HttpServletResponse response // 기존에는 응답을 통해 다음 요청에 사용될 Cookie를 아예 만료시킴
            HttpServletRequest request // 이제는 기존 요청의 Cookie를 만료시키는 것이 아닌 SessionStore 내의 데이터를 삭제
    ){
        //expireCookie(response, "memberId");

        /**
         * SessionManager를 통해 expire() 호출
         */
        sessionManager.expire(request);

        return "redirect:/";
    }

    //Spring이 제공하는 HttpSession 사용하기
    @PostMapping("/logout")
    public String logoutV3(
            HttpServletRequest request
    ){
        HttpSession session = request.getSession(false);
        // *** logout은 session 내 데이터를 삭제하는 것이 목표 - session이 존재하지 않읋 경우, true 옵션으로 session을 생성할 필요가 없음

        if(session != null){
            session.invalidate();
        }

        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null); // Ctrl + Alt + P로 파라미터 변수화
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
