package user.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import user.userservice.domain.Member;
import user.userservice.service.MemberService;

import java.util.List;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;

        System.out.println("memberService = " + memberService.getClass());
        //Aop적용 후 Controller에서 호출되는 memberService 인스턴스 정보 확인하기 위한 statement
        //Spring Container 컨테이너 띄울 때 확인 가능
        //memberService = class user.userservice.service.MemberService$$EnhancerBySpringCGLIB$$419f2078
        //SpringCGLIB - 해당 bean을 복제해서 Spring이 사용할 수 있도록 코드를 조작하는 Library
        //Aop 적용이 필요할 때 Spring은 이 proxy로 복제된 인스턴스를 '주입'해 처리한 후 proceed() 진행 - * DI가 적극 활요되는 부분 *

    } //MemberController가 생성될 때 컨테이너에 저장하고 있는 memberService 빈 객체를 주입해 연결해줌
      //DI


/* 필드 주입 - 확장성이 결여(다른 객체로 전환 불가능)
    @Autowired private MemberService memberService;
 */

/* setter 주입 - setter는 public으로 노출되기 때문에 문제가 생길 수 있음
    private MemberService memberService;

    @Autowired
    public void setMemberService(MemberService memberService){
        this.memberService = memberService;
    }
 */

    @GetMapping("/members/new")
    public String createForm(){
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm memberForm){
        Member member = new Member();
        member.setName(memberForm.getName());

        System.out.println("member name == " + member.getName());

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String memberList(Model model){
        List<Member> memberList = memberService.findAllMember();
        model.addAttribute("memberList", memberList);

        return "members/memberList";
    }

}
