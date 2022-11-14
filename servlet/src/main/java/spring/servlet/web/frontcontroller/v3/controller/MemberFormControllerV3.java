package spring.servlet.web.frontcontroller.v3.controller;

import spring.servlet.web.frontcontroller.ModelView;
import spring.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.Map;

public class MemberFormControllerV3 implements ControllerV3 {

    @Override
    public ModelView process(Map<String, String> paraMap) {
        return new ModelView("new-form"); //viewPath의 물리적 위치가 아닌 논리적 이름만 생성자를 통해 설정
    }
}
