package spring.servlet.web.frontcontroller.v3;

import spring.servlet.web.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {

    //이제 servlet기술 없이 Http Request에 관한 정보를 Map을 통해 받아오도록 설계
    ModelView process(Map<String, String> paraMap);
}
