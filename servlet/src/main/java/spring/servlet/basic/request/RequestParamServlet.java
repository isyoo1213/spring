package spring.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 1. 파라미터 전송 기능
 * http://localhost:8080/request-param?username=hello&age=20
 */
@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("RequestParamServlet.service");

        System.out.println("[전체 Parameter 조회] - start");

        request.getParameterNames().asIterator()
                        .forEachRemaining(paramName -> System.out.println(paramName + " = " + request.getParameter(paramName)));

        System.out.println("[전체 Parameter 조회] - end");
        System.out.println();

        System.out.println("[단일 Parameter 조회] - start");

        String username = request.getParameter("username");
        String age = request.getParameter("age");
        // *** getParameter() - 파라미터에 대한 값이 단 1개일 경우에만 사용해야 한다
        // + 값이 2개 이상 들어가있을 경우, getParameterValues()의 첫 번째 값을 반환

        System.out.println("username = " + username);
        System.out.println("age = " + age);

        System.out.println("[단일 Parameter 조회] - end");
        System.out.println();

        System.out.println("[이름이 같은 복수 Parameter 조회]");
        String[] usernames = request.getParameterValues("username");
        for (String name : usernames) {
            System.out.println("username = " + name);
        }

        response.getWriter().write("OK");
    }
}
