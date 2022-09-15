package user.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import user.userservice.service.MemberService;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
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

}
