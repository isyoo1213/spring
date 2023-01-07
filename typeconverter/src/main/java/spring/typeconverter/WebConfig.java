package spring.typeconverter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import spring.typeconverter.converter.IntegerToStringConverter;
import spring.typeconverter.converter.IpPortToStringConverter;
import spring.typeconverter.converter.StringToIntegerConverter;
import spring.typeconverter.converter.StringToIpPortConverter;
import spring.typeconverter.formatter.MyNumberFormatter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // * WebMvcConfigurer가 제공하는 addFormatter() -> 스프링이 제공하는 ConversionService
    //Fomatter는 Converter의 확장된 기능 정도
    @Override
    public void addFormatters(FormatterRegistry registry) {

        //직접 만든 Formatter와의 우선순위 이슈로 주석처리 - Converter의 처리 범위가 더욱 다양하고 디테일하므로 우선순위 가짐
        //registry.addConverter(new StringToIntegerConverter());
        //registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        //Formatter 추가
        registry.addFormatter(new MyNumberFormatter());
    }
    // 이렇게 Converter를 등록하면 스프링 내부적으로 동작하는 ConversionService에 컨버터를 등록함
}
