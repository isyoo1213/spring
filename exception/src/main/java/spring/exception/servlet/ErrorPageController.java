package spring.exception.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ErrorPageController {

    //*** Error에 관한 정보를 WAS에서 인식한 뒤, 다시 ErrorPage에 따른 Controller를 호출할 때, 단순히 호출뿐만 아니라
    // + 오류 정보를 request의 attribute에 추가해서 넘겨준다
    //RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION = "jakarta.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "jakarta.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "jakarta.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "jakarta.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "jakarta.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "jakarta.servlet.error.status_code";

    //HttpMethod 한번에 처리하려고 @RequestMapping 사용
    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404"; //View 반환
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-page/500";
    }

    //예외 발생 시 JSON 응답할 수 있도록 처리하기
    // *** MediaType은 springFramework를 사용해야 함 + 테스트시 꼭 Request에 Accept 지정하기
    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    //****** 클라이언트에서 Request Header의 Accept에 Application/json일 경우 -> produces의 타입이 같은 경우 우선순위를 가짐
    // -> 바로 위의 errorPage500()메서드 또한 같은 url을 받고있지만, JSON요청일 경우 우선순위가 밀림
    public ResponseEntity<Map<String, Object>> errorPage500Api(
            HttpServletRequest request, HttpServletResponse response){

        log.info("API errorPage 500");
        printErrorInfo(request);

        //Body에 반환할 Data 생성 및 추가
        Map<String, Object> result = new HashMap<>(); // *** HashMap은 순서를 보장하지 않음

        //WAS에서 처리해준 Exception 받기
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION); //Exceiption으로 캐스팅해서 사용 가능
        //WAS에서 ErrorPage에 따른 컨트롤러 재호출 시, request에 exception에 관한 정보를 attribute에 담아서 보내주던 것 복습

        result.put("status", request.getAttribute(ERROR_STATUS_CODE)); //이것 또한 상수로 정의되어있는 부분
        result.put("message", ex.getMessage());

        //상태코드 받기
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        //상수가 저장되어있는 RequestDispatcher에서 바로 받기 - 인터페이스 들어가보면 확인 가능

        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
    }

    private void printErrorInfo(HttpServletRequest request){
        //오류에 대한 WAS의 오류정보 처리를 확인하는 로그
        // ColumnSelection (Ctrl+Ctrl)로 작성연습
        log.info("ERROR_EXCEPTION: {}", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));

        log.info("dispatchType = {}", request.getDispatcherType());

        // *** 현재 코드 실행 시 printErrorInfo() 정보 제대로 출력되지 않는 부분 추후 확인하기
    }

}
