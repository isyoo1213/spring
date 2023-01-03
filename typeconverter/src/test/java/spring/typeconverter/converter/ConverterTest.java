package spring.typeconverter.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import spring.typeconverter.type.IpPort;

import static org.assertj.core.api.Assertions.*;

class ConverterTest {

    @Test
    void stringToInteger() {
        StringToIntegerConverter converter = new StringToIntegerConverter();
        Integer result = converter.convert("10");

        assertThat(result).isEqualTo(10);
    }

    @Test
    void IntegerToString(){
        IntegerToStringConverter converter = new IntegerToStringConverter();
        String result = converter.convert(10);

        assertThat(result).isEqualTo("10");
    }

    @Test
    void stringToIpPort(){
        IpPortToStringConverter converter = new IpPortToStringConverter();

        IpPort source = new IpPort("127.0.0.1", 8080);
        String result = converter.convert(source);

        assertThat(result).isEqualTo("127.0.0.1:8080");
    }

    @Test
    void ipPortToString(){
        StringToIpPortConverter converter = new StringToIpPortConverter();

        String source = "127.0.0.1:8080";
        IpPort result = converter.convert(source);

        //assertThat(result).isInstanceOf(IpPort.class);

        // *** IpPort에 @EqualsAndHashCode 어노테이션이 붙어있으므로, 메서드가 override되어 있음
        // -> 참조값이 달라도 내부 데이터가 같으면 equal 처리
        assertThat(result).isEqualTo(new IpPort("127.0.0.1", 8080));
    }
}
