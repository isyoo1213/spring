package spring.exception.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        //기존 Login 프로젝트에서 Filter 연습 시 사용한 LogFilter에서 getDispatcherType() 로그만 변경
        try {
            log.info("REQUEST [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
            chain.doFilter(request, response);
            //FIlter 호출 -> DispatcherServlet호출 -> 컨트롤러 호출 -> 예외발생
        } catch (Exception e){
            log.info("EXCEPTION {}", e.getMessage());
            throw e;
            //예외를 던지고 throws하고 있으므로 WAS까지 예외가 전달됨
            // ***** 이후 ErrorPage에 의해 DispatcherType:Error로 내부적은 요청 및 컨트롤러 호출
            // - WebConfig에서 LogFilter를 등록할 때 REQUEST와 ERROR타입 모두 등록했으므로 필터에 따른 로그 모두 뜸
        } finally {
            log.info("RESPONSE [{}][{}][{}]", uuid, request.getDispatcherType(), requestURI);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
