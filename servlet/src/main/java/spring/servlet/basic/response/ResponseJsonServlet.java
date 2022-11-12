package spring.servlet.basic.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import spring.servlet.basic.JsonData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
public class ResponseJsonServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Content-Type: application/json
        response.setContentType("application/json");
        //application/json은 자체적으로 utf-8만 사용하도록 정의.
        //-> application/json;charset=utf-8 << 이와 같이 추가 Parameter지정하면 의미없는 parameter를 지정한 것
        // + response.getWriter()를 사용 시 추가 Parameter를 자동으로 생성
        //-> response.getOutputStream()을 사용하면 정상 출력
        response.setCharacterEncoding("utf-8");

        //Data클래스에 매핑
        JsonData jsonData = new JsonData();
        jsonData.setUsername("인성");
        jsonData.setAge(32);

        //Json 형식으로 파싱 by ObjectMapper by Jackson
        String result = objectMapper.writeValueAsString(jsonData);
        response.getWriter().write(result);
    }
}
