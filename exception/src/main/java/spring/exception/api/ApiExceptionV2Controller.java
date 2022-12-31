package spring.exception.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.exception.exHandler.ErrorResult;
import spring.exception.exception.UserException;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

/* // exHandler.advice의 ExControllerAdvice로 분리 -> 이제 이 Controller에서는 Exception 처리에 관한 메서드가 없음

    //다른 처리없이 Exception 발생하는 요청시, BasicErrorController에서 자동으로 만든 오류페이지가 sendError()로 Body로 반환됨
    // -> @ExceptionHandler사용 -> Exception 종류에 따른 resolver내의 handler를 간단하게 구성할 수 있도록 해주는 어노테이션

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    // *** 컨트롤러 내에서 해당 Exception 발생 시, 어노테이션을 적용한 메서드가 잡아냄
    // -> 컨트롤러가 @RestController이거나, @ResponseBody가 붙은 메서드일 경우, return하는 객체를 그대로 JSON으로 파싱해서 return함
    // + 그냥 @Controller일 경우, View name을 return 가능함 --> Controller에서 parameter로 받거나, return하는 것들 대부분 사용 가능
    // + *** ResponseEntity 반환시 조건에 따른 동적인 구성도 가능
    // * 순서
    // Controller의 Exception -> DispatcherServlet의 ExceptionResolver 확인 -> 1순위 *** ExceptionHandlerExceptionResolver
    // * ExceptionHandlerExceptionResolver 의 기능
    // - @ExceptionHandler 어노테이션 존재여부 확인 -> 존재하면 어노테이션이 붙은 메서드 호출
    // *** resolver를 통하므로 '정상 흐름' -> httpStatus 상태코드는 200OK로 응답 -> *** @ResponseStatus로 핸들링
    public ErrorResult illegalExceptionHandler(IllegalArgumentException e){
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    //ResponseEntity에 ErrorResult와 HttpStatus 담기
    @ExceptionHandler//(UserException.class) -> 생략하더라도 parameter에서 정의해주면 인식함
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler]", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    //위의 ExceptionHandler에서 처리하는 exception 종류 외의 모든 Exception들을 처리하는 핸들러 by Exception
    // - ExceptionHandler에서 처리하는 Exception들은 자식 오류까지 모두 캐치함 -> 거의 최상위 부모인 Exception을 통해 나머지 잡음
    // -> *** 더 디테일한 자식 exceptionHandler가 우선권을 가진다
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e){
        log.error("[exceptionHandler]", e);

        return new ErrorResult("EX", "내부 오류");
    }

*/
    @GetMapping("/api2/members/{id}")
    public MemberDTO getMember(@PathVariable("id") String id) {

        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자입니다.");
        }

        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력값입니다.");
        }

        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류입니다.");
        }

        return new MemberDTO(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDTO {
        private String memberId;
        private String name;
    }
}
