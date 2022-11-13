package spring.servlet.web.frontcontroller.v1;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ControllerV1 {

    //servlet과 비슷한 모양의 인터페이스 - request, response, exception
    void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
