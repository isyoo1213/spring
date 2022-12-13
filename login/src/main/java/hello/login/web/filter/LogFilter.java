package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

//Filter 인터페이스는 javax.servlet.Filter를 import
@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");

        //ServletRequest - HttpServletRequest의 부모 interface
        // -> 별다른 기능이 없어서 DownCasting - *** Servlet은 초기 설계시 httpRequest 외에 다른 것들도 받을 수 있도록 설계됐었음
        // *** 현재의 servlet은 dispatcherServlet
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String requestURI = httpRequest.getRequestURI();

        //모든 사용자 요청을 구분하기 위한 것으로 uuid 사용
        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            chain.doFilter(request, response); //다음 filter가 존재하면 filter호출 or servlet 호출
            //servlet 호출 -> controller 호출 -> ... 모든 로직 수행 후 finally 블럭 실행
        } catch (Exception e){
            throw e;
        } finally {
            //doFilter()에서 filter, servlet, controller 로직 실행 후 반환된 결과 정보
            // -> 현재 controller에서 response를 담당하고 있으니 그에 대한 uuid 식별 값만 출력 + 어떤 uri Request에 대한 response인지
            // -> request와 response의 uuid 값이 동일하게 찍히고 있는 것을 확인
            // -> 같은 uuid 내의 try, finally를 사용해 성능최적화 등도 작업 가능
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }

        //*** 같은 uuid에 포함되는 Request와 Response Cycle에 대해, Controller 등의 '오류 상황' 및 다양한 상황에서도 uuid 출력하고 싶다면?
        // -> 즉, 실무에서 동일한 Cycle 내의 로그에 모두 같은 '식별자'를 자동으로 남기는 방법 -> logback mdc

    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
