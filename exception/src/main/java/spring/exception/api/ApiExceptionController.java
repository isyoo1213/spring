package spring.exception.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
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

    @Data
    @AllArgsConstructor
    static class MemberDTO{
        private String memberId;
        private String name;
    }
}
