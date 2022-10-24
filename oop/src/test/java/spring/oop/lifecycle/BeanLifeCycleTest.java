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
    }

    @Configuration
    static class LifeCycleConfig{
        @Bean
        public NetworkClient networkClient(){
            NetworkClient networkClient = new NetworkClient();
            //초기화가 먼저 일어나고 수정자 주입(setter)으로 의존관계를 주입하는 상태
            networkClient.setUrl("http://spring.oop-dev");
            return networkClient;
        }
    }
}
