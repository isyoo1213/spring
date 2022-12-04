package hello.itemservice.web.validation;

import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult){
        // ***** 컨트롤러 호출은 Request Body의 Json 데이터가 BindingResult에서 ItemSaveForm 객체(정확히는 캐스팅 되지 않은 target?)에 parsing이 되는지 확인된 후 관련된 인스턴스 생성 후 실행
        // 오류시 Json을 객체화시키지 못하므로 컨트롤러 호출 자체가 안되고 예외 발생시킴
        // 따라서 오류는 2가지 1. 객체화 실패 오류 2. 검증 실패 오류 (FieldError나 ObjectError가 Json화 되어 화면 출력)

        //******* Data의 객체화 과정에서의 @ModelAttribute 와 @RequestBody의 차이
        //@ModelAttribute는 Request Parameter를 객체화할 때 필드 단위로 세분화해서 적용
        // -> 특정 필드 타입이 맞지 않더라도 나머지 필드 정상 처리 가능
        //@RequestBody의 HttpMessageConverter는 각각의 필드 단위가 아닌 전체 객체 단위
        // -> 메시지컨버터의 작동이 성공해서 객체화에 성공해야 @Validated, @Valid 사용 가능 + 이 단계에서 실패시 예외 발생


        log.info("API 컨트롤러 호출");

        if(bindingResult.hasErrors()){
            log.info("검증 오류 발생 errors = {}", bindingResult);

            return bindingResult.getAllErrors();
            //-> JSON으로 변환된 오류 내용(ObjectError, FieldError)을 반환 by @ResponseBody in @RestController
            //List 형태의 ObjectError를 반환 (FieldError도 ObjectError의 자식이므로 가능)
            //참고 - BindingResult는 Errors의 자식
        }

        log.info("성공 로직 실행");

        return form;
    }
}
