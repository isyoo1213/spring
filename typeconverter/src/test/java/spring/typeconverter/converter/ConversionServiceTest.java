package spring.typeconverter.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;
import spring.typeconverter.type.IpPort;

import static org.assertj.core.api.Assertions.*;

public class ConversionServiceTest {

    @Test
    void conversionService(){
        // DefaultConversionService 등록 후, Converter 추가시키는 방식
        // -> *** 등록과 사용의 분리 -> 사용 시에는 내부에 숨은 컨버터 알 필요 없음 -> *** ConverterService 인터페이스에만 의존
        // -> 이후 필요한 곳에서 주입받아서 사용하는 방식으로 구성 가능

        // * DefaultConversionService - 부모를 거슬러올라가면, ConversionService + 인터페이스의 구현체
        // - * 사용하는 입장 - ConversionService - canConvert(), convert() 메서드
        // - * 등록, 관리하는 입장 - ConverterRegistry - addConverter().. 메서드
        // -> *** 관심사 분리 -> ISP 인터페이스 분리 원칙 -> 즉, 사용하는 입장의 클라이언트는 ConversionService 인터페이스 기능만 의존가능
        DefaultConversionService conversionService = new DefaultConversionService();

        //converter 추가
        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        //사용
        Integer result = conversionService.convert("10", Integer.class);
        System.out.println("result = " + result);
        //로그 - [Test worker] INFO spring.typeconverter.converter.StringToIntegerConverter - conver source = 10
        // -> *** ConversionService가 각각의 컨버터가 구현하는 Converter interface의 Generic을 파악해 적용

        //검증
        assertThat(result).isEqualTo(10);
        assertThat(conversionService.convert(10, String.class)).isEqualTo("10");
        assertThat(conversionService.convert("127.0.0.1:8080", IpPort.class))
                .isEqualTo(new IpPort("127.0.0.1", 8080));
        assertThat(conversionService.convert(new IpPort("127.0.0.1", 8080), String.class))
                .isEqualTo("127.0.0.1:8080");

    }
}
