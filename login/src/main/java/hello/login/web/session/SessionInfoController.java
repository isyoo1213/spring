package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request){

        HttpSession session = request.getSession(false);

        if(session == null){
            return "세션이 없습니다";
        }

        //Session 데이터 출력
        //getAttributeNames() + Iterator 인터페이스를 통해 session에 담겨있는 값을 확인
        //forEachRemaining() - 모든 요소가 처리 or 작업 예외 발생 시까지 수행
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name = {}, value = {}", name, session.getAttribute(name)));

        //Login한 후 세션 확인 결과
        //session name = loginMember, value = Member(id=1, loginId=test, name=테스터, password=test!)

        //Session이 기본적으로 제공하는 데이터

        log.info("session Id = {}", session.getId()); //Cookie에 저장되는 JSESSIONID와 동일
        //session Id = 9801AF97B8172A3AECF31591681B1116

        log.info("getMaxInactiveInterval = {}", session.getMaxInactiveInterval());
        //getMaxInactiveInterval = 1800

        log.info("creationTime = {}", new Date(session.getCreationTime())); //getCreationTime()은 Long return -> Date로
        //creationTime = Sun Dec 11 22:06:36 KST 2022

        log.info("lastAccessedTime = {}", new Date(session.getLastAccessedTime()));
        //클라이언트에서 서버로 Cookie를 통해 sessionId(JSESSIONID)를 요청한 경우에 갱신
        //lastAccessedTime = Sun Dec 11 22:06:37 KST 2022

        log.info("isNew = {}", session.isNew());
        //클라이언트에서 서버로 sessionId(JSESSIONID)를 요청한 경우에는 false
        //isNew = false

        return "세션 출력";
    }

}
