package spring.servlet.web.springmvc.v2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import spring.servlet.domain.member.Member;
import spring.servlet.domain.member.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
//이미 @Controller 인터페이스가 붙은 것과 유사한 구현체 내에서 + urlPattern에 따른 메서드를 바로 정의해버림
//즉, 기존의 Front-controller는 DispatcherServlet이 담당하고, @Controller 인터페이스의 구현체인 지금의 Controller에서
//바로 url Mapping 정보를 '매서드' 단위로 설정해서 실행되도록 함
// *** 즉, 기존 Front-controller에서는 HandlerMapping이 '클래스의 인스턴스' 단위로 저장했다면,
// spring mvc에서는 Front-Controller, HandlerMaping, HandlerAdapter 가 은닉되어 이 과정이 자동화 + 은닉되고,
// 해당 과정 + 메서드 단위의 urlMapping까지 지원하는 것으로 유추 -> 컨트롤러 통합의 효과
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/new-form")
    public ModelAndView newForm(){
        return new ModelAndView("new-form");
    }

    @RequestMapping("/save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response){
        //현재 process의 Parameter는 Form, List와 일치되지 않음
        // V3~V4는 ControllerVx의 인터페이스에서 process()의 parameter를 강제하지만, RequestMapping에서는 매피된 컨트롤러마다 자유롭게 가능

        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);

        ModelAndView mv = new ModelAndView("save-result"); //view는 생성자에서

//        mv.getModel().put("member",  memberRepository.save(member));
        mv.addObject("member", memberRepository.save(member)); //model은 메서드로
        //spring web에서 지원하는 ModelAndView에서는 getModel().put()의 기능을 하는 addObject() 메서드 존재

        return mv;
    }

    @RequestMapping("")
    public ModelAndView members(){

        ModelAndView mv = new ModelAndView("members");
        mv.addObject("members", memberRepository.findAll());

        return mv;
    }


}
