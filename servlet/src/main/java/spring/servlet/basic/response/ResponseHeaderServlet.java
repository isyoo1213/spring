package spring.servlet.basic.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Response message의 [status-line]
        response.setStatus(HttpServletResponse.SC_OK);//상수로 선언되어있음

        // [response header]
        response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); //캐시 완전 무효화
        response.setHeader("Pragma", "no-cache"); //과거의 캐시 무효화..?
        response.setHeader("my-header", "hello");

        //[response header 편의 메서드]
        content(response);

        //[response cookie 편의 메서드]
        cookie(response); //헤더에 저장됨

        //[response redirect 편의 메서드]
        redirect(response);

        //[response body]
//        response.getWriter().write("OK");
        PrintWriter writer = response.getWriter();
        writer.println("OK");
    }

    //[content 편의 메서드]
    private void content(HttpServletResponse response) {
        //Content-Type: text/plain;charset=utf-8
        //Content-Length: 2 //println()일 경우, 1이 추가 됨
        //response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        //response.setContentLength(2); //(생략시 자동 생성)
    }

    //[cookie 편의 메서드]
    private void cookie(HttpServletResponse response) {
        //Set-Cookie: myCookie=good; Max-Age=600;
        //response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");
        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600); //600초
        response.addCookie(cookie);
    }

    //[redirect 편의 메서드]
    private void redirect(HttpServletResponse response) throws IOException {
        //Status Code 302
        //Location: /basic/servlet-form.html
        //response.setStatus(HttpServletResponse.SC_FOUND); //302
        //response.setHeader("Location", "/basic/servlet-form.html");
        response.sendRedirect("/basic/servlet-form.html");
    }
}
