package spring.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import spring.exception.exception.UserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// ***** 기존의 WAS의 ErrorPage를 통한 controller 재호출과의 차이점
// - 기존에는 response.sendError()를 통해 WAS까지 예외를 넘겨 재호출이 발생했다면,
// - 첫 예외 발생 후 DispatcherServlet으로 전달된 에러는 handlerExceptionResolver를 통해 WAS로 정상응답 처리
// -> WAS(ServletContainer)의 ErrorPage '재호출 없이' 매끄럽게 Error핸들링 가능

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {
    //WebConfig에 설정 등록하기

    //response에 오류 결과를 세팅하기 위한 ObjectMapper 인스턴스
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {

            if (ex instanceof UserException) {
                log.info("Resolve UserException to 400 Error");

                //httpHeader의 accept가 JSON인 경우와 아닌 경우로 나누어서 처리
                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); //응답은 무조건 400Error로 처리하기위해

                //Error 결과를 response에 담기 위해 data를 구성해주는 과정
                // -> *** ModelAndView를 반환해야 하므로 response에 모두 세팅해주어야 함 - MVC1
                if ("application/json".equals(acceptHeader)) {
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());

                    // *** Error 결과를 담은 객체를 문자로 변환해주어야 함
                    // * writeValueAsString() - 객체 to JSON as String
                    String result = objectMapper.writeValueAsString(errorResult);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(result);

                    return new ModelAndView(); //아무것도 넘기지 않는 빈 값으로 리턴
                } else {
                    // text/html -> template -> error -> 500.html view 호출
                    return new ModelAndView("error/500");
                }
            }

        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        return null;
    }

}
