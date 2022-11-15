package spring.servlet.web.springmvc.v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import spring.servlet.domain.member.MemberRepository;

@Controller
public class SpringMemberListControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members")
    public ModelAndView process(){

        ModelAndView mv = new ModelAndView("members");
        mv.addObject("members", memberRepository.findAll());

        return mv;
    }
}
