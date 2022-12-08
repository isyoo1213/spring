package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리
 */
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
    //동시성 이슈로 ConcurrentHashMap 사용 - 동시에 여러 thread가 접근 가능

    /**
     * 세션 생성
     * * 1. sessionId 생성 (임의의 추정 불가능한 랜덤값)
     * * 2. 세션 저장소에 sessionId와 보관할 값 저장
     * * 3. sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
     */
    public void createSession(Object value, HttpServletResponse response){

        //1
        //sessionId를 생성하고 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        //Cookie 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request){
/*  //1. request에 실려온 Coolie를 찾고
    //2. sessionId와 맞는지 값이 있는지 비교해
    //3.sessionStore에서 session에 저장된 value를 return하는 로직

        Cookie[] cookies = request.getCookies(); //Cookie는 배열로 반환됨
        if(cookies == null){
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_COOKIE_NAME)) {
                return sessionStore.get(cookie.getValue());
            }
        }
        return null;
*/
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie == null){
            return null;
        }

        return sessionStore.get(sessionCookie.getValue());
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME); //세션에 접근할 수 있는 쿠키 획득
        if(sessionCookie != null){ //현재 들어온 request가 세션에 접근할 수 있는 쿠키가 존재 한다면 sessionStore 저장된 Member를 삭제
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies(); //Cookie는 배열로 반환됨
        if(cookies == null){
            return null;
        }
        //배열을 Stream으로 변경해주는 메서드
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
        //findFirst()는 순서가 중요한 데이터 스트림을 탐색시
        // cf) findAny()는 추가적으로 병렬처리로 순서 상관없이 처리할 때 사용 가능
    }
}
