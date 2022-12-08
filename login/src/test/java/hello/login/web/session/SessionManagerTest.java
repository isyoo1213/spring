package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.*;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest(){
    //세션 생성
        // *** createSession()의 Test에 필요한 파라미터인 HttpServletResponse - 인터페이스이고 이를 구현체는
        // -> WAS인 Tomcat의 servletContainer에서 제공 - HttpServletResponse/Request의 구현체를 제공
        // -> Test가 어렵지만 Spring에서 제공해줌 - Mock -> 가짜로 Http Response를 구성해줌

        MockHttpServletResponse response = new MockHttpServletResponse();

        Member member = new Member();
        sessionManager.createSession(member, response);

    //Request에 응답 쿠키가 저장되어있는지 확인 - 정확히는 WebBrowser에서 Response를 한 번 받은 뒤 부터의 요청
        MockHttpServletRequest request = new MockHttpServletRequest();
        //HttpServletRequest/Response에는 getCookies() 메서드 없음
        request.setCookies(response.getCookies());
        // Response에서 Cookie를 담아 응답하면 이를 다시 요청에 세팅 ex) mySessionId="123123149-1235135-1.."

    //session을 조회 - 즉 흐름은 서버 - 클라이언트 - 서버
        Object result = sessionManager.getSession(request);

        assertThat(result).isEqualTo(member);

    //session 만료
        sessionManager.expire(request);
        Object expiredSession = sessionManager.getSession(request);

        assertThat(expiredSession).isNull();
    }
}
