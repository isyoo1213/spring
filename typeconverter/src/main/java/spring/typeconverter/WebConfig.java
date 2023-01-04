package spring.typeconverter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import spring.typeconverter.converter.IntegerToStringConverter;
import spring.typeconverter.converter.IpPortToStringConverter;
import spring.typeconverter.converter.StringToIntegerConverter;
import spring.typeconverter.converter.StringToIpPortConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // * WebMvcConfigurer가 제공하는 addFormatter() -> 스프링이 제공하는 ConversionService
    //Fomatter는 Converter의 확장된 기능 정도
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());
    }
    // 이렇게 Converter를 등록하면 스프링 내부적으로 동작하는 ConversionService에 컨버터를 등록함
}
