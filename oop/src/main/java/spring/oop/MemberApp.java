package spring.oop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.oop.member.Grade;
import spring.oop.member.Member;
import spring.oop.member.MemberService;
import spring.oop.member.MemberServiceImpl;

public class MemberApp {

    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
        Member member = new Member(1L, "memberA", Grade.BASIC);

        memberService.join(member);
        System.out.println("New member = " + member.getName());

        Member findMember = memberService.findMember(1L);
        System.out.println("Member Found = " + findMember.getName());
    }

}
