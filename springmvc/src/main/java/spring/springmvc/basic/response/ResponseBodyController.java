package spring.springmvc.basic.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import spring.springmvc.basic.ModelData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//@Controller
//@ResponseBody
// ***** ResponseEntity를 사용하는 곳에 중복해서 사용해도 상관없음 - Response Message Body에 내용을 실어보내는 원리는 동일
@RestController//@Controller + @ResponseBody
public class ResponseBodyController {
    // ***** Html이나 View Template을 반환하더라도 결국 Response Message Body에 해당 파일이나 정보가 담기는 것은 동일
    // + ViewTemplate도 결국 Html을 생성해서 반환

    //HttpServletResponse + Writer의 기본적인 방법
    @GetMapping("response-body-string-v1")
    public void responseBodyStringV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok");
    }

    //ResponseEntity, HttpEntity 사용
    @GetMapping("response-body-string-v2")
    public ResponseEntity<String> responseBodyStringV2() {//Writer 안쓰므로 IOException 처리 X
        return new ResponseEntity<String>("ok", HttpStatus.OK);
    }

    //@ResponseBody 사용
    //과정
    //MVC내부에서 '문자'나 '객체'를 HTTP message Body에 적합한 형태로 변환해주는 HttpMessageConverter가 작동
    //HttpEntity는 Http Spec(request, response 모두 포함)을 객체화 해놓은 것과 비슷 -> 요청과 응답에 모두 사용 가능
    //Response Body에 실을 값을 HttpEntity 자료형으로 생성해 할당하는 과정 생략 가능
    //url이 매핑된 메서드의 Return 타입에 따라 Response Message의 Body에 그냥 그대로 실어서 응답
    //@ResponseBody
    @GetMapping("response-body-string-v3")
    public String responseBodyStringV3() {
        return "ok";
    }

    // JSON 응답하기

    //@ResponseEntity (요청 때처럼 Body 받는 Stream처리, 클래스 매핑과정이 필요없어서 더욱 단순)
    @GetMapping("/response-body-json-v1")
    public ResponseEntity<ModelData> responseBodyJsonV1(){ //HttpEntity<ModelData>로도 반환 가능(부모 자료형으로 캐스팅)
        ModelData modelData = new ModelData();
        modelData.setUsername("userA");
        modelData.setAge(20);

        return new ResponseEntity<>(modelData, HttpStatus.OK);
        //ResponseEntity의 첫 생성자 파라미터는 Body
    }

    //@ResponseBody + @ResponseStatus - ResponseEntity<>의 축약형
    //***** 컨트롤러 메서드 내부 로직에 따라 ResponseStatus를 다르게 '동적으로' 설정하고 싶을 경우에는 ResponseEntity 사용해야함
    @ResponseStatus(HttpStatus.OK)
    //@ResponseBody
    @GetMapping("/response-body-json-v2")
    public ModelData responseBodyJsonV2(){
        ModelData modelData = new ModelData();
        modelData.setUsername("userA");
        modelData.setAge(20);

        return modelData;
        //HttpEntity의 자식인 ResponseEntity에서 제공하는 HttpStatus 활용에 제약이 생김 -> @ResponseStatus 어노테이션 사용
    }
}
