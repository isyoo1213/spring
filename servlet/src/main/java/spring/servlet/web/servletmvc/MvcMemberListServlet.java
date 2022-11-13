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
import java.util.List;

@WebServlet(name="mvcMemberListServlet", urlPatterns = "/servlet-mvc/members")
public class MvcMemberListServlet extends HttpServlet {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //비즈니스 로직 처리 (Request 정보 처리 불필요)
        List<Member> members =  memberRepository.findAll();

        //Model에 저장
        request.setAttribute("members", members);
        //jsp에서 for문을 통해 멤버 출력을 위해 JSTL 문법 사용

        //jsp로 제어권
        String viewPath = "/WEB-INF/views/members.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
