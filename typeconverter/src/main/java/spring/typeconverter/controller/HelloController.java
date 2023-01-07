package spring.typeconverter.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.typeconverter.type.IpPort;

@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {

        String data = request.getParameter("data"); //String type으로 조회
        Integer intVaue = Integer.valueOf(data); //숫자 type으로 변경해주어야 함
        System.out.println("intVaue = " + intVaue);

        return "ok";
    }

    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) {

        //여기에서 출력되는 data는 String type이 아닌 Integer Type
        // -> by Spring + @ModelAttribute, @PathVariable, View Rendering 또한 Spring이 type 변환해줌
        System.out.println("data = " + data);

        //WebConfig의 addFormatter()에 Converter 등록 후에는, 작동하는 Converter의 로그가 찍힘
        // *** StringToIntegerConverter를 등록하지 않아도 정상적으로 작동함
        // -> 스프링이 내부적으로 제공하는 수많은 기본 컨버터들이 작동 -> *** 추가한 컨버터가 우선권을 가짐

        // * Formatter 적용 후 - http://localhost:8080/hello-v2?data=1,000 -> data = 1000 콘솔 출력
        return "ok";
    }

    //직접 만든 IpPort 객체를 컨버팅하기
    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort) {

        // *** 처리 과정
        // @RequestParam은 이를 처리하는 ArgumentResolver인 * RequestParamMethodArgumentResolver 에서 ConversionService 사용
        // -> 내부적으로 상속관계가 복잡하므로, 확인이 필요한 경우에는 IpPortConverter에 디버그 포인트 설정 후 살펴보기
        System.out.println("IpPort IP = " + ipPort.getIp());
        System.out.println("ipPort Port = " + ipPort.getPort());

        return "ok";
    }
}
