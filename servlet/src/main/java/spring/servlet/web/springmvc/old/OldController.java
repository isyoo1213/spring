package spring.servlet.web.springmvc.old;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component("/springmvc/old-controller") //Spring Bean의 이름을 url 패턴으로 맞춘 것
public class OldController implements Controller {
    //이 컨트롤러가 호출되려면 2가지 조건이 필요
    //1. HandlerMapping - 핸들러 매핑에서 이 컨트롤러를 찾을 수 있어야 함
    // + 이 경우, url정보가 Bean이름으로 등록되어 있으므로, Bean 이름을 통해 컨트롤러를 찾는 핸들러 매핑이 존재해야 함
    //2. HandlerAdaptor - 핸들러 매핑을 통해 찾은 handler를 실행할 handlerAdaptor가 필요
    // - 더 정확히는 Controller 인터페이스를 실행할 수 있는 핸들러 어댑터. 더 정확히는 다운캐스팅한 현재 컨트롤러의 인스턴스 by 추상화에 의존하는 스프링 패턴
    // 결과적으로
    // HandlerMapping - BeanNameUrlHandlerMapping 객체 사용
    // HandlerAdapter - SimpleControllerHandlerAdapter 객체 사용
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
        return null;
    }
}
