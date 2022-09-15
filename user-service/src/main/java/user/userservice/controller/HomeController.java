package user.userservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "home";
        //MVC방식 > home.html 호출 됨 > resources - templates에서 탐색
    }
}
