package spring.servlet.web.frontcontroller.v5.adaptor;

import spring.servlet.web.frontcontroller.ModelView;
import spring.servlet.web.frontcontroller.v4.ControllerV4;
import spring.servlet.web.frontcontroller.v5.MyHandlerAdaptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV4HandlerAdaptor implements MyHandlerAdaptor {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV4);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {

        ControllerV4 controller = (ControllerV4) handler;

        //Http Request 정보를 받기 위한 paramMap 생성
        Map<String, String> paramMap = createParamMap(request);

        //model을 위한 Map 생성
        Map<String, Object> model= new HashMap<>();

        //ControllerV4가 return하는 viewName 받기
        String viewName = controller.process(paramMap, model);

        //ControllerV3는 자체적으로 ModelView를 return -> but, V4는 viewName을 return + hadler 메서드는 ModelView를 return
        //-> v4의 handle()은 ModelView를 직접 생성하고 v4의 viewName과 model을 ModelView에 담아서 return 하도록 설계
        //즉, 서로 다른 V3, V4의 return을 Adaptor처럼 변경해주는 역할이 handlerAdaptor 구현체의 역할 중 하나
        ModelView mv = new ModelView(viewName);
        mv.setModel(model);

        return mv;
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
