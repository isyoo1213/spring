package spring.exception.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import spring.exception.exception.BadRequestException;
import spring.exception.exception.UserException;

@Slf4j
@RestController //API형식이므로 Controller에서 return시 Body에 직접 Data를 반환할 수 있도록 RestController 사용
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDTO getmember(@PathVariable("id") String id){
        if(id.equals("ex")){
            throw new RuntimeException("잘못된 사용자입니다.");
            // *** 다른 처리를 해주지 않은 경우, 오류가 발생하면 ErrorPage에 등록한 흐름대로 처리 (500Error)
            // -> ErrorPageController도 JSON 응답을 할 수 있도록 구성해주어야 함
        }

        // * 스프링부트의 BasicErrorController를 활용한 JSON응답의 기본 오류 처리
        // -> WAS, ServletContrainer 입장에서는 내부적인 오류 발생으로 500 에러로 인식
        // -> * but, 사용자의 입력으로 인한 오류 -> 그렇다면 이것을 변경하고 싶다면?
        // -> HandlerExceptionResolver 사용 - 컨트롤러 밖으로 던져진 예외를 해결하고 동작을 정의할 수 있음
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못 입력된 값입니다.");
        }

        // * 직접 만든 Exception 사용하기
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류입니다.");
            // 다른 처리 안해주면 500 Error by BasicErrorController
            // 정상 응답이 아니므로 resolver, Interceptor 외에 BasicErrorController호출 후 dispatcherServlet에서 오류도 발생
        }

        return new MemberDTO(id, "hello " + id);
    }

    //@ResponseStatus + ResponseStatusExceptionResolver 사용하기
    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1(){
        throw new BadRequestException();
    }

    // * ResponseStatusException 예외 + ** ResponseStatusExceptionResolver 사용하기
    //-> @ResponseStatus는 어노테이션이므로 조건에 따른 동적인 로직 구성이나 이미 정의되어 변경하기 어려운 예외에는 사욯에 제한
    //-> 사용자가 만든 예외에 적합함 -> 그렇지 않은 경우에는 ResponseStatusException 예외를 사용 - 부모가 RuntimeException 상속
    // * 상태코드 지정, 에러 메시지, 지정해줄 상태코드
    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2(){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
        //NOT_FOUND는 404 에러 + 3번 째 argument는 실제 Exception
    }

    // * DefaultHandlerExceptionResolver 사용하기 - 스프링 내부의 오류를 500이 아닌 다른 오류로 처리해주는 기능
    // -> 살펴보면 여러 Exception에 대한 doResolve()메서드가 정의됨 + 내부적 오류를 최대한 http 스펙에 맞는 상태코드로 반환해줌
    @GetMapping("/api/default-handler-ex")
    public String defaultException(@RequestParam Integer data) {
        //다른 처리해주지 않을 경우, Binding 에러 발생 시 400Error BadRequest 발생
        //Exception 종류 - exception": "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException"
        // -> DefaultHandlerExceptionResolver에서 *** TypeMismatchException 인스턴스 확인 후 처리
        return "ok";
    }

    @Data
    @AllArgsConstructor
    static class MemberDTO {
        private String memberId;
        private String name;
    }
}
