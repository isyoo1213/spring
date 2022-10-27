package spring.oop.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.*;

class SingletonTest {

    @Test
    void singletonBeanFind(){
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(SingletonBean.class);
        //실제 AnnotationCOnfigApplicationContext의 parameter는 'componentClasses'
        // -> parameter로 넘긴 class는 자동으로 componentscan됨 (기존의 설정정보 클래스가 자동으로 bean등록되는 원리)

        SingletonBean singletonBean1 = ac.getBean(SingletonBean.class);
        SingletonBean singletonBean2 = ac.getBean(SingletonBean.class);
        System.out.println("singletonBean1 = " + singletonBean1);
        System.out.println("singletonBean2 = " + singletonBean2);

        //isSameAs는 '==' 비교연산과 같음 ( heap 영역의 객체 주소 비교 ) vs isEqualTo는 값 비교
        assertThat(singletonBean1).isSameAs(singletonBean2);

        ac.close();
    }

    @Scope("singleton")
    static class SingletonBean{
        @PostConstruct
        public void init(){
            System.out.println("SingletonTest.init");
        }

        @PreDestroy
        public void destroy(){
            System.out.println("SingletonBean.destroy");
        }
    }
}
