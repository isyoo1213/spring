package spring.exception;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import spring.exception.filter.LogFilter;
import spring.exception.interceptor.LogInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "*.ico", "/error", "/error-page/**");
        // **** 중복 호출 피하기 - Interceptor는 Filter처럼 setDispatcherTypes() 메서드가 없음
        // -> 대신 url구성을 통해 ErrorPage에서 호출하는 url들을 exclude하는 방식을 통해 내부적인 요청에 인터셉터 적용을 피함
        // ***** 첫 정상 Request -> prehandle -> Exception -> postHandle 생략 -> afterCompletion
        // ***** 만약 /error-page/**를 exclude하지 않는 경우
        // -> 첫 정상 Request에 대한 오류 처리 후 내부적인 두 번째 호출 또한 정상적인 요청으로 interceptor가 적용됨
        // + *** 정상적인 내부적인 요청이므로 postHandle도 호출됨
    }

    //@Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.addUrlPatterns("/*");
        //Filter가 적용될 DispatcherType을 지정
        //setDispatcherTypes()는 ...문법 -> 여러개 등록 가능
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);

        return filterRegistrationBean;
    }

}
