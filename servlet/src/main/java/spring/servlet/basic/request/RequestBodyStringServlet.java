package spring.servlet.basic.request;

import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "requestBodyStringServlet", urlPatterns = "/request-body-string")
public class RequestBodyStringServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream(); //Http Request message의 Body 내용을 ByteCode로 바로 받을 수 있음
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8); // ByteCode를 문자로 변환하기 위한 설정

        System.out.println("messageBody = " + messageBody);
        response.getWriter().write("OK");
    }
}
