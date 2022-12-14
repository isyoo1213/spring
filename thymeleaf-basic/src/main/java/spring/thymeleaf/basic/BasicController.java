package spring.thymeleaf.basic;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("/text-basic")
    public String textBasic(Model model){
        model.addAttribute("data", "Hello <b>Spring</b>");
        return "basic/text-basic";
    }

    @GetMapping("/text-unescaped")
    public String textEscaped(Model model){
        model.addAttribute("data", "Hello <b>Spring</b>");
        return "basic/text-unescaped";
    }

    @GetMapping("/variable")
    public String variable(Model model){
        User userA = new User("userA", 20);
        User userB = new User("userB", 30);

        //List와 Map 자료형 처리를 확인하기 위한 객체
        List<User> userList = new ArrayList<>();
        userList.add(userA);
        userList.add(userB);

        Map<String, User> userMap = new HashMap<>();
        userMap.put("userA", userA);
        userMap.put("userB", userB);

        //Model에는 User 객체, List, Map을 저장
        model.addAttribute("user", userA);
        model.addAttribute("userList", userList);
        model.addAttribute("userMap", userMap);

        return "basic/variable";
    }

    @GetMapping("/basic-objects")
    public String basicObjects(HttpSession session){
        //HttpServletRequest를 파라미터로 받아 model에 담아 넘기는 것도 있지만 이런 기본적인 기능은 바로 객체로 받을 수 있도록 제공
        //요청 파라미터, session, Bean 또한 model에 담지 않고 바로 사용 가능 by 편의 객체 param, session, @beanName
        session.setAttribute("sessionData", "Hello Session");
        return "basic/basic-objects";
    }

    @GetMapping("/date")
    public String date(Model model){
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "basic/date";
    }

    @GetMapping("/link")
    public String link(Model model){
        model.addAttribute("param1", "data1");
        model.addAttribute("param2", "data2");

        return "basic/link";
    }

    @GetMapping("/literal")
    public String literal(Model model){
        model.addAttribute("data", "Spring!");

        return "basic/literal";
    }

    @GetMapping("/operation")
    public String operation(Model model){
        model.addAttribute("nullData", null);
        model.addAttribute("data", "Spring!");

        return "basic/operation";
    }

    @GetMapping("/attribute")
    public String attribute(){
        return "basic/attribute";
    }

    @GetMapping("/each")
    public String each(Model model){
        addUsers(model);
        return "basic/each";
    }

    @GetMapping("/condition")
    public String condition(Model model){
        addUsers(model);
        return "basic/condition";
    }

    @GetMapping("/comments")
    public String comments(Model model){
        model.addAttribute("data", "Spring!");
        return "basic/comments";
    }

    @GetMapping("/block")
    public String block(Model model){
        addUsers(model);
        return "basic/block";
    }

    @GetMapping("/javascript")
    public String javascript(Model model){
        //JS inline 사용하면 이스케이프 처리 + 내추럴 템플릿(주석처리 부분을 랜더링) 가능하게 해줌
        // + 기존 JAVA의 toString()의 객체 정보를 객체 JSON으로 자동 변환
        model.addAttribute("user", new User("userD", 10));
        addUsers(model);

        return "basic/javascript";
    }

    private void addUsers(Model model) {
        List<User> userList = new ArrayList<>();
        userList.add(new User("userA", 10));
        userList.add(new User("userB", 20));
        userList.add(new User("userC", 30));

        model.addAttribute("users", userList);
    }

    @Component("helloBean")
    static class HelloBean {
        public String hello(String data) {
            return "Hello" + data;
        }
    }

    @Data
    static class User {
        private String username;
        private int age;

        public User(String username, int age) {
            this.username = username;
            this.age = age;
        }
    }

}
