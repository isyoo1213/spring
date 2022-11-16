package spring.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Controller는 View 이름을 반환 -> 뷰를 찾고 뷰가 랜더링 됨
//@RestController는 String 문자 그대로 반환. Http message Body에 문자 그대로를 실어서 출력
@Slf4j //직접 객체생성하지 않고, Lombok에서 제공하는 어노테이션으로 사용 가능
public class LogTestController {
    //slf4j 인터페이스로 import
    //static으로 생성하기도 함
//    private final Logger log = LoggerFactory.getLogger(LogTestController.class); //getLogger(getClass()) -> 현재 클래스 가져옴

    @RequestMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        System.out.println("name = " + name);

        // *** 로그 출력 방식을 formating해서 사용하는 이유
        // JAVA 특성 상, 로그 출력에 message + String 변수를 사용하면 이를 하나의 String으로 연산하는 리소스 낭비가 발생
        // 출력되지 않는 로그 또한 이 문자합 연산이 일어나므로 객체를 parameter로 넘겨 포맷팅해서 사용
        log.trace("trace log={}", name);
        log.debug("debug log={}", name);
        log.info("info log={}", name);
        log.warn("warn log={}", name);
        log.error("error log={}", name);
        //info -> INFO 28716 --- [nio-8080-exec-1] s.springmvc.basic.LogTestController      :  info log=Spring
        //Time + LogLevel + Process ID + Thread + Path + Message

        //root(전체 경로)에 따로 설정하지 않아도 info로 설정되어 출력됨

        return "ok";
    }
}
