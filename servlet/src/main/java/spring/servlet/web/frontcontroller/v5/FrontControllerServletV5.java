package spring.servlet.web.frontcontroller.v5;

import spring.servlet.web.frontcontroller.ModelView;
import spring.servlet.web.frontcontroller.MyView;
import spring.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import spring.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import spring.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import spring.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import spring.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import spring.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import spring.servlet.web.frontcontroller.v5.adaptor.ControllerV3HandlerAdaptor;
import spring.servlet.web.frontcontroller.v5.adaptor.ControllerV4HandlerAdaptor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {
    //final은 크게 신경쓰지 않아도 됨 - Controller 구현체와 Adaptor 구현체를 미리 초기화하기 위해 final 사용
    //기존 컨트롤러 - private Map<String, ControllerV4> controllerMap = new HashMap<>();
    private final Map<String, Object> handlerMappingMap = new HashMap<>(); //모든 Controller 버전을 지원하기 위해 Object 사용

    //버전에 맞는 Controller를 가져오기 위한 Adaptor를 모아놓은 리스트
    private final List<MyHandlerAdaptor> handlerAdaptors = new ArrayList<>();

    public FrontControllerServletV5() {
        //각 버전의 Controller 구현체들을 담아놓고 (현재는 v3만 적용 중) + 메서드로 뽑기
        initHandlerMappingMap();

        //각 버전의 Controller 구현체들을 연결시켜줄 Adaptor를 넣어놓기 + 메서드로 뽑기
        initHandlerAdaptors();
    }

    private void initHandlerMappingMap() {
        //v5에서 호출하는 v3, v4 구분을 위해 uri를 일부러 중첩되게 구성함 - index.html에서 확인
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    private void initHandlerAdaptors() {
        handlerAdaptors.add(new ControllerV3HandlerAdaptor());
        handlerAdaptors.add(new ControllerV4HandlerAdaptor());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1. 핸들러(Controller) 조회 - URI 추출 + Controller 가져오기
        //-> 이후 Handler Adaptor 찾아야함
        Object handler = getHandler(request);

        if (handler == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //2. Adaptor 조회 - 1.에서 조회된 handler를 지원하는 Adaptor 조회

        //조회된 Adaptor를 담을 변수 선언 + List에서 handler에 맞는 Adaptor 조회(메서드로 뽑아버림)
        MyHandlerAdaptor findAdaptor = getHandlerAdaptor(handler);

        //3. Adaptor 내부의 handle메서드를 통해 넘겨받은 handler를 controller로 다운캐스팅해, 로직 처리 + data와 view 정보 반환
        ModelView mv = findAdaptor.handle(request, response, handler);

        //4. 이후 ModelView에서 추출한 정보로 viewResolver를 통한 view와 model처리는 이전 Front-Controller와 동일한 로직
        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        view.render(mv.getModel(), request, response);

    }

    //iter -> for문으로 바꿔주는 단축키
    //for문을 돌려 List에서 handler에 맞는 Adaptor 조회
    private MyHandlerAdaptor getHandlerAdaptor(Object handler) {
        for (MyHandlerAdaptor handlerAdaptor : handlerAdaptors) {
            if(handlerAdaptor.supports(handler)){
                return handlerAdaptor;
            }
        } //for문으로 모든 adaptor 검증을 했음에도 찾지 못하면 예외 처리
        throw new IllegalArgumentException("handler adaptor를 찾을 수 없습니다. handler = " + handler);
    }

    //이제 Controller(handler)를 가져올 때, 버전이 특정된 것이 아니므로, Object로 가져오기 + 이후 검증로직에서도 Object로 parameter 넘겨줌
    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

}
