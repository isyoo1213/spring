package spring.oop.beanfind;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.oop.AppConfig;

class ApplicationContextInfoTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBeans(){
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("bean name = " + beanDefinitionName + "\nObject = " + bean);
        }
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean(){

        String[] beanDefinitionNames = ac.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {

            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);
            //빈에 대한 meta data를 출력하는 함수. BeanDefinition 자료형 존재.

            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION){
                //BeanDefinition의 Role
                //ROLE_APPLICATION 우리가 직접 등록한 어플리케이션 빈
                //ROLE_INFRASTRUCTURE 스프링 내부 동작을 위해 등록되는 빈
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("bean name = " + beanDefinitionName + "\nObject = " + bean);
            }

        }

    }

}
