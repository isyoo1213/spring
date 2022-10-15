package spring.oop.scan.filter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)//type은 클래스 레벨에 붙는 것
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeComponent {
}
