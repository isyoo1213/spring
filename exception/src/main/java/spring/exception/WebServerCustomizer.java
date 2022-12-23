package spring.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

//WebServer, 즉 우리가 사용하고있는 Tomcat의 커스터마이징을 가능하게 해줌
//서버가 뜰 때 Tomcat에 해당 ErrorPage에 대한 정보 등록해줌
//@Component //Bean으로 등록해주기
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        //오류 페이지 만들기

        //**** 서버 내부에서 발생한 오류가 WAS까지 전달되면 -> WAS는 다시 해당 경로의 Controller까지 호출

        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        //404 에러 발생 시, 해당 path로 이동(정확히는 컨트롤러 호출)

        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");

        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");
        //RuntimeError(자식 예외 모두 포함) 발생 시에도 500으로 이동 - 서버 내부에서 발생한 오류라는 의미에서 500번 사용

        //등록하기
        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);

        //다음으로 ErrorPage를 처리하는 Controller 작성하기
    }
}
