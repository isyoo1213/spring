package spring.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Controller // @Controller + 메서드 반환값 String >> ViewResolver를 위한 viewname반환
// *** viewName이 아닌 responseBody에 반환하고 싶은 경우
//1. @RestController - 클래스 레벨
//2. @ResponseBody - 메서드 레벨
public class RequestParamController {

    //1.GET으로 직접 url에 쿼리스트링
    //2.basic/spring-form.html url로 접근해서 POST로 보내보기

    //v1 - HttpServletRequest로 parameter 가져오기
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");
        //void Type의 메서드이지만, response에 write할 경우, 그냥 응답
    }

    //v2 - @RequestParam으로 가져오기
    @ResponseBody // *** 클래스 레벨에서 @Controller가 아닌 @RestController와 같은 효과 - viewName이 아닌 Body응답
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge //자료형 변환은 Spring이 알아서 해줌
    ){
        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }

    //v3 - @RequestParam 축약하기 (요청 파라미터 이름(QueryString의 key)과 메서드 parameter 이름이 같을 경우에만 가능)
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age
    ){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    //v4 - @RequestParam 생략하기 - 메서드 parameter의 Type이 단순할 경우 ex) String, int, Integer 등
    //but, 어노테이션을 생략하는 것은 요청파라미터 인지와 실수의 여지
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    //@Requestparam의 필수 요청 파라미터 설정
    //(requried = true)가 Default
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            // *** 'username=' -> null이 아닌 ''공백으로 값이 전달되므로, 요청 파라미터를 쓰는 것과 동일함
            @RequestParam(required = false) Integer age){
        // *** 현재 age가 int로 캐스팅되어 들어오므로, 생략시 null 불가로 오류발생
        // 1. Integer로 바꿔서 테스트
        // 2. DefaultValue로 세팅
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    //@Requestparam의 DefaultValue 설정
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age){
        // *** 현재 age가 int로 캐스팅되어 들어오므로, 생략시 null 불가로 오류발생
        // 1. Integer로 바꿔서 테스트
        // 2. DefaultValue로 세팅 - *** int형으로 캐스팅하기 전의 값을 세팅하므로 String으로 설정해야함
        // + int에 null 할당 전에 defaultValue가 할당되어 오류없이 응답 가능
        // + DefaultValue가 세팅되면 어떻게든 값이 들어가므로 required 설정이 불필요함
        // + *** ''처럼 빈 문자로 쿼리스트링이 넘어올 경우 > 빈문자 그대로 값을 받는 것이 아닌 defaultValue로 처리
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    //@Requestparam의 Map구조의 parameter 설정
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(
            @RequestParam Map<String, Object> paramMap
            //null로 들어오는 값일 경우, paramMap이 객체형이므로 개별 요청파라미터 자료형 상관없이 그대로 null 담아서 출력
    ){
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

    //@Requestparam의 필수 요청 파라미터 설정 + MultiValueMap 설정
    @ResponseBody
    @RequestMapping("/request-param-multiValueMap")
    public String requestParamMultiValueMap(
            @RequestParam MultiValueMap<String, Object> paramMultiValueMap
            //null로 들어오는 값일 경우 -> null객체 그대로
            //1개 이상의 값이 들어오는 경우 -> 1개더라도 배열로 출력
    ){
        log.info("username={}, age={}", paramMultiValueMap.get("username"), paramMultiValueMap.get("age"));
        return "ok";
    }

    //POST 메서드의 application/json의 request body + QueryParameter 조합이 가능한지 확인 -- 결과 : OK
    @RequestMapping("/request-param-v2-test")
    public void requestParamV2BodyWithQuery(
            @RequestParam("username") String username,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        log.info("username={}", username);
        ServletInputStream data = request.getInputStream();
        String messageBody = StreamUtils.copyToString(data, StandardCharsets.UTF_8);
        response.getWriter().write(messageBody);
    }
}
