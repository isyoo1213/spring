package spring.oop.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.oop.common.MyLogger;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
//    private final MyLogger myLogger;
    // ** MyLogger의 Scope는 request -> 생명주기는 Http request가 왔을 때에 생성
    // -> 스프링 컨테이너를 통한 LogDemoController의 Bean이 생성자 의존관계 주입 시점에서 MyLogger의 인스턴스 생성이 불가능함
    // -> *** 즉, 컨테이너에서 MyLogger 인스턴스를 조회하는 시점이 requestScope를 활용하는 Bean을 생성하는 시점이 아닌,
    // Http Request가 발생하는 시점으로 '지연 Lazy' 시켜야함 ( **** Bean 생성시점을 지연한다는 표현보다, 컨테이너에서 조회하는 시점이 더욱 정확)
    // -> Provider 사용으로 해결
    // -> ObjectProvider.getObject()를 호출하는 시점에는 Http Request가 진행중이므로, request Scope Bean의 생성이 정상적으로 이루어짐

/* //방법 1. ObjectProvider 사용
    //private final ObjectProvider<MyLogger> myLoggerObjectProvider;
    // -> MyLogger 인스턴스가 아닌, MyLogger 인스턴스를 DL(Dependency Lookup)할 수 있는 객체를 주입받음
    // + **** 같은 Http Request 내에서는, 어느 인스턴스에서 DL을 요청하든 같은 인스턴스를 반환!!
    // ex> LogDemoController에서 요청하든, LogDemoService에서 요청하든. by Thread.sleep()을 통해 확인
*/

    //방법 2. ProxyMode 사용
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) throws InterruptedException {
        //HttpServletRequest는 Spring의 servlet 표준을 통한 request 정보를 담고 있는 객체
        String requestURL = request.getRequestURL().toString();

        //방법 2. ProxyMode에서 프록시 클래스가 적용되었는지 확인하기 위한 출력
        System.out.println("myLogger = " + myLogger.getClass());
        //실제출력 -> myLogger = class spring.oop.common.MyLogger$$EnhancerBySpringCGLIB$$8b4ed546
        //프록시가 Provider가 DL을 수행할 객체를 주입하는 것처럼 유사하게 동작
        // **** 아래 스테이트먼트와 같이 실제 인스턴스를 사용해야 하는 경우에는 프록시 객체가 아닌 실제 인스턴스를 조회해 사용

//        MyLogger myLogger = myLoggerObjectProvider.getObject();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        Thread.sleep(1000);
        logDemoService.logic("testId");
        return "OK";
    }

}
