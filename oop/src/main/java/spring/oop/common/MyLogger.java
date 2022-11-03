package spring.oop.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

@Component
@Scope(value = "request")
//생성 시점은 정확하게 스프링 컨테이너에 요청하는 시점
public class MyLogger {

    private String uuid;
    private String requestURL;  //requestURL은 이 Bean이 생성되는 시점에는 알 수 없으므로, setter를 통해 입력받도록 설정

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
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
