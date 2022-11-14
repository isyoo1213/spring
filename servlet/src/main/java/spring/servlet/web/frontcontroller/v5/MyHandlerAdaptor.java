package spring.servlet.web.frontcontroller.v5;

import spring.servlet.web.frontcontroller.ModelView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MyHandlerAdaptor {

    //handler는 Controller와 같고, 적합한 Controller를 불러올 수 있는지 판단
    boolean supports(Object handler);

    //이제 Front-Controller가 Controller를 호출하는 것이 아닌, Adaptor가 Controller를 호출하므로, Controller가 넘기는 data를 처리
    // + V4처럼 Model과 View를 분리해서 넘기기 전에, Controller 자체가 servlet 형식을 취하는 설계
    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException;
}
