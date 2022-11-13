package spring.servlet.web.servletmvc;

import spring.servlet.domain.member.Member;
import spring.servlet.domain.member.MemberRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//즉 해당 controller가 요청을 받는 url은 이전에 forward된 jsp파일의 form이 요청하는 url이 됨
@WebServlet(name = "mvcMemberSaveServlet", urlPatterns = "/servlet-mvc/members/save")
public class MvcMemberSaveServlet extends HttpServlet {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Request 정보 처리
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        //비즈니스 로직(Service 계층이 없는 상태이므로 Controller에서 바로 Repository 호출)
        Member member = new Member(username, age);
        memberRepository.save(member);

        //Model에 데이터를 보관
        request.setAttribute("member", member);
        //이후 jsp에서 사용할 때, request.getAttribute()는 Object 타입으로 반환되므로, 다운캐스팅 필요함
        //ex> id=<%=(Member)request.getAttribute("member").getId()%>
        // -> ${}문법을 통해(Property 접근법, 변수를 부르면 getter가 호출됨) getAttribute()사용하지 않고 바로 변수 사용 가능

        //View로 제어권 위임
        String viewPath = "/WEB-INF/views/save-result.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);

    }
}
