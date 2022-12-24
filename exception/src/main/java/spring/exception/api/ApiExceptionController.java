package spring.exception.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

        return new MemberDTO(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDTO{
        private String memberId;
        private String name;
    }
}
