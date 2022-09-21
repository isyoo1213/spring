package spring.oop;

import spring.oop.member.Grade;
import spring.oop.member.Member;
import spring.oop.member.MemberService;
import spring.oop.member.MemberServiceImpl;

public class MemberApp {

    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        Member member = new Member(1L, "memberA", Grade.BASIC);

        memberService.join(member);
        System.out.println("New member = " + member.getName());

        Member findMember = memberService.findMember(1L);
        System.out.println("Member Found = " + findMember.getName());
    }

}
