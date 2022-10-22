package spring.oop.annotation;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented//@Qualifier에 등록된 4가지 annotation을 통해 @Qualifier처럼 사용 가능
@Qualifier("mainDiscountPolicy")// + @Qualifier를 통해 @Qualifier의 기능 동작 수행 가능
public @interface MainDiscountPolicy {
    // ** JAVA 자체에서 어노테이션에 대한 상속은 존재하지 않음
    // 어노테이션의 기능을 중첩해 사용하는 것은 spring이 제공하는 것
    // @Qualifier 뿐만 아니라 @Autowired와 같은 다른 어노테이션도 다양한 방식으로 조합해서 사용 가능함
}
