package spring.servlet.web.springmvc.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import spring.servlet.domain.member.Member;
import spring.servlet.domain.member.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class SpringMemberSaveControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members/save")
    public ModelAndView process(HttpServletRequest request, HttpServletResponse response){
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
}
