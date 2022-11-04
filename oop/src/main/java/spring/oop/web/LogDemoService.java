package spring.oop.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import spring.oop.common.MyLogger;

// **** requestScope를 사용하지 않고, parameter를 통해 request에 관련된 정보를 Service 계층으로 넘길 수는 있다
// but, 코드가 지저분해지고 + requestURL과 같은 정보들이 '웹'과 관련없는 **Service 계층까지 넘어오게 된다
// ->> '웹'과 관련된 부분은 'Controller'까지만 사용 + 'Service' 계층은 웹 기술과 관련없이 순수하게 유지하는 것이 유지보수 관점에서 좋다
// ->> MyLogger와 같이 requestScope Bean을 통해 웹 관련 정보를 Service에 넘기지 않고 MyLogger의 멤버변수에 저장해 코드와 계층을 깔끔하게 유지 가능
@Service
@RequiredArgsConstructor
public class LogDemoService {

//    private final MyLogger myLogger;
//    private final ObjectProvider<MyLogger> myLoggerObjectProvider;

    private final MyLogger myLogger;

    public void logic(String id) {
//        MyLogger myLogger = myLoggerObjectProvider.getObject();
        myLogger.log("service id = " + id);
    }
}
