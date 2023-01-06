package spring.typeconverter.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Slf4j
public class MyNumberFormatter implements Formatter<Number> {
// * Formatter - printer 인터페이스(객체->문자) + Parser(문자->객체) 인터페이스 둘 모두를 상속
// 기본적으로 String 변환이 목적이므로 String은 구문에 넣지 않음

    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        //ex) "1,000" -> 1000

        log.info("text = {}, locale = {}", text, locale);

        // * NumberFormat - JAVA가 가지고 있는 숫자형 format
        // -> Locale 정보를 활용해 국가마다 다른 숫자 format을 만들어 줌
        NumberFormat instance = NumberFormat.getInstance();
        Number parse = instance.parse(text); //parse() 문자 -> 숫자

        return parse;
    }

    @Override
    public String print(Number object, Locale locale) {
        //ex) 1000 -> "1,000"

        log.info("object = {}, locale = {}", object, locale);

        NumberFormat instance = NumberFormat.getInstance(locale);
        String format = instance.format(object); //format() 객체 -> 문자

        return format;
    }
}
