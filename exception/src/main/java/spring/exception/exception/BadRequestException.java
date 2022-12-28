package spring.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// * @ResponseStatus 와 ResponseStatusExceptionResolver
// - 어노테이션을 통해 상태코드를 지정하면, 해당 예외 발생 시 *** ResponseStatusExceptionResolver 리졸버를 호출
// ResponseStatusExceptionResolver - 부모 클래스가 ExceptionHandlerResolver를 상속받고 있으므로 이를 구현
// -> 어노테이션이 있는지 확인 후, 설정한 code와 reason을 sendError()를 통해 처리 + ModelAndView를 return해줌
// -> 결국 sendError()를 호출했으므로 WAS에서 내부적으로 재호출이 들어감
// + MessageSource에서 reason을 찾아 코드화해서 JSON으로 출력하는 것도 가능
// - application.properties에서 server.error.include-message=always 설정 필요

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad") //"잘못된 요청 오류입니다."
//"error.bad" 라는 message key (messages.properties에 설정)를 사용하는 MessageSource 사용 가능
// -> resolver 살펴보면 reason을 받아와서 messageSource에서 찾아보는 로직 포함 - 못찾으면 defaultMessage
public class BadRequestException extends RuntimeException{

}
