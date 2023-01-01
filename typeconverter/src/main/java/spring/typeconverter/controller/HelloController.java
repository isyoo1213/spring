package spring.typeconverter.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

        return "ok";
    }
}
