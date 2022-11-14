package spring.servlet.web.frontcontroller.v5.adaptor;

import spring.servlet.web.frontcontroller.ModelView;
import spring.servlet.web.frontcontroller.v3.ControllerV3;
import spring.servlet.web.frontcontroller.v5.MyHandlerAdaptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//각각 Controller 버전에 맞는 Adaptor의 구현체
public class ControllerV3HandlerAdaptor implements MyHandlerAdaptor {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV3);
    }

    //adaptor + handler로 실제 controller의 로직 수행 후 반환되는 View와 Data를 반환
    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        //유연하게 다양한 Controller를 받기 위해 Object로 받은 parameter를 다운캐스팅
        ControllerV3 controller = (ControllerV3)handler;

        //Http request 정보 처리 + ModelView반환 (Data 처리 결과 + viewPath 전달)
        //*** jsp를 호출하는 viewResolver 처리는 V3, V4 모두 같으므로 상위에서 공통 처리
        Map<String, String> paramMap = createParamMap(request);
        ModelView mv =  controller.process(paramMap);

        return mv;
    }

    // + ControllerV3는 Front-Controller에서 request 정보를 Map으로 변환 후 Controller으로 전달하는 방식이므로, V3의 Front-Controller처럼 변환
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
