package spring.servlet.web.springmvc.v3;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.servlet.domain.member.Member;
import spring.servlet.domain.member.MemberRepository;


//이제 Controller가 ModelView 객체 자체를 반환하는 것이 아닌, String을 통한 viewPath 논리적 이름만 반환 + 내부에서 model 처리 설계
@RequestMapping("/springmvc/v3/members")
@Controller
public class SpringMemberControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    // *** SpringMVC는 Controller 구현체가 '인터페이스로 고정'되어있지 않음 -> 반환이 자유로움
    // *** (Annotation 기반 Controller라는 인터페이스만 고정 + 구현체 내부의 실질적인 로직 구현 메서드의 반환이 자유로움)
    // -> ModelAndView or ViewName을 반환하면 이를 인식하고 프로세스 진행
//    @RequestMapping(value = "/new-form", method = RequestMethod.GET)
    @GetMapping("/new-form")
    public String newForm(){
        return "new-form";
    }

    // *** 인터페이스로 고정되지 않으므로 메서드의 Parameter 또한 자유로움
    // -> HttpServletRequest 객체로 전달되는 request 정보 뿐만 아니라, @RequestParam을 통해 쿼리스트링 획득 가능
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @PostMapping("/save") //어노테이션 들어가보면 @RequestMapping이 조합된 어노테이션임을 알 수 있음
    public String save(@RequestParam("username") String username, // *** return 또한 String으로 바로 ViewName 반환
                             @RequestParam("age") int age, // *** parameter로 전달되는 쿼리스트링 key의 Type Casting까지 지원
                             Model model){  // *** V4에서 직접 생성했던 Map구조의 Model 객체를 지원
// 필요없어짐
//        String username = request.getParameter("username");
//        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);

// 기존 ModelAndView - View는 String 반환으로 + Model은 SpringMVC가 지원하는 Model객체를 Parameter로 받아서 내부에서 처리
//        ModelAndView mv = new ModelAndView("save-result");
//        mv.addObject("member", memberRepository.save(member));
        model.addAttribute("member", memberRepository.save(member));

        return "save-result";
    }

//    @RequestMapping(value = "", method = RequestMethod.GET)
    @GetMapping("")
    public String members(Model model){ //인터페이스로 강요당하지 않으므로, 필요한 Parameter만 url이 매핑된 메서드별로 사용할 수 있음

//        mv.addObject("members", memberRepository.findAll());
        model.addAttribute("members", memberRepository.findAll());

        return "members";
    }
}
