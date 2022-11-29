package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    @PostMapping("/add")
    //점진적으로 V1부터 바꿔갈 예정
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // *** BindingResult는 위치가 중요 - @ModelAttribute 뒤에 와야함 - 즉, 쿼리스트링이 매핑되는 객체에 에러를 바인딩함

        //검증 오류 결과를 보관 -> 이제 Error를 관리하는 방식이 달라짐
        //Map<String, String> errors = new HashMap<>();

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            //errors.put("itemName", "상품 이름은 필수입니다.");
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수입니다."));
            //Item 클래스의 필드 단위의 정보에서의 error -> FieldError (ObjectError의 자식)
            //인자1 - ObjectName - ModelAttribute에 담기는 객체의 이름 == 주로 Parameter로 전달되는 변수 이름
            //인자2 - FieldName - error를 바인딩 할 객체 내 필드 이름
            //인자3 - defaultMessage - 오류 시 출력할 기본 내용 - 이후 다르게 설정하는 것 존재
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            //errors.put("price", "가격은 1,000 ~ 1,000,000 까지 허용합니다.");
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if(item.getQuantity() == null || item.getQuantity() <= 0|| item.getQuantity() > 9999){
            //errors.put("quantity", "수량은 1 ~ 9,9999까지 허용합니다.");
            bindingResult.addError(new FieldError("item", "quantity", "수량은 1 ~ 9,9999까지 허용합니다."));
        }

        //특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                //errors.put("globalError", "총 금액이 10,000원 이상이어야 합니다. 현재값 = " + resultPrice);
                //복합 룰 검증의 경우 -> ObjectError (특정 필드가 아닌 Object의 단위에서의 오류)
                //인자1 - 복합적으로 사용되는 객체의 이름
                //인자2 - Message
                bindingResult.addError(new ObjectError("item", "총 금액이 10,000원 이상이어야 합니다. 현재값 = " + resultPrice));
            }
        }

        //검증에 실패하면 다시 입력 폼으로 이동
        if(
                //!errors.isEmpty()
                //기존의 Map에 담긴 errors를 bindingTest 인스턴스에서 탐색
                bindingResult.hasErrors()
        ){
            //log.info("errors = {}", errors);
            log.info("errors ={}", bindingResult); //bindingResult 자체를 로그로 출력

            //이제 model에 errors를 담아서 View에 넘겨준 후 출력할 필요 없음 -> BindingResult는 자동으로 View로 오류를 넘김
            //model.addAttribute("errors", errors);
            return "validation/v2/addForm";
        }

        //검증 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}