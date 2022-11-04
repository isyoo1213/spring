package spring.oop.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

@Component
//@Scope(value = "request")
//생성 시점은 정확하게 스프링 컨테이너에 요청하는 시점
//방법 2. Proxy 사용
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS) // 프록시, 가짜를 만드는 것과 유사
//ProxyMode를 적용하는 대상이 클래스면 TARGET_CLASS, 인터페이스면 INTERFACES
//MyLogger를 상속하는 가짜 Proxy 클래스를 만들고, Http Request와 상관없이 의존관계를 필요로하는 Bean에 미리 주입해놓음
//컨테이너에는 myLogger 이름 그대로 Bean 등록 -> ac.getBean("myLogger", MyLogger.class)으로 조회하면 프록시 객체를 반환함
// + *** 의존관계 주입에도 프록시 객체를 주입
// + *** 프록시 객체는 내부에 실제 MyLogger를 찾아오는 로직을 가지고 + 요청이 들어오면 MyLogger의 인스턴스를 요청하는 위임 로직이 내부에 존재
// ********** Provider든 Proxy든, 진짜 객체의 인스턴스를 조회하는 시점까지 지연처리를 가능하게 해준다 by 다형성 + DI 컨테이너
// but, 싱글톤처럼 사용하는 것 같지만 실제로는 싱글톤이 아니므로 주의해서 사용해야 함
// -> 스프링의 엄청난 장점 - 클라이언트 측에서 코드를 조작하지 않아도 됨. 이후 AOP도 마찬가지
public class MyLogger {

    private String uuid;
    private String requestURL;  //requestURL은 이 Bean이 생성되는 시점에는 알 수 없으므로, setter를 통해 입력받도록 설정

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
        //ProxyMode 사용 시, 실제 MyLogger의 인스턴스가 호출될 때 프록시가 사용되는지, MyLogger 인스턴스가 사용되는지 확인하기 위한 출력
        System.out.println("this = " + this.getClass());
        //실제 출력 -> this = class spring.oop.common.MyLogger
    }

    public void log(String message){
        System.out.println("[" + uuid + "]" + "[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init(){
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] Request scope Bean creating : " + this);
    }

    @PreDestroy
    public void close(){
        System.out.println("[" + uuid + "] Request scope Bean closing : " + this);
    }
}
