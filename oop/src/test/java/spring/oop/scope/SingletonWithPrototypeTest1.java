package spring.oop.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.*;

class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind(){
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        prototypeBean2.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);
        assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }

    @Test
    void singletonClientUsePrototype(){
        System.out.println("ApplicationContext 생성 시작");
        ConfigurableApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        //prototypeBean 또한 bean으로 등록해서 컨테이너가 사용하고, clientBean이 의존관계로 주입받을 수 있도록 설정
        System.out.println("ApplicationContext 생성 완료");

        System.out.println("ClientBean1 조회");
        ClientBean clientBean1 = ac.getBean(ClientBean.class);

        System.out.println("ClientBean1의 instance가 logic() 호출");
        int count1 = clientBean1.logic();

        assertThat(count1).isEqualTo(1);

        System.out.println("ClientBean2 조회");
        ClientBean clientBean2 = ac.getBean(ClientBean.class);

        System.out.println("ClientBea21의 instance가 logic() 호출");
        int count2 = clientBean2.logic();
        //이미 client1의 생성 시점에 의존관계 주입 요청으로 prototypeBean이 생성되어 필드에 주입
        //singleton인 clientBean2 또한 client1의 인스턴스와 같은 주소를 가리키고, 인스턴스에 이미 주입된 기존의 prototypeBean을 사용
        assertThat(count2).isEqualTo(1);
    }

    @Scope("singleton")
    static class ClientBean{
//        private final PrototypeBean prototypeBean;
        //스프링 컨테이너를 통한 ClientBean의 싱글톤 인스턴스 생성 시점에 '의존성 주입을 위한 요청' --> 이 시점에서 이미 주입 됨
        //Test에서 logic()호출 시, 이미 생성된 prototypeBean을 사용함

        //요구사항 - clientBean이 logic()을 호출할 때마다, 새로운 prototypeBean의 인스턴스를 사용하도록 하고 싶음
        //방법 1. ApplicationContext가 prototypeBean을 '조회'하도록 (DL) 설계해 새로운 인스턴스를 생성해 사용하도록 함
        // -> ClientBean에 ApplicationContext를 의존관계로 주입해 자유롭게 사용하도록 설정
        //but, ClientBean의 생성자를 살려두면, 생성 시점에 prototypeBean의 인스턴스를 의존관계를 주입받은 인스턴스가 필드로 저장됨
        // -> but, logic()메서드에서 ApplicationContext가 조회해 새롭게 생성한 protypeBean을 사용함
        @Autowired
        ApplicationContext applicationContext;

        // **** 만약 clientBean 외의 다른 클래스에서 생성된 singleton 인스턴스에서도 생성자 주입을 통해 PrototypeBean을 주입받는다면?
        // -> 해당 PrototypeBean은 기존의 clientBean에 주입된 것과 다른 인스턴스로 새로 생성된 protytpeBean을 주입받음
        // + 해당 클래스의 싱글톤 인스턴스에 의존관계가 주입된 채로 prototype의 인스턴스가 계속해서 존재함

        //방법 2. ObjectProvider 인터페이스 (ObjectFactory 인터페이스를 상속, getObject 외의 Optional, Stream등 편의기능 포함) 사용
        //ApplicationContext에서 스프링컨테이너에 저장된 Bean을 직접 찾아서 가져오는 것이 아님
        //prototype 인스턴스가 필요할 때마다 ObjectProvider에서 해당 인스턴스를 조회(생성, DL)해서 제공해주는 방식
        //즉, ApplicationContext라는 종합적이고 무거운 컨테이너 인스턴스에서 DL을 수행하는 것이 아닌, DL 기능만 가진 가벼운 Provider를 통해 DL을 수행
        // + ** but, 외부 라이브러리는 필요 없지만 스프링 프레임워크에 의존
        @Autowired//우선 간단히 필드 주입으로 사용 + 스프링이 자동으로 주입해주는 Bean
        private ObjectProvider<PrototypeBean> prototypeBeanObjectProvider;

//        @Autowired
//        public ClientBean(PrototypeBean prototypeBean){
//            System.out.println("ClientBean 생성자 호출");
//
//            System.out.println("ClientBean의 의존관계를 주입할 prototypeBean = " + prototypeBean);
//            //이미 ApplicationContext에서 bean등록한 prototypeBean이 호출되어 넘어옴 -> 새롭게 생성한 인스턴스 X
//            this.prototypeBean = prototypeBean;
//
//            System.out.println("ClientBean 생성자의 prototypeBean 의존관계 주입 완료");
//        }

        public int logic(){
            System.out.println("ClientBean.logic");
            //방법 1. ApplicationContext가 PrototypeBean을 조회해 새로운 인스턴스를 획득해 사용
//            PrototypeBean prototypeBean = applicationContext.getBean(PrototypeBean.class);
            //방법 2. ObjectProvider 사용
            PrototypeBean prototypeBean = prototypeBeanObjectProvider.getObject();
            System.out.println("ApplicationContext에서 새롭게 생성해 조회한 prototypeBean = " + prototypeBean);
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    @Scope("prototype")
    static class PrototypeBean{
        private int count = 0;

        public void addCount(){
            count++;
        }

        public int getCount(){
            return count;
        }
        
        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init\n" + this);
            System.out.println("count = " + count);
        }

        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean.destroy\n" + this);
        }
    }
}
