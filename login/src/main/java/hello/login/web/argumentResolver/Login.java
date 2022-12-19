package hello.login.web.argumentResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) //Parameter에 적용하는 Annotation을 만들겠다
@Retention(RetentionPolicy.RUNTIME) //실제 동작할 때까지 Annotation이 남아있어야 한다
public @interface Login {
}
