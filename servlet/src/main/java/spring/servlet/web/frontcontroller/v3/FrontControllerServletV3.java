package spring.servlet.web.frontcontroller.v3;

import spring.servlet.web.frontcontroller.ModelView;
import spring.servlet.web.frontcontroller.MyView;
import spring.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import spring.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import spring.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV3.service");
        //로그 찍는게 좋음

        String requestURI = request.getRequestURI();
        ControllerV3 controller = controllerMap.get(requestURI);
        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //개선점
        //1. 비즈니스로직 - controller가 servlet 형식이 아니므로 인자로 전달할 Http Request정보를 paraMap으로 가공
        //2. View 반환 - view만 다루는 것이 아닌 ModelView로 처리
        //3. Data 전달 - ModelView는 servlet 형식의 메서드가 없으므로, MyView처럼 render() 메서드 내에서 request.setAttribute()사용 불가
        //  -> MyView의 render()에 Model도 같이 전달하기 -> by *** Overloading
        // + ModelView에 담긴 model의 데이터를 꺼내서 sevlet형식을 유지하는 MyView에 setAttribute()로 넘겨주기

/*        
        //1
        //paraMap으로 가공하기 위한 Map 생성
        Map<String, String> paramMap = new HashMap<>();

        //parameter 정보를 '모두 다' 꺼내 Map구조로 가공해야 하므로 -> request.getParamemterNames();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
*/

        //1의 과정이 너무 Detail하므로, method로 뽑아버리기
        //단축키 Ctrl + Alt + M
        Map<String, String> paramMap = createParamMap(request);

        ModelView mv =  controller.process(paramMap);
        // 여기까지 ModelView 반환이 완료 - but, ModelView에 담긴 view정보는 논리적 viewPath만을 담고 있으므로, ViewResolver로 처리 필요

        //2
        //ModelView에 담긴 view의 논리적 이름 가져오기
        String viewName = mv.getViewName();

        //물리적 viewPath를 만들어주는 메서드를 viewResolver로 뽑아버리기
        MyView view = viewResolver(viewName);

        //3
        view.render(mv.getModel(), request, response);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
