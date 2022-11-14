package spring.servlet.web.frontcontroller.v3.controller;

import spring.servlet.domain.member.Member;
import spring.servlet.domain.member.MemberRepository;
import spring.servlet.web.frontcontroller.ModelView;
import spring.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.List;
import java.util.Map;

public class MemberListControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paraMap) {

        List<Member> members = memberRepository.findAll();

        ModelView mv = new ModelView("members"); // view에 담길 viewPath정보
        mv.getModel().put("members", members); // model에 담길 데이터 정보

        return mv;
    }
}
