package spring.springmvc.basic.requestmapping.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {

    //기존 HttpServletRequest, response 파라미터 + inputStream을 통한 요청 처리 방법
    @PostMapping("/request-body-string-v1")// *** 최신 기술에선 GET메서드에도 Body에 데이터 넣을 수 있으나 그렇게 사용하지 않음
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream(); // *** stream은 바이트코드다!! 항상 인코딩 신경써야함
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);

        response.getWriter().write("ok");
    } //단점 - HttpServlet 객체를 통으로 받을 필요가 없다

    //InputStream, Writer(OutputStream) 객체를 파라미터로
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);

        responseWriter.write("ok");
    }

    // ***** HttpEntity 객체를 파라미터로
    //MVC내부에서 HTTP message Body를 읽어서 '문자'나 '객체'로 변환해주는 HttpMessageConverter가 작동
    //HttpEntity는 Http Spec(request, response 모두 포함)을 객체화 해놓은 것과 비슷 -> 요청과 응답에 모두 사용 가능
    //*** 요청 파라미터를 조회하는 기능(@RequestParam, @ModelAttribute)과는 전혀 상관 없음
    //@RequestBody로 치환 가능 - 다음 V4에서 다룸
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {
        //HttpMessageConverter가 request Body에 실린 Data를 자동으로 처리
        //String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        String messageBody = httpEntity.getBody(); //HttpEntity가 설정한 자료형으로 변환된 Request Body 데이터를 꺼낼 수 있음
        //getHeaders()

        log.info("messageBody = {}", messageBody);

        //***** 현재 클래스가 @Controller (not @RestController), 메서드에 @RequestBody 어노테이션이 붙어있지 않음
        // -> View 조회와는 관련이 없다 + 응답의 경우 Body와 Header에 작용
        return new HttpEntity<String>("ok"); // Response Body에 실을 data는 String이 Default
    }

    //HttpEntity를 상속받는 RequestEntity + ResponseEntity
    @PostMapping("/request-body-string-v3-plus")
    public HttpEntity<String> requestBodyStringV3Plus(RequestEntity<String> requestEntity) throws IOException {

        //RequestEntity - HttpMethod(getMethod()), url정보(getUrl()) 추가해서 사용 가능
        String messageBody = requestEntity.getBody(); //HttpEntity가 설정한 자료형으로 변환된 Request Body 데이터를 꺼낼 수 있음

        log.info("messageBody = {}", messageBody);

        //ResponseEntity - HTTP 상태코드(HttpStatus.xx) 가능
        return new ResponseEntity<String>("ok", HttpStatus.OK); //기존 메시지 body는 첫 파라미터로 - String이 Default
    }

    //@RequestBody를 파라미터로 이용 + @ResponseBody로 바로 응답
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) throws IOException {

        //@RequestBody
        //HttpEntity에서 값을 꺼내와 할당하는 것까지 생략 가능
        //헤더 정보가 필요할 경우, parameter에 @RequestHeader를 통해 받아올 수 있음
        //**** @RequestParam, @ModelAttribute와는 상관 없음
        //String messageBody = requestEntity.getBody();

        log.info("messageBody = {}", messageBody);

        //@ResponseBody
        //url이 매핑된 메서드의 Return 타입에 따라 Response Message의 Body에 그냥 그대로 실어서 응답
        //return new HttpEntity<String>("ok");
        return "ok";
    }

}
