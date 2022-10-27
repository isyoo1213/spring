package spring.oop.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
//javax. - JAVA 진영에서 공식적으로 지원하는 것들로 Spring이 아니더라도 사용 가능

class NetworkClient {
    //1. 초기화(InitializingBean), 소멸(DisposableBean) 인터페이스 적용
    // - 스프링 전용 인터페이스에 의존 + 메서드 이름 변경 불가 + 코드레벨에 적용하므로 외부 라이브러리에 적용 불가
    //2. 설정정보 사용 (InitMethod, DestroyMethod)
    // - 메서드 이름 자유롭게 설정 가능 + 등록된 bean이 스프링 코드에 의존하지 않는다(NetworkClient 코드들)
    //code가 아닌 설정정보(by @Configuration의 @Bean 어노테이션)를 사용하기 때문에 외부 라이브러리에서도 적용 가능

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    //수정자를 통한 의존관계 주입 - 외부(Test)에서 호출됨
    public void setUrl(String url) {
        this.url = url;
    }

    //서비스 시작시 호출
    public void connect(){
        System.out.println("connection: " + url);
    }

    public void call(String message){
        System.out.println("call: " + url + "\nmessage = " + message);
    }

    //서비스 종료시 호출
    public void disconnection(){
        System.out.println("close: " + url);
    }

/*  //InitializingBean, DestroyBean 인터페이스를 통한 초기화 시점
    @Override
    public void afterPropertiesSet() throws Exception {
        //의존관계 주입이 끝난 후의 세팅을 의미
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메시지");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("NetworkClient.destroy");
        disconnection();
    }
*/

    @PostConstruct
    public void init(){
        //의존관계 주입이 끝난 후의 세팅을 의미
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }

    @PreDestroy
    public void close(){
        System.out.println("NetworkClient.close");
        disconnection();
    }

}
