package spring.oop.beanfind;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.oop.AppConfig;
import spring.oop.member.MemberService;
import spring.oop.member.MemberServiceImpl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationContextBasicFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("이름으로 Bean 조회")
    void findBeanByName(){
        MemberService memberService = ac.getBean("memberService", MemberService.class);

        System.out.println("memberService = " + memberService);
        System.out.println("memberService.getClass() = " + memberService.getClass());

        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("이름없이 Type으로만 Bean 조회")
    void findBeanByType(){
        MemberService memberService = ac.getBean(MemberService.class);

        System.out.println("memberService = " + memberService);
        System.out.println("memberService.getClass() = " + memberService.getClass());

        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    // 여기까지는 interface로 조회


    // 구현체로 조회
    @Test
    @DisplayName("구체 타입(구현체의 클래스)으로 Bean 조회")
    void findBeanByClassName(){
        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);

        System.out.println("memberService = " + memberService);
        System.out.println("memberService.getClass() = " + memberService.getClass());

        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
        //AppConfig에서 인스턴스 생성 시 return되는 Type 기준으로 생성되므로 인터페이스, 구현체 모두 탐색 가능
        //** 역할과 구현의 분리를 위해서 구체화로 탐색하는 것보다 더 추상화된 역할로 탐색하는 것이 바람직
    }

    @Test
    @DisplayName("이름으로 Bean 조회 - 실패케이스")
    void findBeanByNameX(){
//        MemberService memberService = ac.getBean("xxxx", MemberService.class);

        assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean("xxxx", MemberService.class));
        //assertThrows()는 Junit 라이브러리
        //첫 번째 인자 - 무조건 실행되어야 하는 예외
        //두 번째 인자 - 예외가 일어나야 하는 상황
    }

}
