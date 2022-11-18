package spring.springmvc.basic.requestmapping.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.springmvc.basic.ModelData;

@Slf4j
@Controller
public class RequestModelAttributeParamHandler {

    //기존 ModelData 클래스를 사용하는 방식
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(
            @RequestParam String username,
            @RequestParam int age
    ){
        ModelData modelData = new ModelData();
        modelData.setUsername(username);
        modelData.setAge(age);

        log.info("username={}, age={}", modelData.getUsername(), modelData.getAge());
        log.info("modelData={}", modelData); //ModelData의 @Data 어노테이션 내부의 @ToString 어노테이션 기능
        //modelData.toString() 메서드를 사용할 필요 없이, 그냥 인스턴스 참조값만으로 toString() 메서드의 효과 가능
        //출력 - modelData=ModelData(username=인성, age=20)

        return "ok";
    }

    //요청 파라미터 자체를 @ModelAttribute로 받기 + 객체 자동 생성 + 자동 프로퍼티 세팅
    @ResponseBody
    @RequestMapping("/model-attribute-v1-short")
    public String modelAttributeV1Short(@ModelAttribute ModelData modelData){

        log.info("username={}, age={}", modelData.getUsername(), modelData.getAge());
        log.info("modelData={}", modelData);

        //@ModelAttribute의 프로세스
        //1. ModelData 객체 생성
        //2. 요청 파라미터의 이름(쿼리 스트링의 key값)으로 객체의 *프로퍼티 탐색
        //3. 프로퍼티의 setter 호출해서 값을 * 바인딩

        // * 프로퍼티
        // 객체 내부에 setXXX(), getXXX()가 존재하면, XXX는 '프로퍼티'를 가졌다고 표현
        // 프로퍼티의 값을 변경하면 setXXX() 호출, 조회하면 getXXX() 호출

        // * 바인딩
        // 만약 age=abc로 요청하면 'BindingException' 발생 -> 검증/오류 처리가 Controller레벨에서 중요하므로 깊이 알아볼 예정

        return "ok";
    }

    //@ModelAttribute 생략
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(ModelData modelData){

        //@RequestParam도 생략가능 -> 혼란 발생 가능
        // -> 규칙
        // String, int, Integer와 같은 단순타입 -> @RequestParam
        // 나머지 -> @ModelAttribute
        // - 단, * argument resolver로 지정한 타입은 @ModelAttribute에서 제외
        // (ex) HttpServletRequest와 같은 예약어- 뒤에서 학습

        // + @ModelAttribute(name = )
        // name=? 옵션을 지정할 수 있음 (view에 따른 처리와 관련 - 뒤에서 학습)

        log.info("username={}, age={}", modelData.getUsername(), modelData.getAge());
        log.info("modelData={}", modelData);

        return "ok";
    }
    // 여기까지 GET method의 쿼리 스트링 + POST 메서드의 Html Form의 요청 파라미터에 대해서 알아봄
    // 즉, QueryString의 전달에 관한 것. 다음부터는 RequestBody에 실려오는 Json 유형의 요청 처리
    // *** POST method의 Html Form은 Http message Body에 QueryString을 전달함
}
