package spring.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import spring.typeconverter.type.IpPort;

@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String> {

    @Override
    public String convert(IpPort source) {

        log.info("convert source = {}", source);

        //IpPort 객체 -> "127.0.0.1:8080"으로 변환
        return source.getIp() + ":" + source.getPort();
    }
}
