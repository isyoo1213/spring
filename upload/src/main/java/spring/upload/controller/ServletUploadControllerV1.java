package spring.upload.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v1")
public class ServletUploadControllerV1 {

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        // * application.properties의 spring.servlet.multipart.enabled -> true 설정 시의 처리 과정
        // 스프링의 DispatcherServlet에서 MultiPartResolver를 실행
        // -> HttpRequest가 멀티파트인 경우
        // -> ServletContainer가 전달하는 일반적인 * HttpServletRequest(RequestFacade()객체가 아닌,
        // -> * MultiPartHttpServletRequest 객체로 변환해서 Controller로 반환

        // * MultiPartHttpServletRequest - HttpServletRequest 인터페이스의 자식 인터페이스
        // -> 멀티 파트와 관련된 몇 가지 부가적인 기능 추가 제공

        // * 스프링이 제공하는 기본 MultiPartResolver
        // -> MultiPartHttpServletRequest 인터페이스를 구현한 * StandardMultiPartHttpServletRequest를 반환함
        // -> *** DisPatcherServlet의 doDispatch()를 살펴보면, checkMultipart()에서 해당 resolver를 호출
        // -> resolve과정을 통해 request를 StandardMultiPartHttpServletRequest로 가공해 반환함
        // -> *** Controller에서는 HttpServletRequest대신 MultiPartHttpServletREquest를 파라미터로 받아 메서드 사용 가능

        log.info("request = {}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName = {}", itemName);

        //enctype = "multipart/form-data"
        //upload-form.html의 form 내에서, 서로 다른 type의 input을 처리
        Collection<Part> parts = request.getParts();
        log.info("parts = {}", parts);

        return "upload-form";
    }
}
