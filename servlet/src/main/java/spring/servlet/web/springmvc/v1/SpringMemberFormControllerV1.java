package spring.servlet.web.springmvc.v1;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller //@ComponentScan의 Bean등록 대상 + *** Spring MVC에서 annotation 기반 Controller(handler)로 인식
//마치, V3~V4의 Controller 인터페이스처럼 Controller 버전을 지정해주는 것과 같은 원리
/*
// *** 클래스 레벨에서 1. @Controller 혹은 2. @Component + @RequestMapping이 적용된 Bean을 대상으로
// 'RequestMapping'MappingHandler의 isHandler() 메서드에서 annotation 기반의 컨트롤러인지 확인 후 매핑정보를 인식
// + @Component 등록 외, 수동 빈 등록도 가능 (@Bean + return SpringMemberFormControllerV1) -> 여튼 Bean등록은 필수
@Component
@RequestMapping
*/
public class SpringMemberFormControllerV1 {

    //RequestMappingHandlerMapping에서 해당 클래스를 urlPattern과 함께 map등록 -> 이후 getHandler(url)를 통해 꺼내서 사용
    @RequestMapping("/springmvc/v1/members/new-form")
    public ModelAndView process(){
        return new ModelAndView("new-form");
        // -> jsp를 처리하기 위한 internalResourceViewResolver 객체에서 viewPath 완성 후 render()실행
    }

}
