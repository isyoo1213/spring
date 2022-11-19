package spring.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Slf4j
@RestController
public class RequestHeaderController {

    //RequestMapping 컨트롤러 내의 Method들은 인터페이스로 제약받지 않으므로, 스프링의 다양한 parameter를 받아들일 수 있음
    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod httpMethod,
                          Locale locale, //Locale의 경우, 스프링의 LocaleResolver를 통해 우선순위 잡아줄 수 있음
                          @RequestHeader MultiValueMap<String, String> headerMap, //모든헤더
                          @RequestHeader("host") String host, //특정헤더 name 매핑
                          @CookieValue(value = "myCookie", required = false) String cookie
                          //required는 false가 default + defaultValue설정도 가능
                          ){
        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        //MultiValueMap - Header의 경우 하나의 key에 여러 value가 들어올 수 있음 (Servlet에서도 이를 처리하는 방법 있었음)
        //-> LinkedMultiValueMap()으로 생성하는 MultiValueMap을 통해 생성됨
        //-> value가 여러개일 경우, key를 조회할 경우 배열이 반환됨
        log.info("header host={}", host);
        log.info("myCookie={}", cookie);
        return "ok";
    }
    // RequestMapping handler의 컨트롤러가 Request message로부터 사용할 수 있는 parameter모음
    // https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-methods
    // RequestMapping handler의 컨트롤러가 Response message로 응답할 수 있는 Type모음
    // https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-return-types
}
