package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    // ***** Filter의 모든 method를 구현할 필요는 없다
    // *** public 'default'로 잡힌 것들은 필수 구현 필요하지 않음
    // -> 여기에서는 init()과 destroy() 생략
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        //Login Check Filter에는 response도 사용할 예정
        HttpServletResponse httpResponse = (HttpServletResponse) response;
    }

}
