package spring.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//WebServlet이라는 컨테이너에서 관리하는 Servlet객체로 등록 + 주로 네이밍은 클래스 이름을 소문자로
@WebServlet(name = "helloServlet", urlPatterns = "/hello") //servlet name과 url매핑은 서로 겹치면 안됨
//기본적으로 servlet은 HttpServlet을 상속받음
public class HelloServlet extends HttpServlet {

    //Ctrl + O -> 자물쇠 잠겨있는 protected service 메서드를 오버라이드
    //servlet이 호출되면 service 메서드가 호출됨
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("HelloServlet.service");
    //http 요청이 들어오면, WAS의 '서블릿 컨테이너'가 request, response 객체를 만들어서 servlet에 던져줌
        System.out.println("request = " + request);
        //request = org.apache.catalina.connector.RequestFacade@131746b6
        System.out.println("response = " + response);
        //response = org.apache.catalina.connector.ResponseFacade@2d9e2ee1
        //********
        //HttpServletRequest, HttpServletResponse는 인터페이스 - 'WAS인 톰캣'은 '서블릿 컨테이너'를 통해 'servlet 표준'에 따라서 http정보를 담는 '구현체' 인스턴스를 생성한 것
        // -> 이렇게 생성한 객체를 servlet에 던져주는 역할을 하는 것이 'service 메서드'

        //servlet의 기능 - queryParameter 쉽게 가져올 수 있음
        String username = request.getParameter("username");
        System.out.println("username = " + username);

        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8"); //2020년 이후 시스템에서는 EUC-KR 더 이상 쓰지 않고 UTF-8
        // 위 두 줄은 Header의 ContentType에 들어가는 정보
        response.getWriter().write("Hello " + username); //http message body에 데이터를 들어갈 수 있도록 하는 메서드

    }
}
