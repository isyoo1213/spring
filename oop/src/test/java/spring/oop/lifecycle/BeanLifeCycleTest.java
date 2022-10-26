package spring.oop.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest(){
        //ApplicationContext를 상속받은 Interface - close()기능을 활용할 수 있음
        //AnnotationConfigApplicationContext의 상위 Interface이기도 함
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close();
    }

    @Configuration
    static class LifeCycleConfig{
        @Bean(initMethod = "init", destroyMethod = "close")
        //- 직접 수동으로 bean등록하는 경우, @Bean의 destroyMethod에는 default로 "(inferred)"가 등록되어 있음
        // -> close, shutdown이라는 이름의 method를 자동으로 호출 ( destroyMethod="" )으로 등록시 추론 기능 off
        // + JAVA 1.7부터 AutoClosable 인터페이스를 상속받은 close() 메서드를 사용할 수도 있음
        public NetworkClient networkClient(){
            NetworkClient networkClient = new NetworkClient();
            //초기화가 먼저 일어나고 수정자 주입(setter)으로 의존관계를 주입하는 상태 - '객체 생성'과 '의존관계 주입'의 분리
            // ** 생성자 주입의 경우, '객체 생성'과 '의존관계 주입'이 거의 동시에 일어남
            networkClient.setUrl("http://spring.oop-dev");
            // ** 이렇게 '초기화'가 '의존관계 주입'전에 일어난다면, 인스턴스는 준비되지 않은 상태
            // **** 의존 관계를 형성한 url변수가 null으로 '초기화'되어버린 상태 by 생성자를 통한 순수한 JAVA의 실행
            // 즉, 인스턴스의 초기화와 의존관계를 형성한 변수들의 초기화 시점에 대한 조율이 필요
            //+ 생성자 주입의 경우, 인스턴스 초기화 과정 전에, 의존관계를 형성한 변수들의 초기화가 먼저 이뤄짐
            //*** 객체의 '생성'과 '초기화'는 분리시키는 것이 '단일책임원칙'OCP에 적합하다
            // - '초기화'는 객체의 '사용, 동작'적인 측면이다.
            // '생성'된 객체에 값의 할당뿐만 아니라, 생성자 내부의 메서드를 통해 커넥션을 맺는 것 또한 '사용, 동작'의 영역으로 분리
            // '생성' - 메모리 할당하는 것 까지만( 최소한의 데이터 세팅 ) + '초기화' - 동작을 위한 메서드들
            // + ** '생성자'는 필수 정보(파라미터)를 받아 메모리에 할당하는 '생성'에만 집중하도록 하고,
            // + ** 초기화는 이렇게 생성된 객체를 활용해 무거운 동작을 수행하도록 분리
            // ****** 생성자 안에서 무거운 초기화 동작을 함께하는 것보다, '생성'과 '초기화'의 분리가 유지보수 관점에서 유리
            // + 초기화 작업이 내부 필드들의 값들의 간단한 변경 정도라면 생성자에서 처리하는 것도 좋을 수 있다
            // + ***** Lazy한 작업에 이러한 '생성'과 '초기화'의 분리를 사용 가능
            // - 생성 후 대기하면서 최초의 요청이 올 때 초기화를 호출하는 방식으로도 사용 가능
            return networkClient;
        }
    }
}
