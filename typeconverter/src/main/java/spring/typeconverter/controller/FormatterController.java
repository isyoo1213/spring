package spring.typeconverter.controller;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class FormatterController {

    @GetMapping("/formatter/edit")
    public String formatterForm(Model model) {
        Form form = new Form();
        form.setNumber(10000);
        form.setLocalDateTime(LocalDateTime.now());
        model.addAttribute("form", form);

        return "formatter-form";
    }

    // *** Submit 시, 문자로 formatting된 "10,000"과 "yyyy-MM-dd HH:mm:ss"가 전달됨
    // -> 이 문자를 Form 객체로 변환해주어야 하는데, 바로 Formatter가 여기에서도 작동함
    // -> 어노테이션의 포맷을 통해 문자를 해석 -> Integer형과 LocalDateTime형으로 변환해줌
    @PostMapping("/formatter/edit")
    public String formatterEdit(@ModelAttribute Form form) {
        //@ModelAttribute 어노테이션 사용시 자동으로 model에 담기므로, 따로 model 인스턴스로 추가해줄 필요 없음
        return "formatter-view";
    }

    @Data
    static class Form {

        @NumberFormat(pattern = "###,###")
        private Integer number;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime localDateTime;
    }
}
