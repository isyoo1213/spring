package spring.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    //WebConfig.class에 extendHandlerExceptionResolvers 오버라이드로 등록하기
    // * 주의 - ConfiguredHandlerExceptionResolver로 등록하면 스프링이 기본으로 등록하는 ExceptionResolver가 제거됨
    // ->

    // * WAS - DispatcherServlet - Prehandle - Controller - 예외발생 상황
    // -> postHandle 호출하지 않고 DispatcherServlet으로 EX반환
    // *** -> DispatcherServlet은 HandlerExceptionResolver가 있는지 확인하고 있다면 호출
    // *** -> Error에 대한 처리 후 ModelAndView return으로 '정상 응답'함 - View정보가 없으므로(설정도 가능) 그냥 WAS에 정상 응답만 함
    // -> 이후 WAS는 sendError()를 처리하면서 Exception처리함
    // *** 즉 WAS가 기존 Exception을 바로 처리하는 것이 아닌 sendError()로 바뀐 다른 Eception을 처리하는 것
    // ****** 즉, 기존 Exception발생 상황인 1.throw를 2.sendError()로 바꾸는 것
    // * 활용
    // - 1. 상태코드 변경에 따라 WAS가 ErrorPage를 '내부호출'할 수 있도록 설정 가능
    // - 2. ModelAndView에 여러 설정을 통해 예외에 따른 ErrorPage 화면 View 렌더링 제공
    // - 3. API 응답 처리 - response.getWriter().println() 처럼 Body에 직접 데이터를 넣기 or JSON 처리도 가능
    // * 이 경우에도 postHandle은 호출되지 않음

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        log.info("call resolver", ex);

        // * 서버 내부에서 IllegalArgumentException가 터지면 무조건 500이 아닌 400에러로 핸들링하고 싶은 상황
        try {
            if (ex instanceof IllegalArgumentException) {
                log.info("IllegalArgumentException resolver to 400");

                //HandlerExceptionResolver에서 Controller에서 DispatcherServlet까지 전달된 에러를 잡아먹고, sendError()로 응답
                // -> WAS는 결국 400 에러로 인식
                // *** sendError()는 IOException을 throws하고 있으므로, checked exception을 잡아줘야 함
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());

                // ModelAndView에 아무 값도 설정하지 않고 빈 값으로 넘기면 WAS(servletContainer)까지 정상적으로 흐름이 return 됨
                return new ModelAndView();
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }
        // null로 return하게 된다면, 다음 resolver가 있는지 파악 및 적용
        // 더 이상 resolver가 없을 경우 정상 return이 아닌, 예외가 발생한 상태 그대로 WAS까지 전달됨

        return null;
    }

}
