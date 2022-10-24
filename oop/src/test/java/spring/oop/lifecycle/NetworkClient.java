package spring.oop.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

class NetworkClient implements InitializingBean, DisposableBean {
    //1. 초기화(InitializingBean), 소멸(DisposableBean) 인터페이스 적용
    // - 스프링 전용 인터페이스에 의존 + 메서드 이름 변경 불가 + 코드레벨에 적용하므로 외부 라이브러리에 적용 불가
    //2.

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
}
