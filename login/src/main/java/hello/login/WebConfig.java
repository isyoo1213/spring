package hello.login;

import hello.login.web.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

//SpringBoot에서 Filter 등록하기
@Configuration
public class WebConfig {

    //SpringBoot는 WAS를 직접 들고 띄우기 때문에, WAS를 띄울 때 servletContainer에 Filter를 같이 구성해줌
    //filter를 관리해주는 클래스를 Bean등록
    @Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        //직접 만든 filter를 생성해서 setting
        filterRegistrationBean.setFilter(new LogFilter());

        //filter 체인의 순서를 설정
        filterRegistrationBean.setOrder(1);

        //filter를 적용할 url
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

}
