package spring.servlet.web.frontcontroller.v4.controller;

import spring.servlet.web.frontcontroller.v4.ControllerV4;

import java.util.Map;

public class MemberFormControllerV4 implements ControllerV4 {

    @Override
    public String process(Map<String, String> paraMap, Map<String, Object> model) {
        return "new-form";
    }
}
