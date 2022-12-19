package hello.login.web.argumentResolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //*** 스프링 내부의 Cache같은 곳에 Controller마다 이 메서드의 결과값을 저장 해놓음
        //-> 2번째 실행할 때는 supportsParameter()실행하지 않으므로 로그 안찍힘 -> resolveArgument() 메서드만 실행
        log.info("supportsParameter 실행");

        //Controller 호출하기 전에 Parameter에 Annotation이 들어있는지 확인
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);

        //Parameter에 명시된 Type이 Member인지 확인
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());


        return (hasLoginAnnotation && hasMemberType);
        //위의 두 조건을 모두 만족할 경우에만 true 반환 -> 아래의 resolveArgument() 메서드 실행 -> 실제 argument만들어서 반환
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");

        //위의 supportsParameter()에서 true로 넘어왔을 경우의 처리
        //즉 home(@Login Member loginMember){...} 에서
        //Request에 따라 Member 자료형의 loginMember에 어떤 값의 argument를 넘겨줄지 처리해주는 과정

        //session을 사용하기 위해 HttpServletRequest가 필요하므로 제공되는 NativeWebRequest를 캐스팅해 뽑아냄
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if(session == null){
            return null;
        }

        Object member = session.getAttribute(SessionConst.LOGIN_MEMBER);

        //session이 존재하더라도 저장된 값이 없을 경우 null 반환 or session에 저장된 member객체 반환
        return member;
    }
}
