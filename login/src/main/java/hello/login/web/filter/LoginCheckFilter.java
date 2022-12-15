package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whiteList = {"/", "/members/add", "/login", "/logout", "/css/*"};


    // ***** Filter의 모든 method를 구현할 필요는 없다
    // *** public 'default'로 잡힌 것들은 필수 구현 필요하지 않음
    // -> 여기에서는 init()과 destroy() 생략
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        //Login Check Filter에는 response도 사용할 예정
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        try {
            log.info("인증 체크 필터 시작 {}", requestURI);

            //체크 로직 by whiteList
            if(isLoginCheckPath(requestURI)){
                log.info("인증 체크 로직 실행 {}",requestURI);

                //인증 체크 후 Session 확인
                HttpSession session = httpRequest.getSession(false);

                //session자체가 존재하지 않거나, session에 저장된 값이 없을 경우의 처리 - 미인증 사용자
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){

                    log.info("미인증 사용자 요청 {}", requestURI);

                    //로그인으로 redirect
                    // *** + redirect된 로그인 페이지에서 인증 성공시 기존에 접속하려했던 URI로 보내주는 처리
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return; //*** 단순히 return을 해줌으로써 filter나 servlet등 다음 chain 실행 종료
                }
            }

            chain.doFilter(request, response);

        } catch (Exception e){
            throw e; //예외 로깅도 가능하지만, servletContainer를 들고있는 WAS, 톰캣까지 예외를 보내주어야 가능
        } finally {
            log.info("인증 체크 필터 종료 {} ", requestURI);
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크 X
     */
    private boolean isLoginCheckPath(String requestURI){
        //Spring이 제공하는 PatternMatchUtils 사용
        // -> whiteList에 포함되지 않는 것은 false를 반환
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }

}
