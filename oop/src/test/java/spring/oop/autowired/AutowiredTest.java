package spring.oop.autowired;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;
import spring.oop.member.Member;

import java.util.Optional;

class AutowiredTest {

    @Test
    void AutowiredOption(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
        //** Context를 생성할 때 인자로 넣어준 설정정보 클래스는 @Configuration 어노테이션을 붙이지 않아도 bean 등록됨
    }

    static class TestBean{
        //임의의 bean에 의존관계 설정 상황
        //Member클래스는 순수 자바 클래스 --> spring에 bean등록이 안됨 --> 오류가 터지는게 일반적인 상황

        //1. 수정자 주입에 required 설정
        //호출 자체가 안됨
        @Autowired(required = false)
        public void setNoBean1(Member noBean1){

            System.out.println("noBean1 = " + noBean1);
        }

        //2. 호출은 되지만 의존관계를 주입할 인스턴스 자체는 null
        @Autowired
        public void setNoBean2(@Nullable Member noBean2){
            System.out.println("noBean2 = " + noBean2);
        }

        //3. 의존관계를 주입하는 인스턴스가 있으면 Optional. 으로 감싸서 들어오고 없으면 Optional.empty
        @Autowired
        public void setNoBean3(Optional<Member> noBean3){
            System.out.println("noBean3 = " + noBean3);
        }

    }

}
