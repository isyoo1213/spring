package hello.itemservice.message;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
//SpringBoot로 설정해 자동으로 message관련 설정을 불러서 등록할 수 있도록 하기 위함
class MessageSourceTest {

    @Autowired//필드 주입 - SpringBoot가 제공하는 DI를 제공받으므로 가능? 원래 고립된 순수한 단위테스트에서는 불가능
    MessageSource messageSource;

    //기본적으로 스프링은 HttpRequest의 Accept 헤더에 따라 Locale을 설정하도록 설정됨 by localeResolver(인터페이스)
    //-> 로케일 선택 방식을 바꾸고 싶다면 구현체를 변경해주면 됨 ex) 쿠키나 세션

    @Test
    void helloMessage(){
        //오류 발생시 - Settings - File Encodings - Properties files - UTF-8인코딩 설정
        String result = messageSource.getMessage("hello", null, null);
        //3번째 argument인 locale 정보가 없을 경우, basename에 설정한 기본 이름의 message 설정 파일을 조회 - messages.properties
        //2번째 인자는 arguments - .properties 파일의 {} 처리된 부분을 사용하는 인자
        assertThat(result).isEqualTo("안녕");
    }

    @Test //설정파일에 설정된 값이 없을 경우 -> 예외발생
    void notFoundMessageCode(){
        assertThatThrownBy(()->messageSource.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test // 값이 없을 경우 '기본 메시지'를 통해 예외 피하기
    void notFoundMessageCodeDefaultMessagge(){
        //defaultMessage 인자 추가
        String result = messageSource.getMessage("no_code", null, "기본 메시지", null);
        assertThat(result).isEqualTo("기본 메시지");
    }

    @Test
    void argumentMessage(){
        //2번째 인자 - 설정 파일의 {}부분에 치환해서 들어감 -> Object[] 배열 타입
        String result = messageSource.getMessage("hello.name", new Object[]{"spring"}, null);
        assertThat(result).isEqualTo("안녕 spring");
    }

    @Test
    void defaultLang(){
        assertThat(messageSource.getMessage("hello", null, null)).isEqualTo("안녕");
        assertThat(messageSource.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
    }

    @Test
    void enLang(){
        assertThat(messageSource.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }

}
