package spring.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());

    //url을 통한 매핑은 복수로도 가능 + 실질적 처리는 [] 배열로 처리
    @RequestMapping(value = {"/springMVC-basic", "/springMVC-go"})
    // *** 매핑 - /springMVC-basic
    // *** url 요청 - /springMVC-basic or /springMVC-basic/
    // **** 실제로 두 개는 다른 url이지만, 편의적으로 같은 url 요청으로 매핑 처리해줌
    public String springMVCBasic(){
        log.info("springMVCBasic");
        return "OK";
    }

    @RequestMapping(value = "mapping-get-v1", method = RequestMethod.GET)
    public String mappingGetV1(){
        log.info("mapping-get-v1");
        return "OK";
    }

    //축약 어노테이션
    @GetMapping(value = "mapping-get-v2")
    public String mappingGetV2(){
        log.info("mapping-get-v2");
        return "OK";
    }

    /**
     * PathVariable 사용
     * Parameter와 변수명이 같으면 생략 가능
     * @PathVariable("userId") String userId -> @PathVariable userId
     */
    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data) {
        log.info("mappingPath userId={}", data);
        return "ok";
    }

    /**
     * PathVariable 사용 다중
     */
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long
            orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }

    /**
     * 파라미터로 추가 매핑 - url + ( ? + 파라미터 ) 로 올 경우에만 응답
     * params="mode",
     * params="!mode"
     * params="mode=debug"
     * params="mode!=debug" (! = )
     * params = {"mode=debug","data=good"}
     */
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    // *** params는 쿼리 스트링처럼 url + ?params 형식으로 붙음
    // -> params는 쿼리 스트링처럼 request message의 Body에 실려옴
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }

    /**
     * 특정 헤더로 추가 매핑
     * headers="mode",
     * headers="!mode"
     * headers="mode=debug"
     * headers="mode!=debug" (! = )
     */
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    //request message에 특정 header를 꼭 첨부해서 보내야 응답
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }

    /**
     * Content-Type 헤더 기반 추가 매핑 Media Type
     * consumes="application/json" * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     */
    //MediaType.APPLICATION_JSON_VALUE  - 클릭해서 들어가보면 "application/json"이 나옴
    //즉, Spring이 정의해놓은 Content-type 종류가 모두 치환되어 제공됨
    @PostMapping(value = "/mapping-consume", consumes = "application/json")
    //ex) Post 메서드 + ContentType에 따라 처리를 다르게 해주고 싶을 때 사용하면 유용
    // -> *** header를 통한 ContentType이 아니라, Spring 내부적으로 처리하는 양식에 맞추기 위해 consumes를 사용
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }

    /**
     * Accept 헤더 기반 Media Type
     * produces = "text/html"
     * produces = "!text/html"
     * produces = "text/*"
     * produces = "*\/*"
     */
    @PostMapping(value = "/mapping-produce", produces = "text/html")
    //Accept 헤더 - Request message의 header 종류 + Response로 기대하는 Content-Type(MIME)을 명시
    //MIME - 바이너리 -> 텍스트 : Encoding  / 텍스트 -> 바이너리 : Decoding
    //기존의 ASCII 표준에 따른 '텍스트'로만 파일을 주고받다, 음악, 영상, 워드등 '바이너리'파일을 전송하기 위해 '텍스트'로 'Encoding'함
    //-> 이렇게 바이너리를 텍스트로 Encoding했다는 파일 정보임을 알려주는 것이 MIME이고, Content-type정보를 파일 내부에 가지고 있음
    //-> Conetent-type의 값으로 사용됨
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    } //즉, produces는 Controller에서 제공할 response의 content-type을 미리 명시함으로써, request의 accept와 일치할 경우에만 응답
    //즉, Controller입장에서 response의 content-type을 '제공'하는 의미
    // cf) consumes는 Controller입장에서 request의 Content-type을 '소비'하는 의미

}
