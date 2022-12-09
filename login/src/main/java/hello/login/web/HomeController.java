package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/")
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
}