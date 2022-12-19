package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentResolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

    //Login처리까지 되는 새로운 Home Controller
    //@GetMapping("/")
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

    //@GetMapping("/")
    public String homeLoginV2(
            //@CookieValue(name = "memberId", required = false) Long memberId, //스프링이 Type Converting해줌
            // -> 이제 Only Cookie를 통해 로그인 정보를 파악하는 것이 아닌, SessionStore에 저장된 Member 정보 데이터를 확인
            HttpServletRequest request,
            Model model
    ){
/*
        if(memberId == null){
            return "home";
        }
*/

        /**
         * SessionManager에 저장된 회원 정보 조회
         */
        Member member = (Member)sessionManager.getSession(request);
        //getSession()의 Return Type이 Object로 범용성있게 설계 -> Type Casting

/*        //쿠키 존재시 로그인한 사용자 정보 확인
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null){//사용자가 존재하지 않을 수도, 쿠키가 만료됐을 수도 있는 상황
            return "home";
        }
        //View에서 로그인한 사용자 정보를 활용하기 위해서 Model에 저장
        model.addAttribute("member", loginMember);
        return "loginHome";
*/
        /**
         * Cookie가 아닌, SessionManager에서 얻은 member 데이터 정보로 로그인한 사용자 정보 확인
         */
        if(member == null){
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model){

        HttpSession session = request.getSession(false);
        //로그인하지 않은 사용자의 요청에 Session을 생성하지 않도록 false 세팅

        //Session에 사용자 데이터가 없으면 login되지 않은 home으로 이동
        if(session == null){
            return "home";
        }

        //Session에 저장된 사용자 정보 획득
        //getAttribute()의 return Type은 Object -> Type Casting
        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        //Session이 유지되면 Login된 home 화면으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //@SessionAttribute 사용
    //@GetMapping("/")
    public String homeLoginV3Spring(
            //HttpServletRequest request,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            //Request에서 Session 획득 + getAttribute()를 통해 저장된 정보 획득
            //*** Session을 생성하는 것은 아닌, Login정보를 Session으로부터 획득하기만 함
            Model model){

        // *** 의문점
        //Spring의 Session 사용 시 JSESSIONID가 Cookie로 전달되는 과정
        //- 기존에는 Cookie를 통해 데이터가 저장된 sessionStrogae의 key에 바로 접근
        //- but 스프링의 세션 사용하면 Cookie로 전달되는 JSESSIONID와, setAttribute()를 통해 데이터를 저장하는 key가 다름
        //- 이 중간 과정에서의 스프링의 처리 원리

        //HttpSession session = request.getSession(false);

        //Session에 사용자 데이터가 없으면 login되지 않은 home으로 이동
        if(loginMember == null){
            return "home";
        }

        //Session에 저장된 사용자 정보 획득
        //Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        //Session이 유지되면 Login된 home 화면으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    //ArgumentResolver 사용 - Argument를 어떻게 처리할지에 관한 Resolver
    // *** MVC의 Reuqest Mapping 부분 복습
    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(
            //@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            @Login Member loginMember, //Session에서 login된 유저를 찾아 반환하는 과정을 어노테이션 하나로 처리
            //@Login 어노테이션에 아무 로직처리를 하지 않으면 ModelAttribute처럼 동작 -> 리졸버가 동작하도록 로직 구성

            Model model){

        if(loginMember == null){
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}