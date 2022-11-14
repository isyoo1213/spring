package spring.servlet.web.frontcontroller.v3.controller;

import spring.servlet.domain.member.Member;
import spring.servlet.domain.member.MemberRepository;
import spring.servlet.web.frontcontroller.ModelView;
import spring.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.Map;

public class MemberSaveControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paraMap) {

        String username = paraMap.get("username");
        // 이제 controller는 servlet 형식이 아니므로, Front-controller에서 가공된 HttpRequest 정보를 paraMap으로 받아와 꺼내서 사용
        int age = Integer.parseInt(paraMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        ModelView mv = new ModelView("save-result");
        mv.getModel().put("member", member);
        //ModelView 클래스는 Map구조의 model 변수가 존재 -> 컨트롤러에서 view로 전달해야할 데이터를 이 model에 저장
        //이후 model에 담긴 데이터와 view에 담긴 viewPath 관련 정보를 Front-Controller로 넘겨줌
        return mv;
    }
}
