package spring.servlet.web.frontcontroller.v4.controller;

import spring.servlet.domain.member.Member;
import spring.servlet.domain.member.MemberRepository;
import spring.servlet.web.frontcontroller.v4.ControllerV4;

import java.util.Map;

public class MemberSaveControllerV4 implements ControllerV4 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Map<String, String> paraMap, Map<String, Object> model) {

        //1. Request 정보 처리
        String username = paraMap.get("username");
        int age = Integer.parseInt(paraMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        //2. Data 처리
        //->이제 ModelView를 직접 생성해 viewPath와 data를 넣어주는 것이 아닌, Front-Controller에서 생성한 model Map을 받아 data를 저장
        model.put("member", member);

        //3. View 처리
        return "save-result";
    }
}
