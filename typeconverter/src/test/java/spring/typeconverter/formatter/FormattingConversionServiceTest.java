package spring.typeconverter.formatter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;
import spring.typeconverter.converter.IpPortToStringConverter;
import spring.typeconverter.converter.StringToIpPortConverter;
import spring.typeconverter.type.IpPort;

import static org.assertj.core.api.Assertions.*;

class FormattingConversionServiceTest {

    @Test
    void formattingConversionService(){
        // * FormattingConversionService + 통화, 숫자 등등이 추가된 버젼 -> DefaultFormattingService
        // * 스프링은 WebConversionService가 DefaultFormattingService를 상속받아 내부적으로 사용함

        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

        //컨버터 등록 - * 가능한 이유 - 부모 따라가다 보면 ConversionService 구현하고 있음
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        //포맷터 등록
        conversionService.addFormatter(new MyNumberFormatter());

        //컨버터 사용
        IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));

        //포맷터 사용 - convert() 메서드 그대로 사용
        String result = conversionService.convert(1000, String.class);
        assertThat(result).isEqualTo("1,000");

        Number number = conversionService.convert("1,000", Long.class);
        assertThat(number).isEqualTo(1000L);

    }
}
