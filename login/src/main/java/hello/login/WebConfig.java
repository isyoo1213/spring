package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LoginCheckInterceptor;
import hello.login.web.interceptor.LoginInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

//SpringBoot에서 Filter 등록하기
@Configuration
public class WebConfig implements WebMvcConfigurer {

    //Override로 인터셉터를 추가하는 것은 그냥 스프링이 제공하는 방식이므로 사용
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**") //Servlet Filter와 다른 구성
                .excludePathPatterns("/css/**", "/*.ico", "/error"); //인터셉터를 적용하지 않을 Path

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error");
    }

    //SpringBoot는 WAS를 직접 들고 띄우기 때문에, WAS를 띄울 때 servletContainer에 Filter를 같이 구성해줌
    //filter를 관리해주는 클래스를 Bean등록
    //@Bean
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

    //*** Filter 호출이 메모리 리소스에 주는 부담은 매우 적다 - 성능 저하 걱정 크게 필요 없음
    //@Bean
    public FilterRegistrationBean loginCheckFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();

        //직접 만든 filter를 생성해서 setting
        filterRegistrationBean.setFilter(new LoginCheckFilter());

        //filter 체인의 순서를 설정
        filterRegistrationBean.setOrder(2);

        //filter를 적용할 url
        filterRegistrationBean.addUrlPatterns("/*"); // WhiteList를 구성했으므로 모든 URL에 가능

        return filterRegistrationBean;
    }

}
