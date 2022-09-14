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

}
