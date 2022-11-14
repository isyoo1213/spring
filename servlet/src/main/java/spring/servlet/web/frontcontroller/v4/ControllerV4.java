package spring.servlet.web.frontcontroller.v4;

import java.util.Map;

public interface ControllerV4 {

    //이제 Controller에서 ModelView를 생성해서 반환하는 것이 아닌, Front-Controller에서 받아서 사용 + view의 이름만 반환

    /**
     *
     * @param paraMap
     * @param model
     * @return viewName
     */
    String process(Map<String, String> paraMap, Map<String, Object> model);
}
