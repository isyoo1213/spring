package spring.oop.scan;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import spring.oop.scan.filter.BeanA;
import spring.oop.scan.filter.BeanB;
import spring.oop.scan.filter.MyExcludeComponent;
import spring.oop.scan.filter.MyIncludeComponent;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.context.annotation.ComponentScan.*;

public class ComponentFilterAppConfigTest {

    @Test
    void filterScan(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);
        BeanA beanA = ac.getBean("beanA", BeanA.class);
        assertThat(beanA).isNotNull();

//        BeanB beanB = ac.getBean("beanB", BeanB.class);
        assertThrows(
                NoSuchBeanDefinitionException.class,
                ()->ac.getBean("beanB", BeanB.class)
        );
    }

    @Configuration
    @ComponentScan(
            //type=FilterType.ANNOTATION << 이 부분은 기본값으로 삭제해도 됨
            includeFilters = @Filter(classes = MyIncludeComponent.class),
            excludeFilters = {
                    @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class),
//                    @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BeanA.class)
                    //ASSIGNABLE_TYPE --> @ConponentScan을 통한 bean등록을 제외할 '클래스'를 직접 지정 가능
            }

    )
    static class ComponentFilterAppConfig {

    }
}
