package spring.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.springmvc.basic.ModelData;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * {"username":"hello", "age":20}
 * content-type: application/json
 */
@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    //기존 HttpServletReuqest를 이용한 json Body 요청 처리
    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);

        //request Body -> Data 클래스 - 요청처리
        //요청 메시지 Body의 내용을 Data 클래스로 매핑
        ModelData modelData = objectMapper.readValue(messageBody, ModelData.class);

        //Data 클래스 -> request Body - 응답처리
        //cf) objectMapper.writeValueAsString(modelData) - 응답처리 시 Data클래스의 객체를 Json으로 변환하는 것
        log.info("username={}, age={}", modelData.getUsername(), modelData.getAge());

        response.getWriter().write("ok");
    }

    /**
     * @RequestBody
     * HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     *
     * @ResponseBody
     * - 모든 메서드에 @ResponseBody 적용
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    //@RequestBody로 문자 바로 받아오기 + @ResponseBody로 문자 바로 응답하기
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        //Request Message Body의 Data를 문자로 바로 받아오기 by @RequestBody
        //ServletInputStream inputStream = request.getInputStream();
        //String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);

        ModelData modelData = objectMapper.readValue(messageBody, ModelData.class);

        log.info("username={}, age={}", modelData.getUsername(), modelData.getAge());

        return "ok";
    }

    /**
     * @RequestBody 생략 불가능(@ModelAttribute 가 적용되어 버림)
     * HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter (contenttype:
    application/json)
     *
     */
    //@RequestBody에 Data 클래스 '객체'를 Parameter로 사용해, Body 데이터를 바로 받아오기
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody ModelData modelData) {
        // ******** @RequestBody 생략시 -> @ModelAttritute가 생략된 것으로 간주 -> Body내에서 쿼리스트링 찾음
        //-> ModelData의 초기화 값인 username=null, age=0이 담김
        // 즉, Request Message의 Body가 아닌 '요청 파라미터'를 처리하게 됨

        //Request Message Body의 Data를 문자로 바로 받아오기 by @RequestBody
        //ServletInputStream inputStream = request.getInputStream();
        //String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("ModelData = {}", modelData);

        //HttpEntity의 자료형을 Data 클래스 객체로 지정해 Parameter로 사용
        //HttpMessageConverter가 Body의 데이터를 문자로 변환 + Data 클래스에 매핑
        //문자 처리한 Request Message의 Body를 Data 클래스로 바로 매핑하면서 objectMapper 사용 X
        //ModelData modelData = objectMapper.readValue(messageBody, ModelData.class);

        // *** 즉, @RequestBody의 StringHttpMessageConverter
        // -> text 타입의 Body Data -> String 객체로 컨버팅
        // *** MappingJackson2HttpMessageConverter
        // -> application/json 타입의 Body Data -> Data 클래스 객체로 컨버팅

        log.info("username={}, age={}", modelData.getUsername(), modelData.getAge());

        return "ok";
    }

    //V3의 HttpEntity 버전
    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<ModelData> modelData) {

        //HttpEntity에는 Header정보도 포함되어 있음
        log.info("ModelData = {}", modelData);

        //getBody()로 접근 후 Data 꺼내오기
        log.info("username={}, age={}", modelData.getBody().getUsername(), modelData.getBody().getAge());

        return "ok";
    }

    /** * @RequestBody 생략 불가능(@ModelAttribute 가 적용되어 버림)
     * HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter (contenttype:
     application/json)
     *
     * @ResponseBody 적용
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter 적용
    (Accept: application/json) ***
     */
    //V3,4의 반환 Type 설정 버전 - HttpEntity 사용 가능
    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public ModelData requestBodyJsonV5(@RequestBody ModelData modelData) {

        log.info("ModelData = {}", modelData);

        log.info("username={}, age={}", modelData.getUsername(), modelData.getAge());

        //Request Body의 Data가 매핑된 ModelData가 Converter에 의해 Json형태로 컨버팅 된 후 반환 by @ResponseBody
        return modelData;
    }

    //가장 중요한 핵심개념
    //@RequestBody 요청
    //JSON 요청 -> HTTP 메시지 컨버터 -> 객체
    //@ResponseBody 응답
    //객체 -> HTTP 메시지 컨버터 -> JSON 응답
}
