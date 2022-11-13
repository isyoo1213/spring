package spring.servlet.web.servletmvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//의미적으로는 Controller역할을 Servlet으로 구성하는 것
@WebServlet(name = "mvcMemberForServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberForServlet extends HttpServlet {

    //**** WEB-INF의 룰
    //현재 프로젝트에서 webapp/jsp/... 처럼 외부에서 직접 jsp url에 접근하지 않고, Controller를 거쳐서 jsp를 호출하게 하기 위한 WAS의 규약
    //즉, WEB-INF 아래의 자원들은 외부에서 직접 호출하더라도 호출되지 않음
    //localhost:8080/WEB-IFN/views/new-form.jsp로 접속하면 Whitelabel Error Page 뜸
    //즉, Servlet이나 서버 내부의 forwarding 등의 과정을 거쳐야 호출될 수 있도록 하는 것

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. jsp로 구성된 회원가입 form을 보여주고 싶음 - 여기에서 Controller의 역할은 jsp를 호출하는 것 뿐
        String viewPath = "/WEB-INF/views/new-form.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath); //Controller에서 View로 이동할 때 사용되는 메서드
        dispatcher.forward(request, response);
        //다른 servlet이나 jsp로 이동할 수 있는 기능.
        // ***** 서버 내부에서 다시 호출이 일어남
        // cf) redirect : 클라이언트의 요청 -> 클라이언트에 응답 -> 클라이언트의 redirect 경로로의 요청 -> 응답 >>>> '웹브라우저 입장' 실제 2번의 요청
        // cf) forward : 클라이언트의 요청 -> 서버 내부로의 응답 -> 서버의 요청 -> 응답의 구조
        // -> Controller로 들어온 요청 url이, jsp로 넘겨주는 viewPath로 변하지 않고 그대로 유지됨
        //즉 Controller에서 View로 제어권을 넘겨주는 것
    }
}
