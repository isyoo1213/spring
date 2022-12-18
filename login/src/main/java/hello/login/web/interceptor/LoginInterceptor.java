package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    //Ctrl + O -> Override
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        //uuid를 afterCompletion() 메서드로 넘기고 싶지만 현재 불가능 + 인터셉터는 Singleton이므로 필드로 지정하면 덮어써버리는 문제

        // -> request.setAttribute() 사용 -> afterCompletion()에서 request.getAttribute()를 통해 사용 가능
        request.setAttribute(LOG_ID, uuid); //Ctrl + Alt + C 로 상수화

        if(handler instanceof HandlerMethod){
            // *** MVC 복습하기
            // @RequestMapping 어노테이션으로 처리되는 handler
            //*** 일반적으로 HandlerMapping으로 @Controller나 @RequestMapping을 사용 -> 핸들러 정보로 HandlerMethod가 넘어옴
            // handler는 범용성을 위해 Object로 받고있지만, 다운캐스팅을 통해 HandlerMethod 형을 사용함

            HandlerMethod hm = (HandlerMethod) handler; //호출할 컨트롤러 메서드의 모든 정보가 포함되어있음

            //cf) 정적 리소스를 처리하는 컨트롤러(핸들러) - ResourceHttpRequestHandler 형을 사용
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler); //어떤 handler를 호출하는지도 확인 - toString()으로 찍어줌
        // 실제 로그
        //REQUEST [b7fae46d-a703-4b87-a74e-32ef55eacde6][/][hello.login.web.HomeController#homeLoginV3Spring(Member, Model)]

        return true; //실제 true를 반환하면 handlerAdaptor 호출 -> handler 호출로 이어짐
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
        //실제로그
        //postHandle [ModelAndView [view="home"; model={}]]
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String) request.getAttribute(LOG_ID); //getAttribute의 return은 Object

        log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);
        //실제 로그
        //RESPONSE [b7fae46d-a703-4b87-a74e-32ef55eacde6][/][hello.login.web.HomeController#homeLoginV3Spring(Member, Model)]

        //예외 처리
        if(ex != null){
            log.error("afterCompletion error!!", ex); //log찍을 때 error는 {}로 포매팅 안해도 됨
        }
    }
}
