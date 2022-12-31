package spring.exception.exHandler.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.exception.exHandler.ErrorResult;
import spring.exception.exception.UserException;

@Slf4j
@RestControllerAdvice(basePackages = "spring.exception.api") //@ControllerAdvice + @ResponseBody
// * @ControllerAdvice - 여러 Controller에서 발생한 Exception들을 모아서 처리해주는 기능 (Rest는 @ResponseBody가 붙는지의 여부)
// * -> 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler, @InitBinder의 기능을 부여해주는 기능
// + @ControllerAdvice에 대상을 지정해주지 않는 경우 -> 모든 Controller에 적용
// * 대상 Controller 지정 방법 by 공식문서
// 1. @ControllerAdvice(annotations = RestController.class) - 특정 Annotation이 붙은 Controller에만 적용
// 2. @ControllerAdvice("org.example.controllers") - 특정 Package 및 하위
// 3. @ControllerAdvice(assinableTypes = {ControllerInterface.class, AbstractController.class} - 부모 타입, 특정 클래스
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)

    public ErrorResult illegalExceptionHandler(IllegalArgumentException e){
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler]", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());

        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e){
        log.error("[exceptionHandler]", e);

        return new ErrorResult("EX", "내부 오류");
    }

}
