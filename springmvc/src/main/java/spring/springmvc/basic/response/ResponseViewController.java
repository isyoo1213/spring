package spring.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

//Thymeleaf 라이브러리 사용시 Default로 잡히는 설정
// spring.thymeleaf.prefix=classpath:/templates/
// spring.thymeleaf.suffix=.html

@Controller
public class ResponseViewController {
    //개발자 도구 열어서 text로 body 응답하는지 html로 응답하는지까지 확인

    //ModelAndView로 View의 논리적 이름 처리 by viewResolver
    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1(){
        ModelAndView mav = new ModelAndView("response/hello")
                .addObject("data", "hello!");

        return mav;
    }

    //매서드 String 반환으로 View의 논리적 이름 처리 by viewResolver
    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model){
        model.addAttribute("data", "hello!");

        return "response/hello.html"; // .html 생략가능 by Thymeleaf
    }

    //Controller의 경로를 Spring에서 view 논리적 이름으로 해석 - 불명확하므로 지양할 것
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model){
        //조건
        //1. @Controller 사용 - viewResolver 호출 가능해야함
        //2. HttpServletRequest, OutputStream(Writer)와 같이 HTTP message Body를 처리하는 'Parameter'가 없을 경우

        model.addAttribute("data", "hello!");
    }
}
