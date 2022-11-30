package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.*;

class MessageCodesResolverTest {

    //MessageCodesResolver는 인터페이스
    //인자1 - 사용할 errorCode / 인자2 - ObjectName / return - String[] (여러 메시지코드)
    // 구현체는 DefaultMessageCodesResolver
    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject(){ //ObjectError() 생성 시 ObjectName만 주어졌을 때 - 객체 오류
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
        //즉, messageCodesResolver는 FieldError나 ObjectError의 2번째 생성자 파라미터인 errorCodes의 String 배열을 만들어주는 역할
        // + 순서는 디테일한 것이 우선 ex) {"required.item", "required"}

        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodesResolverField(){ //FieldError() 생성 시 ObjectName과 FieldName, type이 주어졌을 때 - 필드 오류
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
/*      결과 - 4가지
        messageCode = required.item.itemName
        messageCode = required.itemName
        messageCode = required.java.lang.String //필수 타입에 관련된 것
        messageCode = required
*/
        // *** bindingResult.rejectValue("itemName", "required"); 에서도 messageCodesResolver를 사용
        // - rejectValue()에서 codesResolver.resolverMessageCodes 호출 -> 4가지 메시지 코드가 생성됨
        // -> new FieldError("item", "itemName", null, false, **** codes <- messageCodes, null, null);
        // 즉, FieldError() 생성 시, codes에 messageCodesResolver가 생성한 4가지 String배열을 전달함
        // **** 이는 bindingResult의 log를 통해서도 확인 가능함

        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
    }
}
