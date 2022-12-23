package spring.exception.servlet;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Slf4j
@Controller
public class ServletExceptionController {

    @GetMapping("/error-ex")
    public void errorEx() {
        throw new RuntimeException("예외 발생");
        //서버 내부에서 해결할 수 없는 오류가 Controller에서 WAS까지 전달될 경우 -> 500Error
        //cf)잘못된 요청 - 서버에 Resource자체가 없으므로 -> 404Error
    }

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 오류");
        //현재 message는 출력되지 않고 기본 예외 화면이 출력됨
        // -> 옵션을 통해 설정 가능 - 뒤에서 다룰 예정
    }

    @GetMapping("/error-400")
    public void error400(HttpServletResponse response) throws IOException {
        response.sendError(400, "400 오류");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500);
    }
}
